# Tomotoru Offline — Reporte de Investigación

> **Fecha**: 2026-06-19
> **Objetivo**: Determinar si es posible hacer que Tomotoru funcione 100% offline en un dispositivo Android sin root.
> **Veredicto**: **SÍ, es factible.** No hay obstáculos técnicos insalvables.

---

## Resumen Ejecutivo

Tomotoru (~Hello Kitty Happy Life~) es un juego mobile de pesca de CyberStep que fue apagado en 2018-2019. El juego corre sobre SakanaGL, un motor propietario de CyberStep que usa Squirrel como lenguaje de scripting. Todo el código del cliente está en scripts `.sk` (bytecode Squirrel modificado) dentro de bundles SXR.

**La solución**: Modificar el APK para que apunte a un servidor mock embebido en la misma app. El servidor corre en `127.0.0.1:PORT` dentro del dispositivo. No necesita root, no necesita conexión a internet.

**Dificultad estimada**: Media-Alta (comparable a Zombie Cafe Revival, que ya es un proyecto exitoso).
**Tiempo estimado**: 2-3 meses de trabajo dedicado.

---

## 1. Arquitectura del Juego

### 1.1 Capas del Cliente

```
┌─────────────────────────────────────────────┐
│  Java Layer                                  │
│  ├── SanrioActivity (launcher)               │
│  ├── SakanaApplication (lifecycle)           │
│  ├── SakanaView (SurfaceView + GL thread)    │
│  ├── SakanaVM (JNI bridge a Squirrel)        │
│  └── URLRequest (HTTP client Java-side)      │
├─────────────────────────────────────────────┤
│  Native Layer (libsakanagl.so)               │
│  ├── Squirrel VM (scripting engine)          │
│  ├── OpenGL ES renderer                      │
│  ├── Audio engine                            │
│  └── todos los sk* JNI functions             │
├─────────────────────────────────────────────┤
│  Script Layer (Squirrel .sk files)           │
│  ├── boot.sq (bootstrap del engine)          │
│  ├── GameConnection (HTTP + Blowfish)        │
│  ├── GameSession (lógica del juego)          │
│  ├── ServletConstants (IDs de comandos)      │
│  └── ~1124 scripts descompilados             │
└─────────────────────────────────────────────┘
```

### 1.2 Flujo de Arranque

```
1. Android lanza SakanaApplication.onCreate()
   → registerActivityLifecycleCallbacks()

2. Android lanza SanrioActivity.onCreate()
   → System.loadLibrary("sakanagl")
   → Crea SakanaView

3. SakanaView constructor
   → skinit(datadir, docdir, cachedir, osver, lang)
   → Crea SKThread

4. SKThread.run()
   → sknew()
   → skchecksxr(sk, mainsxrpath, version)
   → skmain(sk, sakanaView, appargs)  ← control al engine C++
   → skloop(sk)  ← frame loop infinito

5. Engine nativo carga Squirrel VM
   → boot.sq (resource tree, pkg.json, package manager)
   → sys.sq (system init)
   → 3-0111/AppMain (punto de entrada del juego)

6. AppMain
   → Crea pantalla
   → Inicializa sonido
   → Carga settings del servidor
   → GameSession.login()
```

### 1.3 Sistema de Red

**Protocolo HTTP plano** (sin HTTPS, sin certificate pinning):

```
Client                          Server
  │                                │
  │──── POST /entrance ───────────>│  Auth
  │<──── {session_id, uuid} ──────│
  │                                │
  │──── POST /main ───────────────>│  Game commands
  │     (Blowfish encrypt +        │
  │      Base64 + multipart)       │
  │<──── {response} ──────────────│  (Base64 + Blowfish decrypt)
  │                                │
  │──── POST /report ─────────────>│  Analytics
```

**Endpoints**:
- `/entrance` → Auth (login, registro)
- `/main` → Todos los comandos del juego (gacha, tienda, misiones, habitaciones, etc.)
- `/report` → Analytics/telemetría

**Cifrado**:
- Blowfish con key = `17` (hardcodeada en `112.sk.sq`)
- Request: JSON → compress (zlib si >= 10240 bytes) → Blowfish encrypt → Base64 encode
- Response: Base64 decode → Blowfish decrypt → JSON

