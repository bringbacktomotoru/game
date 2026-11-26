# Tomotoru Server Protocol (Reverse-Engineered)

Verified directly from `112.sk.bin` (`GameConnection`), `114.sk.bin` (`GameSession` / `ServerDataLoader`), `144.sk.bin` (`PackageUpdator`), `176.sk.bin` (`UserData`), `193.sk.bin` (`ServletConstants`), and `194.sk.bin` (URLs).

## Original Server URLs

| Source | Variable | Original Value |
|--------|----------|----------------|
| `1-011b.sxr` (`194.sk`) | `SERVER_URL` | `http://13.113.101.2:8080` |
| `1-011b.sxr` (`194.sk`) | `INSPECT_SERVER_URL` | `http://52.194.165.198:8080` |
| `main.sxr` (`sakana.proj`) & `0-0111.sxr` (`1052`) | `serverurl` | `http://52.69.85.104:8080` |
| `main.sxr` (`sakana.proj`) & `0-0111.sxr` (`1052`) | `pkgurl` | `http://download.tomotoru.cyberstep.com/bin/main` |
| `main.sxr` (`sakana.proj`) & `0-0111.sxr` (`1052`) | `inspect_pkgurl` | `http://download.tomotoru.cyberstep.com/bin/bin_inspect` |

## Wire Encryption (`GameConnection.encrypt` / `decrypt`)

- **Cipher**: Blowfish ECB (8-byte blocks)
- **Key**: `b"test"` (string index 17 in `112.sk.bin`: `Blowfish("test")`)
- **Frame layout (before Blowfish)**:
  1. `flag` (1 byte): `0` = raw UTF-8 JSON, `1` = zlib-compressed UTF-8 JSON (used when JSON length >= 10240 bytes)
  2. `size` (4 bytes, big-endian `>I`): byte length of the (possibly compressed) payload
  3. `payload` (`size` bytes)
  4. Zero padding (0..7 bytes) to 8-byte block boundary (client calls `ebb.resize(size + 5)` after decrypting)
- **Transport**: Base64-encoded ASCII string in the HTTP POST body

## Endpoints

### Package & Maintenance Checks (Plain JSON, GET)

- `GET /setting.json` (and `/dl/setting.json`)
  ```json
  {
    "release_pkg_version": 1027,
    "min_pkg_version": 1027,
    "min_sk_ver_ios": 0,
    "min_sk_ver_android": 0,
    "mode": "0",
    "debug_friendcode": "",
    "server_time": 1700000000
  }
  ```
  (`mode == "1"` triggers maintenance mode in `PackageUpdator.check()`.)
- `GET /pkg.json` (and `/dl/pkg.json`) — SXR manifest with `version`, `mainsxr`, and `sxrlist`
- `GET /dl/<filename>` — Serves SXR bundle files

### Game API (Encrypted Blowfish + Base64, POST)

All responses from `/app_kitty/entrance`, `/app_kitty/main`, and `/app_kitty/report` are **JSON arrays containing a single response object**: `[ { "ret": 1, "server_time": ..., ... } ]`.

#### `POST /app_kitty/entrance`
- **Request**: `{"uuid": "...", "cmd": 0, "token_id": "...", "platform_id": 1, "room_index": 0, "sk_ver": ...}`
- **Response** (`ENTRANCE_CMD_AUTH = 0`):
  ```json
  [{
    "cmd": 0,
    "ret": 1,
    "server_time": 1700000000,
    "uuid": "...",
    "sessionId": "...",
    "first_login": true,
    "userdata": [ ... ]
  }]
  ```
  `userdata` is a flat sequential array read by `ServerDataLoader` inside `UserData.load()` (`176.sk.bin`).

#### `POST /app_kitty/main`
- **Request**: `{"sid": "<sessionId>", "cmd": <int>, "params": <dict|int>, "seq": <int>}`
- **Response**: `[{"cmd": <int>, "ret": 1, "server_time": <int>, ...}]`

#### `POST /app_kitty/report`
- **Request**: `{"uuid": "...", "cmd": 0, "params": ...}`
- **Response**: `[{"ret": 1, "server_time": <int>}]`

## Response Codes (`ServletConstants` in `193.sk.bin`)

| Constant | Value |
|----------|-------|
| `RESPONSE_SUCCEED` | `1` |
| `RESPONSE_CONNECTION_FAIL` | `0` |
| `RESPONSE_ERROR_OTHERS` | `-1` |
| `RESPONSE_SESSION_CLOSED` | `-2` |
| `RESPONSE_MAINTENANCE` | `-3` |
| `RESPONSE_CONGESTION` | `-4` |
| `RESPONSE_UNAVAILABLE` | `-5` |
| `RESPONSE_INVALID_REQUEST` | `-6` |
| `RESPONSE_NOT_ALLOWED` | `-7` |
| `RESPONSE_REJECT_WITH_MESSAGE` | `-8` |
| `RESPONSE_INCOMPLETE` | `-9` |
| `RESPONSE_TAKEOVER_MISS` | `-100` |
