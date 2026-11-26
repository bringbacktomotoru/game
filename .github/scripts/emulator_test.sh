#!/bin/bash
# =============================================================================
# Emulator Test Script for Tomotoru Offline
# =============================================================================
# Runs inside the Android emulator via reactivecircus/android-emulator-runner.
# Installs the patched APK, launches the game, waits, collects diagnostics.
# =============================================================================
set -uo pipefail

PKG="${PKG:-com.cyberstep.sanriocharacters}"
ACT="${ACT:-com.cyberstep.sanriocharacters.SanrioActivity}"
WAIT_SEC="${WAIT_SEC:-120}"
OUTDIR="/tmp/emulator_diagnostics"
mkdir -p "${OUTDIR}"

echo "=== Tomotoru Emulator Test ==="
echo "  Package: ${PKG}"
echo "  Activity: ${ACT}"
echo "  Wait: ${WAIT_SEC}s"

# ---- Install APK ----
echo "=== Installing APK ==="
adb install -r -t /tmp/tomotoru_offline.apk
echo "APK installed"

# ---- Clear logcat ----
adb logcat -c 2>/dev/null || true

# ---- Launch activity ----
echo "=== Launching activity ==="
adb shell am start -n "${PKG}/${ACT}"
sleep 3

# ---- Get PID ----
PID=$(adb shell pidof "${PKG}" 2>/dev/null || echo "")
echo "App PID: ${PID:-not found}"

if [ -z "${PID}" ]; then
  echo "WARNING: App process not found after launch, waiting..."
  sleep 5
  PID=$(adb shell pidof "${PKG}" 2>/dev/null || echo "")
  echo "App PID (retry): ${PID:-not found}"
fi

# ---- Wait loop ----
echo "=== Waiting ${WAIT_SEC}s ==="
ELAPSED=0
INTERVAL=15
CRASH_DETECTED=0

while [ "${ELAPSED}" -lt "${WAIT_SEC}" ]; do
  REMAINING=$(( WAIT_SEC - ELAPSED ))
  SLEEP_TIME=${INTERVAL}
  if [ "${REMAINING}" -lt "${INTERVAL}" ]; then SLEEP_TIME=${REMAINING}; fi
  sleep "${SLEEP_TIME}"
  ELAPSED=$(( ELAPSED + SLEEP_TIME ))

  # Check if process is still alive
  if [ -n "${PID}" ]; then
    ALIVE=$(adb shell ps -p "${PID}" 2>/dev/null | grep -c "${PID}" || echo "0")
    if [ "${ALIVE}" -eq 0 ]; then
      echo "  [${ELAPSED}s] PID ${PID} died"
      CRASH_DETECTED=1
      PID=$(adb shell pidof "${PKG}" 2>/dev/null || echo "")
      if [ -n "${PID}" ]; then
        echo "  [${ELAPSED}s] New PID: ${PID}"
      fi
    fi
  fi

  # Send tap events to unblock scripts waiting for interaction
  if [ "${ELAPSED}" -ge 15 ]; then
    adb shell input tap 270 480 2>/dev/null || true
    sleep 0.3
    adb shell input tap 270 300 2>/dev/null || true
  fi

  # Capture screenshot periodically
  if [ $(( ELAPSED % 30 )) -eq 0 ]; then
    adb exec-out screencap -p > "${OUTDIR}/screen_${ELAPSED}s.png" 2>/dev/null || true
  fi

  # Check engine logs
  SKLOOP=$(adb logcat -d 2>/dev/null | grep -c "skloop" || echo "0")
  echo "  [${ELAPSED}s] PID=${PID:-none} skloop_count=${SKLOOP}"
done

# ---- Final screenshot ----
adb exec-out screencap -p > "${OUTDIR}/screen_final.png" 2>/dev/null || true

# ---- Collect engine logs ----
echo "=== Sakana engine logs ==="
adb logcat -d 2>/dev/null | grep -iE "sakana|sxr|skinit|sknew|skmain|skloop|skstate|ssconnect|SSConnect|SSBlowfish|setting\.json|pkg\.json" | tail -60 || true

# ---- Process state ----
if [ -n "${PID}" ]; then
  ALIVE=$(adb shell ps -p "${PID}" 2>/dev/null | grep -c "${PID}" || echo "0")
  echo "Process alive: $([ "${ALIVE}" -gt 0 ] && echo YES || echo NO) (PID ${PID})"
fi

# ---- Network connections ----
if [ -n "${PID}" ]; then
  adb shell "cat /proc/net/tcp /proc/net/tcp6 2>/dev/null" | \
    grep -v "local_address" | head -20 > "${OUTDIR}/tcp_connections.txt" 2>/dev/null || true
fi

# ---- Dumpsys ----
adb shell dumpsys activity activities > "${OUTDIR}/dumpsys_activity.txt" 2>/dev/null || true

echo "=== TEST COMPLETE ==="
ls -lhS "${OUTDIR}/"