**Multipart format** (request):
```
------boundary
Content-Disposition: form-data; name="params"

<Blowfish encrypted + Base64 encoded data>
------boundary--
```

### 1.4 Sistema de Assets (SXR)

**Formato SXR DEFL** (22 bytes header):
```
[Header: "SXR " + "DEFL" + "&F" + trailer_offset(u64) + trailer_length(u32)]
[Entity Data: XOR cipher per entity]
[Trailer: entity metadata, XOR stream cipher]
```

**Criptografía**: Todo simétrico (misma función para encriptar y desencriptar):
- Trailer: XOR stream cipher con keystream derivado de offset/length
- Entities: XOR cipher con key = FNV-1a hash del nombre de la entity

**Compresión**: zlib (type 6) o LZ4 (type 10)

**Bundles del juego**:
| Bundle | Contenido | Tamaño |
|--------|-----------|--------|
| main.sxr | Engine core, shaders, UI framework | 20MB |
| 1-011b.sxr | Game logic, auth, protocol, gacha | Variable |
| 0-0111.sxr | Character models, furniture, scripts | 12MB |
| 7-010d.sxr | Audio, 3D models | 8MB |
| a-0108.sxr | AR photo UI configs | Variable |
| 9-0101.sxr | Scene/cutscene scripts | Variable |
| 5-0109.sxr | Image assets | Variable |
| b-010c.sxr | Video + audio | Variable |

---

## 2. Bloqueos Identificados y Soluciones

### 2.1 Blowfish Key → RESUELTA

**Key = 17**, hardcodeada en `112.sk.sq`:
```squirrel
// GameConnection constructor
this._blowfish = Blowfish(17);
```

No hace falta extraer nada del `.so`. La clave está en el bytecode Squirrel.

### 2.2 URLs del Servidor → RESUELTA

Las URLs están en **dos capas**, ambas editables:

**Capa 1: `sakana.proj`** (JSON, trivial de modificar):
```json
{
  "serverurl": "http://52.69.85.104:8080",
  "pkgurl": "http://download.tomotoru.cyberstep.com/bin/main",
  "inspect_pkgurl": "http://52.194.165.198:8080"
}
```

**Capa 2: `194.sk.sq`** (bytecode Squirrel):
```squirrel
SERVER_URL <- "http://13.113.101.2:8080"
ENTRANCE_URL <- "/app_kitty/entrance"
MAIN_URL <- "/app_kitty/main"
REPORT_URL <- "/app_kitty/report"
```

**Solución**: Modificar ambas capas para apuntar a `127.0.0.1:PORT`.

### 2.3 Repacking SXR → FACTIBLE

El extractor (`tools/extractor/extract.py`) ya tiene todas las primitivas criptográficas. La criptografía es simétrica:

```python
# Desencriptar = Encriptar (misma función)
decrypt_entity(name, decrypt_entity(name, data)) == data  # ✅
decode_trailer(offset, length, decode_trailer(offset, length, data)) == data  # ✅
```

**Plan de repacking**:
1. Extraer entidades del SXR original
2. Modificar los archivos que necesiten cambio (e.g., `194.sk.sq`)
3. Re-comprimir (zlib/LZ4)
4. Re-encriptar entidades
5. Reconstruir trailer con metadata actualizada
6. Re-codificar trailer
7. Actualizar header con nuevos offset/length
8. Actualizar `pkg.json` con checksums

**No existe herramienta de repacking actualmente.** Hay que escribirla. Estimación: ~100-200 líneas de código.

### 2.4 Inyección en APK → FACTIBLE

**Puntos de inyección identificados**:

| Punto | Archivo | Método | Uso |
|-------|---------|--------|-----|
| Application.onCreate | `SakanaApplication.java` | `onCreate()` | Código antes de cualquier Activity |
| Activity.onCreate | `SanrioActivity.java` | `onCreate()` | Modificar antes de que arranque el engine |
| SakanaView init | `SakanaView.java` | Constructor | Hook antes de `skinit()` |
| SKThread.run | `SakanaView.java` | `SKThread.run()` | Inyectar antes/después de `skmain()` |
| SakanaFunction | `SakanaView.java` | `registerSakanaFunction()` | Registrar funciones Java para Squirrel |

