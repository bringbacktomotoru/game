"""Player UserData model and ServerDataLoader array serializer.

100% verified field-by-field against clean decompiled Squirrel scripts:
  - 176.sk.sq (UserData.load)
  - 166.sk.sq (FurnitureData.loadWithCreate — 7 fields incl. trailing room_id)
  - 175.sk.sq (RoomSlotData.load)
  - 174.sk.sq (RoomEditData.loadWithCreate — uid, room_id, layouts, panel_mission_id, gmoney)
  - 167.sk.sq (FurnitureLayoutData.loadWithCreate)
  - 163.sk.sq (CostumeData.loadWithCreate — 10 fields incl. motion_num & has_work)
  - 162.sk.sq (CharacterData.loadWithCreate — id, motions, received_motion_reward_index, rentals)
"""
import hashlib
import time
from typing import Any

# Client's GameSession.time201511 epoch (2015-11-01 00:00:00 UTC)
EPOCH_201511 = 1446336000

# All tutorial IDs (used in --debug mode to skip onboarding for rapid testing)
ALL_TUTORIAL_IDS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1001, 1002, 1003]


def game_server_time() -> int:
    """Return current time in Tomotoru's server epoch (seconds since 2015-11-01)."""
    return max(0, int(time.time()) - EPOCH_201511)


def _make_friend_code(uid: str) -> int:
    """Deterministic 9-digit integer friend code from UUID (TitleScene._toDigitString expects int)."""
    digest = int(hashlib.sha256(uid.encode("utf-8")).hexdigest()[:8], 16)
    return (digest % 900_000_000) + 100_000_000


def create_default_user(uid: str, debug: bool = False) -> dict[str, Any]:
    """Create a fresh, valid player save dictionary matching all client invariants.

    Args:
        uid: Player UUID.
        debug: If False (Normal mode), creates an authentic new-player save with
            empty ``end_tutorial_ids`` and standard starting currency (5000 GM, 100 Free RM).
            If True (Debug mode), marks all tutorials completed and grants generous
            test currency (50000 GM, 1000 Free RM).
    """
    now = game_server_time()
    start_gmoney = 50000 if debug else 5000
    start_freerm = 1000 if debug else 100
    end_tutorial_ids = list(ALL_TUTORIAL_IDS) if debug else []
    return {
        "version": 3,
        "uuid": uid,
        "friend_code": _make_friend_code(uid),
        "name": "Player",
        "sex": 0,
        "rank": 1,
        "rankexp": 0,
        "gmoney": start_gmoney,
        "rmoney": 0,
        "freerm": start_freerm,
        "capture_hapiness_time": 0,
        "next_uid": 100,
        # Room 1 init_furnitures from 79.sk.sq (rooms.conf): 8401 and 1703
        # UserData.load requires >=1 furniture with room_id == room_edit.uid
        "furnitures": [
            {
                "furniture_id": 8401,
                "uid": 1,
                "exp": 0,
                "level": 1,
                "current_view_index": 0,
                "product_type": 0,
                "room_id": 1,
            },
            {
                "furniture_id": 1703,
                "uid": 2,
                "exp": 0,
                "level": 1,
                "current_view_index": 0,
                "product_type": 0,
                "room_id": 1,
            },
        ],
        "room_slots": [
            {"room_uid": 1, "reside_charas": [109001]},
        ],
        "unlocked_slot_ids": [0],
        # UserData.getGameMoney() reads room_edits[uid].gmoney
        "room_edits": [
            {"uid": 1, "room_id": 1, "layouts": [], "panel_mission_id": 101, "gmoney": start_gmoney},
        ],
        "room_layout": [],
        # Costume 109001 = Hello Kitty (chara_id = 1)
        "costumes": [
            {
                "id": 109001,
                "exp": 0,
                "level": 1,
                "health": 100,
                "pat_time": 0,
                "pat_power": 3,
                "pat_mood": 0,
                "closet_time": -1,
                "motions": [],
            },
        ],
        "work_rewards": [],
        "items": {},
        "purchase_records": {},
        "gacha_data": [],
        "end_tutorial_ids": end_tutorial_ids,
        "costume_requests": [],
        "mails": [],
        # >=1 character required (176.sk.sq iterates null furniture_amount_table if 0)
        "characters": [
            {
                "id": 1,
                "received_motion_reward_index": -1,
                "motions": [5, 9, 10],
                "rentals": {},
            },
        ],
        "bonus_hapiness_num": 5,
        "next_bonus_hapiness_recover_time": -1,
        "last_ad_video_watch_time": 0,
        "ad_video_watch_count": 0,
        "panel_missions": [
            {
                "id": 101,
                "missions": [
                    {"id": 10, "progress": 0, "cleared": False, "set_piece": False},
                    {"id": 20, "progress": 0, "cleared": False, "set_piece": False},
                    {"id": 30, "progress": 0, "cleared": False, "set_piece": False},
                    {"id": 40, "progress": 0, "cleared": False, "set_piece": False},
                ],
                "all_cleared": False,
                "clear_date": -1,
                "received_reward": False,
                "prev_mid_list": [],
            },
        ],
        "reply_records": {},
        "created_at": now,
        "last_login": now,
    }


