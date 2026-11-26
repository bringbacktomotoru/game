package com.appsflyer.share;

import android.content.Context;
import android.os.AsyncTask;
import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerProperties;
import com.appsflyer.ServerConfigHandler;
import com.appsflyer.ServerParameters;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class CrossPromotionHelper {
    public static void trackAndOpenStore(Context context, String str, String str2) {
        trackAndOpenStore(context, str, str2, null);
    }

    public static void trackAndOpenStore(Context context, String str, String str2, Map<String, String> map) {
        LinkGenerator m201 = m201(context, str, str2, map, ServerConfigHandler.getUrl(Constants.BASE_URL_APP_APPSFLYER_COM));
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, false)) {
            AFLogger.afInfoLog("CustomerUserId not set, track And Open Store is disabled", true);
            return;
        }
        HashMap hashMap = new HashMap();
        if (map != null) {
            hashMap.putAll(map);
        }
        hashMap.put("af_campaign", str2);
        AppsFlyerLib.getInstance().trackEvent(context, "af_cross_promotion", hashMap);
        new d(new c(), context, AppsFlyerLib.getInstance().isTrackingStopped()).execute(m201.generateLink());
    }

    public static void trackCrossPromoteImpression(Context context, String str, String str2) {
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, false)) {
            AFLogger.afInfoLog("CustomerUserId not set, Promote Impression is disabled", true);
        } else {
            new d(null, null, AppsFlyerLib.getInstance().isTrackingStopped()).execute(m201(context, str, str2, null, ServerConfigHandler.getUrl("https://impression.%s")).generateLink());
        }
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    private static LinkGenerator m201(Context context, String str, String str2, Map<String, String> map, String str3) {
        LinkGenerator linkGenerator = new LinkGenerator("af_cross_promotion");
        linkGenerator.m206(str3).m205(str).addParameter(Constants.URL_SITE_ID, context.getPackageName());
        if (str2 != null) {
            linkGenerator.setCampaign(str2);
        }
        if (map != null) {
            linkGenerator.addParameters(map);
        }
        String string = AppsFlyerProperties.getInstance().getString(ServerParameters.ADVERTISING_ID_PARAM);
        if (string != null) {
            linkGenerator.addParameter(Constants.URL_ADVERTISING_ID, string);
        }
        return linkGenerator;
    }

    static class d extends AsyncTask<String, Void, Void> {

        /* renamed from: ˊ, reason: contains not printable characters */
        private c f238;

        /* renamed from: ˎ, reason: contains not printable characters */
        private WeakReference<Context> f239;

        /* renamed from: ˏ, reason: contains not printable characters */
        private boolean f240;

        d(c cVar, Context context, boolean z) {
            this.f238 = cVar;
            this.f239 = new WeakReference<>(context);
            this.f240 = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00a8  */
        @Override // android.os.AsyncTask
        /* renamed from: ˎ, reason: contains not printable characters and merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Void doInBackground(java.lang.String... r8) {
            /*
                r7 = this;
                r2 = 0
                boolean r0 = r7.f240
                if (r0 == 0) goto L6
            L5:
                return r2
            L6:
                r0 = 0
                r1 = r8[r0]     // Catch: java.lang.Throwable -> Lac java.lang.Throwable -> Lb1
                java.net.URL r0 = new java.net.URL     // Catch: java.lang.Throwable -> Lac java.lang.Throwable -> Lb1
                r0.<init>(r1)     // Catch: java.lang.Throwable -> Lac java.lang.Throwable -> Lb1
                java.net.URLConnection r0 = r0.openConnection()     // Catch: java.lang.Throwable -> Lac java.lang.Throwable -> Lb1
                java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch: java.lang.Throwable -> Lac java.lang.Throwable -> Lb1
                r3 = 10000(0x2710, float:1.4013E-41)
                r0.setConnectTimeout(r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                r3 = 0
                r0.setInstanceFollowRedirects(r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                int r3 = r0.getResponseCode()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                r4 = 200(0xc8, float:2.8E-43)
                if (r3 != r4) goto L39
                java.lang.String r3 = "Cross promotion impressions success: "
                java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.String r1 = r3.concat(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                r3 = 0
                com.appsflyer.AFLogger.afInfoLog(r1, r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
            L33:
                if (r0 == 0) goto L5
                r0.disconnect()
                goto L5
            L39:
                r4 = 301(0x12d, float:4.22E-43)
                if (r3 == r4) goto L41
                r4 = 302(0x12e, float:4.23E-43)
                if (r3 != r4) goto L86
            L41:
                java.lang.String r3 = "Cross promotion redirection success: "
                java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.String r1 = r3.concat(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                r3 = 0
                com.appsflyer.AFLogger.afInfoLog(r1, r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                com.appsflyer.share.c r1 = r7.f238     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                if (r1 == 0) goto L33
                java.lang.ref.WeakReference<android.content.Context> r1 = r7.f239     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.Object r1 = r1.get()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                if (r1 == 0) goto L33
                java.lang.String r1 = "Location"
                java.lang.String r1 = r0.getHeaderField(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                com.appsflyer.share.c r3 = r7.f238     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                r3.m208(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                com.appsflyer.share.c r3 = r7.f238     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.ref.WeakReference<android.content.Context> r1 = r7.f239     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.Object r1 = r1.get()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                android.content.Context r1 = (android.content.Context) r1     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                r3.m207(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                goto L33
            L74:
                r1 = move-exception
                r6 = r1
                r1 = r0
                r0 = r6
            L78:
                java.lang.String r3 = r0.getMessage()     // Catch: java.lang.Throwable -> Lae
                r4 = 1
                com.appsflyer.AFLogger.afErrorLog(r3, r0, r4)     // Catch: java.lang.Throwable -> Lae
                if (r1 == 0) goto L5
                r1.disconnect()
                goto L5
            L86:
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.String r5 = "call to "
                r4.<init>(r5)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.StringBuilder r1 = r4.append(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.String r4 = " failed: "
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.StringBuilder r1 = r1.append(r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                com.appsflyer.AFLogger.afInfoLog(r1)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> La3
                goto L33
            La3:
                r1 = move-exception
                r2 = r0
                r0 = r1
            La6:
                if (r2 == 0) goto Lab
                r2.disconnect()
            Lab:
                throw r0
            Lac:
                r0 = move-exception
                goto La6
            Lae:
                r0 = move-exception
                r2 = r1
                goto La6
            Lb1:
                r0 = move-exception
                r1 = r2
                goto L78
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.share.CrossPromotionHelper.d.doInBackground(java.lang.String[]):java.lang.Void");
        }
    }
}
