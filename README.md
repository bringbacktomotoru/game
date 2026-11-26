# Tomotoru

Reverse-engineering toolkit, APK build pipeline, and offline/online servers for **Tomotoru** (*Hello Kitty Happy Life*) by CyberStep.

> Full technical documentation — architecture, wire protocol, command reference, asset formats, server internals, and Android 12+ compatibility — lives in the **[Project Wiki](https://github.com/clysnelzum/tomotoru/wiki)**.

## Repository Layout

```text
tomotoru/
├── game/                # Decompiled game source & lightweight metadata (no heavy binaries)
│   ├── squirrel/        # Decompiled Squirrel (.sq) scripts in their logical directory tree
│   ├── java/            # Decompiled Java sources (SanrioActivity, SakanaGL bridge)
│   ├── resources/       # AndroidManifest.xml & XML/layout resources
│   └── metadata/        # resource_index.json, pkg.json, version
├── server/
│   ├── android/         # Embedded Java HTTP/1.1 server injected into offline APKs
│   └── python/          # Python Flask online server with Blowfish & JSON persistence
├── tools/
│   ├── assets/          # fetch_base.py, sxr_extract.py, sxr_pack.py
│   ├── squirrel/        # decompile.py, sk_parser/decompiler/encoder, patch.py, strings.py
│   ├── android/         # embed_server.py, sign_apk.py
│   └── build/           # build_apk.py (orchestrates original/offline/online/test builds)
├── tests/               # Pytest suite (crypto, decompiler, SXR, Python & Java servers)
└── .github/workflows/   # ci.yml & release.yml
```

Heavy binaries (`tomotoru-v1.0.4-original.apk` and compressed binary assets) are hosted in the [`base-v1.0.4`](https://github.com/clysnelzum/tomotoru/releases/tag/base-v1.0.4) GitHub Release and fetched on demand with SHA-256 verification.

## Quick Start

### 1. Install Dependencies & Fetch Base APK

```bash
pip install -e ".[sxr,dev]"
python -m tools.assets.fetch_base --extract-sxr
```

### 2. Build APK Variants

```bash
# Standalone 100% offline APK (embedded Java server on 127.0.0.1:18080)
python -m tools.build.build_apk --mode offline

# Debug offline test APK (embedded server + --debug workarounds & telemetry)
python -m tools.build.build_apk --mode offline-test

# Online APK (points to ONLINE_SERVER_URL or --url)
python -m tools.build.build_apk --mode online --url https://your-server.example.com

# Debug online test APK (points to loopback http://127.0.0.1:18880 + --debug)
python -m tools.build.build_apk --mode online-test
```

### 3. Run the Online Server (Python)

```bash
# Matches online-test loopback port 18880:
python -m server.python.mock --host 0.0.0.0 --port 18880 --debug
```

### 4. Run Tests

```bash
pytest -v
```
