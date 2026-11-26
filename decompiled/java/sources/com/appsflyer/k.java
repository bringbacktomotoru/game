package com.appsflyer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.appsflyer.f;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class k extends AsyncTask<String, Void, String> {

    /* renamed from: ʻ, reason: contains not printable characters */
    private WeakReference<Context> f170;

    /* renamed from: ˋ, reason: contains not printable characters */
    Map<String, String> f174;

    /* renamed from: ˎ, reason: contains not printable characters */
    private boolean f175;

    /* renamed from: ॱ, reason: contains not printable characters */
    String f177;

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private URL f178;

    /* renamed from: ᐝ, reason: contains not printable characters */
    private HttpURLConnection f179;

    /* renamed from: ˏ, reason: contains not printable characters */
    private String f176 = "";

    /* renamed from: ˊ, reason: contains not printable characters */
    private boolean f173 = false;

    /* renamed from: ʽ, reason: contains not printable characters */
    private boolean f172 = true;

    /* renamed from: ʼ, reason: contains not printable characters */
    private boolean f171 = true;

    k(Context context, boolean z) {
        this.f175 = false;
        this.f170 = new WeakReference<>(context);
        this.f175 = z;
    }

    @Override // android.os.AsyncTask
    protected final void onPreExecute() {
        if (this.f177 == null) {
            this.f177 = new JSONObject(this.f174).toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: ॱ, reason: contains not printable characters and merged with bridge method [inline-methods] */
    public final String doInBackground(String... strArr) {
        if (this.f175) {
            return null;
        }
        try {
            this.f178 = new URL(strArr[0]);
            if (this.f172) {
                s.m186().m200(this.f178.toString(), this.f177);
                int length = this.f177.getBytes("UTF-8").length;
                f.AnonymousClass4.m126(new StringBuilder("call = ").append(this.f178).append("; size = ").append(length).append(" byte").append(length > 1 ? "s" : "").append("; body = ").append(this.f177).toString());
            }
            this.f179 = (HttpURLConnection) this.f178.openConnection();
            this.f179.setReadTimeout(30000);
            this.f179.setConnectTimeout(30000);
            this.f179.setRequestMethod("POST");
            this.f179.setDoInput(true);
            this.f179.setDoOutput(true);
            this.f179.setRequestProperty("Content-Type", "application/json");
            OutputStream outputStream = this.f179.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(this.f177);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            this.f179.connect();
            int responseCode = this.f179.getResponseCode();
            if (this.f171) {
                AppsFlyerLib.getInstance();
                this.f176 = AppsFlyerLib.m43(this.f179);
            }
            if (this.f172) {
                s.m186().m199(this.f178.toString(), responseCode, this.f176);
            }
            if (responseCode == 200) {
                AFLogger.afInfoLog("Status 200 ok");
                Context context = this.f170.get();
                if (this.f178.toString().startsWith(ServerConfigHandler.getUrl(AppsFlyerLib.f21)) && context != null) {
                    SharedPreferences.Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
                    edit.putBoolean("sentRegisterRequestToAF", true);
                    edit.apply();
                    AFLogger.afDebugLog("Successfully registered for Uninstall Tracking");
                }
            } else {
                this.f173 = true;
            }
        } catch (Throwable th) {
            AFLogger.afErrorLog(new StringBuilder("Error while calling ").append(this.f178.toString()).toString(), th);
            this.f173 = true;
        }
        return this.f176;
    }

    @Override // android.os.AsyncTask
    protected final void onCancelled() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: ॱ, reason: contains not printable characters and merged with bridge method [inline-methods] */
    public final void onPostExecute(String str) {
        if (this.f173) {
            AFLogger.afInfoLog("Connection error: ".concat(String.valueOf(str)));
        } else {
            AFLogger.afInfoLog("Connection call succeeded: ".concat(String.valueOf(str)));
        }
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    final void m154() {
        this.f172 = false;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    final HttpURLConnection m153() {
        return this.f179;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final void m152() {
        this.f171 = false;
    }
}
