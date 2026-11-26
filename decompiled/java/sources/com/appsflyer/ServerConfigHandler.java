package com.appsflyer;

import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ServerConfigHandler {
    @Nullable
    /* renamed from: ˎ, reason: contains not printable characters */
    static JSONObject m94(String str) {
        JSONObject jSONObject;
        Throwable th;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            jSONObject = null;
        } catch (Throwable th2) {
            jSONObject = null;
            th = th2;
        }
        try {
            if (jSONObject.optBoolean("monitor", false)) {
                s.m186().m191();
            } else {
                s.m186().m194();
                s.m186().m196();
            }
        } catch (JSONException e2) {
            s.m186().m194();
            s.m186().m196();
            return jSONObject;
        } catch (Throwable th3) {
            th = th3;
            AFLogger.afErrorLog(th.getMessage(), th);
            s.m186().m194();
            s.m186().m196();
            return jSONObject;
        }
        return jSONObject;
    }

    public static String getUrl(String str) {
        return String.format(str, AppsFlyerLib.getInstance().getHost());
    }
}
