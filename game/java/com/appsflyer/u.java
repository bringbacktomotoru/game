package com.appsflyer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.appsflyer.c;
import com.google.firebase.iid.FirebaseInstanceIdService;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
final class u {
    u() {
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    static boolean m213(Context context) {
        return m214(context) | m211(context);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0048, code lost:
    
        if ((r7.getPackageManager().queryIntentServices(r3, 0).size() > 0) != false) goto L13;
     */
    /* renamed from: ˎ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean m214(android.content.Context r7) {
        /*
            r1 = 1
            r0 = 0
            com.appsflyer.AppsFlyerLib r2 = com.appsflyer.AppsFlyerLib.getInstance()
            boolean r2 = r2.isTrackingStopped()
            if (r2 == 0) goto Ld
        Lc:
            return r0
        Ld:
            java.lang.String r2 = "com.google.android.gms.iid.InstanceIDListenerService"
            java.lang.Class.forName(r2)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            android.content.Intent r2 = new android.content.Intent     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.String r3 = "com.google.android.gms.iid.InstanceID"
            r4 = 0
            java.lang.Class<com.appsflyer.GcmInstanceIdListener> r5 = com.appsflyer.GcmInstanceIdListener.class
            r2.<init>(r3, r4, r7, r5)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            android.content.Intent r3 = new android.content.Intent     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.String r4 = "com.google.android.gms.iid.InstanceID"
            r5 = 0
            java.lang.Class<com.google.android.gms.iid.InstanceIDListenerService> r6 = com.google.android.gms.iid.InstanceIDListenerService.class
            r3.<init>(r4, r5, r7, r6)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            android.content.pm.PackageManager r4 = r7.getPackageManager()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            r5 = 0
            java.util.List r2 = r4.queryIntentServices(r2, r5)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            int r2 = r2.size()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            if (r2 <= 0) goto L89
            r2 = r1
        L36:
            if (r2 != 0) goto L4a
            android.content.pm.PackageManager r2 = r7.getPackageManager()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            r4 = 0
            java.util.List r2 = r2.queryIntentServices(r3, r4)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            int r2 = r2.size()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            if (r2 <= 0) goto L8b
            r2 = r1
        L48:
            if (r2 == 0) goto Lc
        L4a:
            android.content.Intent r2 = new android.content.Intent     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.String r3 = "com.google.android.c2dm.intent.RECEIVE"
            r4 = 0
            java.lang.String r5 = "com.google.android.gms.gcm.GcmReceiver"
            java.lang.Class r5 = java.lang.Class.forName(r5)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            r2.<init>(r3, r4, r7, r5)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            android.content.pm.PackageManager r3 = r7.getPackageManager()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            r4 = 0
            java.util.List r2 = r3.queryBroadcastReceivers(r2, r4)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            int r2 = r2.size()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            if (r2 <= 0) goto L8d
            r2 = r1
        L68:
            if (r2 == 0) goto La0
            java.lang.String r2 = r7.getPackageName()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            r3.<init>()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.StringBuilder r2 = r3.append(r2)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.String r3 = ".permission.C2D_MESSAGE"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            java.lang.String r2 = r2.toString()     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            boolean r2 = com.appsflyer.f.AnonymousClass1.m124(r7, r2)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            if (r2 == 0) goto L8f
            r0 = r1
            goto Lc
        L89:
            r2 = r0
            goto L36
        L8b:
            r2 = r0
            goto L48
        L8d:
            r2 = r0
            goto L68
        L8f:
            java.lang.String r1 = "Cannot verify existence of the app's \"permission.C2D_MESSAGE\" permission in the manifest. Please refer to documentation."
            com.appsflyer.AFLogger.afWarnLog(r1)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            goto Lc
        L96:
            r1 = move-exception
            java.lang.String r1 = r1.getMessage()
            com.appsflyer.AFLogger.afRDLog(r1)
            goto Lc
        La0:
            java.lang.String r1 = "Cannot verify existence of GcmReceiver receiver in the manifest. Please refer to documentation."
            com.appsflyer.AFLogger.afWarnLog(r1)     // Catch: java.lang.ClassNotFoundException -> L96 java.lang.Throwable -> La7
            goto Lc
        La7:
            r1 = move-exception
            java.lang.String r2 = "An error occurred while trying to verify manifest declarations: "
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.u.m214(android.content.Context):boolean");
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private static boolean m211(Context context) {
        if (AppsFlyerLib.getInstance().isTrackingStopped()) {
            return false;
        }
        try {
            Class.forName("com.google.firebase.iid.FirebaseInstanceIdService");
            Intent intent = new Intent("com.google.firebase.INSTANCE_ID_EVENT", null, context, FirebaseInstanceIdListener.class);
            Intent intent2 = new Intent("com.google.firebase.INSTANCE_ID_EVENT", null, context, FirebaseInstanceIdService.class);
            if (!(context.getPackageManager().queryIntentServices(intent, 0).size() > 0)) {
                if (!(context.getPackageManager().queryIntentServices(intent2, 0).size() > 0)) {
                    AFLogger.afWarnLog("Cannot verify existence of our InstanceID Listener Service in the manifest. Please refer to documentation.");
                    return false;
                }
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Throwable th) {
            AFLogger.afErrorLog("An error occurred while trying to verify manifest declarations: ", th);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ˋ, reason: contains not printable characters */
    public static String m212(WeakReference<Context> weakReference, String str) {
        try {
            Class<?> cls = Class.forName("com.google.android.gms.iid.InstanceID");
            Class.forName("com.google.android.gms.gcm.GcmReceiver");
            String str2 = (String) cls.getDeclaredMethod("getToken", String.class, String.class).invoke(cls.getDeclaredMethod("getInstance", Context.class).invoke(cls, weakReference.get()), str, "GCM");
            if (str2 == null) {
                AFLogger.afWarnLog("Couldn't get token using reflection.");
                return null;
            }
            return str2;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Throwable th) {
            AFLogger.afErrorLog("Couldn't get token using GoogleCloudMessaging. ", th);
            return null;
        }
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    static void m216(Context context, c.a.C0004c c0004c) {
        AFLogger.afInfoLog(new StringBuilder("updateServerUninstallToken called with: ").append(c0004c.toString()).toString());
        c.a.C0004c m112 = c.a.C0004c.m112(AppsFlyerProperties.getInstance().getString("afUninstallToken"));
        if (!context.getSharedPreferences("appsflyer-data", 0).getBoolean("sentRegisterRequestToAF", false) || m112.m113() == null || !m112.m113().equals(c0004c.m113())) {
            AppsFlyerProperties.getInstance().set("afUninstallToken", c0004c.toString());
            AppsFlyerLib.getInstance().m80(context, c0004c.m113());
        }
    }

    static class a extends AsyncTask<Void, Void, String> {

        /* renamed from: ˎ, reason: contains not printable characters */
        private final WeakReference<Context> f256;

        /* renamed from: ˏ, reason: contains not printable characters */
        private String f257;

        @Override // android.os.AsyncTask
        protected final /* synthetic */ String doInBackground(Void[] voidArr) {
            return m217();
        }

        @Override // android.os.AsyncTask
        protected final /* synthetic */ void onPostExecute(String str) {
            String str2 = str;
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            String string = AppsFlyerProperties.getInstance().getString("afUninstallToken");
            c.a.C0004c c0004c = new c.a.C0004c(str2);
            if (string == null) {
                u.m216(this.f256.get(), c0004c);
                return;
            }
            c.a.C0004c m112 = c.a.C0004c.m112(string);
            if (m112.m114(c0004c)) {
                u.m216(this.f256.get(), m112);
            }
        }

        a(WeakReference<Context> weakReference) {
            this.f256 = weakReference;
        }

        @Override // android.os.AsyncTask
        protected final void onPreExecute() {
            super.onPreExecute();
            this.f257 = AppsFlyerProperties.getInstance().getString("gcmProjectNumber");
        }

        /* renamed from: ˊ, reason: contains not printable characters */
        private String m217() {
            try {
                if (this.f257 != null) {
                    return u.m212(this.f256, this.f257);
                }
                return null;
            } catch (Throwable th) {
                AFLogger.afErrorLog("Error registering for uninstall feature", th);
                return null;
            }
        }
    }
}
