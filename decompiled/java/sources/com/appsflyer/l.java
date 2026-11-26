package com.appsflyer;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import com.appsflyer.o;
import com.appsflyer.share.Constants;

/* loaded from: classes.dex */
final class l {
    l() {
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    static o m158(ContentResolver contentResolver) {
        if (contentResolver == null || AppsFlyerProperties.getInstance().getString("amazon_aid") != null || !"Amazon".equals(Build.MANUFACTURER)) {
            return null;
        }
        int i = Settings.Secure.getInt(contentResolver, "limit_ad_tracking", 2);
        if (i == 0) {
            return new o(o.a.AMAZON, Settings.Secure.getString(contentResolver, Constants.URL_ADVERTISING_ID), false);
        }
        if (i == 2) {
            return null;
        }
        String str = "";
        try {
            str = Settings.Secure.getString(contentResolver, Constants.URL_ADVERTISING_ID);
        } catch (Throwable th) {
            AFLogger.afErrorLog("Couldn't fetch Amazon Advertising ID (Ad-Tracking is limited!)", th);
        }
        return new o(o.a.AMAZON, str, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x0037, code lost:
    
        if (r2.length() == 0) goto L15;
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006d  */
    /* renamed from: ˎ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static void m157(android.content.Context r10, java.util.Map<java.lang.String, java.lang.Object> r11) {
        /*
            Method dump skipped, instructions count: 385
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.l.m157(android.content.Context, java.util.Map):void");
    }

    static class a extends IllegalStateException {
        a(String str) {
            super(str);
        }
    }
}
