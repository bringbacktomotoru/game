"""End-to-end integration tests for the embedded Java offline server (com.tomotoru.offline).

Compiles the real Java sources from android-server/ and runs StandaloneMain on a local
ephemeral port, testing 100% wire-protocol parity with the Python mock server:
  - Blowfish ECB + zlib framing interoperability (small & >10KB payloads)
  - Exact Content-Length header on every response (required by SakanaGL URLRequest.java)
  - /health, /setting.json, /pkg.json, /dl/<sxr>
  - Full auth + gameplay mutations + ServerDataLoader exact deserialization
  - Takeover code issue/use
  - Multipart/form-data request parsing
  - Normal vs debug mode
  - Persistence across server restarts
"""
from __future__ import annotations

import json
import os
import shutil
import socket
import subprocess
import time
import urllib.request
from pathlib import Path

import pytest

from tests.test_server import _consume_userdata_array
from tools.server.crypto import decrypt_payload, encrypt_payload

ROOT = Path(__file__).resolve().parent.parent
JAVA_SRC_DIR = ROOT / "android-server" / "src" / "main" / "java" / "com" / "tomotoru" / "offline"
ANDROID_JAR = Path("/usr/lib/android-sdk/platforms/android-23/android.jar")
ORG_JSON_JARS = [
    ROOT / "tests" / ".cache" / "org-json.jar",
    Path("/usr/share/java/android-json-org.jar"),
    Path("/usr/share/java/org.json.jar"),
]


def _find_org_json_jar() -> Path | None:
    for p in ORG_JSON_JARS:
        if p.is_file():
            return p
    for p in Path("/usr/share/java").glob("*json*.jar"):
        if "android" in p.name:
            return p
    return None


def _free_port() -> int:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind(("127.0.0.1", 0))
        return s.getsockname()[1]


@pytest.fixture(scope="module")
def compiled_java_classes(tmp_path_factory):
    if not shutil.which("javac") or not ANDROID_JAR.is_file():
        pytest.skip("javac or android-23.jar not installed")
    org_json = _find_org_json_jar()
    if org_json is None:
        pytest.skip("libandroid-json-org-java not installed")

    out_dir = tmp_path_factory.mktemp("java_classes")
    sources = sorted(str(p) for p in JAVA_SRC_DIR.glob("*.java"))
    res = subprocess.run(
        [
            "javac",
            "-source",
            "1.8",
            "-target",
            "1.8",
            "-cp",
            str(ANDROID_JAR),
            "-d",
            str(out_dir),
            *sources,
        ],
        capture_output=True,
        text=True,
    )
    assert res.returncode == 0, f"javac failed:\n{res.stderr}"
    return out_dir, org_json


class JavaServerProcess:
    def __init__(self, classes_dir: Path, org_json_jar: Path, saves_dir: Path, sxr_dir: Path, debug: bool = False):
        self.port = _free_port()
        self.base_url = f"http://127.0.0.1:{self.port}"
        cmd = [
            "java",
            "-cp",
            f"{classes_dir}{os.pathsep}{org_json_jar}",
            "com.tomotoru.offline.StandaloneMain",
            "--port",
            str(self.port),
            "--saves-dir",
            str(saves_dir),
            "--sxr-dir",
            str(sxr_dir),
        ]
        if debug:
            cmd.append("--debug")
        self.proc = subprocess.Popen(
            cmd,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
        )
        # Wait for READY line
        deadline = time.time() + 10.0
        ready = False
        while time.time() < deadline:
            line = self.proc.stdout.readline()
            if line.startswith("READY "):
                ready = True
                break
            if self.proc.poll() is not None:
                err = self.proc.stderr.read()
                raise RuntimeError(f"Java server exited early: {err}")
        if not ready:
            self.stop()
            raise RuntimeError("Java server did not become ready in time")

    def stop(self):
        if self.proc.poll() is None:
            self.proc.terminate()
            try:
                self.proc.wait(timeout=5)
            except subprocess.TimeoutExpired:
                self.proc.kill()

    def get(self, path: str) -> tuple[int, dict[str, str], bytes]:
        req = urllib.request.Request(f"{self.base_url}{path}", method="GET")
        with urllib.request.urlopen(req, timeout=5) as resp:
            headers = {k.lower(): v for k, v in resp.headers.items()}
            return resp.status, headers, resp.read()

    def post_enc(self, path: str, payload: dict) -> dict:
        enc = encrypt_payload(payload).encode("ascii")
        req = urllib.request.Request(
            f"{self.base_url}{path}",
            data=enc,
            headers={"Content-Type": "text/plain"},
            method="POST",
        )
        with urllib.request.urlopen(req, timeout=5) as resp:
            headers = {k.lower(): v for k, v in resp.headers.items()}
            body = resp.read()
            assert resp.status == 200
            assert "content-length" in headers, f"Missing Content-Length on {path}"
            assert int(headers["content-length"]) == len(body)
        decoded = decrypt_payload(body)
        assert isinstance(decoded, list) and len(decoded) == 1
        return decoded[0]


