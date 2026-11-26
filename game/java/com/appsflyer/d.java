package com.appsflyer;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import java.util.HashMap;

/* loaded from: classes.dex */
final class d implements InstallReferrerStateListener {

    /* renamed from: ˎ, reason: contains not printable characters */
    private a f120;

    /* renamed from: ˏ, reason: contains not printable characters */
    private InstallReferrerClient f121;

    d() {
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    protected final void m116(Context context, a aVar) {
        this.f120 = aVar;
        try {
            this.f121 = InstallReferrerClient.newBuilder(context).build();
            this.f121.startConnection(this);
        } catch (Throwable th) {
            AFLogger.afErrorLog("referrerClient -> startConnection", th);
        }
    }

    public final void onInstallReferrerSetupFinished(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("code", String.valueOf(i));
        ReferrerDetails referrerDetails = null;
        switch (i) {
            case 0:
                try {
                    AFLogger.afDebugLog("InstallReferrer connected");
                    if (this.f121.isReady()) {
                        referrerDetails = this.f121.getInstallReferrer();
                        this.f121.endConnection();
                    } else {
                        AFLogger.afWarnLog("ReferrerClient: InstallReferrer is not ready");
                        hashMap.put(NotificationCompat.CATEGORY_ERROR, "ReferrerClient: InstallReferrer is not ready");
                    }
                    break;
                } catch (Throwable th) {
                    AFLogger.afWarnLog(new StringBuilder("Failed to get install referrer: ").append(th.getMessage()).toString());
                    hashMap.put(NotificationCompat.CATEGORY_ERROR, th.getMessage());
                    break;
                }
            case 1:
                AFLogger.afWarnLog("InstallReferrer not supported");
                break;
            case 2:
                AFLogger.afWarnLog("InstallReferrer not supported");
                break;
            default:
                AFLogger.afWarnLog("responseCode not found.");
                break;
        }
        if (referrerDetails != null) {
            try {
                if (referrerDetails.getInstallReferrer() != null) {
                    hashMap.put("val", referrerDetails.getInstallReferrer());
                }
                hashMap.put("clk", Long.toString(referrerDetails.getReferrerClickTimestampSeconds()));
                hashMap.put("install", Long.toString(referrerDetails.getInstallBeginTimestampSeconds()));
            } catch (Exception e) {
                e.printStackTrace();
                hashMap.put("val", "-1");
                hashMap.put("clk", "-1");
                hashMap.put("install", "-1");
            }
        }
        if (this.f120 == null) {
            return;
        }
        this.f120.onHandleReferrer(hashMap);
    }

    public final void onInstallReferrerServiceDisconnected() {
        AFLogger.afDebugLog("Install Referrer service disconnected");
    }
}
