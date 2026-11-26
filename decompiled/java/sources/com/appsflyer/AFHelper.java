package com.appsflyer;

import android.os.Build;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AFHelper {
    public static JSONObject convertToJsonObject(Map<String, ?> map) {
        return Build.VERSION.SDK_INT >= 19 ? new JSONObject(map) : toJsonObject(map);
    }

    public static JSONObject toJsonObject(Map<String, ?> map) {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            try {
                jSONObject.put(entry.getKey(), m7(entry.getValue()));
            } catch (JSONException e) {
            }
        }
        return jSONObject;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private static Object m7(Object obj) {
        if (obj == null) {
            return JSONObject.NULL;
        }
        if (!(obj instanceof JSONArray) && !(obj instanceof JSONObject) && !obj.equals(JSONObject.NULL)) {
            try {
                if (obj instanceof Collection) {
                    JSONArray jSONArray = new JSONArray();
                    Iterator it = ((Collection) obj).iterator();
                    while (it.hasNext()) {
                        jSONArray.put(m7(it.next()));
                    }
                    return jSONArray;
                }
                if (obj.getClass().isArray()) {
                    int length = Array.getLength(obj);
                    JSONArray jSONArray2 = new JSONArray();
                    for (int i = 0; i < length; i++) {
                        jSONArray2.put(m7(Array.get(obj, i)));
                    }
                    return jSONArray2;
                }
                if (obj instanceof Map) {
                    return toJsonObject((Map) obj);
                }
                if (!(obj instanceof Boolean) && !(obj instanceof Byte) && !(obj instanceof Character) && !(obj instanceof Double) && !(obj instanceof Float) && !(obj instanceof Integer) && !(obj instanceof Long) && !(obj instanceof Short) && !(obj instanceof String)) {
                    return obj.toString();
                }
                return obj;
            } catch (Exception e) {
                return JSONObject.NULL;
            }
        }
        return obj;
    }
}
