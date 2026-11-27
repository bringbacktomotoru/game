"""Entrance/auth endpoint handlers (verified against 112.sk.sq, 114.sk.sq, 323.sk.sq)."""
import uuid

from flask import current_app, has_app_context

from server.python.handlers import register_entrance
from server.python.persistence import (
    create_default_user,
    game_server_time,
    load_or_create_user,
    serialize_userdata,
)

# ServletConstants from 193.sk.sq
ENTRANCE_CMD_AUTH = 0
ENTRANCE_CMD_AGREED_TOS = 1
ENTRANCE_CMD_TAKEOVER_ISSUE = 10
ENTRANCE_CMD_TAKEOVER_USE = 11
ENTRANCE_CMD_DEBUG_NEW_DATA = 900
ENTRANCE_CMD_DEBUG_TEST_DATA = 901
ENTRANCE_CMD_DEBUG_TUTORIALEND_DATA = 902
ENTRANCE_CMD_DEBUG_COMPLETE_DATA = 903

RESPONSE_SUCCEED = 1
RESPONSE_TAKEOVER_MISS = -100

TAKEOVER_STORE_KEY = "_takeover_codes"


def _make_entrance_response(cmd: int, data: dict | None = None, ret: int = RESPONSE_SUCCEED) -> dict:
    resp = {
        "cmd": cmd,
        "ret": ret,
        "server_time": game_server_time(),
    }
    if data:
        resp.update(data)
    return resp


def _is_debug_mode() -> bool:
    if has_app_context():
        return bool(current_app.config.get("DEBUG_MODE", False))
    return False


@register_entrance(ENTRANCE_CMD_AUTH)
def handle_auth(store, params):
    uid = params.get("uuid") or str(uuid.uuid4())
    session_id = str(uuid.uuid4())

    ud, is_new = load_or_create_user(store, uid, debug=_is_debug_mode())
    ud["last_login"] = game_server_time()
    store.save(uid, ud)

    return _make_entrance_response(
        ENTRANCE_CMD_AUTH,
        {
            "uuid": uid,
            "sessionId": session_id,
            "addr": None,
            "first_login": is_new,
            "userdata": serialize_userdata(ud),
        },
    )


@register_entrance(ENTRANCE_CMD_AGREED_TOS)
def handle_agreed_tos(store, params):
    return _make_entrance_response(ENTRANCE_CMD_AGREED_TOS)


@register_entrance(ENTRANCE_CMD_TAKEOVER_ISSUE)
def handle_takeover_issue(store, params):
    uid = params.get("uuid") or str(uuid.uuid4())
    password = str(params.get("password", ""))
    takeover_code = f"TK{uuid.uuid4().hex[:8].upper()}"

    codes = store.load(TAKEOVER_STORE_KEY, default={})
    codes[takeover_code] = {"uuid": uid, "password": password, "created_at": game_server_time()}
    store.save(TAKEOVER_STORE_KEY, codes)

    return _make_entrance_response(
        ENTRANCE_CMD_TAKEOVER_ISSUE,
        {
            "takeover_code": takeover_code,
        },
    )


@register_entrance(ENTRANCE_CMD_TAKEOVER_USE)
def handle_takeover_use(store, params):
    takeover_code = str(params.get("takeover_code", ""))
    password = str(params.get("password", ""))

    codes = store.load(TAKEOVER_STORE_KEY, default={})
    entry = codes.get(takeover_code)
    if not entry or entry.get("password") != password:
        return _make_entrance_response(ENTRANCE_CMD_TAKEOVER_USE, ret=RESPONSE_TAKEOVER_MISS)

    return _make_entrance_response(
        ENTRANCE_CMD_TAKEOVER_USE,
        {
            "uuid": entry["uuid"],
        },
    )


@register_entrance(ENTRANCE_CMD_DEBUG_NEW_DATA)
@register_entrance(ENTRANCE_CMD_DEBUG_TEST_DATA)
@register_entrance(ENTRANCE_CMD_DEBUG_TUTORIALEND_DATA)
@register_entrance(ENTRANCE_CMD_DEBUG_COMPLETE_DATA)
def handle_debug_auth(store, params):
    cmd = int(params.get("cmd", ENTRANCE_CMD_DEBUG_NEW_DATA))
    uid = params.get("uuid") or str(uuid.uuid4())
    session_id = str(uuid.uuid4())

    ud = create_default_user(uid)
    if cmd in (ENTRANCE_CMD_DEBUG_TUTORIALEND_DATA, ENTRANCE_CMD_DEBUG_COMPLETE_DATA):
        ud["end_tutorial_ids"] = [1, 2, 3, 4, 5]
    if cmd == ENTRANCE_CMD_DEBUG_COMPLETE_DATA:
        ud["gmoney"] = 999999
        ud["room_edits"][0]["gmoney"] = 999999
        ud["freerm"] = 9999
    store.save(uid, ud)

    return _make_entrance_response(
        cmd,
        {
            "uuid": uid,
            "sessionId": session_id,
            "addr": None,
            "first_login": True,
            "userdata": serialize_userdata(ud),
        },
    )
