package com.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerProperties;
import com.appsflyer.OneLinkHttpTask;
import com.appsflyer.c;
import com.appsflyer.cache.CacheManager;
import com.appsflyer.cache.RequestCacheData;
import com.appsflyer.f;
import com.appsflyer.g;
import com.appsflyer.n;
import com.appsflyer.q;
import com.appsflyer.share.Constants;
import com.appsflyer.u;
import com.google.android.gms.common.GoogleApiAvailability;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AppsFlyerLib implements com.appsflyer.a {
    public static final String AF_PRE_INSTALL_PATH = "AF_PRE_INSTALL_PATH";
    public static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    public static final String ATTRIBUTION_ID_CONTENT_URI = "content://com.facebook.katana.provider.AttributionIdProvider";
    public static final String IS_STOP_TRACKING_USED = "is_stop_tracking_used";
    public static final String LOG_TAG = "AppsFlyer_4.8.19";
    public static final String PRE_INSTALL_SYSTEM_DEFAULT = "/data/local/tmp/pre_install.appsflyer";
    public static final String PRE_INSTALL_SYSTEM_DEFAULT_ETC = "/etc/pre_install.appsflyer";
    public static final String PRE_INSTALL_SYSTEM_RO_PROP = "ro.appsflyer.preinstall.path";

    /* renamed from: ʻॱ, reason: contains not printable characters */
    private long f27;

    /* renamed from: ʾ, reason: contains not printable characters */
    private long f30;

    /* renamed from: ʿ, reason: contains not printable characters */
    private n.a f31;

    /* renamed from: ˈ, reason: contains not printable characters */
    private long f32;

    /* renamed from: ˉ, reason: contains not printable characters */
    private String f33;

    /* renamed from: ˊ, reason: contains not printable characters */
    String f34;

    /* renamed from: ˊˊ, reason: contains not printable characters */
    private Map<Long, String> f35;

    /* renamed from: ˊᐝ, reason: contains not printable characters */
    private long f37;

    /* renamed from: ˋᐝ, reason: contains not printable characters */
    private boolean f40;

    /* renamed from: ˌ, reason: contains not printable characters */
    private boolean f41;

    /* renamed from: ˏ, reason: contains not printable characters */
    String f44;

    /* renamed from: ॱᐝ, reason: contains not printable characters */
    private Map<String, String> f49;

    /* renamed from: ॱ, reason: contains not printable characters */
    static final String f23 = BuildConfig.VERSION_NAME.substring(0, BuildConfig.VERSION_NAME.indexOf("."));

    /* renamed from: ᐝ, reason: contains not printable characters */
    private static final String f25 = new StringBuilder().append(f23).append("/androidevent?buildnumber=4.8.19&app_id=").toString();

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private static String f24 = new StringBuilder("https://attr.%s/api/v").append(f25).toString();

    /* renamed from: ʻ, reason: contains not printable characters */
    private static String f15 = new StringBuilder("https://t.%s/api/v").append(f25).toString();

    /* renamed from: ʼ, reason: contains not printable characters */
    private static String f16 = new StringBuilder("https://events.%s/api/v").append(f25).toString();

    /* renamed from: ˎ, reason: contains not printable characters */
    static final String f21 = new StringBuilder("https://register.%s/api/v").append(f25).toString();

    /* renamed from: ˋॱ, reason: contains not printable characters */
    private static final List<String> f20 = Arrays.asList("is_cache");

    /* renamed from: ˊॱ, reason: contains not printable characters */
    private static final List<String> f18 = Arrays.asList("googleplay", "playstore", "googleplaystore");

    /* renamed from: ˏॱ, reason: contains not printable characters */
    private static AppsFlyerConversionListener f22 = null;

    /* renamed from: ˋ, reason: contains not printable characters */
    static AppsFlyerInAppPurchaseValidatorListener f19 = null;

    /* renamed from: ᐝॱ, reason: contains not printable characters */
    private static AppsFlyerTrackingRequestListener f26 = null;

    /* renamed from: ʼॱ, reason: contains not printable characters */
    private static AppsFlyerLib f17 = new AppsFlyerLib();

    /* renamed from: ʽ, reason: contains not printable characters */
    private long f28 = -1;

    /* renamed from: ͺ, reason: contains not printable characters */
    private long f45 = -1;

    /* renamed from: ॱˊ, reason: contains not printable characters */
    private long f46 = TimeUnit.SECONDS.toMillis(5);

    /* renamed from: ॱˎ, reason: contains not printable characters */
    private com.appsflyer.d f48 = null;

    /* renamed from: ॱˋ, reason: contains not printable characters */
    private boolean f47 = false;

    /* renamed from: ʽॱ, reason: contains not printable characters */
    private ScheduledExecutorService f29 = null;
    protected Uri latestDeepLink = null;

    /* renamed from: ˊˋ, reason: contains not printable characters */
    private boolean f36 = false;

    /* renamed from: ˋˊ, reason: contains not printable characters */
    private boolean f38 = false;

    /* renamed from: ˋˋ, reason: contains not printable characters */
    private p f39 = new p();

    /* renamed from: ˎˎ, reason: contains not printable characters */
    private boolean f43 = false;

    /* renamed from: ˍ, reason: contains not printable characters */
    private boolean f42 = false;

    /* renamed from: ˋ, reason: contains not printable characters */
    static /* synthetic */ void m36(AppsFlyerLib appsFlyerLib, String str, String str2, String str3, WeakReference weakReference, String str4, boolean z) throws IOException {
        URL url = new URL(str);
        AFLogger.afInfoLog(new StringBuilder("url: ").append(url.toString()).toString());
        f.AnonymousClass4.m126("data: ".concat(String.valueOf(str2)));
        m65((Context) weakReference.get(), LOG_TAG, "EVENT_DATA", str2);
        try {
            appsFlyerLib.m37(url, str2, str3, weakReference, str4, z);
        } catch (IOException e2) {
            AFLogger.afErrorLog("Exception in sendRequestToServer. ", e2);
            if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.USE_HTTP_FALLBACK, false)) {
                appsFlyerLib.m37(new URL(str.replace("https:", "http:")), str2, str3, weakReference, str4, z);
            } else {
                AFLogger.afInfoLog(new StringBuilder("failed to send requeset to server. ").append(e2.getLocalizedMessage()).toString());
                m65((Context) weakReference.get(), LOG_TAG, "ERROR", e2.getLocalizedMessage());
                throw e2;
            }
        }
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    static /* synthetic */ void m27(AppsFlyerLib appsFlyerLib, Context context, String str, String str2, String str3, String str4, boolean z, boolean z2, Intent intent, String str5) {
        if (context == null) {
            AFLogger.afDebugLog("sendTrackingWithEvent - got null context. skipping event/launch.");
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        AppsFlyerProperties.getInstance().saveProperties(sharedPreferences);
        if (!appsFlyerLib.isTrackingStopped()) {
            AFLogger.afInfoLog(new StringBuilder("sendTrackingWithEvent from activity: ").append(context.getClass().getName()).toString());
        }
        boolean z3 = str2 == null;
        Map<String, Object> m77 = appsFlyerLib.m77(context, str, str2, str3, str4, z, sharedPreferences, z3, intent, str5);
        String str6 = (String) m77.get("appsflyerKey");
        if (str6 == null || str6.length() == 0) {
            AFLogger.afDebugLog("Not sending data yet, waiting for dev key");
            return;
        }
        if (!appsFlyerLib.isTrackingStopped()) {
            AFLogger.afInfoLog("AppsFlyerLib.sendTrackingWithEvent");
        }
        String obj = new StringBuilder().append(z3 ? z2 ? ServerConfigHandler.getUrl(f24) : ServerConfigHandler.getUrl(f15) : ServerConfigHandler.getUrl(f16)).append(context.getPackageName()).toString();
        int m23 = m23(sharedPreferences, "appsFlyerCount", false);
        if (!(AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_ANDROID_ID_FORCE_BY_USER, false) || AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_IMEI_FORCE_BY_USER, false)) && m77.get(ServerParameters.ADVERTISING_ID_PARAM) != null) {
            try {
                if (TextUtils.isEmpty(appsFlyerLib.f44) && m77.remove("android_id") != null) {
                    AFLogger.afInfoLog("validateGaidAndIMEI :: removing: android_id");
                }
                if (TextUtils.isEmpty(appsFlyerLib.f34) && m77.remove("imei") != null) {
                    AFLogger.afInfoLog("validateGaidAndIMEI :: removing: imei");
                }
            } catch (Exception e2) {
                AFLogger.afErrorLog("failed to remove IMEI or AndroidID key from params; ", e2);
            }
        }
        a aVar = new a(appsFlyerLib, obj, m77, context.getApplicationContext(), z3, m23, (byte) 0);
        if (z3 && m52(context)) {
            if (!(appsFlyerLib.f49 != null && appsFlyerLib.f49.size() > 0)) {
                AFLogger.afDebugLog("Failed to get new referrer, wait ...");
                m38(AFExecutor.getInstance().m5(), aVar, 500L, TimeUnit.MILLISECONDS);
                return;
            }
        }
        aVar.run();
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    static /* synthetic */ ScheduledExecutorService m32(AppsFlyerLib appsFlyerLib) {
        appsFlyerLib.f29 = null;
        return null;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    static /* synthetic */ void m50(Map map) {
        if (f22 != null) {
            try {
                f22.onAppOpenAttribution(map);
            } catch (Throwable th) {
                AFLogger.afErrorLog(th.getLocalizedMessage(), th);
            }
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    static /* synthetic */ boolean m53(AppsFlyerLib appsFlyerLib) {
        return appsFlyerLib.f49 != null && appsFlyerLib.f49.size() > 0;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    static /* synthetic */ String m59(String str) {
        return AppsFlyerProperties.getInstance().getString(str);
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final void m74() {
        this.f30 = System.currentTimeMillis();
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m79() {
        this.f32 = System.currentTimeMillis();
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    final void m76(Context context, Intent intent) {
        byte b2 = 0;
        String str = null;
        String stringExtra = intent.getStringExtra(AppsFlyerProperties.IS_MONITOR);
        if (stringExtra != null) {
            AFLogger.afInfoLog("Turning on monitoring.");
            AppsFlyerProperties.getInstance().set(AppsFlyerProperties.IS_MONITOR, stringExtra.equals("true"));
            m65(context, null, "START_TRACKING", context.getPackageName());
            return;
        }
        AFLogger.afInfoLog("****** onReceive called *******");
        AppsFlyerProperties.getInstance().setOnReceiveCalled();
        String stringExtra2 = intent.getStringExtra("referrer");
        AFLogger.afInfoLog("Play store referrer: ".concat(String.valueOf(stringExtra2)));
        if (stringExtra2 != null) {
            if ("AppsFlyer_Test".equals(intent.getStringExtra("TestIntegrationMode"))) {
                SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
                edit.clear();
                if (Build.VERSION.SDK_INT >= 9) {
                    edit.apply();
                } else {
                    edit.commit();
                }
                AppsFlyerProperties.getInstance().setFirstLaunchCalled(false);
                AFLogger.afInfoLog("Test mode started..");
                this.f37 = System.currentTimeMillis();
            }
            SharedPreferences.Editor edit2 = context.getSharedPreferences("appsflyer-data", 0).edit();
            edit2.putString("referrer", stringExtra2);
            if (Build.VERSION.SDK_INT >= 9) {
                edit2.apply();
            } else {
                edit2.commit();
            }
            AppsFlyerProperties.getInstance().setReferrer(stringExtra2);
            if (AppsFlyerProperties.getInstance().isFirstLaunchCalled()) {
                AFLogger.afInfoLog("onReceive: isLaunchCalled");
                if (stringExtra2 == null || stringExtra2.length() <= 5) {
                    return;
                }
                m38(AFExecutor.getInstance().m5(), new c(this, new WeakReference(context.getApplicationContext()), str, str, str, stringExtra2, true, intent, str, b2), 5L, TimeUnit.MILLISECONDS);
            }
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static void m51(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            try {
                JSONArray jSONArray = new JSONArray((String) jSONObject.get(keys.next()));
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(Long.valueOf(jSONArray.getLong(i)));
                }
            } catch (JSONException e2) {
            }
        }
        Collections.sort(arrayList);
        Iterator<String> keys2 = jSONObject.keys();
        String str = null;
        while (keys2.hasNext() && str == null) {
            String next = keys2.next();
            try {
                JSONArray jSONArray2 = new JSONArray((String) jSONObject.get(next));
                int i2 = 0;
                while (i2 < jSONArray2.length()) {
                    if (jSONArray2.getLong(i2) == ((Long) arrayList.get(0)).longValue() || jSONArray2.getLong(i2) == ((Long) arrayList.get(1)).longValue() || jSONArray2.getLong(i2) == ((Long) arrayList.get(arrayList.size() - 1)).longValue()) {
                        str = null;
                        break;
                    } else {
                        i2++;
                        str = next;
                    }
                }
            } catch (JSONException e3) {
                str = str;
            }
        }
        if (str != null) {
            jSONObject.remove(str);
        }
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    static void m33(Context context, String str) {
        JSONArray jSONArray;
        JSONObject jSONObject;
        AFLogger.afDebugLog("received a new (extra) referrer: ".concat(String.valueOf(str)));
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String string = context.getSharedPreferences("appsflyer-data", 0).getString("extraReferrers", null);
            if (string == null) {
                jSONObject = new JSONObject();
                jSONArray = new JSONArray();
            } else {
                JSONObject jSONObject2 = new JSONObject(string);
                if (jSONObject2.has(str)) {
                    jSONArray = new JSONArray((String) jSONObject2.get(str));
                    jSONObject = jSONObject2;
                } else {
                    jSONArray = new JSONArray();
                    jSONObject = jSONObject2;
                }
            }
            if (jSONArray.length() < 5) {
                jSONArray.put(currentTimeMillis);
            }
            if (jSONObject.length() >= 4) {
                m51(jSONObject);
            }
            jSONObject.put(str, jSONArray.toString());
            String jSONObject3 = jSONObject.toString();
            SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
            edit.putString("extraReferrers", jSONObject3);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        } catch (JSONException e2) {
        } catch (Throwable th) {
            AFLogger.afErrorLog(new StringBuilder("Couldn't save referrer - ").append(str).append(": ").toString(), th);
        }
    }

    private AppsFlyerLib() {
        AFVersionDeclaration.init();
    }

    public static AppsFlyerLib getInstance() {
        return f17;
    }

    public void stopTracking(boolean z, Context context) {
        this.f42 = z;
        CacheManager.getInstance().clearCache(context);
        if (this.f42) {
            SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
            edit.putBoolean(IS_STOP_TRACKING_USED, true);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        }
    }

    public String getSdkVersion() {
        s.m186().m195("getSdkVersion", new String[0]);
        return "version: 4.8.19 (build 419)";
    }

    public void onPause(Context context) {
        g.b.m128(context);
        f m119 = f.m119(context);
        m119.f130.post(m119.f135);
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private void m45(Application application) {
        AppsFlyerProperties.getInstance().loadProperties(application.getApplicationContext());
        if (Build.VERSION.SDK_INT >= 14) {
            if (Build.VERSION.SDK_INT >= 14 && this.f31 == null) {
                n.m159();
                this.f31 = new n.a() { // from class: com.appsflyer.AppsFlyerLib.2
                    @Override // com.appsflyer.n.a
                    /* renamed from: ॱ, reason: contains not printable characters */
                    public final void mo82(Activity activity) {
                        if (2 > AppsFlyerLib.m54(AppsFlyerLib.m55(activity))) {
                            f m119 = f.m119(activity);
                            m119.f130.post(m119.f135);
                            m119.f130.post(m119.f127);
                        }
                        AFLogger.afInfoLog("onBecameForeground");
                        AppsFlyerLib.getInstance().m74();
                        AppsFlyerLib.getInstance().m78(activity, (String) null, (Map<String, Object>) null);
                        AFLogger.resetDeltaTime();
                    }

                    @Override // com.appsflyer.n.a
                    /* renamed from: ˊ, reason: contains not printable characters */
                    public final void mo81(WeakReference<Context> weakReference) {
                        g.b.m128(weakReference.get());
                        f m119 = f.m119(weakReference.get());
                        m119.f130.post(m119.f135);
                    }
                };
                n.m161().m165(application, this.f31);
                return;
            }
            return;
        }
        AFLogger.afInfoLog("SDK<14 call trackEvent manually");
        AFLogger.afInfoLog("onBecameForeground");
        getInstance().f30 = System.currentTimeMillis();
        getInstance().m78(application, (String) null, (Map<String, Object>) null);
        AFLogger.resetDeltaTime();
    }

    @Deprecated
    public void setGCMProjectID(String str) {
        s.m186().m195("setGCMProjectID", str);
        AFLogger.afWarnLog("Method 'setGCMProjectNumber' is deprecated. Please follow the documentation.");
        enableUninstallTracking(str);
    }

    @Deprecated
    public void setGCMProjectNumber(String str) {
        s.m186().m195("setGCMProjectNumber", str);
        AFLogger.afWarnLog("Method 'setGCMProjectNumber' is deprecated. Please follow the documentation.");
        enableUninstallTracking(str);
    }

    @Deprecated
    public void setGCMProjectNumber(Context context, String str) {
        s.m186().m195("setGCMProjectNumber", str);
        AFLogger.afWarnLog("Method 'setGCMProjectNumber' is deprecated. Please use 'enableUninstallTracking'.");
        enableUninstallTracking(str);
    }

    public void enableUninstallTracking(String str) {
        s.m186().m195("enableUninstallTracking", str);
        AppsFlyerProperties.getInstance().set("gcmProjectNumber", str);
    }

    public void updateServerUninstallToken(Context context, String str) {
        if (str != null) {
            u.m216(context, new c.a.C0004c(str));
        }
    }

    public void setDebugLog(boolean z) {
        s.m186().m195("setDebugLog", String.valueOf(z));
        AppsFlyerProperties.getInstance().set("shouldLog", z);
        AppsFlyerProperties.getInstance().set("logLevel", (z ? AFLogger.LogLevel.DEBUG : AFLogger.LogLevel.NONE).getLevel());
    }

    public void setImeiData(String str) {
        s.m186().m195("setImeiData", str);
        this.f34 = str;
    }

    public void setAndroidIdData(String str) {
        s.m186().m195("setAndroidIdData", str);
        this.f44 = str;
    }

    public AppsFlyerLib enableLocationCollection(boolean z) {
        this.f36 = z;
        return this;
    }

    @Deprecated
    public void setAppUserId(String str) {
        s.m186().m195("setAppUserId", str);
        setCustomerUserId(str);
    }

    public void setCustomerUserId(String str) {
        s.m186().m195("setCustomerUserId", str);
        AFLogger.afInfoLog("setCustomerUserId = ".concat(String.valueOf(str)));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.APP_USER_ID, str);
    }

    public void waitForCustomerUserId(boolean z) {
        AFLogger.afInfoLog("initAfterCustomerUserID: ".concat(String.valueOf(z)), true);
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, z);
    }

    public void setCustomerIdAndTrack(String str, @NonNull Context context) {
        if (context != null) {
            if (!(AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, false) && AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.APP_USER_ID) == null)) {
                setCustomerUserId(str);
                AFLogger.afInfoLog("waitForCustomerUserId is false; setting CustomerUserID: ".concat(String.valueOf(str)), true);
                return;
            }
            setCustomerUserId(str);
            AppsFlyerProperties.getInstance().set(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, false);
            AFLogger.afInfoLog(new StringBuilder("CustomerUserId set: ").append(str).append(" - Initializing AppsFlyer Tacking").toString(), true);
            String referrer = AppsFlyerProperties.getInstance().getReferrer(context);
            String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY);
            if (referrer == null) {
                referrer = "";
            }
            m35(context, string, (String) null, (String) null, referrer, context instanceof Activity ? ((Activity) context).getIntent() : null, (String) null);
            if (AppsFlyerProperties.getInstance().getString("afUninstallToken") != null) {
                m80(context, AppsFlyerProperties.getInstance().getString("afUninstallToken"));
            }
        }
    }

    public String getOutOfStore(Context context) {
        String string = AppsFlyerProperties.getInstance().getString("api_store_value");
        if (string == null) {
            String m67 = m67((WeakReference<Context>) new WeakReference(context), "AF_STORE");
            if (m67 == null) {
                AFLogger.afInfoLog("No out-of-store value set");
                return null;
            }
            return m67;
        }
        return string;
    }

    public void setOutOfStore(String str) {
        if (str != null) {
            String lowerCase = str.toLowerCase();
            AppsFlyerProperties.getInstance().set("api_store_value", lowerCase);
            AFLogger.afInfoLog("Store API set with value: ".concat(String.valueOf(lowerCase)), true);
            return;
        }
        AFLogger.m17("Cannot set setOutOfStore with null");
    }

    public void setAppInviteOneLink(String str) {
        s.m186().m195("setAppInviteOneLink", str);
        AFLogger.afInfoLog("setAppInviteOneLink = ".concat(String.valueOf(str)));
        if (str == null || !str.equals(AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.ONELINK_ID))) {
            AppsFlyerProperties.getInstance().remove(AppsFlyerProperties.ONELINK_DOMAIN);
            AppsFlyerProperties.getInstance().remove("onelinkVersion");
            AppsFlyerProperties.getInstance().remove(AppsFlyerProperties.ONELINK_SCHEME);
        }
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.ONELINK_ID, str);
    }

    public void setAdditionalData(HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            s.m186().m195("setAdditionalData", hashMap.toString());
            AppsFlyerProperties.getInstance().setCustomData(new JSONObject(hashMap).toString());
        }
    }

    public void sendDeepLinkData(Activity activity) {
        if (activity != null && activity.getIntent() != null) {
            s.m186().m195("sendDeepLinkData", activity.getLocalClassName(), new StringBuilder("activity_intent_").append(activity.getIntent().toString()).toString());
        } else if (activity != null) {
            s.m186().m195("sendDeepLinkData", activity.getLocalClassName(), "activity_intent_null");
        } else {
            s.m186().m195("sendDeepLinkData", "activity_null");
        }
        try {
            m45(activity.getApplication());
            AFLogger.afInfoLog(new StringBuilder("getDeepLinkData with activity ").append(activity.getIntent().getDataString()).toString());
        } catch (Exception e2) {
            AFLogger.afInfoLog("getDeepLinkData Exception: ".concat(String.valueOf(e2)));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x009b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void sendPushNotificationData(android.app.Activity r14) {
        /*
            Method dump skipped, instructions count: 429
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.AppsFlyerLib.sendPushNotificationData(android.app.Activity):void");
    }

    @Deprecated
    public void setUserEmail(String str) {
        s.m186().m195("setUserEmail", str);
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.USER_EMAIL, str);
    }

    public void setUserEmails(String... strArr) {
        s.m186().m195("setUserEmails", strArr);
        setUserEmails(AppsFlyerProperties.EmailsCryptType.NONE, strArr);
    }

    public void setUserEmails(AppsFlyerProperties.EmailsCryptType emailsCryptType, String... strArr) {
        ArrayList arrayList = new ArrayList(strArr.length + 1);
        arrayList.add(emailsCryptType.toString());
        arrayList.addAll(Arrays.asList(strArr));
        s.m186().m195("setUserEmails", (String[]) arrayList.toArray(new String[strArr.length + 1]));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.EMAIL_CRYPT_TYPE, emailsCryptType.getValue());
        HashMap hashMap = new HashMap();
        ArrayList arrayList2 = new ArrayList();
        String str = null;
        for (String str2 : strArr) {
            switch (AnonymousClass5.f56[emailsCryptType.ordinal()]) {
                case 2:
                    str = "md5_el_arr";
                    arrayList2.add(C0006r.m178(str2));
                    break;
                case 3:
                    str = "sha256_el_arr";
                    arrayList2.add(C0006r.m176(str2));
                    break;
                case 4:
                    str = "plain_el_arr";
                    arrayList2.add(str2);
                    break;
                default:
                    str = "sha1_el_arr";
                    arrayList2.add(C0006r.m179(str2));
                    break;
            }
        }
        hashMap.put(str, arrayList2);
        AppsFlyerProperties.getInstance().setUserEmails(new JSONObject(hashMap).toString());
    }

    /* renamed from: com.appsflyer.AppsFlyerLib$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {

        /* renamed from: ˊ, reason: contains not printable characters */
        static final /* synthetic */ int[] f56 = new int[AppsFlyerProperties.EmailsCryptType.values().length];

        static {
            try {
                f56[AppsFlyerProperties.EmailsCryptType.SHA1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f56[AppsFlyerProperties.EmailsCryptType.MD5.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f56[AppsFlyerProperties.EmailsCryptType.SHA256.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f56[AppsFlyerProperties.EmailsCryptType.NONE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public void setCollectAndroidID(boolean z) {
        s.m186().m195("setCollectAndroidID", String.valueOf(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.COLLECT_ANDROID_ID, Boolean.toString(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.COLLECT_ANDROID_ID_FORCE_BY_USER, Boolean.toString(z));
    }

    public void setCollectIMEI(boolean z) {
        s.m186().m195("setCollectIMEI", String.valueOf(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.COLLECT_IMEI, Boolean.toString(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.COLLECT_IMEI_FORCE_BY_USER, Boolean.toString(z));
    }

    @Deprecated
    public void setCollectFingerPrint(boolean z) {
        s.m186().m195("setCollectFingerPrint", String.valueOf(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.COLLECT_FINGER_PRINT, Boolean.toString(z));
    }

    public AppsFlyerLib init(String str, AppsFlyerConversionListener appsFlyerConversionListener) {
        s m186 = s.m186();
        String[] strArr = new String[2];
        strArr[0] = str;
        strArr[1] = appsFlyerConversionListener == null ? "null" : "conversionDataListener";
        m186.m195("init", strArr);
        AFLogger.m19(String.format("Initializing AppsFlyer SDK: (v%s.%s)", BuildConfig.VERSION_NAME, "419"));
        this.f41 = true;
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.AF_KEY, str);
        f.AnonymousClass4.m125(str);
        f22 = appsFlyerConversionListener;
        return this;
    }

    public AppsFlyerLib init(String str, AppsFlyerConversionListener appsFlyerConversionListener, Context context) {
        if (context != null) {
            if (m52(context)) {
                if (this.f48 == null) {
                    this.f48 = new com.appsflyer.d();
                    this.f48.m116(context, this);
                } else {
                    AFLogger.afWarnLog("AFInstallReferrer instance already created");
                }
            }
        } else {
            AFLogger.afWarnLog("init :: context is null, Google Install Referrer will be not initialized!");
        }
        return init(str, appsFlyerConversionListener);
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static boolean m52(@NonNull Context context) {
        if (m23(context.getSharedPreferences("appsflyer-data", 0), "appsFlyerCount", false) > 2) {
            AFLogger.afRDLog("Install referrer will not load, the counter > 2, ");
            return false;
        }
        try {
            Class.forName("com.android.installreferrer.api.InstallReferrerClient");
            if (f.AnonymousClass1.m124(context, "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE")) {
                AFLogger.afDebugLog("Install referrer is allowed");
                return true;
            }
            AFLogger.afDebugLog("Install referrer is not allowed");
            return false;
        } catch (ClassNotFoundException e2) {
            AFLogger.afRDLog("Class com.android.installreferrer.api.InstallReferrerClient not found");
            return false;
        } catch (Throwable th) {
            AFLogger.afErrorLog("An error occurred while trying to verify manifest : com.android.installreferrer.api.InstallReferrerClient", th);
            return false;
        }
    }

    public void startTracking(Application application) {
        if (!this.f41) {
            AFLogger.afWarnLog("ERROR: AppsFlyer SDK is not initialized! The API call 'startTracking(Application)' must be called after the 'init(String, AppsFlyerConversionListener)' API method, which should be called on the Application's onCreate.");
        } else {
            startTracking(application, null);
        }
    }

    public void startTracking(Application application, String str) {
        startTracking(application, str, null);
    }

    public void startTracking(Application application, String str, AppsFlyerTrackingRequestListener appsFlyerTrackingRequestListener) {
        s.m186().m195("startTracking", str);
        AFLogger.afInfoLog(String.format("Starting AppsFlyer Tracking: (v%s.%s)", BuildConfig.VERSION_NAME, "419"));
        AFLogger.afInfoLog("Build Number: 419");
        AppsFlyerProperties.getInstance().loadProperties(application.getApplicationContext());
        if (!TextUtils.isEmpty(str)) {
            AppsFlyerProperties.getInstance().set(AppsFlyerProperties.AF_KEY, str);
            f.AnonymousClass4.m125(str);
        } else if (TextUtils.isEmpty(AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY))) {
            AFLogger.afWarnLog("ERROR: AppsFlyer SDK is not initialized! You must provide AppsFlyer Dev-Key either in the 'init' API method (should be called on Application's onCreate),or in the startTracking API method (should be called on Activity's onCreate).");
            return;
        }
        if (appsFlyerTrackingRequestListener != null) {
            f26 = appsFlyerTrackingRequestListener;
        }
        m45(application);
    }

    public void setAppId(String str) {
        s.m186().m195("setAppId", str);
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.APP_ID, str);
    }

    public void setExtension(String str) {
        s.m186().m195("setExtension", str);
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.EXTENSION, str);
    }

    public void setIsUpdate(boolean z) {
        s.m186().m195("setIsUpdate", String.valueOf(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.IS_UPDATE, z);
    }

    public void setCurrencyCode(String str) {
        s.m186().m195("setCurrencyCode", str);
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.CURRENCY_CODE, str);
    }

    public void trackLocation(Context context, double d2, double d3) {
        s.m186().m195("trackLocation", String.valueOf(d2), String.valueOf(d3));
        HashMap hashMap = new HashMap();
        hashMap.put(AFInAppEventParameterName.LONGTITUDE, Double.toString(d3));
        hashMap.put(AFInAppEventParameterName.LATITUDE, Double.toString(d2));
        m78(context, AFInAppEventType.LOCATION_COORDINATES, hashMap);
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final void m75(WeakReference<Context> weakReference) {
        if (weakReference.get() == null) {
            return;
        }
        AFLogger.afInfoLog("app went to background");
        SharedPreferences sharedPreferences = weakReference.get().getSharedPreferences("appsflyer-data", 0);
        AppsFlyerProperties.getInstance().saveProperties(sharedPreferences);
        long j = this.f32 - this.f30;
        HashMap hashMap = new HashMap();
        String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY);
        if (string == null) {
            AFLogger.afWarnLog("[callStats] AppsFlyer's SDK cannot send any event without providing DevKey.");
            return;
        }
        String string2 = AppsFlyerProperties.getInstance().getString("KSAppsFlyerId");
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, false)) {
            hashMap.put(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, "true");
        }
        o m158 = l.m158(weakReference.get().getContentResolver());
        if (m158 != null) {
            hashMap.put("amazon_aid", m158.m167());
            hashMap.put("amazon_aid_limit", String.valueOf(m158.m168()));
        }
        String string3 = AppsFlyerProperties.getInstance().getString(ServerParameters.ADVERTISING_ID_PARAM);
        if (string3 != null) {
            hashMap.put(ServerParameters.ADVERTISING_ID_PARAM, string3);
        }
        hashMap.put("app_id", weakReference.get().getPackageName());
        hashMap.put("devkey", string);
        hashMap.put("uid", t.m210(weakReference));
        hashMap.put("time_in_app", String.valueOf(j / 1000));
        hashMap.put("statType", "user_closed_app");
        hashMap.put("platform", "Android");
        hashMap.put("launch_counter", Integer.toString(m23(sharedPreferences, "appsFlyerCount", false)));
        hashMap.put(AppsFlyerProperties.CHANNEL, m60(weakReference));
        hashMap.put("originalAppsflyerId", string2 != null ? string2 : "");
        if (!this.f43) {
            AFLogger.afDebugLog("Stats call is disabled, ignore ...");
            return;
        }
        try {
            k kVar = new k(null, isTrackingStopped());
            kVar.f174 = hashMap;
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                AFLogger.afDebugLog("Main thread detected. Running callStats task in a new thread.");
                kVar.execute(ServerConfigHandler.getUrl("https://stats.%s/stats"));
            } else {
                AFLogger.afDebugLog(new StringBuilder("Running callStats task (on current thread: ").append(Thread.currentThread().toString()).append(" )").toString());
                kVar.onPreExecute();
                kVar.onPostExecute(kVar.doInBackground(ServerConfigHandler.getUrl("https://stats.%s/stats")));
            }
        } catch (Throwable th) {
            AFLogger.afErrorLog("Could not send callStats request", th);
        }
    }

    public void trackAppLaunch(Context context, String str) {
        if (m52(context)) {
            if (this.f48 == null) {
                this.f48 = new com.appsflyer.d();
                this.f48.m116(context, this);
            } else {
                AFLogger.afWarnLog("AFInstallReferrer instance already created");
            }
        }
        m35(context, str, (String) null, (String) null, "", (Intent) null, (String) null);
    }

    protected void setDeepLinkData(Intent intent) {
        if (intent != null) {
            try {
                if ("android.intent.action.VIEW".equals(intent.getAction())) {
                    this.latestDeepLink = intent.getData();
                    AFLogger.afDebugLog(new StringBuilder("Unity setDeepLinkData = ").append(this.latestDeepLink).toString());
                }
            } catch (Throwable th) {
                AFLogger.afErrorLog("Exception while setting deeplink data (unity). ", th);
            }
        }
    }

    public void reportTrackSession(Context context) {
        s.m186().m195("reportTrackSession", new String[0]);
        s.m186().m190();
        m78(context, (String) null, (Map<String, Object>) null);
    }

    public void trackEvent(Context context, String str, Map<String, Object> map) {
        s.m186().m195("trackEvent", str, new JSONObject(map == null ? new HashMap<>() : map).toString());
        m78(context, str, map);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    final void m78(Context context, String str, Map<String, Object> map) {
        Intent intent;
        Uri uri;
        String str2 = "";
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            intent = activity.getIntent();
            AFDeepLinkManager.getInstance().currentActivityHash = System.identityHashCode(activity);
            try {
                uri = ActivityCompat.getReferrer(activity);
            } catch (Throwable th) {
                AFLogger.afErrorLog(th.getLocalizedMessage(), th);
                uri = null;
            }
            if (uri != null) {
                str2 = uri.toString();
            }
        } else {
            intent = null;
        }
        if (AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY) == null) {
            AFLogger.afWarnLog("[TrackEvent/Launch] AppsFlyer's SDK cannot send any event without providing DevKey.");
            return;
        }
        if (map == null) {
            map = new HashMap<>();
        }
        JSONObject jSONObject = new JSONObject(map);
        String referrer = AppsFlyerProperties.getInstance().getReferrer(context);
        String jSONObject2 = jSONObject.toString();
        if (referrer == null) {
            referrer = "";
        }
        m35(context, (String) null, str, jSONObject2, referrer, intent, str2);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private static void m65(Context context, String str, String str2, String str3) {
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.IS_MONITOR, false)) {
            Intent intent = new Intent("com.appsflyer.MonitorBroadcast");
            intent.setPackage("com.appsflyer.nightvision");
            intent.putExtra("message", str2);
            intent.putExtra("value", str3);
            intent.putExtra("packageName", "true");
            intent.putExtra(Constants.URL_MEDIA_SOURCE, new Integer(Process.myPid()));
            intent.putExtra("eventIdentifier", str);
            intent.putExtra("sdk", BuildConfig.VERSION_NAME);
            context.sendBroadcast(intent);
        }
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m80(Context context, String str) {
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, false) && AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.APP_USER_ID) == null) {
            AFLogger.afInfoLog("CustomerUserId not set, Tracking is disabled", true);
            return;
        }
        HashMap hashMap = new HashMap();
        String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY);
        if (string == null) {
            AFLogger.afWarnLog("[registerUninstall] AppsFlyer's SDK cannot send any event without providing DevKey.");
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            hashMap.put("app_version_code", Integer.toString(packageInfo.versionCode));
            hashMap.put("app_version_name", packageInfo.versionName);
            hashMap.put("app_name", packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
            long j = packageInfo.firstInstallTime;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssZ", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            hashMap.put("installDate", simpleDateFormat.format(new Date(j)));
        } catch (Throwable th) {
            AFLogger.afErrorLog("Exception while collecting application version info.", th);
        }
        m49(context, hashMap);
        String string2 = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.APP_USER_ID);
        if (string2 != null) {
            hashMap.put("appUserId", string2);
        }
        try {
            hashMap.put("model", Build.MODEL);
            hashMap.put("brand", Build.BRAND);
        } catch (Throwable th2) {
            AFLogger.afErrorLog("Exception while collecting device brand and model.", th2);
        }
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, false)) {
            hashMap.put(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, "true");
        }
        o m158 = l.m158(context.getContentResolver());
        if (m158 != null) {
            hashMap.put("amazon_aid", m158.m167());
            hashMap.put("amazon_aid_limit", String.valueOf(m158.m168()));
        }
        String string3 = AppsFlyerProperties.getInstance().getString(ServerParameters.ADVERTISING_ID_PARAM);
        if (string3 != null) {
            hashMap.put(ServerParameters.ADVERTISING_ID_PARAM, string3);
        }
        hashMap.put("devkey", string);
        hashMap.put("uid", t.m210((WeakReference<Context>) new WeakReference(context)));
        hashMap.put("af_gcm_token", str);
        hashMap.put("launch_counter", Integer.toString(m23(context.getSharedPreferences("appsflyer-data", 0), "appsFlyerCount", false)));
        hashMap.put("sdk", Integer.toString(Build.VERSION.SDK_INT));
        String m60 = m60((WeakReference<Context>) new WeakReference(context));
        if (m60 != null) {
            hashMap.put(AppsFlyerProperties.CHANNEL, m60);
        }
        try {
            k kVar = new k(context, isTrackingStopped());
            kVar.f174 = hashMap;
            kVar.execute(new StringBuilder().append(ServerConfigHandler.getUrl(f21)).append(packageName).toString());
        } catch (Throwable th3) {
            AFLogger.afErrorLog(th3.getMessage(), th3);
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static void m46(Context context, String str) {
        Intent intent = new Intent("com.appsflyer.testIntgrationBroadcast");
        intent.putExtra("params", str);
        if (Build.VERSION.SDK_INT < 26) {
            context.sendBroadcast(intent);
        } else if (context.getPackageManager().queryBroadcastReceivers(intent, 0).toString().contains("com.appsflyer.referrerSender")) {
            Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName("com.appsflyer.referrerSender", "com.appsflyer.referrerSender.Receiver"));
            context.sendBroadcast(intent2);
        }
    }

    public void setDeviceTrackingDisabled(boolean z) {
        s.m186().m195("setDeviceTrackingDisabled", String.valueOf(z));
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ॱ, reason: contains not printable characters */
    public static Map<String, String> m68(Context context) throws m {
        String string = context.getSharedPreferences("appsflyer-data", 0).getString("attributionId", null);
        if (string != null && string.length() > 0) {
            return m70(string);
        }
        throw new m();
    }

    public void registerConversionListener(Context context, AppsFlyerConversionListener appsFlyerConversionListener) {
        s.m186().m195("registerConversionListener", new String[0]);
        if (appsFlyerConversionListener != null) {
            f22 = appsFlyerConversionListener;
        }
    }

    public void unregisterConversionListener() {
        s.m186().m195("unregisterConversionListener", new String[0]);
        f22 = null;
    }

    public void registerValidatorListener(Context context, AppsFlyerInAppPurchaseValidatorListener appsFlyerInAppPurchaseValidatorListener) {
        s.m186().m195("registerValidatorListener", new String[0]);
        AFLogger.afDebugLog("registerValidatorListener called");
        if (appsFlyerInAppPurchaseValidatorListener == null) {
            AFLogger.afDebugLog("registerValidatorListener null listener");
        } else {
            f19 = appsFlyerInAppPurchaseValidatorListener;
        }
    }

    protected void getConversionData(Context context, final ConversionDataListener conversionDataListener) {
        f22 = new AppsFlyerConversionListener() { // from class: com.appsflyer.AppsFlyerLib.4
            @Override // com.appsflyer.AppsFlyerConversionListener
            public final void onInstallConversionDataLoaded(Map<String, String> map) {
                conversionDataListener.onConversionDataLoaded(map);
            }

            @Override // com.appsflyer.AppsFlyerConversionListener
            public final void onInstallConversionFailure(String str) {
                conversionDataListener.onConversionFailure(str);
            }

            @Override // com.appsflyer.AppsFlyerConversionListener
            public final void onAppOpenAttribution(Map<String, String> map) {
            }

            @Override // com.appsflyer.AppsFlyerConversionListener
            public final void onAttributionFailure(String str) {
            }
        };
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private static Map<String, String> m26(Context context, String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        String[] split = str.split("&");
        int length = split.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            String str2 = split[i];
            int indexOf = str2.indexOf("=");
            String substring = indexOf > 0 ? str2.substring(0, indexOf) : str2;
            if (!linkedHashMap.containsKey(substring)) {
                if (substring.equals(Constants.URL_CAMPAIGN)) {
                    substring = "campaign";
                } else if (substring.equals(Constants.URL_MEDIA_SOURCE)) {
                    substring = "media_source";
                } else if (substring.equals("af_prt")) {
                    z = true;
                    substring = "agency";
                }
                linkedHashMap.put(substring, "");
            }
            boolean z2 = z;
            linkedHashMap.put(substring, (indexOf <= 0 || str2.length() <= indexOf + 1) ? null : str2.substring(indexOf + 1));
            i++;
            z = z2;
        }
        try {
            if (!linkedHashMap.containsKey("install_time")) {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                long j = packageInfo.firstInstallTime;
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                linkedHashMap.put("install_time", simpleDateFormat.format(new Date(j)));
            }
        } catch (Exception e2) {
            AFLogger.afErrorLog("Could not fetch install time. ", e2);
        }
        if (!linkedHashMap.containsKey("af_status")) {
            linkedHashMap.put("af_status", "Non-organic");
        }
        if (z) {
            linkedHashMap.remove("media_source");
        }
        return linkedHashMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ॱ, reason: contains not printable characters */
    public static Map<String, String> m70(String str) {
        HashMap hashMap = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                if (!f20.contains(next)) {
                    String string = jSONObject.getString(next);
                    if (!TextUtils.isEmpty(string) && !"null".equals(string)) {
                        hashMap.put(next, string);
                    }
                }
            }
            return hashMap;
        } catch (JSONException e2) {
            AFLogger.afErrorLog(e2.getMessage(), e2);
            return null;
        }
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private void m35(Context context, String str, String str2, String str3, String str4, Intent intent, String str5) {
        Context applicationContext = context.getApplicationContext();
        boolean z = str2 == null;
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.AF_WAITFOR_CUSTOMERID, false) && AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.APP_USER_ID) == null) {
            AFLogger.afInfoLog("CustomerUserId not set, Tracking is disabled", true);
            return;
        }
        if (z) {
            if (!AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.LAUNCH_PROTECT_ENABLED, true)) {
                AFLogger.afInfoLog("Allowing multiple launches within a 5 second time window.");
            } else if (m39()) {
                return;
            }
            this.f28 = System.currentTimeMillis();
        }
        m38(AFExecutor.getInstance().m5(), new c(this, new WeakReference(applicationContext), str, str2, str3, str4, false, intent, str5, (byte) 0), 150L, TimeUnit.MILLISECONDS);
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private boolean m39() {
        if (this.f28 > 0) {
            long currentTimeMillis = System.currentTimeMillis() - this.f28;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z", Locale.US);
            long j = this.f28;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String format = simpleDateFormat.format(new Date(j));
            long j2 = this.f45;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String format2 = simpleDateFormat.format(new Date(j2));
            if (currentTimeMillis < this.f46 && !isTrackingStopped()) {
                AFLogger.afInfoLog(String.format(Locale.US, "Last Launch attempt: %s;\nLast successful Launch event: %s;\nThis launch is blocked: %s ms < %s ms", format, format2, Long.valueOf(currentTimeMillis), Long.valueOf(this.f46)));
                return true;
            }
            if (!isTrackingStopped()) {
                AFLogger.afInfoLog(String.format(Locale.US, "Last Launch attempt: %s;\nLast successful Launch event: %s;\nSending launch (+%s ms)", format, format2, Long.valueOf(currentTimeMillis)));
            }
        } else if (!isTrackingStopped()) {
            AFLogger.afInfoLog("Sending first launch for this session!");
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:321:0x0ab9 A[Catch: Throwable -> 0x089a, TRY_ENTER, TryCatch #0 {Throwable -> 0x089a, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0036, B:14:0x0049, B:16:0x0053, B:18:0x006f, B:19:0x007b, B:21:0x0083, B:22:0x0088, B:24:0x0090, B:27:0x0097, B:28:0x009e, B:30:0x00d5, B:34:0x00e7, B:36:0x00f1, B:37:0x00fe, B:41:0x010d, B:42:0x0114, B:44:0x0118, B:46:0x0125, B:48:0x014a, B:49:0x0156, B:50:0x08b4, B:51:0x08b9, B:53:0x0174, B:55:0x0194, B:56:0x0199, B:58:0x01ac, B:59:0x01c0, B:61:0x01d0, B:63:0x01ea, B:64:0x08d9, B:65:0x01ed, B:68:0x0205, B:70:0x020f, B:71:0x0219, B:73:0x0225, B:75:0x022a, B:77:0x0238, B:79:0x023d, B:81:0x0249, B:83:0x024f, B:84:0x0254, B:86:0x0263, B:88:0x026a, B:90:0x0274, B:91:0x0279, B:93:0x0288, B:95:0x0291, B:96:0x029a, B:98:0x02ad, B:100:0x02b5, B:102:0x02be, B:103:0x0981, B:107:0x0993, B:109:0x099f, B:113:0x09a8, B:115:0x09bc, B:119:0x09c5, B:121:0x09cd, B:125:0x09d6, B:127:0x09df, B:131:0x09e8, B:134:0x0a09, B:142:0x09f6, B:145:0x02c9, B:146:0x02d2, B:148:0x02d8, B:150:0x02e3, B:152:0x02e9, B:153:0x02ee, B:155:0x02fa, B:156:0x02ff, B:158:0x030b, B:160:0x0312, B:162:0x0319, B:163:0x0320, B:165:0x032c, B:166:0x033b, B:168:0x0347, B:170:0x034e, B:171:0x0366, B:172:0x036b, B:174:0x0377, B:175:0x037c, B:370:0x03aa, B:177:0x03af, B:179:0x03bc, B:181:0x03c3, B:183:0x03ce, B:186:0x03d3, B:188:0x03e0, B:191:0x03ed, B:192:0x03fa, B:194:0x0411, B:210:0x04e9, B:212:0x04ef, B:213:0x04f6, B:215:0x0501, B:216:0x0506, B:218:0x0512, B:219:0x051f, B:221:0x053d, B:223:0x0546, B:225:0x0553, B:226:0x056a, B:227:0x0574, B:229:0x0578, B:230:0x0590, B:233:0x05a7, B:237:0x05b2, B:238:0x05d3, B:240:0x05df, B:242:0x05ee, B:243:0x0bd6, B:244:0x05f5, B:246:0x05ff, B:247:0x0615, B:249:0x061f, B:251:0x0625, B:253:0x062d, B:254:0x0632, B:257:0x066c, B:261:0x0680, B:263:0x0694, B:267:0x06a3, B:269:0x06dd, B:271:0x06e1, B:273:0x06ef, B:274:0x0716, B:276:0x071c, B:277:0x0721, B:279:0x073a, B:282:0x0746, B:284:0x0754, B:285:0x0759, B:287:0x085c, B:288:0x086e, B:290:0x087a, B:299:0x0bcc, B:302:0x0bbd, B:305:0x0bb5, B:307:0x0bad, B:310:0x0ba5, B:312:0x0b8c, B:313:0x0a6d, B:315:0x0a89, B:317:0x0a91, B:349:0x0a97, B:321:0x0ab9, B:322:0x0ac3, B:324:0x0ad8, B:326:0x0ae0, B:332:0x0ae6, B:336:0x0b53, B:329:0x0af5, B:330:0x0b84, B:342:0x0b65, B:343:0x0b73, B:344:0x0b7c, B:346:0x0b80, B:347:0x0b4a, B:352:0x0b03, B:361:0x0b15, B:362:0x0b23, B:357:0x0b2c, B:358:0x0b3a, B:364:0x0b42, B:366:0x0b46, B:375:0x0a5b, B:373:0x0a64, B:376:0x0a43, B:378:0x0a4f, B:379:0x0a15, B:381:0x0a21, B:383:0x0a27, B:384:0x0a2e, B:386:0x0960, B:390:0x0972, B:391:0x0976, B:397:0x0959, B:400:0x08de, B:402:0x08e9, B:404:0x08f2, B:405:0x092b, B:407:0x0946, B:408:0x0953, B:410:0x094c, B:412:0x08a7, B:415:0x0893, B:368:0x0396, B:196:0x0437, B:198:0x0451, B:199:0x045e, B:201:0x04b2, B:205:0x04c4, B:206:0x04d2, B:209:0x04d7), top: B:4:0x0025, inners: #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:324:0x0ad8 A[Catch: Throwable -> 0x089a, TryCatch #0 {Throwable -> 0x089a, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0036, B:14:0x0049, B:16:0x0053, B:18:0x006f, B:19:0x007b, B:21:0x0083, B:22:0x0088, B:24:0x0090, B:27:0x0097, B:28:0x009e, B:30:0x00d5, B:34:0x00e7, B:36:0x00f1, B:37:0x00fe, B:41:0x010d, B:42:0x0114, B:44:0x0118, B:46:0x0125, B:48:0x014a, B:49:0x0156, B:50:0x08b4, B:51:0x08b9, B:53:0x0174, B:55:0x0194, B:56:0x0199, B:58:0x01ac, B:59:0x01c0, B:61:0x01d0, B:63:0x01ea, B:64:0x08d9, B:65:0x01ed, B:68:0x0205, B:70:0x020f, B:71:0x0219, B:73:0x0225, B:75:0x022a, B:77:0x0238, B:79:0x023d, B:81:0x0249, B:83:0x024f, B:84:0x0254, B:86:0x0263, B:88:0x026a, B:90:0x0274, B:91:0x0279, B:93:0x0288, B:95:0x0291, B:96:0x029a, B:98:0x02ad, B:100:0x02b5, B:102:0x02be, B:103:0x0981, B:107:0x0993, B:109:0x099f, B:113:0x09a8, B:115:0x09bc, B:119:0x09c5, B:121:0x09cd, B:125:0x09d6, B:127:0x09df, B:131:0x09e8, B:134:0x0a09, B:142:0x09f6, B:145:0x02c9, B:146:0x02d2, B:148:0x02d8, B:150:0x02e3, B:152:0x02e9, B:153:0x02ee, B:155:0x02fa, B:156:0x02ff, B:158:0x030b, B:160:0x0312, B:162:0x0319, B:163:0x0320, B:165:0x032c, B:166:0x033b, B:168:0x0347, B:170:0x034e, B:171:0x0366, B:172:0x036b, B:174:0x0377, B:175:0x037c, B:370:0x03aa, B:177:0x03af, B:179:0x03bc, B:181:0x03c3, B:183:0x03ce, B:186:0x03d3, B:188:0x03e0, B:191:0x03ed, B:192:0x03fa, B:194:0x0411, B:210:0x04e9, B:212:0x04ef, B:213:0x04f6, B:215:0x0501, B:216:0x0506, B:218:0x0512, B:219:0x051f, B:221:0x053d, B:223:0x0546, B:225:0x0553, B:226:0x056a, B:227:0x0574, B:229:0x0578, B:230:0x0590, B:233:0x05a7, B:237:0x05b2, B:238:0x05d3, B:240:0x05df, B:242:0x05ee, B:243:0x0bd6, B:244:0x05f5, B:246:0x05ff, B:247:0x0615, B:249:0x061f, B:251:0x0625, B:253:0x062d, B:254:0x0632, B:257:0x066c, B:261:0x0680, B:263:0x0694, B:267:0x06a3, B:269:0x06dd, B:271:0x06e1, B:273:0x06ef, B:274:0x0716, B:276:0x071c, B:277:0x0721, B:279:0x073a, B:282:0x0746, B:284:0x0754, B:285:0x0759, B:287:0x085c, B:288:0x086e, B:290:0x087a, B:299:0x0bcc, B:302:0x0bbd, B:305:0x0bb5, B:307:0x0bad, B:310:0x0ba5, B:312:0x0b8c, B:313:0x0a6d, B:315:0x0a89, B:317:0x0a91, B:349:0x0a97, B:321:0x0ab9, B:322:0x0ac3, B:324:0x0ad8, B:326:0x0ae0, B:332:0x0ae6, B:336:0x0b53, B:329:0x0af5, B:330:0x0b84, B:342:0x0b65, B:343:0x0b73, B:344:0x0b7c, B:346:0x0b80, B:347:0x0b4a, B:352:0x0b03, B:361:0x0b15, B:362:0x0b23, B:357:0x0b2c, B:358:0x0b3a, B:364:0x0b42, B:366:0x0b46, B:375:0x0a5b, B:373:0x0a64, B:376:0x0a43, B:378:0x0a4f, B:379:0x0a15, B:381:0x0a21, B:383:0x0a27, B:384:0x0a2e, B:386:0x0960, B:390:0x0972, B:391:0x0976, B:397:0x0959, B:400:0x08de, B:402:0x08e9, B:404:0x08f2, B:405:0x092b, B:407:0x0946, B:408:0x0953, B:410:0x094c, B:412:0x08a7, B:415:0x0893, B:368:0x0396, B:196:0x0437, B:198:0x0451, B:199:0x045e, B:201:0x04b2, B:205:0x04c4, B:206:0x04d2, B:209:0x04d7), top: B:4:0x0025, inners: #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0af5 A[Catch: Throwable -> 0x089a, TRY_ENTER, TRY_LEAVE, TryCatch #0 {Throwable -> 0x089a, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0036, B:14:0x0049, B:16:0x0053, B:18:0x006f, B:19:0x007b, B:21:0x0083, B:22:0x0088, B:24:0x0090, B:27:0x0097, B:28:0x009e, B:30:0x00d5, B:34:0x00e7, B:36:0x00f1, B:37:0x00fe, B:41:0x010d, B:42:0x0114, B:44:0x0118, B:46:0x0125, B:48:0x014a, B:49:0x0156, B:50:0x08b4, B:51:0x08b9, B:53:0x0174, B:55:0x0194, B:56:0x0199, B:58:0x01ac, B:59:0x01c0, B:61:0x01d0, B:63:0x01ea, B:64:0x08d9, B:65:0x01ed, B:68:0x0205, B:70:0x020f, B:71:0x0219, B:73:0x0225, B:75:0x022a, B:77:0x0238, B:79:0x023d, B:81:0x0249, B:83:0x024f, B:84:0x0254, B:86:0x0263, B:88:0x026a, B:90:0x0274, B:91:0x0279, B:93:0x0288, B:95:0x0291, B:96:0x029a, B:98:0x02ad, B:100:0x02b5, B:102:0x02be, B:103:0x0981, B:107:0x0993, B:109:0x099f, B:113:0x09a8, B:115:0x09bc, B:119:0x09c5, B:121:0x09cd, B:125:0x09d6, B:127:0x09df, B:131:0x09e8, B:134:0x0a09, B:142:0x09f6, B:145:0x02c9, B:146:0x02d2, B:148:0x02d8, B:150:0x02e3, B:152:0x02e9, B:153:0x02ee, B:155:0x02fa, B:156:0x02ff, B:158:0x030b, B:160:0x0312, B:162:0x0319, B:163:0x0320, B:165:0x032c, B:166:0x033b, B:168:0x0347, B:170:0x034e, B:171:0x0366, B:172:0x036b, B:174:0x0377, B:175:0x037c, B:370:0x03aa, B:177:0x03af, B:179:0x03bc, B:181:0x03c3, B:183:0x03ce, B:186:0x03d3, B:188:0x03e0, B:191:0x03ed, B:192:0x03fa, B:194:0x0411, B:210:0x04e9, B:212:0x04ef, B:213:0x04f6, B:215:0x0501, B:216:0x0506, B:218:0x0512, B:219:0x051f, B:221:0x053d, B:223:0x0546, B:225:0x0553, B:226:0x056a, B:227:0x0574, B:229:0x0578, B:230:0x0590, B:233:0x05a7, B:237:0x05b2, B:238:0x05d3, B:240:0x05df, B:242:0x05ee, B:243:0x0bd6, B:244:0x05f5, B:246:0x05ff, B:247:0x0615, B:249:0x061f, B:251:0x0625, B:253:0x062d, B:254:0x0632, B:257:0x066c, B:261:0x0680, B:263:0x0694, B:267:0x06a3, B:269:0x06dd, B:271:0x06e1, B:273:0x06ef, B:274:0x0716, B:276:0x071c, B:277:0x0721, B:279:0x073a, B:282:0x0746, B:284:0x0754, B:285:0x0759, B:287:0x085c, B:288:0x086e, B:290:0x087a, B:299:0x0bcc, B:302:0x0bbd, B:305:0x0bb5, B:307:0x0bad, B:310:0x0ba5, B:312:0x0b8c, B:313:0x0a6d, B:315:0x0a89, B:317:0x0a91, B:349:0x0a97, B:321:0x0ab9, B:322:0x0ac3, B:324:0x0ad8, B:326:0x0ae0, B:332:0x0ae6, B:336:0x0b53, B:329:0x0af5, B:330:0x0b84, B:342:0x0b65, B:343:0x0b73, B:344:0x0b7c, B:346:0x0b80, B:347:0x0b4a, B:352:0x0b03, B:361:0x0b15, B:362:0x0b23, B:357:0x0b2c, B:358:0x0b3a, B:364:0x0b42, B:366:0x0b46, B:375:0x0a5b, B:373:0x0a64, B:376:0x0a43, B:378:0x0a4f, B:379:0x0a15, B:381:0x0a21, B:383:0x0a27, B:384:0x0a2e, B:386:0x0960, B:390:0x0972, B:391:0x0976, B:397:0x0959, B:400:0x08de, B:402:0x08e9, B:404:0x08f2, B:405:0x092b, B:407:0x0946, B:408:0x0953, B:410:0x094c, B:412:0x08a7, B:415:0x0893, B:368:0x0396, B:196:0x0437, B:198:0x0451, B:199:0x045e, B:201:0x04b2, B:205:0x04c4, B:206:0x04d2, B:209:0x04d7), top: B:4:0x0025, inners: #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:330:0x0b84 A[Catch: Throwable -> 0x089a, TryCatch #0 {Throwable -> 0x089a, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0036, B:14:0x0049, B:16:0x0053, B:18:0x006f, B:19:0x007b, B:21:0x0083, B:22:0x0088, B:24:0x0090, B:27:0x0097, B:28:0x009e, B:30:0x00d5, B:34:0x00e7, B:36:0x00f1, B:37:0x00fe, B:41:0x010d, B:42:0x0114, B:44:0x0118, B:46:0x0125, B:48:0x014a, B:49:0x0156, B:50:0x08b4, B:51:0x08b9, B:53:0x0174, B:55:0x0194, B:56:0x0199, B:58:0x01ac, B:59:0x01c0, B:61:0x01d0, B:63:0x01ea, B:64:0x08d9, B:65:0x01ed, B:68:0x0205, B:70:0x020f, B:71:0x0219, B:73:0x0225, B:75:0x022a, B:77:0x0238, B:79:0x023d, B:81:0x0249, B:83:0x024f, B:84:0x0254, B:86:0x0263, B:88:0x026a, B:90:0x0274, B:91:0x0279, B:93:0x0288, B:95:0x0291, B:96:0x029a, B:98:0x02ad, B:100:0x02b5, B:102:0x02be, B:103:0x0981, B:107:0x0993, B:109:0x099f, B:113:0x09a8, B:115:0x09bc, B:119:0x09c5, B:121:0x09cd, B:125:0x09d6, B:127:0x09df, B:131:0x09e8, B:134:0x0a09, B:142:0x09f6, B:145:0x02c9, B:146:0x02d2, B:148:0x02d8, B:150:0x02e3, B:152:0x02e9, B:153:0x02ee, B:155:0x02fa, B:156:0x02ff, B:158:0x030b, B:160:0x0312, B:162:0x0319, B:163:0x0320, B:165:0x032c, B:166:0x033b, B:168:0x0347, B:170:0x034e, B:171:0x0366, B:172:0x036b, B:174:0x0377, B:175:0x037c, B:370:0x03aa, B:177:0x03af, B:179:0x03bc, B:181:0x03c3, B:183:0x03ce, B:186:0x03d3, B:188:0x03e0, B:191:0x03ed, B:192:0x03fa, B:194:0x0411, B:210:0x04e9, B:212:0x04ef, B:213:0x04f6, B:215:0x0501, B:216:0x0506, B:218:0x0512, B:219:0x051f, B:221:0x053d, B:223:0x0546, B:225:0x0553, B:226:0x056a, B:227:0x0574, B:229:0x0578, B:230:0x0590, B:233:0x05a7, B:237:0x05b2, B:238:0x05d3, B:240:0x05df, B:242:0x05ee, B:243:0x0bd6, B:244:0x05f5, B:246:0x05ff, B:247:0x0615, B:249:0x061f, B:251:0x0625, B:253:0x062d, B:254:0x0632, B:257:0x066c, B:261:0x0680, B:263:0x0694, B:267:0x06a3, B:269:0x06dd, B:271:0x06e1, B:273:0x06ef, B:274:0x0716, B:276:0x071c, B:277:0x0721, B:279:0x073a, B:282:0x0746, B:284:0x0754, B:285:0x0759, B:287:0x085c, B:288:0x086e, B:290:0x087a, B:299:0x0bcc, B:302:0x0bbd, B:305:0x0bb5, B:307:0x0bad, B:310:0x0ba5, B:312:0x0b8c, B:313:0x0a6d, B:315:0x0a89, B:317:0x0a91, B:349:0x0a97, B:321:0x0ab9, B:322:0x0ac3, B:324:0x0ad8, B:326:0x0ae0, B:332:0x0ae6, B:336:0x0b53, B:329:0x0af5, B:330:0x0b84, B:342:0x0b65, B:343:0x0b73, B:344:0x0b7c, B:346:0x0b80, B:347:0x0b4a, B:352:0x0b03, B:361:0x0b15, B:362:0x0b23, B:357:0x0b2c, B:358:0x0b3a, B:364:0x0b42, B:366:0x0b46, B:375:0x0a5b, B:373:0x0a64, B:376:0x0a43, B:378:0x0a4f, B:379:0x0a15, B:381:0x0a21, B:383:0x0a27, B:384:0x0a2e, B:386:0x0960, B:390:0x0972, B:391:0x0976, B:397:0x0959, B:400:0x08de, B:402:0x08e9, B:404:0x08f2, B:405:0x092b, B:407:0x0946, B:408:0x0953, B:410:0x094c, B:412:0x08a7, B:415:0x0893, B:368:0x0396, B:196:0x0437, B:198:0x0451, B:199:0x045e, B:201:0x04b2, B:205:0x04c4, B:206:0x04d2, B:209:0x04d7), top: B:4:0x0025, inners: #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0b80 A[Catch: Throwable -> 0x089a, TryCatch #0 {Throwable -> 0x089a, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0036, B:14:0x0049, B:16:0x0053, B:18:0x006f, B:19:0x007b, B:21:0x0083, B:22:0x0088, B:24:0x0090, B:27:0x0097, B:28:0x009e, B:30:0x00d5, B:34:0x00e7, B:36:0x00f1, B:37:0x00fe, B:41:0x010d, B:42:0x0114, B:44:0x0118, B:46:0x0125, B:48:0x014a, B:49:0x0156, B:50:0x08b4, B:51:0x08b9, B:53:0x0174, B:55:0x0194, B:56:0x0199, B:58:0x01ac, B:59:0x01c0, B:61:0x01d0, B:63:0x01ea, B:64:0x08d9, B:65:0x01ed, B:68:0x0205, B:70:0x020f, B:71:0x0219, B:73:0x0225, B:75:0x022a, B:77:0x0238, B:79:0x023d, B:81:0x0249, B:83:0x024f, B:84:0x0254, B:86:0x0263, B:88:0x026a, B:90:0x0274, B:91:0x0279, B:93:0x0288, B:95:0x0291, B:96:0x029a, B:98:0x02ad, B:100:0x02b5, B:102:0x02be, B:103:0x0981, B:107:0x0993, B:109:0x099f, B:113:0x09a8, B:115:0x09bc, B:119:0x09c5, B:121:0x09cd, B:125:0x09d6, B:127:0x09df, B:131:0x09e8, B:134:0x0a09, B:142:0x09f6, B:145:0x02c9, B:146:0x02d2, B:148:0x02d8, B:150:0x02e3, B:152:0x02e9, B:153:0x02ee, B:155:0x02fa, B:156:0x02ff, B:158:0x030b, B:160:0x0312, B:162:0x0319, B:163:0x0320, B:165:0x032c, B:166:0x033b, B:168:0x0347, B:170:0x034e, B:171:0x0366, B:172:0x036b, B:174:0x0377, B:175:0x037c, B:370:0x03aa, B:177:0x03af, B:179:0x03bc, B:181:0x03c3, B:183:0x03ce, B:186:0x03d3, B:188:0x03e0, B:191:0x03ed, B:192:0x03fa, B:194:0x0411, B:210:0x04e9, B:212:0x04ef, B:213:0x04f6, B:215:0x0501, B:216:0x0506, B:218:0x0512, B:219:0x051f, B:221:0x053d, B:223:0x0546, B:225:0x0553, B:226:0x056a, B:227:0x0574, B:229:0x0578, B:230:0x0590, B:233:0x05a7, B:237:0x05b2, B:238:0x05d3, B:240:0x05df, B:242:0x05ee, B:243:0x0bd6, B:244:0x05f5, B:246:0x05ff, B:247:0x0615, B:249:0x061f, B:251:0x0625, B:253:0x062d, B:254:0x0632, B:257:0x066c, B:261:0x0680, B:263:0x0694, B:267:0x06a3, B:269:0x06dd, B:271:0x06e1, B:273:0x06ef, B:274:0x0716, B:276:0x071c, B:277:0x0721, B:279:0x073a, B:282:0x0746, B:284:0x0754, B:285:0x0759, B:287:0x085c, B:288:0x086e, B:290:0x087a, B:299:0x0bcc, B:302:0x0bbd, B:305:0x0bb5, B:307:0x0bad, B:310:0x0ba5, B:312:0x0b8c, B:313:0x0a6d, B:315:0x0a89, B:317:0x0a91, B:349:0x0a97, B:321:0x0ab9, B:322:0x0ac3, B:324:0x0ad8, B:326:0x0ae0, B:332:0x0ae6, B:336:0x0b53, B:329:0x0af5, B:330:0x0b84, B:342:0x0b65, B:343:0x0b73, B:344:0x0b7c, B:346:0x0b80, B:347:0x0b4a, B:352:0x0b03, B:361:0x0b15, B:362:0x0b23, B:357:0x0b2c, B:358:0x0b3a, B:364:0x0b42, B:366:0x0b46, B:375:0x0a5b, B:373:0x0a64, B:376:0x0a43, B:378:0x0a4f, B:379:0x0a15, B:381:0x0a21, B:383:0x0a27, B:384:0x0a2e, B:386:0x0960, B:390:0x0972, B:391:0x0976, B:397:0x0959, B:400:0x08de, B:402:0x08e9, B:404:0x08f2, B:405:0x092b, B:407:0x0946, B:408:0x0953, B:410:0x094c, B:412:0x08a7, B:415:0x0893, B:368:0x0396, B:196:0x0437, B:198:0x0451, B:199:0x045e, B:201:0x04b2, B:205:0x04c4, B:206:0x04d2, B:209:0x04d7), top: B:4:0x0025, inners: #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:347:0x0b4a A[Catch: Throwable -> 0x089a, TRY_LEAVE, TryCatch #0 {Throwable -> 0x089a, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0036, B:14:0x0049, B:16:0x0053, B:18:0x006f, B:19:0x007b, B:21:0x0083, B:22:0x0088, B:24:0x0090, B:27:0x0097, B:28:0x009e, B:30:0x00d5, B:34:0x00e7, B:36:0x00f1, B:37:0x00fe, B:41:0x010d, B:42:0x0114, B:44:0x0118, B:46:0x0125, B:48:0x014a, B:49:0x0156, B:50:0x08b4, B:51:0x08b9, B:53:0x0174, B:55:0x0194, B:56:0x0199, B:58:0x01ac, B:59:0x01c0, B:61:0x01d0, B:63:0x01ea, B:64:0x08d9, B:65:0x01ed, B:68:0x0205, B:70:0x020f, B:71:0x0219, B:73:0x0225, B:75:0x022a, B:77:0x0238, B:79:0x023d, B:81:0x0249, B:83:0x024f, B:84:0x0254, B:86:0x0263, B:88:0x026a, B:90:0x0274, B:91:0x0279, B:93:0x0288, B:95:0x0291, B:96:0x029a, B:98:0x02ad, B:100:0x02b5, B:102:0x02be, B:103:0x0981, B:107:0x0993, B:109:0x099f, B:113:0x09a8, B:115:0x09bc, B:119:0x09c5, B:121:0x09cd, B:125:0x09d6, B:127:0x09df, B:131:0x09e8, B:134:0x0a09, B:142:0x09f6, B:145:0x02c9, B:146:0x02d2, B:148:0x02d8, B:150:0x02e3, B:152:0x02e9, B:153:0x02ee, B:155:0x02fa, B:156:0x02ff, B:158:0x030b, B:160:0x0312, B:162:0x0319, B:163:0x0320, B:165:0x032c, B:166:0x033b, B:168:0x0347, B:170:0x034e, B:171:0x0366, B:172:0x036b, B:174:0x0377, B:175:0x037c, B:370:0x03aa, B:177:0x03af, B:179:0x03bc, B:181:0x03c3, B:183:0x03ce, B:186:0x03d3, B:188:0x03e0, B:191:0x03ed, B:192:0x03fa, B:194:0x0411, B:210:0x04e9, B:212:0x04ef, B:213:0x04f6, B:215:0x0501, B:216:0x0506, B:218:0x0512, B:219:0x051f, B:221:0x053d, B:223:0x0546, B:225:0x0553, B:226:0x056a, B:227:0x0574, B:229:0x0578, B:230:0x0590, B:233:0x05a7, B:237:0x05b2, B:238:0x05d3, B:240:0x05df, B:242:0x05ee, B:243:0x0bd6, B:244:0x05f5, B:246:0x05ff, B:247:0x0615, B:249:0x061f, B:251:0x0625, B:253:0x062d, B:254:0x0632, B:257:0x066c, B:261:0x0680, B:263:0x0694, B:267:0x06a3, B:269:0x06dd, B:271:0x06e1, B:273:0x06ef, B:274:0x0716, B:276:0x071c, B:277:0x0721, B:279:0x073a, B:282:0x0746, B:284:0x0754, B:285:0x0759, B:287:0x085c, B:288:0x086e, B:290:0x087a, B:299:0x0bcc, B:302:0x0bbd, B:305:0x0bb5, B:307:0x0bad, B:310:0x0ba5, B:312:0x0b8c, B:313:0x0a6d, B:315:0x0a89, B:317:0x0a91, B:349:0x0a97, B:321:0x0ab9, B:322:0x0ac3, B:324:0x0ad8, B:326:0x0ae0, B:332:0x0ae6, B:336:0x0b53, B:329:0x0af5, B:330:0x0b84, B:342:0x0b65, B:343:0x0b73, B:344:0x0b7c, B:346:0x0b80, B:347:0x0b4a, B:352:0x0b03, B:361:0x0b15, B:362:0x0b23, B:357:0x0b2c, B:358:0x0b3a, B:364:0x0b42, B:366:0x0b46, B:375:0x0a5b, B:373:0x0a64, B:376:0x0a43, B:378:0x0a4f, B:379:0x0a15, B:381:0x0a21, B:383:0x0a27, B:384:0x0a2e, B:386:0x0960, B:390:0x0972, B:391:0x0976, B:397:0x0959, B:400:0x08de, B:402:0x08e9, B:404:0x08f2, B:405:0x092b, B:407:0x0946, B:408:0x0953, B:410:0x094c, B:412:0x08a7, B:415:0x0893, B:368:0x0396, B:196:0x0437, B:198:0x0451, B:199:0x045e, B:201:0x04b2, B:205:0x04c4, B:206:0x04d2, B:209:0x04d7), top: B:4:0x0025, inners: #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13, #14 }] */
    /* renamed from: ˏ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final java.util.Map<java.lang.String, java.lang.Object> m77(android.content.Context r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, boolean r18, android.content.SharedPreferences r19, boolean r20, @android.support.annotation.Nullable android.content.Intent r21, @android.support.annotation.Nullable java.lang.String r22) {
        /*
            Method dump skipped, instructions count: 3057
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.AppsFlyerLib.m77(android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, android.content.SharedPreferences, boolean, android.content.Intent, java.lang.String):java.util.Map");
    }

    public void setConsumeAFDeepLinks(boolean z) {
        AppsFlyerProperties.getInstance().set("consumeAfDeepLink", z);
        s.m186().m195("setConsumeAFDeepLinks: ".concat(String.valueOf(z)), new String[0]);
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static void m49(Context context, Map<String, ? super String> map) {
        g gVar = g.d.f144;
        g.b m127 = g.m127(context);
        map.put("network", m127.m131());
        if (m127.m130() != null) {
            map.put("operator", m127.m130());
        }
        if (m127.m129() != null) {
            map.put("carrier", m127.m129());
        }
    }

    protected void handleDeepLinkCallback(Context context, Map<String, Object> map, Uri uri) {
        final Map<String, String> hashMap;
        map.put("af_deeplink", uri.toString());
        if (uri.getQueryParameter("af_deeplink") != null) {
            this.f38 = "AppsFlyer_Test".equals(uri.getQueryParameter("media_source")) && Boolean.parseBoolean(uri.getQueryParameter("is_retargeting"));
            hashMap = m26(context, uri.getQuery());
            String path = uri.getPath();
            if (path != null) {
                hashMap.put("path", path);
            }
            String scheme = uri.getScheme();
            if (scheme != null) {
                hashMap.put("scheme", scheme);
            }
            String host = uri.getHost();
            if (host != null) {
                hashMap.put("host", host);
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("link", uri.toString());
        }
        final WeakReference weakReference = new WeakReference(context);
        q qVar = new q(uri, this);
        qVar.setConnProvider(new OneLinkHttpTask.HttpsUrlConnectionProvider());
        if (qVar.m175()) {
            qVar.m174(new q.e() { // from class: com.appsflyer.AppsFlyerLib.3
                @Override // com.appsflyer.q.e
                /* renamed from: ˋ, reason: contains not printable characters */
                public final void mo84(String str) {
                    if (AppsFlyerLib.f22 != null) {
                        m83(hashMap);
                        AppsFlyerLib.f22.onAttributionFailure(str);
                    }
                }

                /* renamed from: ˎ, reason: contains not printable characters */
                private void m83(Map<String, String> map2) {
                    if (weakReference.get() != null) {
                        AppsFlyerLib.m64((Context) weakReference.get(), "deeplinkAttribution", new JSONObject(map2).toString());
                    }
                }

                @Override // com.appsflyer.q.e
                /* renamed from: ॱ, reason: contains not printable characters */
                public final void mo85(Map<String, String> map2) {
                    for (String str : map2.keySet()) {
                        hashMap.put(str, map2.get(str));
                    }
                    m83(hashMap);
                    AppsFlyerLib.m50(hashMap);
                }
            });
            AFExecutor.getInstance().getThreadPoolExecutor().execute(qVar);
        } else if (f22 != null) {
            try {
                f22.onAppOpenAttribution(hashMap);
            } catch (Throwable th) {
                AFLogger.afErrorLog(th.getLocalizedMessage(), th);
            }
        }
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private static boolean m40(Context context) {
        try {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == 0) {
                return true;
            }
        } catch (Throwable th) {
            AFLogger.afErrorLog("WARNING:  Google play services is unavailable. ", th);
        }
        try {
            context.getPackageManager().getPackageInfo("com.google.android.gms", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e2) {
            AFLogger.afErrorLog("WARNING:  Google Play Services is unavailable. ", e2);
            return false;
        }
    }

    /* renamed from: ʼ, reason: contains not printable characters */
    private static boolean m22(Context context) {
        return (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_ANDROID_ID_FORCE_BY_USER, false) || AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_IMEI_FORCE_BY_USER, false)) || !m40(context);
    }

    /* renamed from: ʻ, reason: contains not printable characters */
    private static String m21(Context context) {
        String string = AppsFlyerProperties.getInstance().getString("api_store_value");
        if (string == null) {
            String m67 = m67((WeakReference<Context>) new WeakReference(context), "AF_STORE");
            if (m67 == null) {
                return null;
            }
            return m67;
        }
        return string;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private static String m24(String str) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke(null, str);
        } catch (Throwable th) {
            AFLogger.afErrorLog(th.getMessage(), th);
            return null;
        }
    }

    @Nullable
    /* renamed from: ॱ, reason: contains not printable characters */
    private static String m67(WeakReference<Context> weakReference, String str) {
        if (weakReference.get() == null) {
            return null;
        }
        return m30(str, weakReference.get().getPackageManager(), weakReference.get().getPackageName());
    }

    @Nullable
    /* renamed from: ˋ, reason: contains not printable characters */
    private static String m30(String str, PackageManager packageManager, String str2) {
        Object obj;
        try {
            Bundle bundle = ((PackageItemInfo) packageManager.getApplicationInfo(str2, 128)).metaData;
            if (bundle == null || (obj = bundle.get(str)) == null) {
                return null;
            }
            return obj.toString();
        } catch (Throwable th) {
            AFLogger.afErrorLog(new StringBuilder("Could not find ").append(str).append(" value in the manifest").toString(), th);
            return null;
        }
    }

    public void setPreinstallAttribution(String str, String str2, String str3) {
        AFLogger.afDebugLog("setPreinstallAttribution API called");
        JSONObject jSONObject = new JSONObject();
        if (str != null) {
            try {
                jSONObject.put(Constants.URL_MEDIA_SOURCE, str);
            } catch (JSONException e2) {
                AFLogger.afErrorLog(e2.getMessage(), e2);
            }
        }
        if (str2 != null) {
            jSONObject.put(Constants.URL_CAMPAIGN, str2);
        }
        if (str3 != null) {
            jSONObject.put(Constants.URL_SITE_ID, str3);
        }
        if (!jSONObject.has(Constants.URL_MEDIA_SOURCE)) {
            AFLogger.afWarnLog("Cannot set preinstall attribution data without a media source");
        } else {
            AppsFlyerProperties.getInstance().set("preInstallName", jSONObject.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0069 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* renamed from: ˏ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String m58(java.io.File r5, java.lang.String r6) {
        /*
            r0 = 0
            java.util.Properties r2 = new java.util.Properties     // Catch: java.io.FileNotFoundException -> L24 java.lang.Throwable -> L4b java.lang.Throwable -> L63
            r2.<init>()     // Catch: java.io.FileNotFoundException -> L24 java.lang.Throwable -> L4b java.lang.Throwable -> L63
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.io.FileNotFoundException -> L24 java.lang.Throwable -> L4b java.lang.Throwable -> L63
            r1.<init>(r5)     // Catch: java.io.FileNotFoundException -> L24 java.lang.Throwable -> L4b java.lang.Throwable -> L63
            r2.load(r1)     // Catch: java.lang.Throwable -> L76 java.lang.Throwable -> L7b java.io.FileNotFoundException -> L80
            java.lang.String r3 = "Found PreInstall property!"
            com.appsflyer.AFLogger.afInfoLog(r3)     // Catch: java.lang.Throwable -> L76 java.lang.Throwable -> L7b java.io.FileNotFoundException -> L80
            java.lang.String r0 = r2.getProperty(r6)     // Catch: java.lang.Throwable -> L76 java.lang.Throwable -> L7b java.io.FileNotFoundException -> L80
            r1.close()     // Catch: java.lang.Throwable -> L1b
        L1a:
            return r0
        L1b:
            r1 = move-exception
            java.lang.String r2 = r1.getMessage()
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L1a
        L24:
            r1 = move-exception
            r1 = r0
        L26:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L76
            java.lang.String r3 = "PreInstall file wasn't found: "
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L76
            java.lang.String r3 = r5.getAbsolutePath()     // Catch: java.lang.Throwable -> L76
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L76
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L76
            com.appsflyer.AFLogger.afDebugLog(r2)     // Catch: java.lang.Throwable -> L76
            if (r1 == 0) goto L1a
            r1.close()     // Catch: java.lang.Throwable -> L42
            goto L1a
        L42:
            r1 = move-exception
            java.lang.String r2 = r1.getMessage()
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L1a
        L4b:
            r1 = move-exception
            r2 = r0
        L4d:
            java.lang.String r3 = r1.getMessage()     // Catch: java.lang.Throwable -> L78
            com.appsflyer.AFLogger.afErrorLog(r3, r1)     // Catch: java.lang.Throwable -> L78
            if (r2 == 0) goto L1a
            r2.close()     // Catch: java.lang.Throwable -> L5a
            goto L1a
        L5a:
            r1 = move-exception
            java.lang.String r2 = r1.getMessage()
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L1a
        L63:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L67:
            if (r1 == 0) goto L6c
            r1.close()     // Catch: java.lang.Throwable -> L6d
        L6c:
            throw r0
        L6d:
            r1 = move-exception
            java.lang.String r2 = r1.getMessage()
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L6c
        L76:
            r0 = move-exception
            goto L67
        L78:
            r0 = move-exception
            r1 = r2
            goto L67
        L7b:
            r2 = move-exception
            r4 = r2
            r2 = r1
            r1 = r4
            goto L4d
        L80:
            r2 = move-exception
            goto L26
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.AppsFlyerLib.m58(java.io.File, java.lang.String):java.lang.String");
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static File m41(String str) {
        if (str != null) {
            try {
                if (str.trim().length() > 0) {
                    return new File(str.trim());
                }
            } catch (Throwable th) {
                AFLogger.afErrorLog(th.getMessage(), th);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    /* renamed from: ˏ, reason: contains not printable characters */
    public static String m60(WeakReference<Context> weakReference) {
        String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.CHANNEL);
        if (string == null) {
            string = m67(weakReference, "CHANNEL");
        }
        if (string != null && string.equals("")) {
            return null;
        }
        return string;
    }

    public boolean isPreInstalledApp(Context context) {
        try {
            return (context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags & 1) != 0;
        } catch (PackageManager.NameNotFoundException e2) {
            AFLogger.afErrorLog("Could not check if app is pre installed", e2);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ᐝ, reason: contains not printable characters */
    public static String m73(Context context, String str) throws PackageManager.NameNotFoundException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        if (sharedPreferences.contains("CACHED_CHANNEL")) {
            return sharedPreferences.getString("CACHED_CHANNEL", null);
        }
        m64(context, "CACHED_CHANNEL", str);
        return str;
    }

    public String getAttributionId(ContentResolver contentResolver) {
        String str = null;
        Cursor query = contentResolver.query(Uri.parse(ATTRIBUTION_ID_CONTENT_URI), new String[]{ATTRIBUTION_ID_COLUMN_NAME}, null, null, null);
        if (query != null) {
            try {
                try {
                } catch (Exception e2) {
                    AFLogger.afErrorLog("Could not collect cursor attribution. ", e2);
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Exception e3) {
                            AFLogger.afErrorLog(e3.getMessage(), e3);
                        }
                    }
                }
                if (query.moveToFirst()) {
                    str = query.getString(query.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME));
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Exception e4) {
                            AFLogger.afErrorLog(e4.getMessage(), e4);
                        }
                    }
                    return str;
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e5) {
                        AFLogger.afErrorLog(e5.getMessage(), e5);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            try {
                query.close();
            } catch (Exception e6) {
                AFLogger.afErrorLog(e6.getMessage(), e6);
            }
        }
        return str;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    static SharedPreferences m55(Context context) {
        return context.getSharedPreferences("appsflyer-data", 0);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    static int m54(SharedPreferences sharedPreferences) {
        return m23(sharedPreferences, "appsFlyerCount", false);
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private static int m23(SharedPreferences sharedPreferences, String str, boolean z) {
        int i = sharedPreferences.getInt(str, 0);
        if (z) {
            i++;
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(str, i);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        }
        if (s.m186().m189()) {
            s.m186().m198(String.valueOf(i));
        }
        return i;
    }

    public String getAppsFlyerUID(Context context) {
        s.m186().m195("getAppsFlyerUID", new String[0]);
        return t.m210((WeakReference<Context>) new WeakReference(context));
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private void m37(URL url, String str, String str2, WeakReference<Context> weakReference, String str3, boolean z) throws IOException {
        HttpURLConnection httpURLConnection;
        OutputStreamWriter outputStreamWriter;
        Context context = weakReference.get();
        boolean z2 = z && f22 != null;
        try {
            s.m186().m200(url.toString(), str);
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (Throwable th) {
            th = th;
            httpURLConnection = null;
        }
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(str.getBytes().length));
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoOutput(true);
            try {
                OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                try {
                    outputStreamWriter2.write(str);
                    outputStreamWriter2.close();
                    int responseCode = httpURLConnection.getResponseCode();
                    String m43 = m43(httpURLConnection);
                    s.m186().m199(url.toString(), responseCode, m43);
                    AFLogger.afInfoLog("response code: ".concat(String.valueOf(responseCode)));
                    m65(context, LOG_TAG, "SERVER_RESPONSE_CODE", Integer.toString(responseCode));
                    SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
                    if (responseCode == 200) {
                        if (weakReference.get() != null && z) {
                            this.f45 = System.currentTimeMillis();
                            if (f26 != null) {
                                f26.onTrackingRequestSuccess();
                            }
                        }
                        String string = AppsFlyerProperties.getInstance().getString("afUninstallToken");
                        if (string != null) {
                            AFLogger.afDebugLog("Uninstall Token exists: ".concat(String.valueOf(string)));
                            if (!sharedPreferences.getBoolean("sentRegisterRequestToAF", false)) {
                                AFLogger.afDebugLog("Resending Uninstall token to AF servers: ".concat(String.valueOf(string)));
                                u.m216(context, new c.a.C0004c(string));
                            }
                        } else if (AppsFlyerProperties.getInstance().getString("gcmProjectNumber") != null) {
                            AFLogger.afDebugLog("GCM Project number exists. Fetching token and sending to AF servers");
                            new u.a(new WeakReference(context)).execute(new Void[0]);
                        }
                        if (this.latestDeepLink != null) {
                            this.latestDeepLink = null;
                        }
                        if (str3 != null) {
                            CacheManager.getInstance().deleteRequest(str3, context);
                        }
                        if (weakReference.get() != null && str3 == null) {
                            m64(context, "sentSuccessfully", "true");
                            if (!this.f47 && System.currentTimeMillis() - this.f27 >= 15000 && this.f29 == null) {
                                this.f29 = AFExecutor.getInstance().m5();
                                m38(this.f29, new d(context), 1L, TimeUnit.SECONDS);
                            }
                        }
                        this.f43 = ServerConfigHandler.m94(m43).optBoolean("send_background", false);
                    } else if (f26 != null) {
                        f26.onTrackingRequestFailure("Failure: ".concat(String.valueOf(responseCode)));
                    }
                    int i = sharedPreferences.getInt("appsflyerConversionDataRequestRetries", 0);
                    long j = sharedPreferences.getLong("appsflyerConversionDataCacheExpiration", 0L);
                    if (j != 0 && System.currentTimeMillis() - j > 5184000000L) {
                        m64(context, "attributionId", (String) null);
                        m63(context, "appsflyerConversionDataCacheExpiration", 0L);
                    }
                    if (sharedPreferences.getString("attributionId", null) == null && str2 != null && z2 && f22 != null && i <= 5) {
                        ScheduledThreadPoolExecutor m5 = AFExecutor.getInstance().m5();
                        m38(m5, new e(context.getApplicationContext(), str2, m5), 10L, TimeUnit.MILLISECONDS);
                    } else if (str2 == null) {
                        AFLogger.afWarnLog("AppsFlyer dev key is missing.");
                    } else if (z2 && f22 != null && sharedPreferences.getString("attributionId", null) != null && m23(sharedPreferences, "appsFlyerCount", false) > 1) {
                        try {
                            Map<String, String> m68 = m68(context);
                            if (m68 != null) {
                                try {
                                    if (!m68.containsKey("is_first_launch")) {
                                        m68.put("is_first_launch", Boolean.toString(false));
                                    }
                                    f22.onInstallConversionDataLoaded(m68);
                                } catch (Throwable th2) {
                                    AFLogger.afErrorLog(th2.getLocalizedMessage(), th2);
                                }
                            }
                        } catch (m e2) {
                            AFLogger.afErrorLog(e2.getMessage(), e2);
                        }
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outputStreamWriter = outputStreamWriter2;
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    } else if (f26 != null) {
                        f26.onTrackingRequestFailure("No Connectivity");
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                outputStreamWriter = null;
            }
        } catch (Throwable th5) {
            th = th5;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
    }

    public void validateAndTrackInAppPurchase(Context context, String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        s m186 = s.m186();
        String[] strArr = new String[6];
        strArr[0] = str;
        strArr[1] = str2;
        strArr[2] = str3;
        strArr[3] = str4;
        strArr[4] = str5;
        strArr[5] = map == null ? "" : map.toString();
        m186.m195("validateAndTrackInAppPurchase", strArr);
        if (!isTrackingStopped()) {
            AFLogger.afInfoLog(new StringBuilder("Validate in app called with parameters: ").append(str3).append(" ").append(str4).append(" ").append(str5).toString());
        }
        if (str != null && str4 != null && str2 != null && str5 != null && str3 != null) {
            m38(AFExecutor.getInstance().m5(), new j(context.getApplicationContext(), AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY), str, str2, str3, str4, str5, map, context instanceof Activity ? ((Activity) context).getIntent() : null), 10L, TimeUnit.MILLISECONDS);
        } else if (f19 != null) {
            f19.onValidateInAppFailure("Please provide purchase parameters");
        }
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private static void m38(ScheduledExecutorService scheduledExecutorService, Runnable runnable, long j, TimeUnit timeUnit) {
        if (scheduledExecutorService != null) {
            try {
                if (!scheduledExecutorService.isShutdown() && !scheduledExecutorService.isTerminated()) {
                    scheduledExecutorService.schedule(runnable, j, timeUnit);
                }
            } catch (RejectedExecutionException e2) {
                AFLogger.afErrorLog("scheduleJob failed with RejectedExecutionException Exception", e2);
                return;
            } catch (Throwable th) {
                AFLogger.afErrorLog("scheduleJob failed with Exception", th);
                return;
            }
        }
        AFLogger.afWarnLog("scheduler is null, shut downed or terminated");
    }

    @Override // com.appsflyer.a
    public void onHandleReferrer(Map<String, String> map) {
        this.f49 = map;
    }

    public boolean isTrackingStopped() {
        return this.f42;
    }

    class c implements Runnable {

        /* renamed from: ʼ, reason: contains not printable characters */
        private String f69;

        /* renamed from: ʽ, reason: contains not printable characters */
        private boolean f70;

        /* renamed from: ˊ, reason: contains not printable characters */
        private String f71;

        /* renamed from: ˋ, reason: contains not printable characters */
        private WeakReference<Context> f72;

        /* renamed from: ˎ, reason: contains not printable characters */
        private String f73;

        /* renamed from: ˏ, reason: contains not printable characters */
        private final Intent f74;

        /* renamed from: ॱ, reason: contains not printable characters */
        private final String f75;

        /* renamed from: ॱॱ, reason: contains not printable characters */
        private boolean f76;

        /* renamed from: ᐝ, reason: contains not printable characters */
        private String f77;

        /* synthetic */ c(AppsFlyerLib appsFlyerLib, WeakReference weakReference, String str, String str2, String str3, String str4, boolean z, Intent intent, String str5, byte b) {
            this(weakReference, str, str2, str3, str4, z, intent, str5);
        }

        private c(WeakReference<Context> weakReference, String str, String str2, String str3, String str4, boolean z, Intent intent, String str5) {
            this.f72 = weakReference;
            this.f71 = str;
            this.f73 = str2;
            this.f77 = str3;
            this.f69 = str4;
            this.f70 = true;
            this.f76 = z;
            this.f74 = intent;
            this.f75 = str5;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AppsFlyerLib.m27(AppsFlyerLib.this, this.f72.get(), this.f71, this.f73, this.f77, this.f69, this.f70, this.f76, this.f74, this.f75);
        }
    }

    class a implements Runnable {

        /* renamed from: ˊ, reason: contains not printable characters */
        private boolean f57;

        /* renamed from: ˋ, reason: contains not printable characters */
        private Map<String, Object> f58;

        /* renamed from: ˎ, reason: contains not printable characters */
        private String f59;

        /* renamed from: ˏ, reason: contains not printable characters */
        private int f60;

        /* renamed from: ॱ, reason: contains not printable characters */
        private WeakReference<Context> f61;

        /* synthetic */ a(AppsFlyerLib appsFlyerLib, String str, Map map, Context context, boolean z, int i, byte b) {
            this(str, map, context, z, i);
        }

        private a(String str, Map<String, Object> map, Context context, boolean z, int i) {
            this.f61 = null;
            this.f59 = str;
            this.f58 = map;
            this.f61 = new WeakReference<>(context);
            this.f57 = z;
            this.f60 = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            String str = null;
            if (!AppsFlyerLib.this.isTrackingStopped()) {
                if (this.f57 && this.f60 <= 2 && AppsFlyerLib.m53(AppsFlyerLib.this)) {
                    this.f58.put("rfr", AppsFlyerLib.this.f49);
                }
                this.f58.putAll(new c.a(this.f58, this.f61.get()));
                try {
                    String str2 = (String) this.f58.get("appsflyerKey");
                    str = AFHelper.convertToJsonObject(this.f58).toString();
                    AppsFlyerLib.m36(AppsFlyerLib.this, this.f59, str, str2, this.f61, (String) null, this.f57);
                } catch (IOException e) {
                    AFLogger.afErrorLog("Exception while sending request to server. ", e);
                    if (str != null && this.f61 != null && !this.f59.contains("&isCachedRequest=true&timeincache=")) {
                        CacheManager.getInstance().cacheRequest(new RequestCacheData(this.f59, str, BuildConfig.VERSION_NAME), this.f61.get());
                        AFLogger.afErrorLog(e.getMessage(), e);
                    }
                } catch (Throwable th) {
                    AFLogger.afErrorLog(th.getMessage(), th);
                }
            }
        }
    }

    class e extends b {
        public e(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
            super(context, str, scheduledExecutorService);
        }

        @Override // com.appsflyer.AppsFlyerLib.b
        /* renamed from: ˎ */
        public final String mo86() {
            return ServerConfigHandler.getUrl("https://api.%s/install_data/v3/");
        }

        @Override // com.appsflyer.AppsFlyerLib.b
        /* renamed from: ˎ */
        protected final void mo88(Map<String, String> map) {
            map.put("is_first_launch", Boolean.toString(true));
            AppsFlyerLib.f22.onInstallConversionDataLoaded(map);
            AppsFlyerLib.m47(this.f66.get(), "appsflyerConversionDataRequestRetries", 0);
        }

        @Override // com.appsflyer.AppsFlyerLib.b
        /* renamed from: ˎ */
        protected final void mo87(String str, int i) {
            AppsFlyerLib.f22.onInstallConversionFailure(str);
            if (i >= 400 && i < 500) {
                AppsFlyerLib.m47(this.f66.get(), "appsflyerConversionDataRequestRetries", AppsFlyerLib.m55(this.f66.get()).getInt("appsflyerConversionDataRequestRetries", 0) + 1);
            }
        }
    }

    abstract class b implements Runnable {

        /* renamed from: ˋ, reason: contains not printable characters */
        private ScheduledExecutorService f64;

        /* renamed from: ˎ, reason: contains not printable characters */
        private String f65;

        /* renamed from: ˏ, reason: contains not printable characters */
        WeakReference<Context> f66;

        /* renamed from: ॱ, reason: contains not printable characters */
        private AtomicInteger f67 = new AtomicInteger(0);

        /* renamed from: ˎ, reason: contains not printable characters */
        public abstract String mo86();

        /* renamed from: ˎ, reason: contains not printable characters */
        protected abstract void mo87(String str, int i);

        /* renamed from: ˎ, reason: contains not printable characters */
        protected abstract void mo88(Map<String, String> map);

        b(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
            this.f66 = null;
            this.f66 = new WeakReference<>(context);
            this.f65 = str;
            if (scheduledExecutorService == null) {
                this.f64 = AFExecutor.getInstance().m5();
            } else {
                this.f64 = scheduledExecutorService;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:77:0x022e  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 622
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.AppsFlyerLib.b.run():void");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x006d A[Catch: Throwable -> 0x008c, TRY_LEAVE, TryCatch #4 {Throwable -> 0x008c, blocks: (B:54:0x0068, B:49:0x006d), top: B:53:0x0068 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0068 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @android.support.annotation.NonNull
    /* renamed from: ˎ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static java.lang.String m43(java.net.HttpURLConnection r7) {
        /*
            r1 = 0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.io.InputStream r0 = r7.getErrorStream()     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L9b
            if (r0 != 0) goto L10
            java.io.InputStream r0 = r7.getInputStream()     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L9b
        L10:
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L9b
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L9b
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> L9e
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> L9e
        L1a:
            java.lang.String r0 = r3.readLine()     // Catch: java.lang.Throwable -> L2a java.lang.Throwable -> L93
            if (r0 == 0) goto L5b
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch: java.lang.Throwable -> L2a java.lang.Throwable -> L93
            r1 = 10
            r0.append(r1)     // Catch: java.lang.Throwable -> L2a java.lang.Throwable -> L93
            goto L1a
        L2a:
            r0 = move-exception
            r1 = r2
            r2 = r3
        L2d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L97
            java.lang.String r5 = "Could not read connection response from: "
            r3.<init>(r5)     // Catch: java.lang.Throwable -> L97
            java.net.URL r5 = r7.getURL()     // Catch: java.lang.Throwable -> L97
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L97
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch: java.lang.Throwable -> L97
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L97
            com.appsflyer.AFLogger.afErrorLog(r3, r0)     // Catch: java.lang.Throwable -> L97
            if (r2 == 0) goto L4c
            r2.close()     // Catch: java.lang.Throwable -> L99
        L4c:
            if (r1 == 0) goto L51
            r1.close()     // Catch: java.lang.Throwable -> L99
        L51:
            java.lang.String r0 = r4.toString()
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: org.json.JSONException -> L71
            r1.<init>(r0)     // Catch: org.json.JSONException -> L71
        L5a:
            return r0
        L5b:
            r3.close()     // Catch: java.lang.Throwable -> L62
            r2.close()     // Catch: java.lang.Throwable -> L62
            goto L51
        L62:
            r0 = move-exception
            goto L51
        L64:
            r0 = move-exception
            r2 = r1
        L66:
            if (r2 == 0) goto L6b
            r2.close()     // Catch: java.lang.Throwable -> L8c
        L6b:
            if (r1 == 0) goto L70
            r1.close()     // Catch: java.lang.Throwable -> L8c
        L70:
            throw r0
        L71:
            r1 = move-exception
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            java.lang.String r2 = "string_response"
            r1.put(r2, r0)     // Catch: org.json.JSONException -> L81
            java.lang.String r0 = r1.toString()     // Catch: org.json.JSONException -> L81
            goto L5a
        L81:
            r0 = move-exception
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>()
            java.lang.String r0 = r0.toString()
            goto L5a
        L8c:
            r1 = move-exception
            goto L70
        L8e:
            r0 = move-exception
            r6 = r2
            r2 = r1
            r1 = r6
            goto L66
        L93:
            r0 = move-exception
            r1 = r2
            r2 = r3
            goto L66
        L97:
            r0 = move-exception
            goto L66
        L99:
            r0 = move-exception
            goto L51
        L9b:
            r0 = move-exception
            r2 = r1
            goto L2d
        L9e:
            r0 = move-exception
            r6 = r2
            r2 = r1
            r1 = r6
            goto L2d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.AppsFlyerLib.m43(java.net.HttpURLConnection):java.lang.String");
    }

    class d implements Runnable {

        /* renamed from: ॱ, reason: contains not printable characters */
        private WeakReference<Context> f79;

        public d(Context context) {
            this.f79 = null;
            this.f79 = new WeakReference<>(context);
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (!AppsFlyerLib.this.f47) {
                AppsFlyerLib.this.f27 = System.currentTimeMillis();
                if (this.f79 != null) {
                    AppsFlyerLib.this.f47 = true;
                    try {
                        String m59 = AppsFlyerLib.m59(AppsFlyerProperties.AF_KEY);
                        synchronized (this.f79) {
                            for (RequestCacheData requestCacheData : CacheManager.getInstance().getCachedRequests(this.f79.get())) {
                                AFLogger.afInfoLog(new StringBuilder("resending request: ").append(requestCacheData.getRequestURL()).toString());
                                try {
                                    AppsFlyerLib.m36(AppsFlyerLib.this, new StringBuilder().append(requestCacheData.getRequestURL()).append("&isCachedRequest=true&timeincache=").append(Long.toString((System.currentTimeMillis() - Long.parseLong(requestCacheData.getCacheKey(), 10)) / 1000)).toString(), requestCacheData.getPostData(), m59, (WeakReference) this.f79, requestCacheData.getCacheKey(), false);
                                } catch (Exception e) {
                                    AFLogger.afErrorLog("Failed to resend cached request", e);
                                }
                            }
                        }
                    } catch (Exception e2) {
                        AFLogger.afErrorLog("failed to check cache. ", e2);
                    } finally {
                        AppsFlyerLib.this.f47 = false;
                    }
                    AppsFlyerLib.this.f29.shutdown();
                    AppsFlyerLib.m32(AppsFlyerLib.this);
                }
            }
        }
    }

    /* renamed from: ᐝ, reason: contains not printable characters */
    private static float m72(Context context) {
        try {
            Intent registerReceiver = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = registerReceiver.getIntExtra("level", -1);
            int intExtra2 = registerReceiver.getIntExtra("scale", -1);
            if (intExtra == -1 || intExtra2 == -1) {
                return 50.0f;
            }
            return (intExtra / intExtra2) * 100.0f;
        } catch (Throwable th) {
            AFLogger.afErrorLog(th.getMessage(), th);
            return 1.0f;
        }
    }

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private static boolean m71(Context context) {
        if (context != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                    for (Network network : connectivityManager.getAllNetworks()) {
                        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                        if (networkCapabilities.hasTransport(4) && !networkCapabilities.hasCapability(15)) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e2) {
                    AFLogger.afErrorLog("Failed collecting ivc data", e2);
                }
            } else if (Build.VERSION.SDK_INT >= 16) {
                ArrayList arrayList = new ArrayList();
                try {
                    Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
                    while (it.hasNext()) {
                        NetworkInterface networkInterface = (NetworkInterface) it.next();
                        if (networkInterface.isUp()) {
                            arrayList.add(networkInterface.getName());
                        }
                    }
                    return arrayList.contains("tun0");
                } catch (Exception e3) {
                    AFLogger.afErrorLog("Failed collecting ivc data", e3);
                }
            }
        }
        return false;
    }

    public void setLogLevel(AFLogger.LogLevel logLevel) {
        AppsFlyerProperties.getInstance().set("logLevel", logLevel.getLevel());
    }

    public void setHostName(String str) {
        AppsFlyerProperties.getInstance().set("custom_host", str);
    }

    public String getHost() {
        String string = AppsFlyerProperties.getInstance().getString("custom_host");
        return string != null ? string : ServerParameters.DEFAULT_HOST;
    }

    public void setMinTimeBetweenSessions(int i) {
        this.f46 = TimeUnit.SECONDS.toMillis(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ˏ, reason: contains not printable characters */
    public static void m64(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
        edit.putString(str, str2);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ˎ, reason: contains not printable characters */
    public static void m47(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
        edit.putInt(str, i);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: ˏ, reason: contains not printable characters */
    public static void m63(Context context, String str, long j) {
        SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
        edit.putLong(str, j);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }
}
