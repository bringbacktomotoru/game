# Engineering Notes

## Game Overview

- **App**: Tomotoru (Sanrio Characters fishing game)
- **Package**: `com.cyberstep.sanriocharacters`
- **Engine**: SakanaGL (`ls.sakana.SakanaApplication`)
- **Scripting**: Squirrel 3.x (compiled to `.sk`/`.sk.bin` bytecode)
- **Assets**: SXR bundles (custom encrypted archives)

## SXR Format

SXR files use a custom archive format with:

1. **Header**: Magic `SXR `, crypter `DEFL`, magic code `&F`, trailer offset/length
2. **Trailer**: XOR-encrypted metadata (decoded with state machine using header offset as key)
3. **Entities**: Each entry has name, data offset, encryption flag, compression type
4. **Encryption**: Per-entity XOR cipher using FNV-1a hash of entity name as key
5. **Compression**: zlib (type 6) or LZ4 raw block (type 10)

### Entity Structure

```
[child_count:u16][name_size:u16][name:utf8][unknown:8bytes]
[data_offset:u64][data_size:u32][encrypted:u8][compression:u8][decompressed_size:u32]
```

## SXR Bundle Inventory

9 SXR bundles extracted from the APK:

| Bundle | Files | Content |
|--------|-------|---------|
| `main.sxr` | 92 | Engine core: UI widgets, 3D, audio, networking, serialization |
| `1-011b.sxr` | 434 | Game logic: auth, network protocol, gacha, missions, shop |
| `0-0111.sxr` | 1055 | Game data: character models, furniture, scripts, initialization |
| `7-010d.sxr` | 241 | Audio/voice + 3D models + images |
| `a-0108.sxr` | 641 | AR photo UI + configuration JSON + images |
| `9-0101.sxr` | 132 | Scene/cutscene scripts (visual novel format) |
| `5-0109.sxr` | 12 | Image assets (PNG/JPG thumbnails) |
| `6-0100.sxr` | 5 | Unity asset bundle metadata (JSON) |
| `b-010c.sxr` | 3 | Video (MP4) + audio (OGG) |

## Squirrel Bytecode (.sk / .sk.bin)

CyberStep modified Squirrel 3.x bytecode:

- **Magic**: `0xFAFA` header
- **Format**: Header → func_data (serialized AST) → string table
- ** func_data tags**: `FE` = function start, `FD` = section end, `80` = string ref, `81` = u16, `82` = u32
- **FD section types**: `0x99` local, `0x9C` return, `0x8E` if, `0x90` while, `0x95` foreach, `0x92` for, `0xA3` try, `0x9B` function, `0x8A` switch

### Key Decompiled Scripts

#### Network & Auth (`1-011b/112.sk.sq`)
- `GameConnection` class: HTTP client with Blowfish encryption
- Auth flow: entrance → auth → main server
- Takeover code system (password-based account recovery)
- Command queue with sequence numbers

#### Game Settings (`1-011b/114.sk.sq`)
- Main servlet API commands (buy, sell, gacha, missions, etc.)
- Room/furniture management
- Costume actions and rewards
- Panel missions and diary system

#### Server URLs (`1-011b/194.sk.sq`)
- Entrance URL, main URL, report URL endpoints

#### Game Init (`0-0111/3.sk.sq`)
- Screen setup, sound initialization
- GameSession creation and main loop
- Debug mode and argument parsing

#### Character Model (`0-0111/4.sk.sq`)
- Character model structure (body, face, hands)
- Goods/inventory system

## Scene Script Format (9-0101)

Custom visual novel-style scripting language for cutscenes/events:

```
; Comments start with ;
[playbgm bgm="BGM01"]           ; Play background music
[changeroom id=1]                ; Switch room
[popchara costume=109001 x=-10 y=2 z=1 angle=0]  ; Spawn character
[popfurniture fname=chair1 id=803 x=-9 y=2 z=2]   ; Place furniture
[startanime name=kitty time=2000 script=piano_play] ; Start animation
[waitaction name=kitty script=piano_play]           ; Wait for anim
[movecamera type=0 x=5 y=3 z=-15 time=0]          ; Move camera
[rotatecamera pitch=5 head=0 time=0]               ; Rotate camera
[wait time=1000]                 ; Delay
[shutterstart] / [shutterend]   ; Scene transition
[fadeoutbgm spd=12]             ; Fade music
[usefurniture name=kitty fname=chair1 bone=chara1 script=event_start]  ; Use furniture
[turnchara name=kitty angle=50]  ; Turn character
[balloonoffset]                  ; Dialog balloon
```

### Scene Commands Reference