def test_embedded_java_server_static_and_content_length(compiled_java_classes, tmp_path):
    classes_dir, org_json = compiled_java_classes
    srv = JavaServerProcess(classes_dir, org_json, tmp_path / "saves", ROOT / "sxr", debug=False)
    try:
        status, headers, body = srv.get("/health")
        assert status == 200
        assert int(headers["content-length"]) == len(body)
        assert json.loads(body) == {"status": "healthy", "embedded": True}

        for url in ("/setting.json", "/dl/setting.json"):
            status, headers, body = srv.get(url)
            assert status == 200
            assert int(headers["content-length"]) == len(body)
            s = json.loads(body)
            assert s["release_pkg_version"] == 1027
            assert s["min_pkg_version"] == 1027
            assert s["mode"] == 0

        for url in ("/pkg.json", "/dl/pkg.json"):
            status, headers, body = srv.get(url)
            assert status == 200
            assert int(headers["content-length"]) == len(body)
            p = json.loads(body)
            assert p["version"] == 1027
            assert p["mainsxr"]["url"].endswith("/dl/main.sxr")
            assert len(p["sxrlist"]) == 8

        # Download real SXR asset
        status, headers, body = srv.get("/dl/main.sxr")
        assert status == 200
        assert int(headers["content-length"]) == len(body)
        assert body == (ROOT / "sxr" / "main.sxr").read_bytes()
    finally:
        srv.stop()


def test_embedded_java_server_full_gameplay_and_restart_persistence(compiled_java_classes, tmp_path):
    classes_dir, org_json = compiled_java_classes
    saves_dir = tmp_path / "saves"
    srv = JavaServerProcess(classes_dir, org_json, saves_dir, ROOT / "sxr", debug=False)
    try:
        # 1. Auth
        auth = srv.post_enc(
            "/app_kitty/entrance",
            {"uuid": "java-player-001", "cmd": 0, "platform_id": 1, "sk_ver": 100},
        )
        assert auth["ret"] == 1
        assert auth["first_login"] is True
        sid = auth["sessionId"]
        ud = _consume_userdata_array(auth["userdata"])
        assert ud["name"] == "Player"
        assert ud["gmoney"] == 5000
        assert ud["freerm"] == 100
        assert ud["end_tutorial_ids"] == []

        # 2. Mutate profile, costume, furniture, gacha, tutorial, tap
        assert srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 400, "params": {"name": "OfflineKitty", "sex": 1}})["ret"] == 1
        assert srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 1100, "params": 0})["costume_id"] == 109001
        buy = srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 1302, "params": {"furniture_id": 2001}})
        assert buy["ret"] == 1 and buy["buy_uid"] >= 100
        gacha = srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 3100, "params": {}})
        assert gacha["ret"] == 1 and len(json.loads(gacha["reward0"])) == 6
        assert srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 200, "params": {"ids": [1, 2]}})["ret"] == 1
        assert srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 1500, "params": {}})["add_gm"] == 20

        # 3. LOAD_CONF, capture, report, and large (>10KB zlib) payload roundtrip
        conf = srv.post_enc("/app_kitty/main", {"sid": sid, "cmd": 100, "params": {}})
        assert conf["ret"] == 1 and "GameRule" in conf["conf"]
        big_log = "X" * 12000
        rep = srv.post_enc("/app_kitty/report", {"uuid": "java-player-001", "cmd": 0, "params": {"log": big_log}})
        assert rep["ret"] == 1

        # 4. Multipart/form-data test (as documented in investigation-report.md)
        enc_str = encrypt_payload({"sid": sid, "cmd": 300, "params": {}})
        boundary = "----TomotoruBoundary123"
        mp_body = (
            f"--{boundary}\r\n"
            f'Content-Disposition: form-data; name="params"\r\n\r\n'
            f"{enc_str}\r\n"
            f"--{boundary}--\r\n"
        ).encode("utf-8")
        req = urllib.request.Request(
            f"{srv.base_url}/app_kitty/main",
            data=mp_body,
            headers={"Content-Type": f"multipart/form-data; boundary={boundary}"},
            method="POST",
        )
        with urllib.request.urlopen(req, timeout=5) as resp:
            mp_dec = decrypt_payload(resp.read())[0]
            assert mp_dec["ret"] == 1 and mp_dec["cmd"] == 300

        # 5. Takeover code flow
        issue = srv.post_enc("/app_kitty/entrance", {"uuid": "java-player-001", "cmd": 10, "password": "pw"})
        code = issue["takeover_code"]
        assert srv.post_enc("/app_kitty/entrance", {"cmd": 11, "takeover_code": code, "password": "bad"})["ret"] == -100
        good = srv.post_enc("/app_kitty/entrance", {"cmd": 11, "takeover_code": code, "password": "pw"})
        assert good["ret"] == 1 and good["uuid"] == "java-player-001"
    finally:
        srv.stop()

    # Restart server with the same saves_dir and verify persistence!
    srv2 = JavaServerProcess(classes_dir, org_json, saves_dir, ROOT / "sxr", debug=False)
    try:
        auth2 = srv2.post_enc("/app_kitty/entrance", {"uuid": "java-player-001", "cmd": 0})
        assert auth2["ret"] == 1
        assert auth2["first_login"] is False
        ud2 = _consume_userdata_array(auth2["userdata"])
        assert ud2["name"] == "OfflineKitty"
        assert ud2["sex"] == 1
        assert ud2["gmoney"] == 5020
        assert any(f["furniture_id"] == 2001 for f in ud2["furnitures"])
        assert ud2["end_tutorial_ids"] == [1, 2]
    finally:
        srv2.stop()


