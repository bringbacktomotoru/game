package com.appsflyer;

import android.support.v4.app.NotificationCompat;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AppsFlyer2dXConversionCallback implements AppsFlyerConversionListener {
    public native void onAppOpenAttributionNative(Object obj);

    public native void onAttributionFailureNative(Object obj);

    public native void onInstallConversionDataLoadedNative(Object obj);

    public native void onInstallConversionFailureNative(Object obj);

    @Override // com.appsflyer.AppsFlyerConversionListener
    public void onInstallConversionDataLoaded(Map<String, String> map) {
        onInstallConversionDataLoadedNative(map);
    }

    @Override // com.appsflyer.AppsFlyerConversionListener
    public void onInstallConversionFailure(String str) {
        m20("onAttributionFailure", str);
    }

    @Override // com.appsflyer.AppsFlyerConversionListener
    public void onAppOpenAttribution(Map<String, String> map) {
        onAppOpenAttributionNative(map);
    }

    @Override // com.appsflyer.AppsFlyerConversionListener
    public void onAttributionFailure(String str) {
        m20("onInstallConversionFailure", str);
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private void m20(String str, String str2) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(NotificationCompat.CATEGORY_STATUS, "failure");
            jSONObject.put("data", str2);
            char c = 65535;
            switch (str.hashCode()) {
                case -1390007222:
                    if (str.equals("onAttributionFailure")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1050716216:
                    if (str.equals("onInstallConversionFailure")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    onInstallConversionFailureNative(jSONObject);
                    break;
                case 1:
                    onAttributionFailureNative(jSONObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
