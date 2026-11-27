"""Main endpoint handlers for game commands.

All response keys, types, and request param shapes audited against clean
decompiled Squirrel scripts (114.sk.sq, 123.sk.sq, 176.sk.sq, 181.sk.sq,
193.sk.sq, 214.sk.sq, 262.sk.sq, 375.sk.sq, 376.sk.sq, 388.sk.sq, 395.sk.sq, 424.sk.sq).
"""
import json
import random

from server.python.data.game_settings import get_game_rule
from server.python.handlers import register_main
from server.python.persistence import game_server_time, load_or_create_user, serialize_userdata

# ServletConstants from 193.sk.sq
MAIN_CMD_SAVE_USERDATA = -1
MAIN_CMD_SEND_LOG = 1
MAIN_CMD_CLIENT_REPORT = 2
MAIN_CMD_UPDATE_USERDATA = 10
MAIN_CMD_LOAD_CONF = 100
MAIN_CMD_END_TUTORIAL = 200
MAIN_CMD_TUTORIAL_ADD_HAPINESS = 201
MAIN_CMD_SESSION_CHECK = 300
MAIN_CMD_INPUT_PLAYERINFO = 400
MAIN_CMD_GET_ALL = 900
MAIN_CMD_GET_RM = 901
MAIN_CMD_GET_GM = 902
MAIN_CMD_REGISTER_TOKEN = 903
MAIN_CMD_SELECT_FIRST_COSTUME = 1100
MAIN_CMD_WORK_START = 1101
MAIN_CMD_WORK_UPDATE = 1102
MAIN_CMD_WORK_COMPLETE = 1103
MAIN_CMD_WORK_CANCEL = 1104
MAIN_CMD_WORK_SHORTENING = 1105
MAIN_CMD_LEVEL_UP_COSTUME = 1106
MAIN_CMD_PAT_COSTUME = 1107
MAIN_CMD_RENTAL_COSTUME = 1108
MAIN_CMD_COSTUMEREQUEST_RECIEVE = 1150
MAIN_CMD_COSTUMEREQUEST_TIMEOUT = 1151
MAIN_CMD_COSTUMEREQUEST_FIRST = 1152
MAIN_CMD_PRODUCT_UPDATE = 1250
MAIN_CMD_PRODUCT_START = 1251
MAIN_CMD_PRODUCT_COMPLETE = 1252
MAIN_CMD_PRODUCT_LEVELUP_START = 1253
MAIN_CMD_PRODUCT_LEVELUP_COMPLETE = 1254
MAIN_CMD_PRODUCT_SHORTEN = 1255
MAIN_CMD_PRODUCT_CANCEL = 1256
MAIN_CMD_PRODUCT_DELIVER_COMPLETE = 1257
MAIN_CMD_PRODUCT_FINISH = 1258
MAIN_CMD_PRODUCT_RECIEVE_TIMEOUT = 1259
MAIN_CMD_PRODUCT_TAP = 1260
MAIN_CMD_PRODUCT_REWARDSTATE_CHANGE = 1261
MAIN_CMD_LAYOUT_EDIT = 1300
MAIN_CMD_LAYOUT_REMOVE = 1301
MAIN_CMD_LAYOUT_BUY = 1302
MAIN_CMD_LAYOUT_BUY_NO_DELIVER = 1303
MAIN_CMD_USE_ONCE_ITEM = 1400
MAIN_CMD_USE_BOOST_ITEM = 1401
MAIN_CMD_COMUNICATION_TAP = 1500
MAIN_CMD_ADD_POOL_HAPINESS = 1501
MAIN_CMD_PANELMISSION_SELECT = 1600
MAIN_CMD_PANELMISSION_SEND_PROGRESS = 1601
MAIN_CMD_PANELMISSION_SETPIECE = 1605
MAIN_CMD_PANELMISSION_SHOWEDEVENT = 1606
MAIN_CMD_PANELMISSION_RECEIVE_REWARD = 1607
MAIN_CMD_SELECT_REPLYKEY = 1650
MAIN_CMD_RECIEVE_AD_VIDEO_REWARD = 1900
MAIN_CMD_ADD_ROOM_CHARA = 2000
MAIN_CMD_REMOVE_ROOM_CHARA = 2001
MAIN_CMD_CHANGE_ROOM_CHARA = 2002
MAIN_CMD_SELL_FURNITURE = 3000
MAIN_CMD_SELL_ROOM = 3001
MAIN_CMD_SHOP_BUY = 3002
MAIN_CMD_UNLOCK_ROOM = 3003
MAIN_CMD_EXECUTE_GACHA = 3100
MAIN_CMD_EXECUTE_LIMIT_GACHA = 3101
MAIN_CMD_EXECUTE_STEP_GACHA = 3102
MAIN_CMD_ROOM_BUY = 4000
MAIN_CMD_ROOM_UNLOCK_SLOT = 4100
MAIN_CMD_ROOM_SLOT_EDIT = 4101
MAIN_CMD_ROOM_SLOT_SELECT = 4102
MAIN_CMD_CHARGE_RM = 5000
MAIN_CMD_RESTORE_RM = 5001
MAIN_CMD_RECEIVE_NEW_MAIL = 6000
MAIN_CMD_SET_READ_MAIL_FLAG = 6001
MAIN_CMD_RECEIVE_MAIL_ITEM = 6002
MAIN_CMD_RECEIVE_ALL_MAIL_ITEM = 6003
MAIN_CMD_DELETE_MAIL = 6004
MAIN_CMD_DELETE_MAILS = 6005
MAIN_CMD_CHARA_MOTION_REWARD = 7000
MAIN_CMD_UPDATE_CAPTURE_TIME = 8000
MAIN_CMD_EXECUTE_CAPTURE_HAPINESS = 8001
MAIN_CMD_RECEIVE_SHARE_BONUS = 8002