**Servidor embebido**: NanoHTTPD (Java, ~150KB). Se agrega como JAR en `libs/`, se inyecta smali que inicie el servidor en `Application.onCreate()` o `SanrioActivity.onCreate()`.

### 2.5 Falta: Mock Server Completo

El esqueleto en `tools/server/mock.py` tiene bugs:
- Códigos de respuesta incorrectos (usa 0, el protocolo real usa 1 para SUCCEED)
- Formato de respuesta incorrecto
- Endpoints incompletos
- Sin persistencia

Hay ~109 comandos que implementar (ver `193.sk.sq` para la lista completa).

---

## 3. Casos de Éxito Similar

### 3.1 Spider-Man Unlimited (Gameloft)

**Situación**: Juego runner, servers apagados ~2017.
**Técnicas**:
- Crackearo TEA encryption en `player.dat`
- Descubrieron flag oculto `SKIP_VALIDATION=1`
- Inyectaron código Java vía JNI
- Modificaron `GameData.json` para restaurar contenido cortado
- Construyeron mod menu completo con UI custom
- Usaron AWS Lambda para encryptar saves

**Resultado**: Totalmente jugable. 40,000 miembros en Discord.
**Lección**: Sin acceso al servidor, si la lógica está en el cliente, se puede hacer jugable localmente.

### 3.2 Zombie Cafe Revival

**Situación**: Zombie Cafe (2011), servers offline.
**Técnicas**:
- Crearon `libZombieCafeExtension.so` (librería custom que se carga junto con la original)
- Smali patching para cargar la librería custom
- URL rewriting para redirigir tráfico a servidor local
- Servidor en Go
- NOP en un crash de textura (aceptaron memory leak)

**Resultado**: Totalmente jugable. 101 estrellas en GitHub.
**URL**: https://github.com/Airyzz/zombie-cafe-revival
**Lección**: El approach de ".so companion" funciona bien para juegos nativos C++.

### 3.3 Girl Alone Revival

**Situación**: Girl Alone (discontinuado 2023), simulación single-player.
**Técnicas**:
- Servidor privado para reemplazar anti-cheat/progressión
- APK patching para redirigir al servidor custom
- Eliminación de anti-cheat server-side
- IAP bypass (precios en blanco)

**Resultado**: Totalmente jugable.
**URL**: https://github.com/timschneeb/girl-alone-revival
**Lección**: Para juegos single-player, el servidor privado puede ser minimalista.

### 3.4 Groove Coaster 2

**Situación**: Rhythm game, servers shutdown.
**Técnicas**:
- App wrapper que parchea el APK en el dispositivo
- Edición de OBB files
- Datos de canciones embebidos en OBB para offline
- Servidor público como opción adicional

**Resultado**: Totalmente offline con todas las canciones.
**URL**: https://github.com/helloyanis/Groove2offline
**Lección**: El approach más simple: app que parchea el APK y sirve como wrapper.

### 3.5 Lecciones Clave de Todos los Proyectos

1. **Ninguno necesitó modificar el .so nativo** — todos working at the Java/script layer
2. **El mock server no necesita ser completo** — solo los endpoints que el juego realmente usa
3. **La criptografía siempre se puede crackear** — está en el cliente, el servidor necesita la key
4. **El APK patching es estándar** — apktool + smali + apksigner es el workflow universal
5. **Los juegos single-player son más fáciles** — no hay lógica server-side critica

---

## 4. Estado Actual del Proyecto

### 4.1 Herramientas Existentes

| Herramienta | Estado | Ubicación |
|-------------|--------|-----------|
| SXR Extractor | Funcional | `tools/extractor/extract.py` |
| SK Decompiler | Funcional (1600 líneas) | `tools/decompiler/nut.py` |
| Mock Server | Esqueleto con bugs | `tools/server/mock.py` |
| Protocol Docs | Básicas | `tools/server/protocol.md` |
| Batch Decompiler | Funcional | `tools/batch_decompile.py` |

