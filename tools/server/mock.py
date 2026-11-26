#!/usr/bin/env python3
"""Mock server for Tomotoru — implements the real SakanaGL / app_kitty protocol."""
import argparse
import json
import logging
import os
import time
import uuid
from datetime import datetime
from typing import Any

from flask import Flask, Response, jsonify, request, send_file

from tools.server.crypto import decrypt_payload, encrypt_payload
from tools.server.handlers import get_entrance_handler, get_main_handler
from tools.server.persistence import JSONStore

# Import handlers to register them via decorators
import tools.server.handlers.auth as auth_handlers  # noqa: F401
import tools.server.handlers.main as main_handlers  # noqa: F401

_ENTRANCE_CMD_NAMES = {
    v: k for k, v in vars(auth_handlers).items() if k.startswith("ENTRANCE_CMD_") and isinstance(v, int)
}
_MAIN_CMD_NAMES = {
    v: k for k, v in vars(main_handlers).items() if k.startswith("MAIN_CMD_") and isinstance(v, int)
}


def _log(tag: str, message: str) -> None:
    ts = datetime.now().strftime("%H:%M:%S")
    print(f"[{ts}] [{tag:<8}] {message}", flush=True)

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.dirname(os.path.dirname(BASE_DIR))
SXR_DIR = os.path.join(PROJECT_ROOT, "sxr")
SAVES_DIR = os.path.join(BASE_DIR, "saves")

# Response codes from 193.sk.bin (ServletConstants)
RESPONSE_SUCCEED = 1
RESPONSE_CONNECTION_FAIL = 0
RESPONSE_ERROR_OTHERS = -1
RESPONSE_SESSION_CLOSED = -2
RESPONSE_MAINTENANCE = -3
RESPONSE_CONGESTION = -4
RESPONSE_UNAVAILABLE = -5
RESPONSE_INVALID_REQUEST = -6
RESPONSE_NOT_ALLOWED = -7
RESPONSE_REJECT_WITH_MESSAGE = -8
RESPONSE_INCOMPLETE = -9
RESPONSE_ERROR_OTHER = -10
RESPONSE_TAKEOVER_MISS = -100