RESPONSE_SUCCEED = 1
FIRST_COSTUME_IDS = [109001, 108002, 111101, 101102, 108003, 106103]


def _make_main_response(cmd: int, data: dict | None = None, ret: int = RESPONSE_SUCCEED) -> dict:
    resp = {
        "cmd": cmd,
        "ret": ret,
        "server_time": game_server_time(),
    }
    if data:
        resp.update(data)
    return resp


def _alloc_uid(ud: dict) -> int:
    uid = int(ud.get("next_uid", 100))
    ud["next_uid"] = uid + 1
    return uid


def _add_gm(ud: dict, delta: int) -> None:
    ud["gmoney"] = int(ud.get("gmoney", 5000)) + delta
    if ud.get("room_edits"):
        ud["room_edits"][0]["gmoney"] = int(ud["room_edits"][0].get("gmoney", 5000)) + delta


def _ensure_costume(ud: dict, costume_id: int) -> None:
    if not any(c["id"] == costume_id for c in ud.get("costumes", [])):
        ud.setdefault("costumes", []).append(
            {
                "id": costume_id,
                "exp": 0,
                "level": 1,
                "health": 100,
                "pat_time": 0,
                "pat_power": 3,
                "pat_mood": 0,
                "closet_time": -1,
                "motions": [],
            }
        )


def _extract_int_param(params, key: str | None = None, default: int = 0) -> int:
    if isinstance(params, int):
        return params
    if isinstance(params, dict) and key and key in params:
        return int(params[key])
    return default


# ---- Session, UserData & Config ----


@register_main(MAIN_CMD_SESSION_CHECK)
def handle_session_check(store, params, uid):
    return _make_main_response(MAIN_CMD_SESSION_CHECK)