### 4.2 Lo que ya Sabemos

- **Protocolo completo mapeado**: 109+ comandos documentados en `193.sk.sq`
- **1124 scripts descompilados** y disponibles en `decompiled/squirrel/`
- **Estructura del APK analizada**: Activities, Services, Native libs, Injection points
- **Sistema de assets documentado**: SXR format, encryption, compression
- **Auth flow completo**: boot → load → AppMain → Settings → PackageUpdator → GameSession.login → GameConnection
- **Todas las setting classes mapeadas**: GachaSetting, ShopSetting, MissionSetting, etc.
- **Response codes correctos**: SUCCEED=1, CONNECTION_FAIL=0, ERROR_OTHERS=-1, etc.

### 4.3 Archivos Clave del Proyecto

| Archivo | Descripción |
|---------|-------------|
| `decompiled/squirrel/1-011b/112.sk.sq` | GameConnection — HTTP client, Blowfish |
| `decompiled/squirrel/1-011b/114.sk.sq` | GameSession — lógica del juego |
| `decompiled/squirrel/1-011b/193.sk.sq` | ServletConstants — 109+ command IDs |
| `decompiled/squirrel/1-011b/194.sk.sq` | Server URLs hardcoded |
| `decompiled/squirrel/1-011b/144.sk.sq` | PackageUpdator — check/download bundles |
| `decompiled/squirrel/1-011b/23.sk.sq` | Default GameRuleSetting values |
| `decompiled/squirrel/1-011b/214.sk.sq` | GameSetting — loads all configs |
| `decompiled/squirrel/main/boot.sq` | Engine bootstrap |
| `decompiled/squirrel/main/HTTPClient.sq` | HTTP infrastructure |
| `decompiled/squirrel/main/packagemanager.sq` | Package manager |
| `decompiled/java/sources/com/cyberstep/sanriocharacters/SanrioActivity.java` | Main Activity |
| `decompiled/java/sources/ls/sakana/SakanaApplication.java` | Application class |
| `decompiled/java/sources/ls/sakana/SakanaView.java` | GL View + SKThread |
| `tools/extractor/extract.py` | SXR extractor |

---

## 5. Plan de Trabajo

### Fase 1: Repacker de SXR (2-3 semanas)

**Objetivo**: Poder modificar archivos dentro de bundles SXR y reempaquetarlos.

**Pasos**:
1. Estudiar el extractor existente (`extract.py`) — entender la estructura del trailer
2. Implementar `recompress(data, compression_type)` — inverso de la descompresión
3. Implementar `rebuild_trailer(entities)` — serializar metadata de entidades
4. Implementar `pack_sxr(entities, header)` — escribir SXR completo
5. Test roundtrip: extract → pack → extract y verificar que los datos son idénticos
6. Test con modificación: extract → modificar un .sk → pack → extract → verificar el cambio
7. Test con el juego: reempaquetar un bundle, copiar al APK, verificar que el engine lo carga

**Entregable**: Script `tools/packer/pack_sxr.py` que tome un directorio de archivos extraídos y genere un SXR válido.

### Fase 2: Mock Server Completo (4-6 semanas)

**Objetivo**: Servidor que responda correctamente a todos los comandos del juego.

**Pasos**:
1. Corregir bugs en `mock.py` (response codes, format)
2. Implementar auth flow completo (ENTRANCE_CMD_AUTH, etc.)
3. Implementar LOAD_CONF con todos los settings (GachaSetting, ShopSetting, etc.)
4. Implementar GET_ALL (datos del usuario: inventario, monedas, etc.)
5. Implementar comandos de gacha
6. Implementar comandos de furniture/rooms
7. Implementar comandos de shop/missions
8. Implementar persistencia (JSON files o SQLite)
9. Test contra el APK real (via proxy o directo)
10. Test offline (servidor en localhost, APK modificado)

**Entregable**: Servidor Flask/FastAPI completo con persistencia.

### Fase 3: Inyección en APK (1-2 semanas)

**Objetivo**: APK modificado que arranque servidor local y conecte a él.

