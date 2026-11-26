package com.appsflyer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import com.appsflyer.share.Constants;
import com.appsflyer.share.LinkGenerator;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CreateOneLinkHttpTask extends OneLinkHttpTask {

    /* renamed from: ˊ, reason: contains not printable characters */
    private String f89;

    /* renamed from: ˋ, reason: contains not printable characters */
    private Map<String, String> f90;

    /* renamed from: ˎ, reason: contains not printable characters */
    private ResponseListener f91;

    /* renamed from: ॱ, reason: contains not printable characters */
    private String f92;

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private boolean f93;

    /* renamed from: ᐝ, reason: contains not printable characters */
    private Context f94;

    public interface ResponseListener {
        @WorkerThread
        void onResponse(String str);

        @WorkerThread
        void onResponseError(String str);
    }

    public CreateOneLinkHttpTask(@NonNull String str, @NonNull Map<String, String> map, AppsFlyerLib appsFlyerLib, @NonNull Context context, boolean z) {
        super(appsFlyerLib);
        this.f92 = "";
        this.f93 = false;
        this.f93 = z;
        this.f94 = context;
        if (this.f94 != null) {
            this.f92 = context.getPackageName();
        } else {
            AFLogger.afWarnLog("CreateOneLinkHttpTask: context can't be null");
        }
        this.f96 = str;
        this.f89 = "-1";
        this.f90 = map;
    }

    public void setListener(@NonNull ResponseListener responseListener) {
        this.f91 = responseListener;
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˏ, reason: contains not printable characters */
    final void mo92(HttpsURLConnection httpsURLConnection) throws JSONException, IOException {
        if (!this.f93) {
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject(this.f90);
            jSONObject.put("ttl", this.f89);
            jSONObject.put("data", jSONObject2);
            httpsURLConnection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            dataOutputStream.writeBytes(jSONObject.toString());
            dataOutputStream.flush();
            dataOutputStream.close();
        }
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˏ, reason: contains not printable characters */
    final String mo91() {
        return new StringBuilder().append(ServerConfigHandler.getUrl("https://onelink.%s/shortlink-sdk/v1")).append(Constants.URL_PATH_DELIMITER).append(this.f96).toString();
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˊ, reason: contains not printable characters */
    final void mo89(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                this.f91.onResponse(jSONObject.optString(keys.next()));
            }
        } catch (JSONException e) {
            this.f91.onResponseError("Can't parse one link data");
            AFLogger.afErrorLog("Error while parsing to json ".concat(String.valueOf(str)), e);
        }
    }

    @Override // com.appsflyer.OneLinkHttpTask
    /* renamed from: ˋ, reason: contains not printable characters */
    final void mo90() {
        LinkGenerator addParameters = new LinkGenerator(Constants.USER_INVITE_LINK_TYPE).setBaseURL(this.f96, AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.ONELINK_DOMAIN), this.f92).addParameter(Constants.URL_SITE_ID, this.f92).addParameters(this.f90);
        String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.APP_USER_ID);
        if (string != null) {
            addParameters.setReferrerCustomerId(string);
        }
        this.f91.onResponse(addParameters.generateLink());
    }
}
