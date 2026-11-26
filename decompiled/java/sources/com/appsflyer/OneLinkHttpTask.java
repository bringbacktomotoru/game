package com.appsflyer;

import android.text.TextUtils;
import com.sony.smartar.CameraDeviceInterface;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;

/* loaded from: classes.dex */
public abstract class OneLinkHttpTask implements Runnable {

    /* renamed from: ˎ, reason: contains not printable characters */
    private AppsFlyerLib f95;

    /* renamed from: ˏ, reason: contains not printable characters */
    String f96;

    /* renamed from: ॱ, reason: contains not printable characters */
    private HttpsUrlConnectionProvider f97;

    /* renamed from: ˊ */
    abstract void mo89(String str);

    /* renamed from: ˋ */
    abstract void mo90();

    /* renamed from: ˏ */
    abstract String mo91();

    /* renamed from: ˏ */
    abstract void mo92(HttpsURLConnection httpsURLConnection) throws JSONException, IOException;

    OneLinkHttpTask(AppsFlyerLib appsFlyerLib) {
        this.f95 = appsFlyerLib;
    }

    public void setConnProvider(HttpsUrlConnectionProvider httpsUrlConnectionProvider) {
        this.f97 = httpsUrlConnectionProvider;
    }

    public static class HttpsUrlConnectionProvider {
        /* renamed from: ˋ, reason: contains not printable characters */
        static HttpsURLConnection m93(String str) throws IOException {
            return (HttpsURLConnection) new URL(str).openConnection();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        String str = "";
        String str2 = "";
        String mo91 = mo91();
        AFLogger.afRDLog("oneLinkUrl: ".concat(String.valueOf(mo91)));
        try {
            HttpsURLConnection m93 = HttpsUrlConnectionProvider.m93(mo91);
            m93.addRequestProperty("content-type", "application/json");
            StringBuilder sb = new StringBuilder();
            sb.append(AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY)).append(currentTimeMillis);
            m93.addRequestProperty("authorization", C0006r.m179(sb.toString()));
            m93.addRequestProperty("af-timestamp", String.valueOf(currentTimeMillis));
            m93.setReadTimeout(CameraDeviceInterface.AUTO_ADJUST_DURATION);
            m93.setConnectTimeout(CameraDeviceInterface.AUTO_ADJUST_DURATION);
            mo92(m93);
            int responseCode = m93.getResponseCode();
            str = AppsFlyerLib.m43(m93);
            if (responseCode == 200) {
                AFLogger.afInfoLog("Status 200 ok");
            } else {
                str2 = new StringBuilder("Response code = ").append(responseCode).append(" content = ").append(str).toString();
            }
        } catch (Throwable th) {
            AFLogger.afErrorLog("Error while calling ".concat(String.valueOf(mo91)), th);
            str2 = new StringBuilder("Error while calling ").append(mo91).append(" stacktrace: ").append(th.toString()).toString();
        }
        if (TextUtils.isEmpty(str2)) {
            AFLogger.afInfoLog("Connection call succeeded: ".concat(String.valueOf(str)));
            mo89(str);
        } else {
            AFLogger.afWarnLog("Connection error: ".concat(String.valueOf(str2)));
            mo90();
        }
    }
}
