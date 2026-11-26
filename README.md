# Tomotoru

Reverse engineering toolkit, APK patcher, and offline mock server for **Tomotoru** (Sanrio Characters) by CyberStep.

The game's original servers are offline. This project provides everything needed to patch the APK to point at a local mock server that implements the real SakanaGL / `app_kitty` protocol with persistent player saves.

## Quick Start (Play Offline)

### 1. Install Dependencies

```bash
pip install -e ".[sxr,dev]"
```

### 2. Patch the APK

Requires Java (`keytool`) and Android SDK `build-tools` (`zipalign`, `apksigner`) — auto-discovered from `PATH`, `ANDROID_HOME`, or the default SDK location.

```bash
# Normal mode (clean preservation / full CDN asset packs):
python -m tools.patcher.patch_apk apk/tomotoru_v1.0.4.apk http://127.0.0.1:5000 -o apk/tomotoru_offline.apk

# Debug mode (standalone APK workarounds + live /app_kitty/report telemetry):
python -m tools.patcher.patch_apk apk/tomotoru_v1.0.4.apk http://127.0.0.1:5000 -o apk/tomotoru_offline_debug.apk --debug
```

*(Use your PC's LAN IP for a real phone, or `http://10.0.2.2:5000` for an Android emulator.)*

The patcher automatically:
1. Patches `SERVER_URL` and `INSPECT_SERVER_URL` in `assets/sxr/1-011b.sxr` (entity `194`, preserving `.sk` string table indices)
2. Patches `serverurl`, `pkgurl`, and `inspect_pkgurl` in `assets/sxr/main.sxr` (`sakana.proj`, LZ4) and `assets/sxr/0-0111.sxr` (entity `1052`, LZ4)
3. Updates `assets/sxr/pkg.json` MD5 checksums and sizes
4. Repacks, aligns (`zipalign`), signs (`apksigner`), and verifies the APK

### 3. Start the Mock Server

```bash
# Normal mode (authentic new-player save, clean concise logs):
python -m tools.server.mock --host 0.0.0.0 --port 5000

# Debug mode (tutorial-skip saves with test currency, verbose request logs, highlighted /app_kitty/report traces):
python -m tools.server.mock --host 0.0.0.0 --port 5000 --debug
```

Player saves are stored atomically as JSON files in `tools/server/saves/`.

#### Normal vs `--debug` Mode

| Mode | APK Patcher (`tools.patcher.patch_apk`) | Mock Server (`tools.server.mock`) | When to use |
| :--- | :--- | :--- | :--- |
| **Normal** (default) | Clean URL/config patching only (no gameplay script hooks) | Authentic new-player saves (`end_tutorial_ids=[]`, `5000` GM, `100` Free RM), concise aligned logs | Authentic preservation & playing with full CDN asset packs |
| **`--debug`** | Adds standalone APK workarounds & live `/app_kitty/report` client telemetry | Skips all 19 tutorials, starts with `50000` GM / `1000` Free RM, logs full request params & highlights `[REPORT]` traces | Standalone APK testing, debugging, and rapid feature/menu testing |

### 4. Run Tests

```bash
pytest -v
```

## Project Structure

```
tomotoru/
├── apk/                     # Original APK (tomotoru_v1.0.4.apk)
├── sxr/                     # Raw SXR bundles + pkg.json
├── extracted/               # Extracted contents of all SXR bundles
├── decompiled/              # Decompiled Java (jadx) & Squirrel (.sk → .sq)
├── tools/
│   ├── extractor/           # SXR bundle extractor (XOR + zlib/LZ4)
│   ├── repacker/            # SXR repacker & in-place entity patcher
│   ├── patcher/             # Pure-Python + build-tools APK patcher
│   ├── decompiler/          # CyberStep .sk bytecode parser & decompiler
│   ├── strings/             # Binary string extractor
│   └── server/              # Flask mock server (Blowfish + persistence)
│       ├── crypto.py        # Exact SakanaGL Blowfish ECB + framing
│       ├── mock.py          # Flask app & CLI entrypoint
│       ├── handlers/        # Entrance/auth & 80+ main game commands
│       ├── persistence/     # Atomic JSON store + ServerDataLoader serializer
│       ├── data/            # GameRuleSetting & default configs
│       └── protocol.md      # Reverse-engineered protocol specification
├── tests/                   # Pytest suite (crypto, SXR, patcher, server)
└── docs/                    # Investigation report & engineering worklogs
```

## Documentation

- [`tools/server/protocol.md`](tools/server/protocol.md) — Wire encryption, framing, endpoints, and `ServerDataLoader` schema
- [`docs/investigation-report.md`](docs/investigation-report.md) — Architecture & reverse-engineering report
- [`docs/notes.md`](docs/notes.md) — Format & bytecode notes
