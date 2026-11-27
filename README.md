# Tomotoru Game Client

Preserved Android client sources, resources, and game metadata for **Tomotoru** (*Hello Kitty Happy Life*) by CyberStep. The files in this repository document the original Android **v1.0.4** client.

[Download APKs][releases] · [Build from source][tools] · [Technical wiki][wiki]

## Source map

| Path | What's inside |
| --- | --- |
| [`game/squirrel/`](game/squirrel/) | Decompiled Squirrel scripts organized by game resource path. |
| [`game/java/`](game/java/) | Decompiled Android Java classes, including the SakanaGL bridge. |
| [`game/resources/`](game/resources/) | Android manifest, layouts, and other Android resources. |
| [`game/metadata/`](game/metadata/) | Resource index, package manifest, version, and structured game configuration. |

## Quick start

- Download the revival APKs from [Tomotoru Builds][releases].
  - **Offline APK:** The server is built into the app. You can play without Internet or a separate server. Progress is saved on your device.
  - **Official Online APK:** The app connects over the Internet to the server run by this project's maintainer. The server must be available to play, and your progress is saved there.
- The [`v0.0.1` archive][base-release] contains the unmodified original Android v1.0.4 APK and binary assets. The original APK is neither revival edition.
- For the APK builder, SXR tools, and Squirrel bytecode tools, see [Tomotoru Tools][tools].
- For APK installation and signing details, see [Installing APKs][installing].

- To test the client with a server on your computer, see [Run tomoserver locally][local-server] for the `online-test` build, asset extraction, and Android loopback setup.

## Related repositories

- [Builds][builds] — APK build workflows, releases, and the archival [`v0.0.1` base assets][base-release].
- [Tools][tools] — APK builder, asset and bytecode tools, and tooling tests.
- [Offline][offline] — Java server embedded in standalone APKs.
- [Server][server] — JavaScript backend for online APKs.

Game tags from before the split record the source and tooling used at the time. See [APK releases and reproducibility][reproducibility] for rebuilding from those revisions.

[builds]: https://github.com/bringbacktomotoru/builds
[releases]: https://github.com/bringbacktomotoru/builds/releases
[base-release]: https://github.com/bringbacktomotoru/builds/releases/tag/v0.0.1
[tools]: https://github.com/bringbacktomotoru/tools
[local-server]: https://github.com/bringbacktomotoru/tools#run-tomoserver-locally
[reproducibility]: https://github.com/bringbacktomotoru/tools#apk-releases-and-reproducibility
[wiki]: https://github.com/bringbacktomotoru/game/wiki
[installing]: https://github.com/bringbacktomotoru/game/wiki/Installing-APKs
[offline]: https://github.com/bringbacktomotoru/offline
[server]: https://github.com/bringbacktomotoru/server
