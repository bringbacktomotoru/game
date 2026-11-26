#!/usr/bin/env python3
"""APK Patcher for Tomotoru Offline.

Normal mode (default) performs pure URL + pkg.json patching only (zero Squirrel logic changes):
  - assets/sxr/1-011b.sxr -> entity "194" (.sk bytecode string table: SERVER_URL, INSPECT_SERVER_URL)
  - assets/sxr/main.sxr   -> entity "sakana.proj" (LZ4 JSON: serverurl, pkgurl, inspect_pkgurl)
  - assets/sxr/0-0111.sxr -> entity "1052" (LZ4 JSON: sakana.proj copy)
  - assets/sxr/pkg.json   -> updates MD5 and size for every modified SXR bundle (external + internal)

Debug mode (--debug) additionally applies runtime workarounds, missing-CDN fallbacks, AR disable,
and live /app_kitty/report exception telemetry via debug_patches.apply_debug_patch().

Steps:
  1. Extract assets/sxr/ from APK (pure Python zipfile, no apktool needed)
  2. Patch SXR bundles cleanly (preserving compression, encryption, and .sk string tables)
  3. Update assets/sxr/pkg.json checksums/sizes
  4. Repack APK without old META-INF signatures
  5. Align with zipalign and sign with apksigner

Usage:
  python tools/patcher/patch_apk.py apk/tomotoru_v1.0.4.apk http://127.0.0.1:5000 -o apk/tomotoru_offline.apk
  python tools/patcher/patch_apk.py apk/tomotoru_v1.0.4.apk http://127.0.0.1:5000 --debug -o apk/tomotoru_debug.apk
"""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import shutil
import struct
import subprocess
import sys
import tempfile
import zipfile
from pathlib import Path

_PATCHER_DIR = str(Path(__file__).resolve().parent)
_REPACKER_DIR = str(Path(__file__).resolve().parent.parent / "repacker")
for _d in (_PATCHER_DIR, _REPACKER_DIR):
    if _d not in sys.path:
        sys.path.insert(0, _d)

from debug_patches import apply_debug_patch  # noqa: E402
from embed_server import inject_embedded_server_into_dex  # noqa: E402
from repack import patch_sxr  # noqa: E402

OLD_SK_URLS = (
    "http://13.113.101.2:8080",
    "http://52.194.165.198:8080",
)

OLD_PROJ_URLS = (
    ("http://52.69.85.104:8080", ""),
    ("http://download.tomotoru.cyberstep.com/bin/main", "/dl"),
    ("http://download.tomotoru.cyberstep.com/bin/bin_inspect", "/dl"),
)


def _find_sdk_tool(tool_name: str) -> str:
    """Locate an Android build-tools executable (zipalign, apksigner) or JDK tool (keytool)."""
    found = shutil.which(tool_name)
    if found:
        return found

    candidates = []
    for env_var in ("ANDROID_HOME", "ANDROID_SDK_ROOT"):
        sdk = os.environ.get(env_var)
        if sdk:
            candidates.append(Path(sdk))

    if os.name == "nt":
        local_app = os.environ.get("LOCALAPPDATA")
        if local_app:
            candidates.append(Path(local_app) / "Android" / "Sdk")
    else:
        candidates.append(Path.home() / "Android" / "Sdk")
        candidates.append(Path.home() / "Library" / "Android" / "sdk")

    exts = (".exe", ".bat", "") if os.name == "nt" else ("",)
    for sdk in candidates:
        bt_dir = sdk / "build-tools"
        if bt_dir.is_dir():
            for ver_dir in sorted(bt_dir.iterdir(), reverse=True):
                for ext in exts:
                    p = ver_dir / f"{tool_name}{ext}"
                    if p.is_file():
                        return str(p)

    raise FileNotFoundError(
        f"Could not find '{tool_name}'. Add Android SDK build-tools to PATH or set ANDROID_HOME."
    )


def run_cmd(args: list[str], desc: str = "") -> str:
    """Run a command without shell quoting issues, raise on failure."""
    if desc:
        print(f"  > {desc}")
    result = subprocess.run(args, capture_output=True, text=True)
    if result.returncode != 0:
        err = result.stderr.strip() or result.stdout.strip()
        raise RuntimeError(f"Command failed ({args[0]}): {err}")
    return result.stdout


