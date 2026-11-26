package com.appsflyer;

import com.appsflyer.c;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/* loaded from: classes.dex */
public class FirebaseInstanceIdListener extends FirebaseInstanceIdService {
    /* JADX WARN: Multi-variable type inference failed */
    public void onTokenRefresh() {
        String str;
        super.onTokenRefresh();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            str = FirebaseInstanceId.getInstance().getToken();
        } catch (Throwable th) {
            AFLogger.afErrorLog("Error registering for uninstall tracking", th);
            str = null;
        }
        if (str != null) {
            AFLogger.afInfoLog("Firebase Refreshed Token = ".concat(String.valueOf(str)));
            c.a.C0004c m112 = c.a.C0004c.m112(AppsFlyerProperties.getInstance().getString("afUninstallToken"));
            c.a.C0004c c0004c = new c.a.C0004c(currentTimeMillis, str);
            if (m112.m114(c0004c)) {
                u.m216(getApplicationContext(), c0004c);
            }
        }
    }
}