**Pasos**:
1. Extraer APK con `apktool d`
2. Modificar `sakana.proj` en `assets/sxr/` (URLs → `127.0.0.1:PORT`)
3. Extraer `1-011b.sxr`, modificar `194.sk.sq` (URLs → `127.0.0.1:PORT`), reempaquetar
4. Agregar NanoHTTPD.jar a `libs/`
5. Inyectar smali de `WebServer` class (que implemente los endpoints)
6. Inyectar startup en `SakanaApplication.onCreate()` o `SanrioActivity.onCreate()`
7. Actualizar `pkg.json` si es necesario
8. Rebuild: `apktool b`
9. Align: `zipalign -f 4`
10. Sign: `apksigner sign --ks my-key.keystore`

**Entregable**: APK parcheado firmado que funcione offline.

### Fase 4: Testing y Pulido (1-2 semanas)

**Objetivo**: Verificar que el juego funciona completamente offline.

**Pasos**:
1. Test en dispositivo real (no emulator)
2. Verificar que el servidor arranca correctamente
3. Verificar auth flow completo
4. Verificar que el juego carga sin errores
5. Verificar que las funciones principales funcionan (pesca, gacha, habitaciones, etc.)
6. Test en modo avión (verificar que no intenta conexiones externas)
7. Medir uso de memoria/CPU del servidor embebido
8. Optimizar si es necesario

---

## 6. Riesgos

### 6.1 Riesgos Técnicos

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|---------|------------|
| Mock server incompleto | Alta | Alto | Priorizar endpoints críticos, implementar incrementalmente |
| Repacker con bugs | Media | Alto | Test roundtrip exhaustivo |
| Crash al inyectar smali | Media | Medio | Estudiar smali existente, seguir conventions |
| Memory leak del servidor | Baja | Medio | NanoHTTPD es ligero (~150KB), monitorear |
| Juego detecta APK modificado | Baja | Baja | No hay anti-tamper detectado |

### 6.2 Riesgos Legales

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|---------|------------|
| CyberStep envía C&D | Muy Baja | Alto | CyberStep se pivotó del gaming. Juego muerto, comunidad pequeña |
| Sanrio objeta | Muy Baja | Alto | Sanrio no tiene servers. Fan preservation generalmente tolerada |
| Distribución pública | Media | Medio | Mantener proyecto privado o solo para comunidades pequeñas |

### 6.3 Riesgos de Tiempo

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|---------|------------|
| Alcance demasiado grande | Alta | Alto | MVP: auth + LOAD_CONF + pesca básica. No implementar todo de una |
| Burnout | Media | Alto | Trabajar en fases, celebrar victorias parciales |
| Dependencia de herramientas externas | Baja | Baja | Todo el toolchain es open source |

---

## 7. Recursos y Lecturas

### 7.1 Repositorios de Referencia