def create_app(saves_dir: str = SAVES_DIR, sxr_dir: str = SXR_DIR, debug: bool = False) -> Flask:
    """Application factory for the Tomotoru mock server."""
    flask_app = Flask(__name__)
    store = JSONStore(saves_dir)
    sessions: dict[str, str] = {}  # sessionId -> uuid

    flask_app.config["STORE"] = store
    flask_app.config["SESSIONS"] = sessions
    flask_app.config["SXR_DIR"] = sxr_dir
    flask_app.config["DEBUG_MODE"] = bool(debug)

    def _parse_request() -> tuple[dict[str, Any], bool]:
        """Parse incoming client request. Returns (payload_dict, was_encrypted)."""
        content_type = request.content_type or ""

        if "multipart/form-data" in content_type:
            params = request.form.get("params", "")
            if params:
                try:
                    return decrypt_payload(params), True
                except Exception:
                    try:
                        return json.loads(params), False
                    except Exception:
                        return {}, False
            return dict(request.form), False

        raw = request.get_data(as_text=True).strip()
        if not raw:
            return {}, False

        # Try plain JSON first if it clearly starts with '{' or '['
        if raw[0] in ("{", "["):
            try:
                parsed = json.loads(raw)
                return (parsed if isinstance(parsed, dict) else {}), False
            except Exception:
                pass

        try:
            parsed = decrypt_payload(raw)
            return (parsed if isinstance(parsed, dict) else {}), True
        except Exception:
            try:
                parsed = json.loads(raw)
                return (parsed if isinstance(parsed, dict) else {}), False
            except Exception:
                return {}, False

    def _wrap_response(result_obj: dict[str, Any]) -> Response:
        """GameConnection expects a JSON array `[ { ... } ]` encrypted with Blowfish+Base64."""
        payload = result_obj if isinstance(result_obj, list) else [result_obj]
        encoded = encrypt_payload(payload)
        return Response(encoded, content_type="text/plain")

    def _setting_json():
        # Field names verified from 144.sk.bin (PackageUpdator.check)
        pkg_version = 1027
        pkg_path = os.path.join(sxr_dir, "pkg.json")
        if os.path.exists(pkg_path):
            try:
                with open(pkg_path, "r", encoding="utf-8") as f:
                    pkg_version = json.load(f).get("version", 1027)
            except Exception:
                pass
        return jsonify(
            {
                "release_pkg_version": pkg_version,
                "min_pkg_version": pkg_version,
                "min_sk_ver_ios": 0,
                "min_sk_ver_android": 0,
                "mode": 0,
                "debug_friendcode": "",
                "server_time": int(time.time()),
            }
        )

    def _pkg_json():
        # Prefer pkg.json from the patched APK if present so SXR sizes match the device's cache
        pkg = None
        patched_apk = os.path.join(PROJECT_ROOT, "apk", "tomotoru_offline.apk")
        if os.path.isfile(patched_apk):
            try:
                import zipfile

                with zipfile.ZipFile(patched_apk, "r") as z:
                    pkg = json.loads(z.read("assets/sxr/pkg.json").decode("utf-8"))
            except Exception:
                pkg = None

        if pkg is None:
            pkg_path = os.path.join(sxr_dir, "pkg.json")
            if os.path.exists(pkg_path):
                with open(pkg_path, "r", encoding="utf-8") as f:
                    pkg = json.load(f)

        if pkg is not None:
            base_url = request.host_url.rstrip("/")
            if "mainsxr" in pkg:
                pkg["mainsxr"]["url"] = f"{base_url}/dl/main.sxr"
            for sxr in pkg.get("sxrlist", []):
                sxr["url"] = f"{base_url}/dl/{sxr['sxrname']}-{sxr['version']:04x}.sxr"
            return jsonify(pkg)
        return jsonify({"error": "pkg.json not found"}), 404

    @flask_app.route("/setting.json", methods=["GET"])
    @flask_app.route("/dl/setting.json", methods=["GET"])
    def get_setting():
        _log("HTTP", f"GET {request.path} -> 200")
        return _setting_json()

    @flask_app.route("/pkg.json", methods=["GET"])
    @flask_app.route("/dl/pkg.json", methods=["GET"])
    def get_pkg():
        _log("HTTP", f"GET {request.path} -> 200")
        return _pkg_json()

    @flask_app.route("/conf/gamesetting.conf", methods=["GET"])
    def get_gamesetting():
        return jsonify({"version": 1027, "language": "JP", "timezone": "UTC", "debug": False})

    @flask_app.route("/conf/<path:confname>", methods=["GET"])
    def get_conf(confname):
        return jsonify({"version": 1027, "config": confname})

    @flask_app.route("/app_kitty/entrance", methods=["GET", "POST"])
    def entrance():
        params, was_enc = _parse_request()
        cmd = int(params.get("cmd", 0))
        cmd_name = _ENTRANCE_CMD_NAMES.get(cmd, f"CMD_{cmd}")
        uid_in = params.get("uuid")

        handler = get_entrance_handler(cmd)
        if handler:
            result = handler(store, params)
        else:
            uid = uid_in or str(uuid.uuid4())
            session_id = str(uuid.uuid4())
            result = {
                "cmd": cmd,
                "ret": RESPONSE_SUCCEED,
                "server_time": int(time.time()),
                "uuid": uid,
                "sessionId": session_id,
            }

        # Record sessionId -> uuid mapping for subsequent /main calls
        sid = result.get("sessionId")
        uid = result.get("uuid") or uid_in
        if sid and uid:
            sessions[sid] = uid

        sid_short = sid[:8] if isinstance(sid, str) else "-"
        uid_short = str(uid)[:8] if uid else "-"
        _log(
            "ENTRANCE",
            f"{cmd_name}({cmd}) enc={was_enc} uid={uid_short} -> ret={result.get('ret')} sid={sid_short}",
        )
        if flask_app.config["DEBUG_MODE"]:
            _log("DEBUG", f"ENTRANCE params={json.dumps(params, ensure_ascii=False)}")
        return _wrap_response(result)

    @flask_app.route("/app_kitty/main", methods=["GET", "POST"])
    def main_endpoint():
        req_data, was_enc = _parse_request()
        cmd = int(req_data.get("cmd", 900))
        cmd_name = _MAIN_CMD_NAMES.get(cmd, f"CMD_{cmd}")
        sid = req_data.get("sid", "")
        uid = sessions.get(sid) or req_data.get("uuid") or "default"
        cmd_params = req_data.get("params")
        if cmd_params is None:
            cmd_params = {}

        handler = get_main_handler(cmd)
        if handler:
            result = handler(store, cmd_params, uid)
        else:
            result = {
                "cmd": cmd,
                "ret": RESPONSE_SUCCEED,
                "server_time": int(time.time()),
            }

        uid_short = str(uid)[:8]
        params_full = json.dumps(cmd_params, ensure_ascii=False)
        params_preview = params_full
        if not flask_app.config["DEBUG_MODE"] and len(params_preview) > 120:
            params_preview = params_preview[:117] + "..."
        _log(
            "MAIN",
            f"{cmd_name}({cmd}) uid={uid_short} params={params_preview} -> ret={result.get('ret')}",
        )
        if flask_app.config["DEBUG_MODE"]:
            _log("DEBUG", f"MAIN req={json.dumps(req_data, ensure_ascii=False)}")
        return _wrap_response(result)

    @flask_app.route("/app_kitty/report", methods=["GET", "POST"])
    def report():
        data, was_enc = _parse_request()
        uid_short = str(data.get("uuid", "-"))[:8]
        params = data.get("params") if isinstance(data, dict) else None
        log_text = params.get("log") if isinstance(params, dict) else None
        debug_mode = flask_app.config["DEBUG_MODE"]
        if isinstance(log_text, str) and log_text.strip():
            if debug_mode:
                print("=" * 60, flush=True)
            _log("REPORT", f"uid={uid_short} enc={was_enc}")
            for line in log_text.rstrip().splitlines():
                prefix = ">>> " if debug_mode else "    | "
                print(f"{prefix}{line}", flush=True)
            if debug_mode:
                print("=" * 60, flush=True)
        else:
            payload_str = json.dumps(data, ensure_ascii=False)
            if not debug_mode:
                payload_str = payload_str[:400]
            if debug_mode:
                print("=" * 60, flush=True)
            _log("REPORT", f"uid={uid_short} enc={was_enc} data={payload_str}")
            if debug_mode:
                print("=" * 60, flush=True)
        return _wrap_response({"ret": RESPONSE_SUCCEED, "server_time": int(time.time())})

    @flask_app.route("/dl/<filename>", methods=["GET"])
    def download_file(filename):
        safe_name = os.path.basename(filename)
        filepath = os.path.join(sxr_dir, safe_name)
        if os.path.isfile(filepath):
            return send_file(filepath, as_attachment=True)
        return jsonify({"error": "File not found"}), 404

    @flask_app.route("/health", methods=["GET"])
    def health():
        return jsonify({"status": "healthy"})

    return flask_app


