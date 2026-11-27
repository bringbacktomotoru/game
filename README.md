# Tomotoru Game Client

Preservation toolkit for the Android edition of **Tomotoru** (*Hello Kitty Happy Life*) by CyberStep. Build a standalone APK that runs without an internet connection, point the client at a separate server, or explore the original game's Squirrel scripts and asset bundles.

[Download APKs](https://github.com/bringbacktomotoru/game/releases) · [Technical wiki](https://github.com/bringbacktomotoru/game/wiki) · [Build from source](#build-from-source)

## Choose an APK

| APK | Use it for |
| --- | --- |
| `tomotoru-offline.apk` | Standalone play. Includes [tomoffline](https://github.com/bringbacktomotoru/offline), a Java server running inside the app. |
| `tomotoru-online.apk` | Connecting to the server configured when the APK was built. Build your own APK to use a different [tomoserver](https://github.com/bringbacktomotoru/server) instance. |
| `tomotoru-v1.0.4-original.apk` | Archival reference and build input: the unmodified CyberStep APK. |

GitHub release tags such as `v0.3.0` version **this toolkit**. The builds are based on the original Android **v1.0.4** APK. The historical [`v0.0.1` base-assets release](https://github.com/bringbacktomotoru/game/releases/tag/v0.0.1) hosts the original APK and compressed binary assets; the build tools verify the input APK against a pinned SHA-256 checksum.

For installation and signing details, see [Installing APKs](https://github.com/bringbacktomotoru/game/wiki/Installing-APKs).

## Build from Source

### 1. Install dependencies and fetch the base APK

Use Python 3.10+, JDK 17, and Android tooling (`zipalign`, `apksigner`). Offline builds also require `smali`, `baksmali`, `dalvik-exchange` or `dx`, and an Android platform JAR. See [Build and Tooling](https://github.com/bringbacktomotoru/game/wiki/Build-and-Tooling) for details.

```bash
git clone --recurse-submodules https://github.com/bringbacktomotoru/game.git
cd game
python -m venv .venv
source .venv/bin/activate
python -m pip install -e ".[dev]"
python -m tools.assets.fetch_base --extract-sxr
```

In an existing checkout, run `git submodule update --init -- tomoffline` after pulling changes to sync the embedded server to the revision pinned by the game client.

### 2. Build APK variants

Build outputs go to `dist/` by default:

```bash
# Standalone APK (embedded Java server on 127.0.0.1:18080)
python -m tools.build.build_apk --mode offline

# Online APK for a separate server
python -m tools.build.build_apk --mode online --url https://your-server.example.com

# Debug variants with extra telemetry; online-test defaults to 127.0.0.1:18880
python -m tools.build.build_apk --mode offline-test
python -m tools.build.build_apk --mode online-test
```

### 3. Run tomoserver locally

From the `game` checkout, clone [tomoserver](https://github.com/bringbacktomotoru/server) into a sibling directory. With Node.js installed and the base assets extracted in step 1, start the server on the port used by `online-test`:

```bash
git clone https://github.com/bringbacktomotoru/server.git ../server
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

### 4. Run tests

From the `game` checkout:

```bash
python -m pytest -v
```

## Source Map

| Path | What's inside |
| --- | --- |
| [`game/`](game/) | Decompiled Squirrel and Java, Android resources, and game metadata. |
| [`tomoffline/`](https://github.com/bringbacktomotoru/offline) | Pinned Java server submodule for offline APKs and Java integration tests. |
| [`tools/`](tools/) | SXR extraction and repacking, Squirrel decompilation and patching, DEX injection, and APK builds and signing. |
| [`tests/`](tests/) | Protocol framing, bytecode roundtrips, SXR bundles, runtime patches, and embedded server tests. |
| [`.github/workflows/`](.github/workflows/) | CI builds and tag-triggered APK releases. |
