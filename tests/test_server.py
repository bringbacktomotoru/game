"""End-to-end tests for the Tomotoru mock server and ServerDataLoader format."""
import json

import pytest

from server.python.crypto import decrypt_payload, encrypt_payload
from server.python.mock import create_app


def _consume_userdata_array(datas: list):
    """Exact Python mirror of 176.sk.bin UserData.load(ServerDataLoader).

    Verifies that every element is consumed and idx == len(datas) at loader.close().
    """
    idx = 0

    def get():
        nonlocal idx
        assert idx < len(datas), f"ServerDataLoader underflow at idx={idx}"
        val = datas[idx]
        idx += 1
        return val

    parsed = {
        "friend_code": get(),
        "name": get(),
        "sex": get(),
        "rank": get(),
        "rankexp": get(),
        "gmoney": get(),
        "rmoney": get(),
        "freerm": get(),
        "capture_hapiness_time": get(),
    }

    # FurnitureData (166.sk.sq:32-85: furniture_id, uid, exp, level, current_view_index, product_type, room_id)
    f_num = get()
    parsed["furnitures"] = []
    for _ in range(f_num):
        fid, uid, exp, lvl, view_idx, ptype, room_id = [get() for _ in range(7)]
        assert ptype == 0
        parsed["furnitures"].append({"furniture_id": fid, "uid": uid, "level": lvl, "room_id": room_id})

    # RoomSlotData
    slot_num = get()
    for _ in range(slot_num):
        _room_uid = get()
        reside_len = get()
        _reside = [get() for _ in range(reside_len)]

    # Unlocked slots
    unlocked_num = get()
    _unlocked = [get() for _ in range(unlocked_num)]

    # RoomEditData (uid, room_id, furniture_num, ...layouts, panel_mission_id, gmoney)
    room_num = get()
    for _ in range(room_num):
        _uid, _room_id, flen = get(), get(), get()
        assert flen == 0
        _panel_mission_id = get()
        _room_gmoney = get()

    # room_layout 2D array
    rl_size = get()
    for _ in range(rl_size):
        row_len = get()
        _row = [get() for _ in range(row_len)]

    # CostumeData (163.sk.bin: id, exp, level, health, pat_time, pat_power, pat_mood, closet_time, motion_num, *motions, has_work)
    cos_num = get()
    parsed["costumes"] = []
    for _ in range(cos_num):
        cid, exp, lvl, hp, pt, pp, pm, ct = [get() for _ in range(8)]
        m_num = get()
        _motions = [get() for _ in range(m_num)]
        has_work = get()
        assert has_work == 0
        parsed["costumes"].append({"id": cid, "level": lvl})

    # WorkRewardData
    assert get() == 0

    # Items
    item_num = get()
    for _ in range(item_num):
        _id, _nums = get(), get()

    # Purchase records
    pr_num = get()
    for _ in range(pr_num):
        _id, _nums = get(), get()

    # GachaData
    assert get() == 0

    # End tutorial IDs
    tut_num = get()
    parsed["end_tutorial_ids"] = [get() for _ in range(tut_num)]

    # Costume requests
    assert get() == 0

    # Mails
    assert get() == 0

    # Characters (162.sk.bin: id, motion_len, *motions, received_motion_reward_index, rental_len, *rentals)
    ch_num = get()
    for _ in range(ch_num):
        _cid, mlen = get(), get()
        _motions = [get() for _ in range(mlen)]
        _rmri = get()
        rlen = get()
        for _ in range(rlen):
            _k, _rid, _lim = get(), get(), get()

    # Bonus happiness & ad video
    _bh, _nbhrt, _lavwt, _avwc = get(), get(), get(), get()

    # Panel missions (172.sk.sq + 171.sk.sq)
    pm_num = get()
    for _ in range(pm_num):
        _pmid = get()
        m_len = get()
        for _ in range(m_len):
            _mid, _prog, _clr, _piece = get(), get(), get(), get()
        _all_clr, _clr_date, _recv_rew = get(), get(), get()
        prev_len = get()
        _prev_mids = [get() for _ in range(prev_len)]

    # Reply records (key:str, val:str)
    rep_num = get()
    for _ in range(rep_num):
        _k, _v = get(), get()
        assert isinstance(_k, str) and isinstance(_v, str)

    # ServerDataLoader.close() check!
    assert idx == len(datas), f"Data not ended: idx={idx}, len={len(datas)}"
    return parsed


