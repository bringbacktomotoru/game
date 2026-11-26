# Worklog 03 - 2026-06-19: APK Patching

## Objetivo
Modificar el APK para que apunte a un servidor mock local.

## Investigación Realizada

### Fuentes Consultadas
1. **Investigation report** (proyecto) - Análisis de puntos de inyección
2. **Zombie Cafe Revival** - Técnicas de APK patching
3. **Girl Alone Revival** - Mock server + APK patching
4. **apktool docs** - Decompilar/recompilar APKs

### Hallazgos Clave

#### 1. No existe `sakana.proj` en el APK
A diferencia de lo reportado, el APK no contiene `sakana.proj`. Las URLs están únicamente en los bundles SXR.

#### 2. URLs en 1-011b.sxr (entity 194)
Las URLs del servidor están hardcodeadas en el script 194.sk dentro del bundle 1-011b.sxr:
- `http://13.113.101.2:8080` (SERVER_URL)
- `http://52.194.165.198:8080` (INSPECT_SERVER_URL)

Los paths de los endpoints están en el mismo script:
- `/app_kitty` (CMD_URL)
- `/entrance` (ENTRANCE_URL)
- `/main` (MAIN_URL)
- `/report` (REPORT_URL)

#### 3. URLs están XOR-encryptadas
Los strings en los bundles SXR están cifrados con XOR usando FNV-1a hash del nombre de la entity como key. El cifrado es simétrico (decrypt = encrypt), lo que permite patching directo.

#### 4. Tamaño de string fijo
Al reemplazar URLs, se debe mantener el mismo tamaño (rellenando con bytes nulos) para no romper el stream cipher.

## Herramientas Instaladas
- `zipalign` (1:10.0.0+r36-1.1build1)
- `apksigner` (35.0.2-1)

## Implementación
Creado `tools/patcher/patch_apk.py` con pipeline completo:
1. Decompile APK con apktool
2. Patch URLs en bundles SXR (entity 194 de 1-011b.sxr)
3. Rebuild APK con apktool
4. Align con zipalign
5. Sign con apksigner (debug keystore)

## Verificación
- URLs parcheadas verificadas extrayendo entity 194 del APK firmado
- Ambos URLs apuntan a `http://127.0.0.1:5000`

## Próximos Pasos
1. Fase 5: Testing en dispositivo real
2. Verificar que el juego arranca y conecta al servidor mock
