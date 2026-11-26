package com.appsflyer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class j implements Runnable {

    /* renamed from: ʻ, reason: contains not printable characters */
    private static String f158 = new StringBuilder("https://validate.%s/api/v").append(AppsFlyerLib.f23).append("/androidevent?buildnumber=4.8.19&app_id=").toString();

    /* renamed from: ʼ, reason: contains not printable characters */
    private String f159;

    /* renamed from: ʽ, reason: contains not printable characters */
    private final Intent f160;

    /* renamed from: ˊ, reason: contains not printable characters */
    private String f161;

    /* renamed from: ˋ, reason: contains not printable characters */
    private WeakReference<Context> f162;

    /* renamed from: ˎ, reason: contains not printable characters */
    private String f163;

    /* renamed from: ˏ, reason: contains not printable characters */
    private String f164;

    /* renamed from: ॱ, reason: contains not printable characters */
    private String f165;

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private String f166;

    /* renamed from: ᐝ, reason: contains not printable characters */
    private Map<String, String> f167;

    j(Context context, String str, String str2, String str3, String str4, String str5, String str6, Map<String, String> map, @Nullable Intent intent) {
        this.f162 = new WeakReference<>(context);
        this.f163 = str;
        this.f161 = str2;
        this.f165 = str4;
        this.f159 = str5;
        this.f166 = str6;
        this.f167 = map;
        this.f164 = str3;
        this.f160 = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.f163 != null && this.f163.length() != 0 && !AppsFlyerLib.getInstance().isTrackingStopped()) {
            HttpURLConnection httpURLConnection = null;
            try {
                try {
                    Context context = this.f162.get();
                    if (context != null) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("public-key", this.f161);
                        hashMap.put("sig-data", this.f165);
                        hashMap.put("signature", this.f164);
                        final HashMap hashMap2 = new HashMap();
                        hashMap2.putAll(hashMap);
                        Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() { // from class: com.appsflyer.j.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                j.m151(j.this, hashMap2, j.this.f167, j.this.f162);
                            }
                        }, 5L, TimeUnit.MILLISECONDS);
                        hashMap.put("dev_key", this.f163);
                        hashMap.put("app_id", context.getPackageName());
                        hashMap.put("uid", AppsFlyerLib.getInstance().getAppsFlyerUID(context));
                        hashMap.put(ServerParameters.ADVERTISING_ID_PARAM, AppsFlyerProperties.getInstance().getString(ServerParameters.ADVERTISING_ID_PARAM));
                        String jSONObject = new JSONObject(hashMap).toString();
                        String url = ServerConfigHandler.getUrl("https://sdk-services.%s/validate-android-signature");
                        s.m186().m200(url, jSONObject);
                        HttpURLConnection m150 = m150(jSONObject, url);
                        int i = -1;
                        if (m150 != null) {
                            i = m150.getResponseCode();
                        }
                        AppsFlyerLib.getInstance();
                        String m43 = AppsFlyerLib.m43(m150);
                        s.m186().m199(url, i, m43);
                        JSONObject jSONObject2 = new JSONObject(m43);
                        jSONObject2.put("code", i);
                        if (i == 200) {
                            AFLogger.afInfoLog(new StringBuilder("Validate response 200 ok: ").append(jSONObject2.toString()).toString());
                            m148(jSONObject2.optBoolean("result") ? jSONObject2.getBoolean("result") : false, this.f165, this.f159, this.f166, jSONObject2.toString());
                        } else {
                            AFLogger.afInfoLog("Failed Validate request");
                            m148(false, this.f165, this.f159, this.f166, jSONObject2.toString());
                        }
                        if (m150 != null) {
                            m150.disconnect();
                        }
                    }
                } catch (Throwable th) {
                    if (AppsFlyerLib.f19 != null) {
                        AFLogger.afErrorLog("Failed Validate request + ex", th);
                        m148(false, this.f165, this.f159, this.f166, th.getMessage());
                    }
                    AFLogger.afErrorLog(th.getMessage(), th);
                    if (0 != 0) {
                        httpURLConnection.disconnect();
                    }
                }
            } catch (Throwable th2) {
                if (0 != 0) {
                    httpURLConnection.disconnect();
                }
                throw th2;
            }
        }
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private static HttpURLConnection m150(String str, String str2) throws IOException {
        HttpURLConnection httpURLConnection = null;
        try {
            k kVar = new k(null, AppsFlyerLib.getInstance().isTrackingStopped());
            kVar.f177 = str;
            kVar.m152();
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                AFLogger.afDebugLog(new StringBuilder("Main thread detected. Calling ").append(str2).append(" in a new thread.").toString());
                kVar.execute(str2);
            } else {
                AFLogger.afDebugLog(new StringBuilder("Calling ").append(str2).append(" (on current thread: ").append(Thread.currentThread().toString()).append(" )").toString());
                kVar.onPreExecute();
                kVar.onPostExecute(kVar.doInBackground(str2));
            }
            httpURLConnection = kVar.m153();
            return httpURLConnection;
        } catch (Throwable th) {
            AFLogger.afErrorLog("Could not send callStats request", th);
            return httpURLConnection;
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static void m148(boolean z, String str, String str2, String str3, String str4) {
        if (AppsFlyerLib.f19 != null) {
            AFLogger.afDebugLog(new StringBuilder("Validate callback parameters: ").append(str).append(" ").append(str2).append(" ").append(str3).toString());
            if (z) {
                AFLogger.afDebugLog("Validate in app purchase success: ".concat(String.valueOf(str4)));
                AppsFlyerLib.f19.onValidateInApp();
                return;
            }
            AFLogger.afDebugLog("Validate in app purchase failed: ".concat(String.valueOf(str4)));
            AppsFlyerInAppPurchaseValidatorListener appsFlyerInAppPurchaseValidatorListener = AppsFlyerLib.f19;
            if (str4 == null) {
                str4 = "Failed validating";
            }
            appsFlyerInAppPurchaseValidatorListener.onValidateInAppFailure(str4);
        }
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    static /* synthetic */ void m151(j jVar, Map map, Map map2, WeakReference weakReference) {
        HttpURLConnection httpURLConnection = null;
        if (weakReference.get() != null) {
            String obj = new StringBuilder().append(ServerConfigHandler.getUrl(f158)).append(((Context) weakReference.get()).getPackageName()).toString();
            SharedPreferences sharedPreferences = ((Context) weakReference.get()).getSharedPreferences("appsflyer-data", 0);
            String string = sharedPreferences.getString("referrer", null);
            if (string == null) {
                string = "";
            }
            Map<String, Object> m77 = AppsFlyerLib.getInstance().m77((Context) weakReference.get(), jVar.f163, AFInAppEventType.PURCHASE, "", string, true, sharedPreferences, false, jVar.f160, null);
            m77.put("price", jVar.f159);
            m77.put("currency", jVar.f166);
            JSONObject jSONObject = new JSONObject(m77);
            JSONObject jSONObject2 = new JSONObject();
            try {
                for (Map.Entry entry : map.entrySet()) {
                    jSONObject2.put((String) entry.getKey(), entry.getValue());
                }
                jSONObject.put("receipt_data", jSONObject2);
            } catch (JSONException e) {
                AFLogger.afErrorLog("Failed to build 'receipt_data'", e);
            }
            if (map2 != null) {
                JSONObject jSONObject3 = new JSONObject();
                try {
                    for (Map.Entry entry2 : map2.entrySet()) {
                        jSONObject3.put((String) entry2.getKey(), entry2.getValue());
                    }
                    jSONObject.put("extra_prms", jSONObject3);
                } catch (JSONException e2) {
                    AFLogger.afErrorLog("Failed to build 'extra_prms'", e2);
                }
            }
            String jSONObject4 = jSONObject.toString();
            s.m186().m200(obj, jSONObject4);
            try {
                try {
                    HttpURLConnection m150 = m150(jSONObject4, obj);
                    int i = -1;
                    if (m150 != null) {
                        i = m150.getResponseCode();
                    }
                    AppsFlyerLib.getInstance();
                    String m43 = AppsFlyerLib.m43(m150);
                    s.m186().m199(obj, i, m43);
                    JSONObject jSONObject5 = new JSONObject(m43);
                    if (i == 200) {
                        AFLogger.afInfoLog(new StringBuilder("Validate-WH response - 200: ").append(jSONObject5.toString()).toString());
                    } else {
                        AFLogger.afWarnLog(new StringBuilder("Validate-WH response failed - ").append(i).append(": ").append(jSONObject5.toString()).toString());
                    }
                    if (m150 != null) {
                        m150.disconnect();
                    }
                } catch (Throwable th) {
                    AFLogger.afErrorLog(th.getMessage(), th);
                    if (0 != 0) {
                        httpURLConnection.disconnect();
                    }
                }
            } catch (Throwable th2) {
                if (0 != 0) {
                    httpURLConnection.disconnect();
                }
                throw th2;
            }
        }
    }
}