def patch_sk_strings(sk_bytes: bytes, replacements: dict[str, str]) -> tuple[bytes, int]:
    """Replace exact string entries in a .sk bytecode file without corrupting string indices.

    .sk binary format:
      [0:2]   0xFA 0xFA magic
      [2:6]   data_sz (u32 BE)
      [6:8]   num_str (u16 BE)
      [8:8+data_sz]  bytecode
      [8+data_sz:]   num_str null-terminated ASCII strings
    """
    if len(sk_bytes) < 8 or sk_bytes[:2] != b"\xfa\xfa":
        return sk_bytes, 0

    data_sz = struct.unpack_from(">I", sk_bytes, 2)[0]
    num_str = struct.unpack_from(">H", sk_bytes, 6)[0]
    header_and_code = sk_bytes[: 8 + data_sz]
    str_section = sk_bytes[8 + data_sz :]

    has_trailing_null = str_section.endswith(b"\x00")
    raw_parts = str_section[:-1].split(b"\x00") if has_trailing_null else str_section.split(b"\x00")

    count = 0
    new_parts = []
    for part in raw_parts:
        try:
            text = part.decode("ascii")
        except UnicodeDecodeError:
            new_parts.append(part)
            continue
        if text in replacements:
            new_parts.append(replacements[text].encode("ascii"))
            count += 1
        else:
            new_parts.append(part)

    if count == 0:
        return sk_bytes, 0

    rebuilt_strs = b"\x00".join(new_parts) + (b"\x00" if has_trailing_null else b"")
    assert len(new_parts) == num_str, (
        f"String table count mismatch: {len(new_parts)} != {num_str}"
    )
    return header_and_code + rebuilt_strs, count


def patch_sakana_proj(raw_bytes: bytes, mock_url: str) -> tuple[bytes, int]:
    """Replace serverurl, pkgurl, and inspect_pkgurl inside sakana.proj text."""
    try:
        text = raw_bytes.decode("utf-8")
    except UnicodeDecodeError:
        return raw_bytes, 0

    count = 0
    base = mock_url.rstrip("/")
    for old, suffix in OLD_PROJ_URLS:
        if old in text:
            text = text.replace(old, f"{base}{suffix}")
            count += 1

    if count == 0:
        return raw_bytes, 0
    return text.encode("utf-8"), count


def patch_sxr_bundle(
    sxr_path: Path,
    mock_url: str,
    updated_sxr_meta: dict[str, tuple[int, str]] | None = None,
    debug: bool = False,
) -> int:
    """Patch URL-bearing entities (and internal pkg.json in main.sxr) inside an SXR bundle.

    Args:
        sxr_path: Path to SXR bundle to patch in place.
        mock_url: Base mock server URL.
        updated_sxr_meta: Optional map of filename (e.g. '1-011b.sxr') -> (size, MD5_HEX)
                          used to update the internal pkg.json inside main.sxr so
                          PackageManager's SxrListItem.checkStatus() accepts patched bundles.
        debug: If True, also apply Squirrel runtime workarounds, missing-CDN fallbacks,
               AR disable, and live /app_kitty/report hooks via apply_debug_patch().
    """
    base = mock_url.rstrip("/")
    sk_replacements = {old: base for old in OLD_SK_URLS}
    url_patches = 0

    def _patch_entity(name: str, data: bytes) -> bytes | None:
        nonlocal url_patches
        if data[:2] == b"\xfa\xfa":
            current = data
            modified = False

            patched, n = patch_sk_strings(current, sk_replacements)
            if n > 0:
                url_patches += n
                current = patched
                modified = True

            if debug:
                patched_dbg, desc = apply_debug_patch(name, current)
                if desc is not None:
                    print(f"    [debug] {desc}")
                    current = patched_dbg
                    modified = True

            if modified:
                return current

        if b"download.tomotoru.cyberstep.com" in data or b"52.69.85.104" in data:
            patched, n = patch_sakana_proj(data, base)
            if n > 0:
                url_patches += n
                return patched

        if name == "pkg.json" and updated_sxr_meta:
            pkg = json.loads(data.decode("utf-8"))
            changed = False
            for item in pkg.get("sxrlist", []):
                fname = f"{item['sxrname']}-0{item['version']:x}.sxr"
                if fname in updated_sxr_meta:
                    new_size, new_md5 = updated_sxr_meta[fname]
                    item["size"] = new_size
                    item["md5"] = new_md5
                    changed = True
            if changed:
                return json.dumps(pkg, separators=(",", ":")).encode("utf-8")

        return None

    entities_patched, _ = patch_sxr(sxr_path, sxr_path, _patch_entity)
    return url_patches if entities_patched > 0 else 0