app = create_app()


def main():
    parser = argparse.ArgumentParser(description="Tomotoru Offline Mock Server")
    parser.add_argument("--host", default="0.0.0.0", help="Bind host (default: 0.0.0.0)")
    parser.add_argument("--port", type=int, default=5000, help="Bind port (default: 5000)")
    parser.add_argument("--saves-dir", default=SAVES_DIR, help="Directory for player save files")
    parser.add_argument(
        "--debug",
        action="store_true",
        help="Enable debug mode (tutorial-skip saves, verbose request logs, highlighted reports)",
    )
    args = parser.parse_args()

    # Suppress noisy default Werkzeug access logs; we emit structured logs above
    logging.getLogger("werkzeug").setLevel(logging.WARNING)

    server_app = create_app(saves_dir=args.saves_dir, debug=args.debug)
    mode_str = "DEBUG (tutorial-skip + verbose logs)" if args.debug else "NORMAL"
    print("=" * 60)
    print("Tomotoru Offline Mock Server")
    print("=" * 60)
    print(f"  Mode:       {mode_str}")
    print(f"  Listening:  http://{args.host}:{args.port}")
    print(f"  Settings:   http://127.0.0.1:{args.port}/setting.json")
    print(f"  Packages:   http://127.0.0.1:{args.port}/pkg.json")
    print(f"  Entrance:   http://127.0.0.1:{args.port}/app_kitty/entrance")
    print(f"  Main:       http://127.0.0.1:{args.port}/app_kitty/main")
    print(f"  Report:     http://127.0.0.1:{args.port}/app_kitty/report")
    print(f"  Health:     http://127.0.0.1:{args.port}/health")
    print("=" * 60)
    server_app.run(host=args.host, port=args.port, debug=args.debug)


if __name__ == "__main__":
    main()
