package com.appsflyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AppsFlyerProperties {
    public static final String ADDITIONAL_CUSTOM_DATA = "additionalCustomData";
    public static final String AF_KEY = "AppsFlyerKey";
    public static final String AF_WAITFOR_CUSTOMERID = "waitForCustomerId";
    public static final String APP_ID = "appid";
    public static final String APP_USER_ID = "AppUserId";
    public static final String CHANNEL = "channel";
    public static final String COLLECT_ANDROID_ID = "collectAndroidId";
    public static final String COLLECT_ANDROID_ID_FORCE_BY_USER = "collectAndroidIdForceByUser";
    public static final String COLLECT_FACEBOOK_ATTR_ID = "collectFacebookAttrId";
    public static final String COLLECT_FINGER_PRINT = "collectFingerPrint";
    public static final String COLLECT_IMEI = "collectIMEI";
    public static final String COLLECT_IMEI_FORCE_BY_USER = "collectIMEIForceByUser";
    public static final String COLLECT_MAC = "collectMAC";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String DEVICE_TRACKING_DISABLED = "deviceTrackingDisabled";
    public static final String DISABLE_KEYSTORE = "keyPropDisableAFKeystore";
    public static final String DISABLE_LOGS_COMPLETELY = "disableLogs";
    public static final String DISABLE_OTHER_SDK = "disableOtherSdk";
    public static final String EMAIL_CRYPT_TYPE = "userEmailsCryptType";
    public static final String ENABLE_GPS_FALLBACK = "enableGpsFallback";
    public static final String EXTENSION = "sdkExtension";
    public static final String IS_MONITOR = "shouldMonitor";
    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String LAUNCH_PROTECT_ENABLED = "launchProtectEnabled";
    public static final String ONELINK_DOMAIN = "onelinkDomain";
    public static final String ONELINK_ID = "oneLinkSlug";
    public static final String ONELINK_SCHEME = "onelinkScheme";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_EMAILS = "userEmails";
    public static final String USE_HTTP_FALLBACK = "useHttpFallback";

    /* renamed from: ˏ, reason: contains not printable characters */
    private static AppsFlyerProperties f81 = new AppsFlyerProperties();

    /* renamed from: ˊ, reason: contains not printable characters */
    private String f83;

    /* renamed from: ˎ, reason: contains not printable characters */
    private boolean f85;

    /* renamed from: ॱ, reason: contains not printable characters */
    private boolean f86;

    /* renamed from: ˋ, reason: contains not printable characters */
    private Map<String, Object> f84 = new HashMap();

    /* renamed from: ʼ, reason: contains not printable characters */
    private boolean f82 = false;

    public void remove(String str) {
        this.f84.remove(str);
    }

    public enum EmailsCryptType {
        NONE(0),
        SHA1(1),
        MD5(2),
        SHA256(3);


        /* renamed from: ˋ, reason: contains not printable characters */
        private final int f88;

        EmailsCryptType(int i) {
            this.f88 = i;
        }

        public final int getValue() {
            return this.f88;
        }
    }

    private AppsFlyerProperties() {
    }

    public static AppsFlyerProperties getInstance() {
        return f81;
    }

    public void set(String str, String str2) {
        this.f84.put(str, str2);
    }

    public void set(String str, String[] strArr) {
        this.f84.put(str, strArr);
    }

    public void set(String str, int i) {
        this.f84.put(str, Integer.toString(i));
    }

    public void set(String str, long j) {
        this.f84.put(str, Long.toString(j));
    }

    public void set(String str, boolean z) {
        this.f84.put(str, Boolean.toString(z));
    }

    public void setCustomData(String str) {
        this.f84.put(ADDITIONAL_CUSTOM_DATA, str);
    }

    public void setUserEmails(String str) {
        this.f84.put(USER_EMAILS, str);
    }

    public String getString(String str) {
        return (String) this.f84.get(str);
    }

    public boolean getBoolean(String str, boolean z) {
        String string = getString(str);
        return string == null ? z : Boolean.valueOf(string).booleanValue();
    }

    public int getInt(String str, int i) {
        String string = getString(str);
        return string == null ? i : Integer.valueOf(string).intValue();
    }

    public long getLong(String str, long j) {
        String string = getString(str);
        return string == null ? j : Long.valueOf(string).longValue();
    }

    public Object getObject(String str) {
        return this.f84.get(str);
    }

    protected boolean isOnReceiveCalled() {
        return this.f86;
    }

    protected void setOnReceiveCalled() {
        this.f86 = true;
    }

    protected boolean isFirstLaunchCalled() {
        return this.f85;
    }

    protected void setFirstLaunchCalled(boolean z) {
        this.f85 = z;
    }

    protected void setFirstLaunchCalled() {
        this.f85 = true;
    }

    protected void setReferrer(String str) {
        set("AF_REFERRER", str);
        this.f83 = str;
    }

    public String getReferrer(Context context) {
        if (this.f83 != null) {
            return this.f83;
        }
        if (getString("AF_REFERRER") != null) {
            return getString("AF_REFERRER");
        }
        if (context != null) {
            return context.getSharedPreferences("appsflyer-data", 0).getString("referrer", null);
        }
        return null;
    }

    public boolean isEnableLog() {
        return getBoolean("shouldLog", true);
    }

    public boolean isLogsDisabledCompletely() {
        return getBoolean(DISABLE_LOGS_COMPLETELY, false);
    }

    public boolean isOtherSdkStringDisabled() {
        return getBoolean(DISABLE_OTHER_SDK, false);
    }

    @SuppressLint({"CommitPrefEdits"})
    public void saveProperties(SharedPreferences sharedPreferences) {
        String jSONObject = new JSONObject(this.f84).toString();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("savedProperties", jSONObject);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }

    public void loadProperties(Context context) {
        String string;
        if (!this.f82 && (string = context.getSharedPreferences("appsflyer-data", 0).getString("savedProperties", null)) != null) {
            AFLogger.afDebugLog("Loading properties..");
            try {
                JSONObject jSONObject = new JSONObject(string);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    if (this.f84.get(next) == null) {
                        this.f84.put(next, jSONObject.getString(next));
                    }
                }
                this.f82 = true;
            } catch (JSONException e) {
                AFLogger.afErrorLog("Failed loading properties", e);
            }
            AFLogger.afDebugLog(new StringBuilder("Done loading properties: ").append(this.f82).toString());
        }
    }
}