@pytest.fixture()
def client(tmp_path):
    app = create_app(saves_dir=str(tmp_path / "saves"))
    app.config["TESTING"] = True
    return app.test_client()


def _post_enc(client, path: str, payload: dict):
    enc = encrypt_payload(payload)
    resp = client.post(path, data=enc, content_type="text/plain")
    assert resp.status_code == 200
    decoded = decrypt_payload(resp.data)
    assert isinstance(decoded, list) and len(decoded) == 1
    return decoded[0]


def test_health_and_static_endpoints(client):
    assert client.get("/health").get_json() == {"status": "healthy"}

    for url in ("/setting.json", "/dl/setting.json"):
        s = client.get(url).get_json()
        assert s["release_pkg_version"] == 1027
        assert s["min_pkg_version"] == 1027
        assert s["mode"] == 0

    for url in ("/pkg.json", "/dl/pkg.json"):
        p = client.get(url).get_json()
        assert p["version"] == 1027
        assert p["mainsxr"]["url"].endswith("/dl/main.sxr")
        assert len(p["sxrlist"]) == 8


def test_full_auth_and_gameplay_persistence(client):
    # 1. Auth
    auth = _post_enc(
        client,
        "/app_kitty/entrance",
        {"uuid": "player-test-001", "cmd": 0, "platform_id": 1, "sk_ver": 100},
    )
    assert auth["ret"] == 1
    assert auth["first_login"] is True
    sid = auth["sessionId"]
    assert isinstance(sid, str) and len(sid) > 0

    ud = _consume_userdata_array(auth["userdata"])
    assert ud["name"] == "Player"
    assert ud["gmoney"] == 5000
    assert ud["freerm"] == 100
    assert ud["end_tutorial_ids"] == []

    # 2. Input player info
    r = _post_enc(
        client,
        "/app_kitty/main",
        {"sid": sid, "cmd": 400, "params": {"name": "KittyFan", "sex": 2}, "seq": 1},
    )
    assert r["ret"] == 1

    # 3. Select first costume
    r = _post_enc(
        client,
        "/app_kitty/main",
        {"sid": sid, "cmd": 1100, "params": 0, "seq": 2},
    )
    assert r["ret"] == 1
    assert r["costume_id"] == 109001

    # 4. Buy furniture
    r = _post_enc(
        client,
        "/app_kitty/main",
        {"sid": sid, "cmd": 1302, "params": {"furniture_id": 2001}, "seq": 3},
    )
    assert r["ret"] == 1
    assert r["buy_uid"] >= 100
    assert r["product_time"] == 180

    # 5. Execute gacha (reward0 is 6-element JSON array)
    r = _post_enc(
        client,
        "/app_kitty/main",
        {"sid": sid, "cmd": 3100, "params": {}, "seq": 4},
    )
    assert r["ret"] == 1
    reward = json.loads(r["reward0"])
    assert len(reward) == 6

    # 6. End tutorial & tap happiness
    r = _post_enc(client, "/app_kitty/main", {"sid": sid, "cmd": 200, "params": {"ids": [1]}, "seq": 5})
    assert r["ret"] == 1
    assert json.loads(r["new_panel_mission_ids"]) == []
    r = _post_enc(client, "/app_kitty/main", {"sid": sid, "cmd": 1500, "params": {}, "seq": 6})
    assert r["ret"] == 1
    assert r["add_gm"] == 20
    assert r["bonus_hapiness"] == 0

    # 7. Verify GET_ALL reflects all persisted mutations & still passes ServerDataLoader
    all_resp = _post_enc(client, "/app_kitty/main", {"sid": sid, "cmd": 900, "params": {}, "seq": 7})
    assert all_resp["ret"] == 1
    ud2 = _consume_userdata_array(all_resp["userdata"])
    assert ud2["name"] == "KittyFan"
    assert ud2["sex"] == 2
    assert ud2["gmoney"] == 5020
    assert any(c["id"] == 109001 for c in ud2["costumes"])
    assert any(f["furniture_id"] == 2001 for f in ud2["furnitures"])
    assert 1 in ud2["end_tutorial_ids"]

    # 8. Load conf, capture happiness & report
    conf = _post_enc(client, "/app_kitty/main", {"sid": sid, "cmd": 100, "params": {}, "seq": 8})
    assert conf["ret"] == 1
    assert "conf" in conf and "GameRule" in conf["conf"]
    assert isinstance(conf["conf"]["GameRule"], str)

    cap = _post_enc(client, "/app_kitty/main", {"sid": sid, "cmd": 8001, "params": {}, "seq": 9})
    assert cap["ret"] == 1 and isinstance(cap["reward"], int)

    rep = _post_enc(client, "/app_kitty/report", {"uuid": "player-test-001", "cmd": 0, "params": {}})
    assert rep["ret"] == 1


