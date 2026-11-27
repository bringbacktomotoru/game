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

- For a playable APK or the original APK and binary assets, see [Tomotoru Builds][releases].
- For the APK builder, SXR tools, and Squirrel bytecode tools, see [Tomotoru Tools][tools].
- For APK installation and signing details, see [Installing APKs][installing].

- To connect the client to a server on your computer, see [Run tomoserver locally][local-server] for asset extraction and Android loopback setup.

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
