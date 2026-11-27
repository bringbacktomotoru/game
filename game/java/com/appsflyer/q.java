package com.appsflyer;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.appsflyer.share.Constants;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class q extends OneLinkHttpTask {

    /* renamed from: ॱ, reason: contains not printable characters */
    private static List<String> f203 = Arrays.asList("onelink.me", "onelnk.com", "app.aflink.com");

    /* renamed from: ˋ, reason: contains not printable characters */
    private String f204;

    /* renamed from: ˎ, reason: contains not printable characters */
    private e f205;

    interface e {
        /* renamed from: ˋ */
        void mo84(String str);

        /* renamed from: ॱ */
        void mo85(Map<String, String> map);
    }

    q(Uri uri, AppsFlyerLib appsFlyerLib) {
        super(appsFlyerLib);
        boolean z;
        if (!TextUtils.isEmpty(uri.getHost()) && !TextUtils.isEmpty(uri.getPath())) {
            boolean z2 = false;
            Iterator<String> it = f203.iterator();
            while (true) {
                z = z2;
                if (!it.hasNext()) {
                    break;
                } else {
                    z2 = uri.getHost().contains(it.next()) ? true : z;
                }
            }
            String[] split = uri.getPath().split(Constants.URL_PATH_DELIMITER);
            if (z && split.length == 3) {
                this.f96 = split[1];
                this.f204 = split[2];
            }
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    final void m174(@NonNull e eVar) {
        this.f205 = eVar;
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final boolean m175() {
        return (TextUtils.isEmpty(this.f96) || TextUtils.isEmpty(this.f204)) ? false : true;
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˏ */
    final void mo92(HttpsURLConnection httpsURLConnection) throws JSONException, IOException {
        httpsURLConnection.setRequestMethod("GET");
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˏ */
    final String mo91() {
        return new StringBuilder().append(ServerConfigHandler.getUrl("https://onelink.%s/shortlink-sdk/v1")).append(Constants.URL_PATH_DELIMITER).append(this.f96).append("?id=").append(this.f204).toString();
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˊ */
    final void mo89(String str) {
        try {
            HashMap hashMap = new HashMap();
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                hashMap.put(next, jSONObject.optString(next));
            }
            this.f205.mo85(hashMap);
        } catch (JSONException e2) {
            this.f205.mo84("Can't parse one link data");
            AFLogger.afErrorLog("Error while parsing to json ".concat(String.valueOf(str)), e2);
        }
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˋ */
    final void mo90() {
        this.f205.mo84("Can't get one link data");
    }
}