| Repo | Descripción | Utilidad |
|------|-------------|----------|
| [Zombie Cafe Revival](https://github.com/Airyzz/zombie-cafe-revival) | Server + APK patching para juego mobile offline | Referencia de arquitectura |
| [Girl Alone Revival](https://github.com/timschneeb/girl-alone-revival) | APK patching + mock server | Referencia de implementación |
| [Groove2offline](https://github.com/helloyanis/Groove2offline) | App wrapper para juego offline | Referencia de distribución |
| [Project SMU](https://nosecurity.blog/smuHack2) | Spider-Man Unlimited reverse engineering | Referencia avanzada |
| [Supercell RE](https://github.com/soufgameyt/Supercell-Reverse-Engineering) | Frida scripts para redirect de tráfico | Técnicas de APK patching |
| [Project Tomotoru](https://github.com/xhyabunny/project-tomotoru) | Esfuerzo previo de fans (early stage) | Contexto, no código |
| [fishing_rod](https://github.com/dotmrjosh/fishing_rod) | Extractor de assets SakanaGL (Zig) | Referencia del formato SXR |
| [lunar-tear](https://github.com/Walter-Sparrow/lunar-tear) | Server para NieR Reincarnation | Referencia de server mobile |

### 7.2 Documentación

| Recurso | URL | Descripción |
|---------|-----|-------------|
| Squirrel Language | http://www.squirrel-lang.org/ | Lenguaje de scripting del juego |
| apktool | https://ibotpeaches.github.io/Apktool/ | Decompilar/recompilar APKs |
| NanoHTTPD | http://nanohttpd.org/ | Servidor HTTP embebido |
| Blowfish | https://en.wikipedia.org/wiki/Blowfish_(cipher) | Algoritmo de cifrado |
| Smali syntax | https://github.com/JesusFreke/smali | Documentación de smali |

### 7.3 Herramientas Necesarias

| Herramienta | Para qué | Instalación |
|-------------|----------|-------------|
| apktool | Decomilar/recompilar APK | `sudo apt install apktool` o descarga manual |
| jadx | Analizar Java source | `sudo apt install jadx` |
| apksigner | Firmar APKs | Android SDK |
| zipalign | Alinear APK | Android SDK |
| keytool | Generar keystores | JDK |
| mitmproxy | Test de tráfico | `pip install mitmproxy` |
| Frida | Runtime hooking (opcional) | `pip install frida-tools` |
| Ghidra | Análisis de .so (opcional) | https://ghidra-sre.org/ |

### 7.4 Comandos Útiles

```bash
# Extraer APK
apktool d -f -r apk/tomotoru_v1.0.4.apk -o extracted_apk/

# Recompilar APK
apktool b -f --use-aapt2 extracted_apk/

# Alinear
zipalign -f 4 extracted_apk/dist/app.apk app-aligned.apk

# Firmar
apksigner sign --ks my-key.keystore --ks-key-alias mykey app-aligned.apk

# Verificar firma
apksigner verify -v app-aligned.apk

# Generar keystore (una vez)
keytool -genkey -v -keystore my-key.keystore -alias mykey -keyalg RSA -keysize 2048 -validity 10000

# Ejecutar mock server
python -m tools.server.mock

# Extraer SXR
python tools/extractor/extract.py sxr/1-011b.sxr -o extracted/

# Descompilar bytecode SK
python tools/decompiler/nut.py extracted/194.sk.bin
```

---

## 8. Decisiones Pendientes

1. **¿Servidor embebido o externo?**
   - Embebido: APK autocontenido, más complejo de parchear
   - Exteno: más fácil de desarrollar, requiere dos apps
   - **Recomendación**: Embebido (meta final), pero empezar con externo para testing

2. **¿NanoHTTPD o Ktor?**
   - NanoHTTPD: más ligero (~150KB), más fácil de inyectar en smali
   - Ktor: más moderno, mejor API, más dependencias
   - **Recomendación**: NanoHTTPD (menos dependencies = más fácil de inyectar)

3. **¿Persistencia en JSON o SQLite?**
   - JSON: más simple, suficiente para single-player
   - SQLite: más robusto, mejor para queries complejas
   - **Recomendación**: JSON para MVP, migrar a SQLite si es necesario

4. **¿Cuántos comandos implementar para MVP?**
   - Mínimo: auth + LOAD_CONF + GET_ALL + pesca
   - Full: los 109+ comandos
   - **Recomendación**: MVP primero, iterar después

5. **¿Distribuir APK parcheado o app wrapper?**
   - APK parcheado: más limpio, una sola app
   - App wrapper: más fácil de actualizar, sin re-firmar
   - **Recomendación**: APK parcheado (similar a Groove2offline)

---

## 9. Próximos Pasos Inmediatos

**Mañana empezar por**:

1. **Escribir el repacker de SXR** (`tools/packer/pack_sxr.py`)
   - Usar las funciones de `extract.py` como base
   - Implementar recompress, rebuild_trailer, pack_sxr
   - Test roundtrip

2. **Corregir el mock server** (`tools/server/mock.py`)
   - Response code: 1 (no 0) para SUCCEED
   - Formato de respuesta correcto
   - Test con el APK real (si es posible)

3. **Extraer `libsakanagl.so`** del APK
   - Con `apktool d` debería estar en `lib/arm64-v8a/`
   - Verificar si hay algo útil que extraer (strings, etc.)

---

*Documento generado el 2026-06-19. Última actualización: 2026-06-19.*
