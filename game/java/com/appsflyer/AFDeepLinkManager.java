package com.appsflyer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.util.Map;

/* loaded from: classes.dex */
public class AFDeepLinkManager {
    protected int currentActivityHash = -1;

    /* renamed from: ˏ, reason: contains not printable characters */
    private static AFDeepLinkManager f1 = null;

    /* renamed from: ˋ, reason: contains not printable characters */
    private static Uri f0 = null;

    /* renamed from: ॱ, reason: contains not printable characters */
    private static Uri f2 = null;

    private AFDeepLinkManager() {
    }

    public static AFDeepLinkManager getInstance() {
        if (f1 == null) {
            f1 = new AFDeepLinkManager();
        }
        return f1;
    }

    protected void processIntentForDeepLink(Intent intent, Context context, Map<String, Object> map) {
        Uri data = (intent == null || !"android.intent.action.VIEW".equals(intent.getAction())) ? null : intent.getData();
        if (data != null) {
            boolean z = AppsFlyerProperties.getInstance().getBoolean("consumeAfDeepLink", false);
            boolean z2 = (intent.getFlags() & 4194304) == 0;
            if (!intent.hasExtra("appsflyer_click_ts") || z) {
                if (z || z2) {
                    Boolean valueOf = Boolean.valueOf(z2);
                    if (f0 == null || !data.equals(f0)) {
                        AppsFlyerLib.getInstance().handleDeepLinkCallback(context, map, data);
                        f0 = data;
                        return;
                    } else {
                        AFLogger.afInfoLog(new StringBuilder("skipping re-use of previously consumed deep link: ").append(data.toString()).append(valueOf.booleanValue() ? " w/sT" : " w/cAPI").toString());
                        return;
                    }
                }
                if (this.currentActivityHash != AppsFlyerProperties.getInstance().getInt("lastActivityHash", 0)) {
                    AppsFlyerLib.getInstance().handleDeepLinkCallback(context, map, data);
                    AppsFlyerProperties.getInstance().set("lastActivityHash", this.currentActivityHash);
                    return;
                } else {
                    AFLogger.afInfoLog(new StringBuilder("skipping re-use of previously consumed deep link: ").append(data.toString()).append(" w/hC: ").append(String.valueOf(this.currentActivityHash)).toString());
                    return;
                }
            }
            long longExtra = intent.getLongExtra("appsflyer_click_ts", 0L);
            long j = AppsFlyerProperties.getInstance().getLong("appsflyer_click_consumed_ts", 0L);
            if (longExtra != 0 && longExtra != j) {
                AppsFlyerLib.getInstance().handleDeepLinkCallback(context, map, data);
                AppsFlyerProperties.getInstance().set("appsflyer_click_consumed_ts", longExtra);
                return;
            } else {
                AFLogger.afInfoLog(new StringBuilder("skipping re-use of previously consumed deep link: ").append(data.toString()).append(" w/Ex: ").append(String.valueOf(longExtra)).toString());
                return;
            }
        }
        if (f2 != null) {
            AppsFlyerLib.getInstance().handleDeepLinkCallback(context, map, f2);
            AFLogger.afInfoLog(new StringBuilder("using trampoline Intent fallback with URI : ").append(f2.toString()).toString());
            f2 = null;
        } else if (AppsFlyerLib.getInstance().latestDeepLink != null) {
            AppsFlyerLib.getInstance().handleDeepLinkCallback(context, map, AppsFlyerLib.getInstance().latestDeepLink);
        }
    }

    protected void collectIntentsFromActivities(Intent intent) {
        Uri uri = null;
        if (intent != null && "android.intent.action.VIEW".equals(intent.getAction())) {
            uri = intent.getData();
        }
        if (uri != null && intent.getData() != f2) {
            f2 = intent.getData();
        }
    }
}