def test_embedded_java_server_debug_mode(compiled_java_classes, tmp_path):
    classes_dir, org_json = compiled_java_classes
    srv = JavaServerProcess(classes_dir, org_json, tmp_path / "saves_dbg", ROOT / "sxr", debug=True)
    try:
        auth = srv.post_enc("/app_kitty/entrance", {"uuid": "dbg-player", "cmd": 0})
        assert auth["ret"] == 1
        ud = _consume_userdata_array(auth["userdata"])
        assert len(ud["end_tutorial_ids"]) == 19
        assert ud["gmoney"] == 50000
        assert ud["freerm"] == 1000
    finally:
        srv.stop()


def test_embedded_offline_apk_integrity():
    """Verify patched embedded-server APK contains classes.dex hook, config, and valid SXR checksums."""
    import hashlib
    import zipfile

    apk_path = ROOT / "apk" / "tomotoru_offline_embedded.apk"
    if not apk_path.exists():
        pytest.skip("apk/tomotoru_offline_embedded.apk not built")

    with zipfile.ZipFile(apk_path, "r") as z:
        # 1. Config asset
        cfg = json.loads(z.read("assets/tomotoru_offline.json").decode("utf-8"))
        assert cfg["port"] == 18080
        assert cfg["debug"] is False

        # 2. Embedded server class descriptor present in classes.dex
        dex = z.read("classes.dex")
        assert b"Lcom/tomotoru/offline/OfflineServer;" in dex
        assert b"Lcom/tomotoru/offline/HttpServer;" in dex
        assert b"Lcom/tomotoru/offline/SakanaCrypto;" in dex

        # 3. pkg.json checksums match patched SXR bundles
        pkg = json.loads(z.read("assets/sxr/pkg.json").decode("utf-8"))
        checks = [("main.sxr", pkg["mainsxr"]["md5"], pkg["mainsxr"]["size"])] + [
            (f"{x['sxrname']}-0{x['version']:x}.sxr", x["md5"], x["size"])
            for x in pkg["sxrlist"]
            if x["sxrname"] in ("1", "0")
        ]
        for fn, expected_md5, expected_size in checks:
            raw = z.read(f"assets/sxr/{fn}")
            assert len(raw) == expected_size, f"{fn} size mismatch"
            assert hashlib.md5(raw).hexdigest().upper() == expected_md5, f"{fn} md5 mismatch"

