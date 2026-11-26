package com.appsflyer.share;

import android.content.Context;
import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerProperties;
import com.appsflyer.CreateOneLinkHttpTask;
import com.appsflyer.ServerConfigHandler;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class LinkGenerator {

    /* renamed from: ʻ, reason: contains not printable characters */
    private String f241;

    /* renamed from: ʼ, reason: contains not printable characters */
    private String f242;

    /* renamed from: ʽ, reason: contains not printable characters */
    private String f243;

    /* renamed from: ˊ, reason: contains not printable characters */
    private String f244;

    /* renamed from: ˋ, reason: contains not printable characters */
    private String f245;

    /* renamed from: ˎ, reason: contains not printable characters */
    private String f247;

    /* renamed from: ˏ, reason: contains not printable characters */
    private String f248;

    /* renamed from: ॱ, reason: contains not printable characters */
    private String f250;

    /* renamed from: ॱˊ, reason: contains not printable characters */
    private String f251;

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private String f252;

    /* renamed from: ᐝ, reason: contains not printable characters */
    private String f253;

    /* renamed from: ˋॱ, reason: contains not printable characters */
    private Map<String, String> f246 = new HashMap();

    /* renamed from: ͺ, reason: contains not printable characters */
    private Map<String, String> f249 = new HashMap();

    public LinkGenerator(String str) {
        this.f244 = str;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    final LinkGenerator m205(String str) {
        this.f242 = str;
        return this;
    }

    public LinkGenerator setDeeplinkPath(String str) {
        this.f243 = str;
        return this;
    }

    public LinkGenerator setBaseDeeplink(String str) {
        this.f251 = str;
        return this;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    final LinkGenerator m206(String str) {
        this.f241 = str;
        return this;
    }

    public LinkGenerator setChannel(String str) {
        this.f250 = str;
        return this;
    }

    public String getChannel() {
        return this.f250;
    }

    public LinkGenerator setReferrerCustomerId(String str) {
        this.f248 = str;
        return this;
    }

    public String getMediaSource() {
        return this.f244;
    }

    public Map<String, String> getParameters() {
        return this.f246;
    }

    public LinkGenerator setCampaign(String str) {
        this.f247 = str;
        return this;
    }

    public String getCampaign() {
        return this.f247;
    }

    public LinkGenerator addParameter(String str, String str2) {
        this.f246.put(str, str2);
        return this;
    }

    public LinkGenerator addParameters(Map<String, String> map) {
        if (map != null) {
            this.f246.putAll(map);
        }
        return this;
    }

    public LinkGenerator setReferrerUID(String str) {
        this.f245 = str;
        return this;
    }

    public LinkGenerator setReferrerName(String str) {
        this.f252 = str;
        return this;
    }

    public LinkGenerator setReferrerImageURL(String str) {
        this.f253 = str;
        return this;
    }

    public LinkGenerator setBaseURL(String str, String str2, String str3) {
        if (str == null || str.length() <= 0) {
            this.f241 = String.format(Constants.AF_BASE_URL_FORMAT, ServerConfigHandler.getUrl(Constants.APPSFLYER_DEFAULT_APP_DOMAIN), str3);
        } else {
            if (str2 == null || str2.length() < 5) {
                str2 = Constants.ONELINK_DEFAULT_DOMAIN;
            }
            this.f241 = String.format(Constants.AF_BASE_URL_FORMAT, str2, str);
        }
        return this;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private StringBuilder m203() {
        StringBuilder sb = new StringBuilder();
        if (this.f241 != null && this.f241.startsWith("http")) {
            sb.append(this.f241);
        } else {
            sb.append(ServerConfigHandler.getUrl(Constants.BASE_URL_APP_APPSFLYER_COM));
        }
        if (this.f242 != null) {
            sb.append('/').append(this.f242);
        }
        this.f249.put(Constants.URL_MEDIA_SOURCE, this.f244);
        sb.append('?').append("pid=").append(m204(this.f244, "media source"));
        if (this.f245 != null) {
            this.f249.put(Constants.URL_REFERRER_UID, this.f245);
            sb.append('&').append("af_referrer_uid=").append(m204(this.f245, "referrerUID"));
        }
        if (this.f250 != null) {
            this.f249.put("af_channel", this.f250);
            sb.append('&').append("af_channel=").append(m204(this.f250, AppsFlyerProperties.CHANNEL));
        }
        if (this.f248 != null) {
            this.f249.put(Constants.URL_REFERRER_CUSTOMER_ID, this.f248);
            sb.append('&').append("af_referrer_customer_id=").append(m204(this.f248, "referrerCustomerId"));
        }
        if (this.f247 != null) {
            this.f249.put(Constants.URL_CAMPAIGN, this.f247);
            sb.append('&').append("c=").append(m204(this.f247, "campaign"));
        }
        if (this.f252 != null) {
            this.f249.put(Constants.URL_REFERRER_NAME, this.f252);
            sb.append('&').append("af_referrer_name=").append(m204(this.f252, "referrerName"));
        }
        if (this.f253 != null) {
            this.f249.put(Constants.URL_REFERRER_IMAGE_URL, this.f253);
            sb.append('&').append("af_referrer_image_url=").append(m204(this.f253, "referrerImageURL"));
        }
        if (this.f251 != null) {
            StringBuilder append = new StringBuilder().append(this.f251);
            append.append(this.f251.endsWith(Constants.URL_PATH_DELIMITER) ? "" : Constants.URL_PATH_DELIMITER);
            if (this.f243 != null) {
                append.append(this.f243);
            }
            this.f249.put(Constants.URL_BASE_DEEPLINK, append.toString());
            sb.append('&').append("af_dp=").append(m204(this.f251, "baseDeeplink"));
            if (this.f243 != null) {
                sb.append(this.f251.endsWith(Constants.URL_PATH_DELIMITER) ? "" : "%2F").append(m204(this.f243, "deeplinkPath"));
            }
        }
        for (String str : this.f246.keySet()) {
            if (!sb.toString().contains(new StringBuilder().append(str).append("=").append(m204(this.f246.get(str), str)).toString())) {
                sb.append('&').append(str).append('=').append(m204(this.f246.get(str), str));
            }
        }
        return sb;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static String m204(String str, String str2) {
        try {
            return URLEncoder.encode(str, "utf8");
        } catch (UnsupportedEncodingException e) {
            AFLogger.afInfoLog(new StringBuilder("Illegal ").append(str2).append(": ").append(str).toString());
            return "";
        } catch (Throwable th) {
            return "";
        }
    }

    public String generateLink() {
        return m203().toString();
    }

    public void generateLink(Context context, CreateOneLinkHttpTask.ResponseListener responseListener) {
        String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.ONELINK_ID);
        if (!this.f246.isEmpty()) {
            for (Map.Entry<String, String> entry : this.f246.entrySet()) {
                this.f249.put(entry.getKey(), entry.getValue());
            }
        }
        m203();
        ShareInviteHelper.generateUserInviteLink(context, string, this.f249, responseListener);
    }
}
