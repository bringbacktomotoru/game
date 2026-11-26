# Worklog 02 - 2026-06-19: SXR Packer Implementation

## Objetivo
Implementar herramienta para reconstruir bundles SXR desde archivos extraídos.

## Investigación Realizada

### Fuentes Consultadas
1. **extract.py** (proyecto) - Análisis del formato SXR original
2. **fishing_rod** (Codeberg) - Reference extractor para SakanaGL
3. **Format binary analysis** - Comparación de múltiples bundles SXR

### Hallazgos Clave

#### 1. Header Size Corregido
El investigation-report dice "22 bytes header" pero el diagrama dice "20 bytes". La realidad:
```
Header: 22 bytes
  "SXR " (4) + "DEFL" (4) + "&F" (2) + trailer_offset(u64) + trailer_length(u32)
  = 4 + 4 + 2 + 8 + 4 = 22 bytes
```

Verificado con extract.py: `read_u64_be(sxr_data, 10)` y `read_u32_be(sxr_data, 18)`.

#### 2. Data Section Preamble
Los bundles SXR tienen un preámbulo de 44 bytes antes de los datos:
```
Bytes 20-21: trailer_length (u16 BE)
Bytes 22-63: zeros (42 bytes)
```
Verificado con 4 bundles diferentes: todos coinciden.

#### 3. Trailer Encryption Requires Correct Offset
El `decode_trailer()` usa `trailer_offset` para inicializar el cipher XOR. Si se usa offset=0, la decodificación falla.

**Bug corregido**: `build_trailer()` usaba `trailer_offset=0` hardcoded. Ahora recibe el offset correcto.

#### 4. XOR Symmetry
Las funciones de encrypt y decrypt son idénticas (XOR es simétrico). Se reutilizan las funciones del extractor directamente.

## Bugs Encontrados y Corregidos

### Bug 1: Header Size
- **Impacto**: Archivos 2 bytes más grandes de lo esperado
- **Causa**: Usaba 20 bytes en vez de 22
- **Fix**: Cambiar constantes a 22

### Bug 2: Trailer Offset en build_trailer
- **Impacto**: Trailer no se puede decodificar después de repack
- **Causa**: `trailer_offset=0` hardcoded en vez del offset real
- **Fix**: Pasar `trailer_offset` como parámetro

### Bug 3: Data Offset Calculation
- **Impacto**: Entity data_offset incorrecto
- **Causa**: Usaba `20 + 44` en vez de `22 + 44`
- **Fix**: Actualizar cálculo

## Verificación
- Roundtrip extract → repack → extract: archivos idénticos (diff sin output)
- Probado con y sin encryption
- Probado con compresión zlib

## Próximos Pasos
1. Fase 4: APK Patching
2. Integrar repacker con flujo de modificación de URLs
