package com.appsflyer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
final class g {

    static final class d {

        /* renamed from: ॱ, reason: contains not printable characters */
        static final g f144 = new g();
    }

    g() {
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:0|1|(2:2|3)|(12:5|(2:7|(1:(2:48|49)(3:9|(1:47)(1:13)|(3:16|17|(1:19)(2:43|(1:45)(1:46)))(1:15))))(3:50|(1:78)(1:54)|(1:56)(3:57|(1:77)(1:61)|(1:63)(3:64|(1:76)(1:68)|(2:70|(1:72)(2:73|(1:75))))))|20|21|22|(2:36|37)|24|(4:26|27|28|29)|31|27|28|29)|79|20|21|22|(0)|24|(0)|31|27|28|29|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x004c, code lost:
    
        if (r1.isEmpty() != false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00d0, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00d1, code lost:
    
        r0 = null;
        r1 = r2;
        r2 = r3;
        r3 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0048 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* renamed from: ˏ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static com.appsflyer.g.b m127(@android.support.annotation.NonNull android.content.Context r11) {
        /*
            Method dump skipped, instructions count: 225
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.g.m127(android.content.Context):com.appsflyer.g$b");
    }

    static final class b {

        /* renamed from: ˊ, reason: contains not printable characters */
        private final String f141;

        /* renamed from: ˋ, reason: contains not printable characters */
        private final String f142;

        /* renamed from: ˎ, reason: contains not printable characters */
        private final String f143;

        b(@NonNull String str, @Nullable String str2, @Nullable String str3) {
            this.f142 = str;
            this.f143 = str2;
            this.f141 = str3;
        }

        /* renamed from: ˎ, reason: contains not printable characters */
        final String m131() {
            return this.f142;
        }

        @Nullable
        /* renamed from: ˊ, reason: contains not printable characters */
        final String m129() {
            return this.f143;
        }

        @Nullable
        /* renamed from: ˋ, reason: contains not printable characters */
        final String m130() {
            return this.f141;
        }

        b() {
        }

        /* renamed from: ॱ, reason: contains not printable characters */
        static void m128(Context context) {
            Context applicationContext = context.getApplicationContext();
            AFLogger.afInfoLog("onBecameBackground");
            AppsFlyerLib.getInstance().m79();
            AFLogger.afInfoLog("callStatsBackground background call");
            AppsFlyerLib.getInstance().m75(new WeakReference<>(applicationContext));
            s m186 = s.m186();
            if (m186.m189()) {
                m186.m196();
                if (applicationContext != null) {
                    s.m182(applicationContext.getPackageName(), applicationContext.getPackageManager());
                }
                m186.m193();
            } else {
                AFLogger.afDebugLog("RD status is OFF");
            }
            AFExecutor.getInstance().m6();
        }
    }
}
