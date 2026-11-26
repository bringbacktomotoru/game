# Worklog 01 - 2026-06-19: Mock Server Core Implementation

## Objetivo
Implementar el mock server core con Blowfish encryption, response codes correctos, auth flow y persistencia JSON.

## Investigación Realizada

### Fuentes Consultadas
1. **PyCryptodome docs** - Blowfish ECB mode implementation
2. **zombie-cafe-revival** - Mock server architecture for game revival
3. **girl-alone-revival** - JSON persistence patterns
4. **Belfast (Azur Lane)** - Session management, response envelope patterns
5. **Flask docs** - Multipart form data parsing
6. **Stack Overflow** - Blowfish + Base64 encoding patterns

### Hallazgos Clave

#### 1. Protocolo de Encriptación
- **Key**: `17` (hardcodeada en `112.sk.sq`)
- **Modo**: ECB (sin IV, key de 1 byte)
- **Pipeline request**: JSON → compress(zlib si >= 10240) → Blowfish encrypt → Base64 → multipart form
- **Pipeline response**: Base64 → Blowfish decrypt → decompress → JSON

#### 2. Response Codes (Corregidos del investigation-report)
El investigation-report tenía los códigos intercambiados. Los correctos de `193.sk.sq`:
```squirrel
RESPONSE_SUCCEED = 1;      // No 0
RESPONSE_CONNECTION_FAIL = 0;
RESPONSE_ERROR_OTHERS = -1;
RESPONSE_SESSION_CLOSED = -2;
RESPONSE_MAINTENANCE = -3;
// ... etc
```

#### 3. Formato de Respuesta
- Keys numéricas para campos de datos: `72` = result code, `146` = time offset
- La respuesta se accede como `ret[0][72]` en el código del juego
- El server retorna JSON con keys numéricas como strings

#### 4. Persistencia
- JSON files con atomic writes (temp file + rename)
- Un archivo por jugador: `{uuid}.json`
- Incluir version para migraciones futuras

## Archivos Creados/Modificados

### Nuevos
- `tools/server/crypto.py` - Blowfish encrypt/decrypt
- `tools/server/persistence/__init__.py` - Package init
- `tools/server/persistence/json_store.py` - JSON file persistence
- `tools/server/handlers/__init__.py` - Handler registry
- `tools/server/handlers/auth.py` - Entrance/auth handlers
- `tools/server/handlers/main.py` - Main game commands (109+ handlers)
- `tools/server/data/game_settings.py` - Game settings from decompiled scripts
- `docs/worklog/` - Directorio de worklogs

### Modificados
- `pyproject.toml` - Added pycryptodome dependency
- `opencode.json` - Permisos totales

## Estado
- [x] Crypto module (Blowfish ECB)
- [x] JSON persistence store
- [x] Handler registry pattern
- [x] Auth handlers (entrance)
- [x] Main handlers (109+ commands)
- [x] Game settings data
- [ ] Main Flask app (mock.py rewrite)
- [ ] Testing
- [ ] Documentación del protocolo

## Próximos Pasos
1. Reescribir `mock.py` con la nueva arquitectura
2. Testear el server
3. Documentar el formato exacto del protocolo