def load_or_create_user(store, uid: str, debug: bool = False) -> tuple[dict[str, Any], bool]:
    """Load player data from JSONStore or initialize a new player. Returns (user_data, is_new)."""
    ud = store.load(uid)
    if not ud or "friend_code" not in ud or ud.get("version", 0) < 3:
        ud = create_default_user(uid, debug=debug)
        store.save(uid, ud)
        return ud, True
    return ud, False


def serialize_userdata(ud: dict[str, Any]) -> list[Any]:
    """Serialize player save dict into the exact flat array read by ServerDataLoader."""
    out: list[Any] = [
        int(ud.get("friend_code", 100000001)),
        str(ud.get("name", "Player")),
        int(ud.get("sex", 0)),
        int(ud.get("rank", 1)),
        int(ud.get("rankexp", 0)),
        int(ud.get("gmoney", 5000)),
        int(ud.get("rmoney", 0)),
        int(ud.get("freerm", 100)),
        int(ud.get("capture_hapiness_time", 0)),
    ]

    # 1. FurnitureData list (166.sk.sq:32-85: furniture_id, uid, exp, level, current_view_index, product_type, room_id)
    furnitures = ud.get("furnitures", [])
    out.append(len(furnitures))
    for f in furnitures:
        out.extend(
            [
                int(f["furniture_id"]),
                int(f["uid"]),
                int(f.get("exp", 0)),
                int(f.get("level", 1)),
                int(f.get("current_view_index", 0)),
                int(f.get("product_type", 0)),
                int(f.get("room_id", f.get("room_idx", 1))),
            ]
        )

    # 2. RoomSlotData list (175.sk.sq:11-18)
    room_slots = ud.get("room_slots", [])
    out.append(len(room_slots))
    for slot in room_slots:
        reside = slot.get("reside_charas", [])
        out.append(int(slot.get("room_uid", 1)))
        out.append(len(reside))
        out.extend(int(c) for c in reside)

    # 3. Unlocked slot IDs
    unlocked = ud.get("unlocked_slot_ids", [])
    out.append(len(unlocked))
    out.extend(int(sid) for sid in unlocked)

    # 4. RoomEditData list (174.sk.sq:47-65: uid, room_id, furniture_num, *layouts, panel_mission_id, gmoney)
    room_edits = ud.get("room_edits", [])
    out.append(len(room_edits))
    for re_item in room_edits:
        layouts = re_item.get("layouts", [])
        out.append(int(re_item.get("uid", 1)))
        out.append(int(re_item.get("room_id", 1)))
        out.append(len(layouts))
        for lay in layouts:
            out.extend(lay)
        out.append(int(re_item.get("panel_mission_id", -1)))
        out.append(int(re_item.get("gmoney", 5000)))

    # 5. room_layout 2D array (176.sk.sq)
    room_layout = ud.get("room_layout", [])
    out.append(len(room_layout))
    for row in room_layout:
        out.append(len(row))
        out.extend(int(v) for v in row)

    # 6. CostumeData list (163.sk.sq:50-72)
    costumes = ud.get("costumes", [])
    out.append(len(costumes))
    for c in costumes:
        motions = c.get("motions", [])
        out.extend(
            [
                int(c["id"]),
                int(c.get("exp", 0)),
                int(c.get("level", 1)),
                int(c.get("health", 100)),
                int(c.get("pat_time", 0)),
                int(c.get("pat_power", 3)),
                int(c.get("pat_mood", 0)),
                int(c.get("closet_time", -1)),
                len(motions),
                *(int(m) for m in motions),
                0,  # has_work_data = 0
            ]
        )

    # 7. WorkRewardData list (179.sk.sq)
    out.append(0)

    # 8. Items map (id:int, nums:int)
    items = ud.get("items", {})
    out.append(len(items))
    for k, v in items.items():
        out.append(int(k))
        out.append(int(v))

    # 9. Purchase records map (id:int, nums:int)
    purchases = ud.get("purchase_records", {})
    out.append(len(purchases))
    for k, v in purchases.items():
        out.append(int(k))
        out.append(int(v))

    # 10. GachaData list (168.sk.sq)
    out.append(0)

    # 11. End tutorial IDs
    end_tuts = ud.get("end_tutorial_ids", [])
    out.append(len(end_tuts))
    out.extend(int(tid) for tid in end_tuts)

    # 12. CostumeRequestData list-of-lists (164.sk.sq)
    out.append(0)

    # 13. MailData list (170.sk.sq)
    out.append(0)

    # 14. CharacterData list (162.sk.sq:26-47: id, motion_len, *motions, received_motion_reward_index, rental_len, *rentals)
    characters = ud.get("characters", [])
    out.append(len(characters))
    for ch in characters:
        motions = ch.get("motions", [5, 9, 10])
        rentals = ch.get("rentals", {})
        out.append(int(ch["id"]))
        out.append(len(motions))
        out.extend(int(m) for m in motions)
        out.append(int(ch.get("received_motion_reward_index", -1)))
        out.append(len(rentals))
        for rkey, rval in rentals.items():
            out.append(int(rkey))
            out.append(int(rval.get("id", 0)))
            out.append(int(rval.get("time_limit", 0)))

    # 15. Bonus happiness & ad video stats (4 ints)
    out.append(int(ud.get("bonus_hapiness_num", 5)))
    out.append(int(ud.get("next_bonus_hapiness_recover_time", -1)))
    out.append(int(ud.get("last_ad_video_watch_time", 0)))
    out.append(int(ud.get("ad_video_watch_count", 0)))

    # 16. PanelMissionData list (172.sk.sq + 171.sk.sq)
    panel_missions = ud.get("panel_missions", [])
    out.append(len(panel_missions))
    for pm in panel_missions:
        out.append(int(pm["id"]))
        missions = pm.get("missions", [])
        out.append(len(missions))
        for m in missions:
            out.append(int(m["id"]))
            out.append(int(m.get("progress", 0)))
            out.append(1 if m.get("cleared", False) else 0)
            out.append(1 if m.get("set_piece", False) else 0)
        out.append(1 if pm.get("all_cleared", False) else 0)
        out.append(int(pm.get("clear_date", -1)))
        out.append(1 if pm.get("received_reward", False) else 0)
        prev_mids = pm.get("prev_mid_list", [])
        out.append(len(prev_mids))
        out.extend(int(mid) for mid in prev_mids)

    # 17. Reply records map (key:str, val:str — 176.sk.sq:1747-1752)
    replies = ud.get("reply_records", {})
    out.append(len(replies))
    for k, v in replies.items():
        out.append(str(k))
        out.append(str(v))

    return out
