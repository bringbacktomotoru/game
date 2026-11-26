package com.appsflyer.cache;

import java.util.Scanner;

/* loaded from: classes.dex */
public class RequestCacheData {

    /* renamed from: ˊ, reason: contains not printable characters */
    private String f116;

    /* renamed from: ˎ, reason: contains not printable characters */
    private String f117;

    /* renamed from: ˏ, reason: contains not printable characters */
    private String f118;

    /* renamed from: ॱ, reason: contains not printable characters */
    private String f119;

    public RequestCacheData(String str, String str2, String str3) {
        this.f118 = str;
        this.f117 = str2;
        this.f119 = str3;
    }

    public RequestCacheData(char[] cArr) {
        Scanner scanner = new Scanner(new String(cArr));
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (nextLine.startsWith("url=")) {
                this.f118 = nextLine.substring(4).trim();
            } else if (nextLine.startsWith("version=")) {
                this.f119 = nextLine.substring(8).trim();
            } else if (nextLine.startsWith("data=")) {
                this.f117 = nextLine.substring(5).trim();
            }
        }
        scanner.close();
    }

    public String getVersion() {
        return this.f119;
    }

    public void setVersion(String str) {
        this.f119 = str;
    }

    public String getPostData() {
        return this.f117;
    }

    public void setPostData(String str) {
        this.f117 = str;
    }

    public String getRequestURL() {
        return this.f118;
    }

    public void setRequestURL(String str) {
        this.f118 = str;
    }

    public String getCacheKey() {
        return this.f116;
    }

    public void setCacheKey(String str) {
        this.f116 = str;
    }
}