@register_main(MAIN_CMD_GET_ALL)
def handle_get_all(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    return _make_main_response(MAIN_CMD_GET_ALL, {"userdata": serialize_userdata(ud)})


@register_main(MAIN_CMD_UPDATE_USERDATA)
def handle_update_userdata(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    return _make_main_response(MAIN_CMD_UPDATE_USERDATA, {"userdata": serialize_userdata(ud)})


@register_main(MAIN_CMD_SAVE_USERDATA)
def handle_save_userdata(store, params, uid):
    return _make_main_response(MAIN_CMD_SAVE_USERDATA)


@register_main(MAIN_CMD_GET_RM)
def handle_get_rm(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    freerm = int(ud.get("freerm", 100))
    rmoney = int(ud.get("rmoney", 0))
    return _make_main_response(
        MAIN_CMD_GET_RM,
        {
            "free_rm": freerm,
            "freerm": freerm,
            "rmoney": rmoney,
        },
    )


@register_main(MAIN_CMD_GET_GM)
def handle_get_gm(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    gm = int(ud.get("gmoney", 5000))
    return _make_main_response(MAIN_CMD_GET_GM, {"gm": gm, "gmoney": gm})


@register_main(MAIN_CMD_LOAD_CONF)
def handle_load_conf(store, params, uid):
    # GameSetting.loadConfSetting(ret[0]) reads ret[0].conf with JSON-encoded string values (214.sk.sq:157-199)
    return _make_main_response(
        MAIN_CMD_LOAD_CONF,
        {
            "conf": {
                "GameRule": json.dumps(get_game_rule()),
                "ConditionSetting": "[]",
            }
        },
    )


@register_main(MAIN_CMD_SEND_LOG)
def handle_send_log(store, params, uid):
    text = params if isinstance(params, str) else json.dumps(params, ensure_ascii=False)
    if text and text.strip():
        print(f"    [CLIENT_LOG uid={str(uid)[:8]}]", flush=True)
        for line in text[:4000].rstrip().splitlines():
            print(f"      | {line}", flush=True)
    return _make_main_response(MAIN_CMD_SEND_LOG)


@register_main(MAIN_CMD_CLIENT_REPORT)
@register_main(MAIN_CMD_REGISTER_TOKEN)
def handle_noop_telemetry(store, params, uid):
    return _make_main_response(MAIN_CMD_CLIENT_REPORT)


# ---- Tutorial & Player Profile ----


@register_main(MAIN_CMD_END_TUTORIAL)
def handle_end_tutorial(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    ids_to_add = []
    if isinstance(params, dict):
        if isinstance(params.get("ids"), list):
            ids_to_add = [int(x) for x in params["ids"]]
        elif "id" in params:
            ids_to_add = [int(params["id"])]
    elif isinstance(params, int):
        ids_to_add = [params]
    elif isinstance(params, list):
        ids_to_add = [int(x) for x in params]

    changed = False
    for tid in ids_to_add:
        if tid not in ud["end_tutorial_ids"]:
            ud["end_tutorial_ids"].append(tid)
            changed = True
    if changed:
        store.save(uid, ud)

    # 114.sk.sq:939 decodes new_panel_mission_ids with decodeJSON()
    return _make_main_response(
        MAIN_CMD_END_TUTORIAL,
        {
            "new_panel_mission_ids": "[]",
            "set_panel_mission_id": -1,
        },
    )


@register_main(MAIN_CMD_TUTORIAL_ADD_HAPINESS)
def handle_tutorial_add_happiness(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    _add_gm(ud, 500)
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_TUTORIAL_ADD_HAPINESS)


@register_main(MAIN_CMD_INPUT_PLAYERINFO)
def handle_input_playerinfo(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    if isinstance(params, dict):
        if "name" in params:
            ud["name"] = str(params["name"])
        if "sex" in params:
            ud["sex"] = int(params["sex"])
        store.save(uid, ud)
    return _make_main_response(MAIN_CMD_INPUT_PLAYERINFO)


# ---- Costumes & Work ----


@register_main(MAIN_CMD_SELECT_FIRST_COSTUME)
def handle_select_first_costume(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    idx = _extract_int_param(params, "idx", 0)
    cos_id = FIRST_COSTUME_IDS[idx] if 0 <= idx < len(FIRST_COSTUME_IDS) else FIRST_COSTUME_IDS[0]
    _ensure_costume(ud, cos_id)
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_SELECT_FIRST_COSTUME, {"costume_id": cos_id})


@register_main(MAIN_CMD_WORK_START)
def handle_work_start(store, params, uid):
    work_id = _extract_int_param(params, "work_id", 1)
    return _make_main_response(
        MAIN_CMD_WORK_START,
        {"work_id": work_id, "start_time": game_server_time()},
    )


@register_main(MAIN_CMD_WORK_UPDATE)
def handle_work_update(store, params, uid):
    return _make_main_response(MAIN_CMD_WORK_UPDATE, {"remaining_time": 0})


@register_main(MAIN_CMD_WORK_COMPLETE)
def handle_work_complete(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    work_id = _extract_int_param(params, "work_id", 1)
    _add_gm(ud, 200)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_WORK_COMPLETE,
        {
            "work_id": work_id,
            "reward_index": 0,
            "reward0": None,
        },
    )


@register_main(MAIN_CMD_WORK_CANCEL)
def handle_work_cancel(store, params, uid):
    work_id = _extract_int_param(params, "work_id", 1)
    return _make_main_response(MAIN_CMD_WORK_CANCEL, {"work_id": work_id})


@register_main(MAIN_CMD_WORK_SHORTENING)
def handle_work_shortening(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    work_id = _extract_int_param(params, "work_id", 1)
    return _make_main_response(
        MAIN_CMD_WORK_SHORTENING,
        {
            "work_id": work_id,
            "rmoney": int(ud.get("rmoney", 0)),
            "freerm": int(ud.get("freerm", 100)),
        },
    )


@register_main(MAIN_CMD_LEVEL_UP_COSTUME)
def handle_level_up_costume(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    cos_id = _extract_int_param(params, "costume_id", -1)
    new_level = 2
    if cos_id >= 0:
        for c in ud.get("costumes", []):
            if c["id"] == cos_id:
                c["level"] = int(c.get("level", 1)) + 1
                new_level = c["level"]
                break
        store.save(uid, ud)
    return _make_main_response(MAIN_CMD_LEVEL_UP_COSTUME, {"costume_level": new_level})


@register_main(MAIN_CMD_PAT_COSTUME)
def handle_pat_costume(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    now = game_server_time()
    cos_id = _extract_int_param(params, "costume_id", -1)
    if cos_id >= 0:
        for c in ud.get("costumes", []):
            if c["id"] == cos_id:
                c["pat_time"] = now
                break
        store.save(uid, ud)
    return _make_main_response(MAIN_CMD_PAT_COSTUME)


@register_main(MAIN_CMD_RENTAL_COSTUME)
def handle_rental_costume(store, params, uid):
    now = game_server_time()
    return _make_main_response(
        MAIN_CMD_RENTAL_COSTUME,
        {
            "time_limit": now + 3600,
            "server_time": now,
        },
    )


@register_main(MAIN_CMD_COSTUMEREQUEST_RECIEVE)
def handle_costumerequest_receive(store, params, uid):
    return _make_main_response(MAIN_CMD_COSTUMEREQUEST_RECIEVE)


@register_main(MAIN_CMD_COSTUMEREQUEST_TIMEOUT)
def handle_costumerequest_timeout(store, params, uid):
    return _make_main_response(MAIN_CMD_COSTUMEREQUEST_TIMEOUT)


@register_main(MAIN_CMD_COSTUMEREQUEST_FIRST)
def handle_costumerequest_first(store, params, uid):
    return _make_main_response(MAIN_CMD_COSTUMEREQUEST_FIRST)


# ---- Production ----


@register_main(MAIN_CMD_PRODUCT_UPDATE)
def handle_product_update(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_UPDATE, {"production_list": []})


@register_main(MAIN_CMD_PRODUCT_START)
def handle_product_start(store, params, uid):
    return _make_main_response(
        MAIN_CMD_PRODUCT_START,
        {
            "start_time": game_server_time(),
            "product_time": 60,
            "reward_states": "[0]",
        },
    )


@register_main(MAIN_CMD_PRODUCT_COMPLETE)
def handle_product_complete(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_COMPLETE, {"reward": []})


@register_main(MAIN_CMD_PRODUCT_LEVELUP_START)
def handle_product_levelup_start(store, params, uid):
    return _make_main_response(
        MAIN_CMD_PRODUCT_LEVELUP_START,
        {
            "start_time": game_server_time(),
            "product_time": 60,
            "reward_states": "[0]",
        },
    )


@register_main(MAIN_CMD_PRODUCT_LEVELUP_COMPLETE)
def handle_product_levelup_complete(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_LEVELUP_COMPLETE)


@register_main(MAIN_CMD_PRODUCT_SHORTEN)
def handle_product_shorten(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_SHORTEN)


@register_main(MAIN_CMD_PRODUCT_CANCEL)
def handle_product_cancel(store, params, uid):
    fuid = _extract_int_param(params, "uid", 0)
    return _make_main_response(MAIN_CMD_PRODUCT_CANCEL, {"uid": fuid})


@register_main(MAIN_CMD_PRODUCT_DELIVER_COMPLETE)
def handle_product_deliver_complete(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_DELIVER_COMPLETE)


@register_main(MAIN_CMD_PRODUCT_FINISH)
def handle_product_finish(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_FINISH)


@register_main(MAIN_CMD_PRODUCT_RECIEVE_TIMEOUT)
def handle_product_receive_timeout(store, params, uid):
    fuid = _extract_int_param(params, "uid", 0)
    return _make_main_response(MAIN_CMD_PRODUCT_RECIEVE_TIMEOUT, {"uid": fuid})


@register_main(MAIN_CMD_PRODUCT_TAP)
def handle_product_tap(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    _add_gm(ud, 10)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_PRODUCT_TAP,
        {
            "add_gm": 10,
            "bonus_hapiness": 0,
            "bonus_recover_time": -1,
        },
    )


@register_main(MAIN_CMD_PRODUCT_REWARDSTATE_CHANGE)
def handle_product_rewardstate_change(store, params, uid):
    return _make_main_response(MAIN_CMD_PRODUCT_REWARDSTATE_CHANGE, {"finished_uids": None})


# ---- Furniture & Room Layout ----


@register_main(MAIN_CMD_LAYOUT_EDIT)
def handle_layout_edit(store, params, uid):
    return _make_main_response(MAIN_CMD_LAYOUT_EDIT)


@register_main(MAIN_CMD_LAYOUT_REMOVE)
def handle_layout_remove(store, params, uid):
    return _make_main_response(MAIN_CMD_LAYOUT_REMOVE)


def _extract_furniture_id(params) -> int:
    if isinstance(params, dict):
        return int(params.get("furniture_id", params.get("shop_id", 8401)))
    if isinstance(params, list) and params:
        return int(params[0])
    return 8401


@register_main(MAIN_CMD_LAYOUT_BUY)
def handle_layout_buy(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    now = game_server_time()
    buy_uid = _alloc_uid(ud)
    furn_id = _extract_furniture_id(params)
    ud.setdefault("furnitures", []).append(
        {
            "furniture_id": furn_id,
            "uid": buy_uid,
            "exp": 0,
            "level": 1,
            "current_view_index": 0,
            "product_type": 0,
            "room_id": 1,
        }
    )
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_LAYOUT_BUY,
        {
            "buy_uid": buy_uid,
            "deliver_start_time": now,
            "product_time": 180,
        },
    )


@register_main(MAIN_CMD_LAYOUT_BUY_NO_DELIVER)
def handle_layout_buy_no_deliver(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    buy_uid = _alloc_uid(ud)
    furn_id = _extract_furniture_id(params)
    ud.setdefault("furnitures", []).append(
        {
            "furniture_id": furn_id,
            "uid": buy_uid,
            "exp": 0,
            "level": 1,
            "current_view_index": 0,
            "product_type": 0,
            "room_id": 1,
        }
    )
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_LAYOUT_BUY_NO_DELIVER, {"buy_uid": buy_uid})


# ---- Items & Happiness ----


@register_main(MAIN_CMD_USE_ONCE_ITEM)
def handle_use_once_item(store, params, uid):
    return _make_main_response(MAIN_CMD_USE_ONCE_ITEM)


@register_main(MAIN_CMD_USE_BOOST_ITEM)
def handle_use_boost_item(store, params, uid):
    return _make_main_response(MAIN_CMD_USE_BOOST_ITEM)


@register_main(MAIN_CMD_COMUNICATION_TAP)
def handle_communication_tap(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    _add_gm(ud, 20)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_COMUNICATION_TAP,
        {
            "add_gm": 20,
            "bonus_hapiness": 0,
            "bonus_recover_time": -1,
        },
    )


@register_main(MAIN_CMD_ADD_POOL_HAPINESS)
def handle_add_pool_happiness(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    _add_gm(ud, 50)
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_ADD_POOL_HAPINESS)


# ---- Panel Missions & Dialog ----


@register_main(MAIN_CMD_PANELMISSION_SELECT)
def handle_panelmission_select(store, params, uid):
    return _make_main_response(MAIN_CMD_PANELMISSION_SELECT)


@register_main(MAIN_CMD_PANELMISSION_SEND_PROGRESS)
def handle_panelmission_send_progress(store, params, uid):
    return _make_main_response(MAIN_CMD_PANELMISSION_SEND_PROGRESS)


@register_main(MAIN_CMD_PANELMISSION_SETPIECE)
def handle_panelmission_setpiece(store, params, uid):
    return _make_main_response(MAIN_CMD_PANELMISSION_SETPIECE)


@register_main(MAIN_CMD_PANELMISSION_SHOWEDEVENT)
def handle_panelmission_showedevent(store, params, uid):
    return _make_main_response(
        MAIN_CMD_PANELMISSION_SHOWEDEVENT,
        {"clear_date": game_server_time()},
    )


@register_main(MAIN_CMD_PANELMISSION_RECEIVE_REWARD)
def handle_panelmission_receive_reward(store, params, uid):
    return _make_main_response(
        MAIN_CMD_PANELMISSION_RECEIVE_REWARD,
        {
            "new_panel_mission_ids": "[]",
            "set_panel_mission_id": -1,
        },
    )


@register_main(MAIN_CMD_SELECT_REPLYKEY)
def handle_select_replykey(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    if isinstance(params, dict):
        keys = params.get("keys")
        vals = params.get("values")
        if isinstance(keys, list) and isinstance(vals, list):
            for k, v in zip(keys, vals):
                ud.setdefault("reply_records", {})[str(k)] = str(v)
            store.save(uid, ud)
        elif "key" in params:
            ud.setdefault("reply_records", {})[str(params["key"])] = str(params.get("val", ""))
            store.save(uid, ud)
    return _make_main_response(MAIN_CMD_SELECT_REPLYKEY)


@register_main(MAIN_CMD_RECIEVE_AD_VIDEO_REWARD)
def handle_receive_ad_video_reward(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    now = game_server_time()
    ud["last_ad_video_watch_time"] = now
    ud["ad_video_watch_count"] = int(ud.get("ad_video_watch_count", 0)) + 1
    _add_gm(ud, 100)
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_RECIEVE_AD_VIDEO_REWARD, {"last_time": now})


# ---- Room Characters ----


@register_main(MAIN_CMD_ADD_ROOM_CHARA)
def handle_add_room_chara(store, params, uid):
    return _make_main_response(MAIN_CMD_ADD_ROOM_CHARA, {"recover_health": 0})


@register_main(MAIN_CMD_REMOVE_ROOM_CHARA)
def handle_remove_room_chara(store, params, uid):
    return _make_main_response(MAIN_CMD_REMOVE_ROOM_CHARA, {"closet_time": game_server_time()})


@register_main(MAIN_CMD_CHANGE_ROOM_CHARA)
def handle_change_room_chara(store, params, uid):
    return _make_main_response(
        MAIN_CMD_CHANGE_ROOM_CHARA,
        {
            "closet_time": game_server_time(),
            "recover_health": "[0,0]",
        },
    )


# ---- Shop, Rooms & Gacha ----


@register_main(MAIN_CMD_SELL_FURNITURE)
def handle_sell_furniture(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    target_uid = _extract_int_param(params, "uid", -1)
    if target_uid >= 0:
        ud["furnitures"] = [f for f in ud.get("furnitures", []) if f["uid"] != target_uid]
        _add_gm(ud, 100)
        store.save(uid, ud)
    return _make_main_response(MAIN_CMD_SELL_FURNITURE)


@register_main(MAIN_CMD_SELL_ROOM)
def handle_sell_room(store, params, uid):
    return _make_main_response(MAIN_CMD_SELL_ROOM)


@register_main(MAIN_CMD_SHOP_BUY)
def handle_shop_buy(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    reward_uid = _alloc_uid(ud)
    if isinstance(params, dict):
        shop_id = params.get("shop_id", params.get("id"))
        if shop_id is not None:
            records = ud.setdefault("purchase_records", {})
            records[str(shop_id)] = int(records.get(str(shop_id), 0)) + 1
            store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_SHOP_BUY,
        {
            "reward0": json.dumps([reward_uid, 1, 109001, 1, 0]),
            "reward1": None,
        },
    )


@register_main(MAIN_CMD_UNLOCK_ROOM)
def handle_unlock_room(store, params, uid):
    # 114.sk.sq:1631-1727 passes room_ids and furniture_layouts to decodeJSON()
    return _make_main_response(
        MAIN_CMD_UNLOCK_ROOM,
        {
            "room_ids": "[]",
            "furniture_layouts": "[]",
            "new_panel_mission_ids": "[]",
            "set_panel_mission_id": -1,
        },
    )


def _roll_gacha_reward(ud: dict) -> str:
    cos_id = random.choice(FIRST_COSTUME_IDS)
    _ensure_costume(ud, cos_id)
    reward_uid = _alloc_uid(ud)
    # 114.sk.sq:1468-1540 expects 6-element JSON array: [uid, kind, id, num, target_id, rank]
    return json.dumps([reward_uid, 1, cos_id, 1, 0, 1])


@register_main(MAIN_CMD_EXECUTE_GACHA)
def handle_execute_gacha(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    reward_json = _roll_gacha_reward(ud)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_EXECUTE_GACHA,
        {"reward0": reward_json, "reward1": None},
    )


@register_main(MAIN_CMD_EXECUTE_LIMIT_GACHA)
def handle_execute_limit_gacha(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    reward_json = _roll_gacha_reward(ud)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_EXECUTE_LIMIT_GACHA,
        {"st": 0, "reward0": reward_json, "reward1": None},
    )


@register_main(MAIN_CMD_EXECUTE_STEP_GACHA)
def handle_execute_step_gacha(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    reward_json = _roll_gacha_reward(ud)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_EXECUTE_STEP_GACHA,
        {"st": 0, "reward0": reward_json, "reward1": None},
    )


@register_main(MAIN_CMD_ROOM_BUY)
def handle_room_buy(store, params, uid):
    return _make_main_response(MAIN_CMD_ROOM_BUY, {"room_uids": []})


@register_main(MAIN_CMD_ROOM_UNLOCK_SLOT)
def handle_room_unlock_slot(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    slot_ids = []
    if isinstance(params, dict):
        if isinstance(params.get("room_slot_ids"), list):
            slot_ids = [int(x) for x in params["room_slot_ids"]]
        elif "slot_id" in params:
            slot_ids = [int(params["slot_id"])]
    changed = False
    for sid in slot_ids:
        if sid not in ud["unlocked_slot_ids"]:
            ud["unlocked_slot_ids"].append(sid)
            changed = True
    if changed:
        store.save(uid, ud)
    return _make_main_response(MAIN_CMD_ROOM_UNLOCK_SLOT)


@register_main(MAIN_CMD_ROOM_SLOT_EDIT)
def handle_room_slot_edit(store, params, uid):
    return _make_main_response(MAIN_CMD_ROOM_SLOT_EDIT)


@register_main(MAIN_CMD_ROOM_SLOT_SELECT)
def handle_room_slot_select(store, params, uid):
    return _make_main_response(MAIN_CMD_ROOM_SLOT_SELECT)


# ---- In-App Purchase (Offline Bypass) ----


@register_main(MAIN_CMD_CHARGE_RM)
def handle_charge_rm(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    ud["freerm"] = int(ud.get("freerm", 100)) + 100
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_CHARGE_RM,
        {
            "add_room": -1,
            "add_rm": 100,
            "bonus_rm": 0,
            "productIds": "",
        },
    )


@register_main(MAIN_CMD_RESTORE_RM)
def handle_restore_rm(store, params, uid):
    return _make_main_response(
        MAIN_CMD_RESTORE_RM,
        {
            "add_rm": 0,
            "bonus_rm": 0,
            "productIds": "",
            "room_ids": None,
        },
    )


# ---- Mail ----


@register_main(MAIN_CMD_RECEIVE_NEW_MAIL)
def handle_receive_new_mail(store, params, uid):
    return _make_main_response(MAIN_CMD_RECEIVE_NEW_MAIL, {"mails": []})


@register_main(MAIN_CMD_SET_READ_MAIL_FLAG)
@register_main(MAIN_CMD_DELETE_MAIL)
def handle_mail_simple(store, params, uid):
    return _make_main_response(MAIN_CMD_SET_READ_MAIL_FLAG)


@register_main(MAIN_CMD_RECEIVE_MAIL_ITEM)
def handle_receive_mail_item(store, params, uid):
    return _make_main_response(
        MAIN_CMD_RECEIVE_MAIL_ITEM,
        {"reward": json.dumps([0, 0, 0, 100, 0])},
    )


@register_main(MAIN_CMD_RECEIVE_ALL_MAIL_ITEM)
def handle_receive_all_mail_item(store, params, uid):
    return _make_main_response(MAIN_CMD_RECEIVE_ALL_MAIL_ITEM, {"rewards": []})


@register_main(MAIN_CMD_DELETE_MAILS)
def handle_delete_mails(store, params, uid):
    return _make_main_response(MAIN_CMD_DELETE_MAILS, {"idxes": []})


# ---- Motions & Capture ----


@register_main(MAIN_CMD_CHARA_MOTION_REWARD)
def handle_chara_motion_reward(store, params, uid):
    return _make_main_response(MAIN_CMD_CHARA_MOTION_REWARD)


@register_main(MAIN_CMD_UPDATE_CAPTURE_TIME)
def handle_update_capture_time(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    next_time = game_server_time() + 3600
    ud["capture_hapiness_time"] = next_time
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_UPDATE_CAPTURE_TIME, {"next": next_time})


@register_main(MAIN_CMD_EXECUTE_CAPTURE_HAPINESS)
def handle_execute_capture_happiness(store, params, uid):
    # 375.sk.sq:2022-2030 reads reward as int GM amount!
    ud, _ = load_or_create_user(store, uid)
    next_time = game_server_time() + 3600
    ud["capture_hapiness_time"] = next_time
    _add_gm(ud, 100)
    store.save(uid, ud)
    return _make_main_response(
        MAIN_CMD_EXECUTE_CAPTURE_HAPINESS,
        {
            "reward": 100,
            "next": next_time,
        },
    )


@register_main(MAIN_CMD_RECEIVE_SHARE_BONUS)
def handle_receive_share_bonus(store, params, uid):
    ud, _ = load_or_create_user(store, uid)
    _add_gm(ud, 100)
    store.save(uid, ud)
    return _make_main_response(MAIN_CMD_RECEIVE_SHARE_BONUS)