| Command | Parameters | Description |
|---------|-----------|-------------|
| `playbgm` | `bgm` | Play background music |
| `changeroom` | `id` | Switch to room |
| `popchara` | `costume, x, y, z, angle, relative` | Spawn character |
| `popfurniture` | `fname, id, x, y, z, angle, relative` | Place furniture |
| `startanime` | `name, time, script, script_args` | Start animation |
| `waitaction` | `name, script` | Wait for animation |
| `movecamera` | `type, x, y, z, time` | Move camera |
| `rotatecamera` | `pitch, head, time` | Rotate camera |
| `wait` | `time` | Delay (ms) |
| `shutterstart/end` | — | Scene transition |
| `fadeoutbgm` | `spd` | Fade music out |
| `usefurniture` | `name, fname, bone, script, script_args` | Use furniture |
| `charavisible` | `name, visible` | Show/hide character |
| `furniturevisible` | `name, visible` | Show/hide furniture |
| `turnchara` | `name, angle, script, time` | Turn character |
| `movechara` | `name, type, x, z, script, time` | Move character |
| `focusdoor` | — | Focus on door (transition) |
| `balloonoffset` | — | Reset dialog balloon |

## UI Script Format (a-0108/0.bin)

Custom UI scripting for AR photo panel:

```javascript
function setup() {
    $front_load_icon.visible = false;    // $ prefix = UI element
}
function loading(){
    $front_load_icon.anime(10,20,5,true);  // anime(from, to, speed, loop)
}
```

## Token Table (.sk bytecode)

Byte values mapped to Squirrel tokens (reverse-engineered from libsakanagl.so):

- `0x28-0x7E`: ASCII punctuation/operators (direct mapping)
- `0x80`: IDENTIFIER, `0x81`: LABEL, `0x82`: INTEGER, `0x83`: STRING
- `0x84-0xCB`: Keywords (delete, if, else, while, for, function, return, class, etc.)
- `0x86-0xC1`: Multi-char operators (==, !=, <=, >=, ::, in, ||, &&, etc.)

## Server Protocol

Original servers (now offline):
- `http://13.113.101.2:8080` — Main server
- `http://52.194.165.198:8080` — Inspect server
- `download.tomotoru.cyberstep.com` — Package downloads

### Response Codes

| Code | Name | Description |
|------|------|-------------|
| 0 | SUCCEED | Success |
| 1 | CONNECTION_FAIL | Connection failed |
| 2 | ERROR_OTHERS | Other error |
| 3 | SESSION_CLOSED | Session expired |
| 4 | MAINTENANCE | Server maintenance |
| 5 | CONGESTION | Server overloaded |
| 6 | UNAVAILABLE | Service unavailable |
| 7 | INVALID_REQUEST | Bad request |
| 8 | NOT_ALLOWED | Action not allowed |
| 9 | REJECT_WITH_MESSAGE | Rejected with message |
| 10 | INCOMPLETE | Incomplete request |
| 11 | TAKEOVER_MISS | Account takeover failed |

## File Type Detection

Magic bytes used in `tools/extractor/extract.py`:

| Magic | Type |
|-------|------|
| `\x89PNG` | PNG image |
| `\xff\xd8\xff` | JPEG image |
| `OggS` | OGG audio |
| `RIFF...WAVE` | WAV audio |
| `SMOT` | SMOT video |
| `SMDL` | SMDL music |
| `\xfa\xfa` | Squirrel bytecode |
| `SQIR` | Nut bytecode |
| `ftypisom` | MP4 video |

## Decompiler Improvements

### Batch Processing (`tools/decompile_all.py`)
- Processes all `.sk.bin` and `.sk` files from extracted bundles
- Outputs to `decompiled/squirrel/{bundle}/` directories
- Supports incremental mode (skips existing files)

### Quality Fixes (`tools/decompiler/sk_decompiler.py`)
- Fixed condition parentheses: `if (cond)` instead of `if ((cond))`
- Fixed `_collect_condition()` to skip opening paren
- Cleaned up switch/while/foreach condition formatting

### Known Limitations
- `in` keyword (0x9D) used for both slot access and assignment-like operations
- Some function bodies appear empty due to AST parsing limitations
- Nested closures and complex expressions may lose structure

## Workflow

1. Decompile APK with jadx → `decompiled/java/`
2. Extract SXR bundles → `extracted/`
3. Decompile Squirrel bytecode → `decompiled/squirrel/` (1124 files)
4. Extract scene scripts from `9-0101/*.bin` (132 cutscene scripts)
5. Extract UI scripts from `a-0108/*.bin` (AR panel, configs)
6. Document protocol from bytecode analysis
7. Build mock server to replace dead backend
