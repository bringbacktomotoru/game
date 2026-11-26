package com.appsflyer;

/* loaded from: classes.dex */
final class p implements b {

    /* renamed from: ˋ, reason: contains not printable characters */
    private b f193 = this;

    interface b {
        /* renamed from: ˎ */
        Class<?> mo170(String str) throws ClassNotFoundException;
    }

    enum e {
        UNITY("android_unity", "com.unity3d.player.UnityPlayer"),
        REACT_NATIVE("android_reactNative", "com.facebook.react.ReactApplication"),
        CORDOVA("android_cordova", "org.apache.cordova.CordovaActivity"),
        SEGMENT("android_segment", "com.segment.analytics.integrations.Integration"),
        COCOS2DX("android_cocos2dx", "org.cocos2dx.lib.Cocos2dxActivity"),
        DEFAULT("android_native", "android_native");


        /* renamed from: ʻ, reason: contains not printable characters */
        private String f201;

        /* renamed from: ʼ, reason: contains not printable characters */
        private String f202;

        e(String str, String str2) {
            this.f202 = str;
            this.f201 = str2;
        }
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final String m171() {
        for (e eVar : e.values()) {
            if (m169(eVar.f201)) {
                return eVar.f202;
            }
        }
        return e.DEFAULT.f202;
    }

    p() {
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private boolean m169(String str) {
        try {
            this.f193.mo170(str);
            AFLogger.afRDLog(new StringBuilder("Class: ").append(str).append(" is found.").toString());
            return true;
        } catch (ClassNotFoundException e2) {
            return false;
        } catch (Throwable th) {
            AFLogger.afErrorLog(th.getMessage(), th);
            return false;
        }
    }

    @Override // com.appsflyer.p.b
    /* renamed from: ˎ, reason: contains not printable characters */
    public final Class<?> mo170(String str) throws ClassNotFoundException {
        return Class.forName(str);
    }
}