def test_takeover_code_flow(client):
    issue = _post_enc(
        client,
        "/app_kitty/entrance",
        {"uuid": "orig-uuid-999", "cmd": 10, "password": "secret"},
    )
    assert issue["ret"] == 1
    code = issue["takeover_code"]

    # Wrong password -> RESPONSE_TAKEOVER_MISS (-100)
    bad = _post_enc(
        client,
        "/app_kitty/entrance",
        {"uuid": "new-dev", "cmd": 11, "takeover_code": code, "password": "wrong"},
    )
    assert bad["ret"] == -100

    # Correct password -> original UUID
    good = _post_enc(
        client,
        "/app_kitty/entrance",
        {"uuid": "new-dev", "cmd": 11, "takeover_code": code, "password": "secret"},
    )
    assert good["ret"] == 1
    assert good["uuid"] == "orig-uuid-999"


def test_normal_vs_debug_mode(tmp_path):
    expected_tutorials = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1001, 1002, 1003]

    # Normal mode (debug=False): authentic new-player save
    normal_app = create_app(saves_dir=str(tmp_path / "saves_normal"), debug=False)
    normal_app.config["TESTING"] = True
    normal_client = normal_app.test_client()

    normal_auth = _post_enc(
        normal_client,
        "/app_kitty/entrance",
        {"uuid": "normal-player", "cmd": 0, "platform_id": 1, "sk_ver": 100},
    )
    assert normal_auth["ret"] == 1
    ud_normal = _consume_userdata_array(normal_auth["userdata"])
    assert ud_normal["end_tutorial_ids"] == []
    assert ud_normal["gmoney"] == 5000
    assert ud_normal["freerm"] == 100

    # Debug mode (debug=True): all 19 tutorials skipped + generous test currency
    debug_app = create_app(saves_dir=str(tmp_path / "saves_debug"), debug=True)
    debug_app.config["TESTING"] = True
    debug_client = debug_app.test_client()

    debug_auth = _post_enc(
        debug_client,
        "/app_kitty/entrance",
        {"uuid": "debug-player", "cmd": 0, "platform_id": 1, "sk_ver": 100},
    )
    assert debug_auth["ret"] == 1
    ud_debug = _consume_userdata_array(debug_auth["userdata"])
    assert len(ud_debug["end_tutorial_ids"]) == 19
    assert ud_debug["end_tutorial_ids"] == expected_tutorials
    assert ud_debug["gmoney"] == 50000
    assert ud_debug["freerm"] == 1000
