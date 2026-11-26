package com.appsflyer;

import android.os.Bundle;
import com.appsflyer.c;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/* loaded from: classes.dex */
public class GcmInstanceIdListener extends InstanceIDListenerService {
    /* JADX WARN: Multi-variable type inference failed */
    public void onTokenRefresh() {
        String str;
        super.onTokenRefresh();
        String string = AppsFlyerProperties.getInstance().getString("gcmProjectNumber");
        long currentTimeMillis = System.currentTimeMillis();
        try {
            str = InstanceID.getInstance(getApplicationContext()).getToken(string, "GCM", (Bundle) null);
        } catch (Throwable th) {
            AFLogger.afErrorLog("Error registering for uninstall tracking", th);
            str = null;
        }
        if (str != null) {
            AFLogger.afInfoLog("GCM Refreshed Token = ".concat(String.valueOf(str)));
            c.a.C0004c m112 = c.a.C0004c.m112(AppsFlyerProperties.getInstance().getString("afUninstallToken"));
            c.a.C0004c c0004c = new c.a.C0004c(currentTimeMillis, str);
            if (m112.m114(c0004c)) {
                u.m216(getApplicationContext(), c0004c);
            }
        }
    }
}
