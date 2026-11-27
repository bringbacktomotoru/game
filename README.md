# Tomotoru

Reverse-engineering toolkit and APK build pipeline for **Tomotoru** (*Hello Kitty Happy Life*) by CyberStep. Use [tomoserver](https://github.com/bringbacktomotoru/server) to run a separate game server.

> Full technical documentation — architecture, wire protocol, command reference, asset formats, server internals, and Android 12+ compatibility — lives in the **[Project Wiki](https://github.com/bringbacktomotoru/game/wiki)**.

## Repository Layout

```text
tomotoru/
├── game/                # Decompiled game source & lightweight metadata (no heavy binaries)
│   ├── squirrel/        # Decompiled Squirrel (.sq) scripts in their logical directory tree
│   ├── java/            # Decompiled Java sources (SanrioActivity, SakanaGL bridge)
│   ├── resources/       # AndroidManifest.xml & XML/layout resources
│   └── metadata/        # resource_index.json, pkg.json, version
├── tomoffline/          # Submodule: Java server embedded in offline APKs
├── tools/
│   ├── assets/          # fetch_base.py, sxr_extract.py, sxr_pack.py
│   ├── squirrel/        # decompile.py, sk_parser/decompiler/encoder, patch.py, strings.py
│   ├── android/         # embed_server.py, sign_apk.py
│   └── build/           # build_apk.py (orchestrates original/offline/online/test builds)
├── tests/               # Pytest suite (wire protocol, decompiler, SXR, embedded Java server)
└── .github/workflows/   # ci.yml & release.yml
```

Heavy binaries (`tomotoru-v1.0.4-original.apk` and compressed binary assets) are hosted in the [`v0.0.1`](https://github.com/bringbacktomotoru/game/releases/tag/v0.0.1) GitHub Release and fetched on demand with SHA-256 verification.

## Quick Start

### 1. Install Dependencies & Fetch Base APK

Initialize the [tomoffline](https://github.com/bringbacktomotoru/offline) submodule for offline
APK builds and Java integration tests. Run the same command after pulling changes to pick
up any updated server revision.

```bash
git submodule update --init -- tomoffline
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

### 3. Run tomoserver Locally

Clone [tomoserver](https://github.com/bringbacktomotoru/server) next to this repository. After fetching the base APK and extracting its assets as shown above, start tomoserver on the port used by the `online-test` APK. These commands assume the default checkout names `game` and `server`:

```bash
cd ../server
npm ci
node scripts/sync-assets.mjs ../game/.cache/sxr
PORT=18880 SESSION_SECRET="$(openssl rand -hex 32)" npm start
```

In another terminal, run `adb reverse tcp:18880 tcp:18880` to let the Android device reach
tomoserver on your computer at `http://127.0.0.1:18880`. You can also build with
`--url http://<computer-lan-ip>:18880` and start tomoserver with `HOST=0.0.0.0` for LAN access.

The Node adapter stores progress in memory for the lifetime of the process. The sync command
uses the base APK's bundles with tomoserver's default manifest. To serve the patched bundles
from a custom APK instead, sync its extracted `assets/sxr` directory and make sure tomoserver
serves the matching `pkg.json` manifest.

### 4. Run Tests

```bash
pytest -v
```
