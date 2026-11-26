package com.appsflyer;

import android.content.pm.PackageManager;
import android.os.Build;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class s {

    /* renamed from: ˎ, reason: contains not printable characters */
    private static s f206;

    /* renamed from: ˉ, reason: contains not printable characters */
    private JSONObject f216;

    /* renamed from: ˋˊ, reason: contains not printable characters */
    private int f223;

    /* renamed from: ˏ, reason: contains not printable characters */
    private boolean f227 = true;

    /* renamed from: ॱ, reason: contains not printable characters */
    private boolean f230 = true;

    /* renamed from: ˋ, reason: contains not printable characters */
    private final String f222 = "brand";

    /* renamed from: ˊ, reason: contains not printable characters */
    private final String f217 = "model";

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private final String f234 = "platform";

    /* renamed from: ᐝ, reason: contains not printable characters */
    private final String f236 = "platform_version";

    /* renamed from: ʻ, reason: contains not printable characters */
    private final String f207 = ServerParameters.ADVERTISING_ID_PARAM;

    /* renamed from: ʽ, reason: contains not printable characters */
    private final String f211 = "imei";

    /* renamed from: ʼ, reason: contains not printable characters */
    private final String f209 = "android_id";

    /* renamed from: ॱˊ, reason: contains not printable characters */
    private final String f231 = "sdk_version";

    /* renamed from: ͺ, reason: contains not printable characters */
    private final String f229 = "devkey";

    /* renamed from: ˋॱ, reason: contains not printable characters */
    private final String f224 = "originalAppsFlyerId";

    /* renamed from: ˏॱ, reason: contains not printable characters */
    private final String f228 = "uid";

    /* renamed from: ˊॱ, reason: contains not printable characters */
    private final String f220 = "app_id";

    /* renamed from: ॱᐝ, reason: contains not printable characters */
    private final String f235 = "app_version";

    /* renamed from: ॱˎ, reason: contains not printable characters */
    private final String f233 = AppsFlyerProperties.CHANNEL;

    /* renamed from: ʻॱ, reason: contains not printable characters */
    private final String f208 = "preInstall";

    /* renamed from: ᐝॱ, reason: contains not printable characters */
    private final String f237 = "data";

    /* renamed from: ॱˋ, reason: contains not printable characters */
    private final String f232 = "r_debugging_off";

    /* renamed from: ʿ, reason: contains not printable characters */
    private final String f214 = "r_debugging_on";

    /* renamed from: ʼॱ, reason: contains not printable characters */
    private final String f210 = "public_api_call";

    /* renamed from: ˈ, reason: contains not printable characters */
    private final String f215 = "exception";

    /* renamed from: ʽॱ, reason: contains not printable characters */
    private final String f212 = "server_request";

    /* renamed from: ʾ, reason: contains not printable characters */
    private final String f213 = "server_response";

    /* renamed from: ˊˋ, reason: contains not printable characters */
    private final String f219 = "yyyy-MM-dd HH:mm:ssZ";

    /* renamed from: ˊˊ, reason: contains not printable characters */
    private final String f218 = "MM-dd HH:mm:ss.SSS";

    /* renamed from: ˍ, reason: contains not printable characters */
    private String f225 = "-1";

    /* renamed from: ˊᐝ, reason: contains not printable characters */
    private JSONArray f221 = new JSONArray();

    /* renamed from: ˎˎ, reason: contains not printable characters */
    private boolean f226 = false;

    private s() {
        this.f223 = 0;
        this.f223 = 0;
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    static s m186() {
        if (f206 == null) {
            f206 = new s();
        }
        return f206;
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final synchronized void m198(String str) {
        this.f225 = str;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final synchronized void m191() {
        this.f226 = true;
        m185("r_debugging_on", new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH).format(Long.valueOf(System.currentTimeMillis())), new String[0]);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    final synchronized void m196() {
        m185("r_debugging_off", new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH).format(Long.valueOf(System.currentTimeMillis())), new String[0]);
        this.f226 = false;
        this.f227 = false;
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    final synchronized void m193() {
        this.f216 = null;
        this.f221 = null;
        f206 = null;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private synchronized void m184(String str, String str2, String str3, String str4, String str5, String str6) {
        try {
            this.f216.put("brand", str);
            this.f216.put("model", str2);
            this.f216.put("platform", "Android");
            this.f216.put("platform_version", str3);
            if (str4 != null && str4.length() > 0) {
                this.f216.put(ServerParameters.ADVERTISING_ID_PARAM, str4);
            }
            if (str5 != null && str5.length() > 0) {
                this.f216.put("imei", str5);
            }
            if (str6 != null && str6.length() > 0) {
                this.f216.put("android_id", str6);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private synchronized void m181(String str, String str2, String str3, String str4) {
        try {
            this.f216.put("sdk_version", str);
            if (str2 != null && str2.length() > 0) {
                this.f216.put("devkey", str2);
            }
            if (str3 != null && str3.length() > 0) {
                this.f216.put("originalAppsFlyerId", str3);
            }
            if (str4 != null && str4.length() > 0) {
                this.f216.put("uid", str4);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    private synchronized void m187(String str, String str2, String str3, String str4) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    this.f216.put("app_id", str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (str2 != null && str2.length() > 0) {
            this.f216.put("app_version", str2);
        }
        if (str3 != null && str3.length() > 0) {
            this.f216.put(AppsFlyerProperties.CHANNEL, str3);
        }
        if (str4 != null && str4.length() > 0) {
            this.f216.put("preInstall", str4);
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    final void m195(String str, String... strArr) {
        m185("public_api_call", str, strArr);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    final void m197(Throwable th) {
        String[] strArr;
        Throwable cause = th.getCause();
        String simpleName = th.getClass().getSimpleName();
        String message = cause == null ? th.getMessage() : cause.getMessage();
        StackTraceElement[] stackTrace = cause == null ? th.getStackTrace() : cause.getStackTrace();
        if (stackTrace == null) {
            strArr = new String[]{message};
        } else {
            String[] strArr2 = new String[stackTrace.length + 1];
            strArr2[0] = message;
            for (int i = 1; i < stackTrace.length; i++) {
                strArr2[i] = stackTrace[i].toString();
            }
            strArr = strArr2;
        }
        m185("exception", simpleName, strArr);
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m200(String str, String str2) {
        m185("server_request", str, str2);
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m199(String str, int i, String str2) {
        m185("server_response", str, String.valueOf(i), str2);
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final void m192(String str, String str2) {
        m185(null, str, str2);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private synchronized void m185(String str, String str2, String... strArr) {
        String format;
        boolean z = true;
        synchronized (this) {
            if (!this.f230 || (!this.f227 && !this.f226)) {
                z = false;
            }
            if (z && this.f223 < 98304) {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    String str3 = "";
                    if (strArr.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int length = strArr.length - 1; length > 0; length--) {
                            sb.append(strArr[length]).append(", ");
                        }
                        sb.append(strArr[0]);
                        str3 = sb.toString();
                    }
                    String format2 = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ENGLISH).format(Long.valueOf(currentTimeMillis));
                    if (str != null) {
                        format = String.format("%18s %5s _/%s [%s] %s %s", format2, Long.valueOf(Thread.currentThread().getId()), AppsFlyerLib.LOG_TAG, str, str2, str3);
                    } else {
                        format = String.format("%18s %5s %s/%s %s", format2, Long.valueOf(Thread.currentThread().getId()), str2, AppsFlyerLib.LOG_TAG, str3);
                    }
                    this.f221.put(format);
                    this.f223 = format.getBytes().length + this.f223;
                } catch (Throwable th) {
                }
            }
        }
    }

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private synchronized String m188() {
        String str;
        str = null;
        try {
            this.f216.put("data", this.f221);
            str = this.f216.toString();
            m180();
        } catch (JSONException e) {
        }
        return str;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private synchronized void m183(String str, PackageManager packageManager) {
        AppsFlyerProperties appsFlyerProperties = AppsFlyerProperties.getInstance();
        AppsFlyerLib appsFlyerLib = AppsFlyerLib.getInstance();
        String string = appsFlyerProperties.getString("remote_debug_static_data");
        if (string != null) {
            try {
                this.f216 = new JSONObject(string);
            } catch (Throwable th) {
            }
        } else {
            this.f216 = new JSONObject();
            m184(Build.BRAND, Build.MODEL, Build.VERSION.RELEASE, appsFlyerProperties.getString(ServerParameters.ADVERTISING_ID_PARAM), appsFlyerLib.f34, appsFlyerLib.f44);
            m181("4.8.19.419", appsFlyerProperties.getString(AppsFlyerProperties.AF_KEY), appsFlyerProperties.getString("KSAppsFlyerId"), appsFlyerProperties.getString("uid"));
            try {
                int i = packageManager.getPackageInfo(str, 0).versionCode;
                m187(str, String.valueOf(i), appsFlyerProperties.getString(AppsFlyerProperties.CHANNEL), appsFlyerProperties.getString("preInstallName"));
            } catch (Throwable th2) {
            }
            appsFlyerProperties.set("remote_debug_static_data", this.f216.toString());
        }
        try {
            this.f216.put("launch_counter", this.f225);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: ʽ, reason: contains not printable characters */
    private synchronized void m180() {
        this.f221 = null;
        this.f221 = new JSONArray();
        this.f223 = 0;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    final synchronized void m194() {
        this.f227 = false;
        m180();
    }

    /* renamed from: ʼ, reason: contains not printable characters */
    final void m190() {
        this.f230 = false;
    }

    /* renamed from: ʻ, reason: contains not printable characters */
    final boolean m189() {
        return this.f226;
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    static void m182(String str, PackageManager packageManager) {
        try {
            if (f206 == null) {
                f206 = new s();
            }
            f206.m183(str, packageManager);
            if (f206 == null) {
                f206 = new s();
            }
            String m188 = f206.m188();
            k kVar = new k(null, AppsFlyerLib.getInstance().isTrackingStopped());
            kVar.f177 = m188;
            kVar.m154();
            kVar.execute(new StringBuilder().append(ServerConfigHandler.getUrl("https://monitorsdk.%s/remote-debug?app_id=")).append(str).toString());
        } catch (Throwable th) {
        }
    }
}