def _update_pkg_json(pkg_path: Path, modified_sxrs: dict[str, Path]) -> None:
    """Update MD5 and file size entries in pkg.json for any modified SXR bundles."""
    if not pkg_path.exists():
        return
    pkg = json.loads(pkg_path.read_text(encoding="utf-8"))

    if "main.sxr" in modified_sxrs and "mainsxr" in pkg:
        raw = modified_sxrs["main.sxr"].read_bytes()
        pkg["mainsxr"]["size"] = len(raw)
        pkg["mainsxr"]["md5"] = hashlib.md5(raw).hexdigest().upper()

    for item in pkg.get("sxrlist", []):
        filename = f"{item['sxrname']}-0{item['version']:x}.sxr"
        if filename in modified_sxrs:
            raw = modified_sxrs[filename].read_bytes()
            item["size"] = len(raw)
            item["md5"] = hashlib.md5(raw).hexdigest().upper()

    pkg_path.write_text(json.dumps(pkg, indent="\t"), encoding="utf-8")


def patch_apk(
    apk_path: str | Path,
    mock_url: str | None = None,
    output_path: str | Path | None = None,
    keystore_path: str | Path | None = None,
    debug: bool = False,
    embed_server: bool = False,
    port: int = 18080,
) -> Path:
    """Full APK patching pipeline."""
    apk_path = Path(apk_path).resolve()
    if not apk_path.exists():
        raise FileNotFoundError(f"APK not found: {apk_path}")

    if mock_url is None:
        if embed_server:
            mock_url = f"http://127.0.0.1:{port}"
        else:
            raise ValueError("mock_url is required unless embed_server=True")

    mock_url = mock_url.rstrip("/")
    if output_path is None:
        suffix = "_offline_embedded" if embed_server else "_offline"
        output_path = apk_path.parent / f"{apk_path.stem}{suffix}.apk"
    output_path = Path(output_path).resolve()
    output_path.parent.mkdir(parents=True, exist_ok=True)

    zipalign_bin = _find_sdk_tool("zipalign")
    apksigner_bin = _find_sdk_tool("apksigner")
    keytool_bin = _find_sdk_tool("keytool")

    work_dir = Path(tempfile.mkdtemp(prefix="tomotoru_patch_"))
    sxr_work_dir = work_dir / "sxr"
    sxr_work_dir.mkdir()

    try:
        # Step 1: Extract assets/sxr/* from APK
        print("\n[1/5] Extracting SXR assets from APK...")
        target_bundles = {"assets/sxr/1-011b.sxr", "assets/sxr/main.sxr", "assets/sxr/0-0111.sxr"}
        extracted_map: dict[str, Path] = {}
        with zipfile.ZipFile(apk_path, "r") as zin:
            for member in zin.namelist():
                if member in target_bundles or member == "assets/sxr/pkg.json":
                    dest = sxr_work_dir / Path(member).name
                    dest.write_bytes(zin.read(member))
                    extracted_map[member] = dest

        if not extracted_map:
            raise RuntimeError("No assets/sxr/ bundles found in APK")

        # Step 2: Patch SXR bundles (patch non-main bundles first so main.sxr's internal pkg.json gets their new sizes/MD5s)
        mode_label = "debug (URLs + runtime workarounds + telemetry)" if debug else "normal (URLs only)"
        print(f"\n[2/5] Patching SXR bundles [{mode_label}] -> {mock_url} ...")
        modified_files: dict[str, Path] = {}
        modified_sxr_names: dict[str, Path] = {}
        updated_meta: dict[str, tuple[int, str]] = {}
        total_patches = 0

        ordered_members = sorted(
            (m for m in extracted_map if m.endswith(".sxr")),
            key=lambda m: (1 if m.endswith("/main.sxr") else 0, m),
        )
        for member in ordered_members:
            local_path = extracted_map[member]
            is_main = member.endswith("/main.sxr")
            n = patch_sxr_bundle(
                local_path,
                mock_url,
                updated_sxr_meta=updated_meta if is_main else None,
                debug=debug,
            )
            if n > 0:
                print(f"  Patched {local_path.name}: {n} URL(s)")
                modified_files[member] = local_path
                modified_sxr_names[local_path.name] = local_path
                raw = local_path.read_bytes()
                updated_meta[local_path.name] = (len(raw), hashlib.md5(raw).hexdigest().upper())
                total_patches += n

        print(f"  Total URL patches: {total_patches}")
        if total_patches == 0:
            raise RuntimeError("No URL patches were applied — unexpected APK contents")

        # Update pkg.json with new sizes & MD5s
        pkg_local = extracted_map.get("assets/sxr/pkg.json")
        if pkg_local and modified_sxr_names:
            _update_pkg_json(pkg_local, modified_sxr_names)
            modified_files["assets/sxr/pkg.json"] = pkg_local
            print("  Updated assets/sxr/pkg.json checksums and sizes")

        added_files: dict[str, bytes] = {}
        if embed_server:
            print(f"\n[2.5/5] Injecting embedded offline server into classes.dex (127.0.0.1:{port})...")
            with zipfile.ZipFile(apk_path, "r") as zin:
                orig_dex = zin.read("classes.dex")
            patched_dex = inject_embedded_server_into_dex(orig_dex)
            dex_local = work_dir / "classes.dex"
            dex_local.write_bytes(patched_dex)
            modified_files["classes.dex"] = dex_local
            added_files["assets/tomotoru_offline.json"] = json.dumps(
                {"port": port, "debug": bool(debug)}, separators=(",", ":")
            ).encode("utf-8")

        # Step 3: Repack APK (strip old META-INF signatures)
        print("\n[3/5] Repacking APK...")
        unsigned_apk = work_dir / "unsigned.apk"
        with zipfile.ZipFile(apk_path, "r") as zin, zipfile.ZipFile(unsigned_apk, "w") as zout:
            written_names: set[str] = set()
            for info in zin.infolist():
                if info.filename.startswith("META-INF/"):
                    upper = info.filename.upper()
                    if upper == "META-INF/MANIFEST.MF" or upper.endswith(
                        (".SF", ".RSA", ".DSA", ".EC")
                    ):
                        continue

                new_info = zipfile.ZipInfo(info.filename, date_time=info.date_time)
                new_info.compress_type = info.compress_type
                new_info.external_attr = info.external_attr

                if info.filename in modified_files:
                    payload = modified_files[info.filename].read_bytes()
                elif info.filename in added_files:
                    payload = added_files[info.filename]
                else:
                    payload = zin.read(info.filename)

                zout.writestr(new_info, payload)
                written_names.add(info.filename)

            for new_name, new_payload in added_files.items():
                if new_name not in written_names:
                    zout.writestr(new_name, new_payload, compress_type=zipfile.ZIP_DEFLATED)

        # Step 4: Align
        print("\n[4/5] Aligning APK...")
        aligned_apk = work_dir / "aligned.apk"
        run_cmd([zipalign_bin, "-f", "4", str(unsigned_apk), str(aligned_apk)], "zipalign -f 4")

        # Step 5: Sign
        print("\n[5/5] Signing APK...")
        if keystore_path:
            keystore = Path(keystore_path).resolve()
        else:
            keystore = work_dir / "debug.keystore"
            run_cmd(
                [
                    keytool_bin,
                    "-genkeypair",
                    "-keystore",
                    str(keystore),
                    "-alias",
                    "tomotoru",
                    "-keyalg",
                    "RSA",
                    "-keysize",
                    "2048",
                    "-validity",
                    "10000",
                    "-storepass",
                    "android",
                    "-keypass",
                    "android",
                    "-dname",
                    "CN=Tomotoru Offline, OU=Dev, O=Local, L=Local, ST=Local, C=JP",
                ],
                "Generating debug keystore",
            )

        run_cmd(
            [
                apksigner_bin,
                "sign",
                "--ks",
                str(keystore),
                "--ks-pass",
                "pass:android",
                "--key-pass",
                "pass:android",
                "--out",
                str(output_path),
                str(aligned_apk),
            ],
            "Signing with apksigner",
        )

        # Verify signature
        run_cmd([apksigner_bin, "verify", str(output_path)], "Verifying APK signature")

        print(f"\n{'=' * 50}")
        print(f"Patched APK: {output_path}")
        print(f"Size: {output_path.stat().st_size / 1024 / 1024:.1f} MB")
        print(f"Mock server: {mock_url}")
        print(f"Mode: {'debug' if debug else 'normal'}")
        print(f"{'=' * 50}")
        return output_path

    finally:
        shutil.rmtree(work_dir, ignore_errors=True)


def main() -> None:
    parser = argparse.ArgumentParser(description="Patch Tomotoru APK for offline play")
    parser.add_argument("apk", help="Path to the original APK")
    parser.add_argument(
        "url",
        nargs="?",
        default=None,
        help="Mock server URL (e.g. http://127.0.0.1:5000). Optional when --embed-server is set.",
    )
    parser.add_argument("-o", "--output", help="Output APK path")
    parser.add_argument("--keystore", help="Optional existing keystore path (pass: android)")
    parser.add_argument(
        "--embed-server",
        action="store_true",
        help="Embed the 100%% offline Java HTTP server directly inside classes.dex (no PC server needed)",
    )
    parser.add_argument(
        "--port",
        type=int,
        default=18080,
        help="Loopback port for --embed-server (default: 18080)",
    )
    parser.add_argument(
        "--debug",
        action="store_true",
        help="Also apply Squirrel runtime workarounds, missing-CDN fallbacks, AR disable, and live /app_kitty/report hooks",
    )
    args = parser.parse_args()

    patch_apk(
        args.apk,
        args.url,
        args.output,
        args.keystore,
        debug=args.debug,
        embed_server=args.embed_server,
        port=args.port,
    )


if __name__ == "__main__":
    main()
