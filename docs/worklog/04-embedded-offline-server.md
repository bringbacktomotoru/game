# Worklog 04 - Embedded 100% Offline Server (Branch `local`)

## Objetivo

Implementar la Fase 3 y Fase 4 de `docs/investigation-report.md`: un servidor HTTP 100% offline embebido dentro del propio APK de Tomotoru, accesible como opción `--embed-server` en `tools/patcher/patch_apk.py`, sin necesidad de servidor en PC, root ni conexión a internet.

## Arquitectura Implementada

### 1. Servidor Java Embebido (`android-server/src/main/java/com/tomotoru/offline/`)

Sin dependencias externas en runtime (usa exclusivamente JDK + `org.json` nativo de Android y compatible desde API 19 hasta Android 12+):

- `SakanaCrypto.java` — Cifrado Blowfish/ECB/NoPadding con clave `"test"`, framing SakanaGL (`[flag:1B][size:4B LE][payload][zero-pad 8B]`), compresión zlib (>= 10240B) y Base64 RFC 4648 en Java puro (compatible con `minSdkVersion=19`).
- `HttpServer.java` — Servidor HTTP/1.1 ligero sobre `ServerSocket` ligado a `127.0.0.1:<port>` con pool de 8 hilos. Envía siempre `Content-Length` exacto (requerido por `ls.sakana.URLRequest`, que ejecuta `ByteBuffer.allocateDirect(this.contentlength)`). Soporta `GET`, `POST`, `HEAD`, `Content-Length`, `Transfer-Encoding: chunked` y `multipart/form-data`.
- `JsonStore.java` — Persistencia atómica en JSON (`context.getFilesDir()/tomotoru_saves/*.json` mediante escritura `.tmp` + `fsync` + `renameTo`).
- `UserData.java` — Modelo de datos y serializador plano 100% fiel a `ServerDataLoader` (`176.sk.sq`).
- `GameSettings.java` — Configuración `GameRuleSetting` (`23.sk.sq`).
- `CommandHandlers.java` — Paridad completa con `tools/server/handlers/auth.py` y `main.py` (entrance, auth, takeover, `LOAD_CONF`, `GET_ALL`, tutoriales, disfraces, trabajo, producción, muebles/layout, tienda, gacha, habitaciones, misiones de panel, correo, capturas, IAP offline).
- `AssetProvider.java` / `AndroidAssetProvider.java` — Sirve `assets/sxr/*` directamente desde el `AssetManager` del APK en `/dl/*`, `/pkg.json` y `/setting.json`.
- `OfflineServer.java` — Punto de entrada invocado en `SakanaApplication.onCreate()`. Lee `assets/tomotoru_offline.json` (`{"port": 18080, "debug": false}`) y abre el `ServerSocket` de forma síncrona antes de que arranque el engine nativo.
- `StandaloneMain.java` — Runner de escritorio JVM usado por `tests/test_embedded_java_server.py`.

### 2. Inyección DEX como Opción en `patch_apk.py`

- `tools/patcher/embed_server.py`:
  1. Compila `android-server/src/main/java/com/tomotoru/offline/*.java` con `javac -source 1.8 -target 1.8` contra `android-23.jar`.
  2. Convierte `.class` -> `server.dex` (~44 KB) con `dalvik-exchange`.
  3. Desensambla `server.dex` y `classes.dex` original con `baksmali`.
  4. Copia `com/tomotoru/offline/*.smali` e inyecta `invoke-static {p0}, Lcom/tomotoru/offline/OfflineServer;->start(Landroid/content/Context;)V` en `ls/sakana/SakanaApplication.smali` -> `onCreate()V`.
  5. Reensambla `classes.dex` con `smali` (sin tocar `AndroidManifest.xml` ni `resources.arsc`).
- `tools/patcher/patch_apk.py` agrega las banderas opcionales `--embed-server` y `--port` (default `18080`), manteniendo intacto el flujo tradicional con servidor Python externo.

## Uso

```bash
# APK 100% offline (modo normal):
python -m tools.patcher.patch_apk apk/tomotoru_v1.0.4.apk --embed-server -o apk/tomotoru_offline_embedded.apk

# APK 100% offline (modo debug con fallbacks de assets CDN):
python -m tools.patcher.patch_apk apk/tomotoru_v1.0.4.apk --embed-server --debug -o apk/tomotoru_offline_embedded_debug.apk
```

## Verificación

- **Suite automatizada (`pytest`)**: 47 passed (incluye pruebas de interoperabilidad Blowfish/zlib Python <-> Java, verificación de `Content-Length`, persistencia tras reinicio, normal vs debug e integridad de APK/DEX/SXR/pkg.json).
- **Prueba en dispositivo real (Moto G22, Android 12, `ZT322HSS98`) vía ADB**:
  - `TomotoruOffline: Embedded offline server started on http://127.0.0.1:18080`
  - `GET /health` -> `{"status":"healthy","embedded":true}`
  - Autenticación, `LOAD_CONF`, `GET_ALL`, carga de la habitación 3D y apertura de Menú funcionando sin servidor externo.
  - Reinicio en frío con WiFi y datos móviles desactivados (`svc wifi disable && svc data disable`): arranque limpio 100% offline sin crashes.
  - Uso de memoria del proceso: ~1.5 MB Java Heap (~44 KB en DEX).
