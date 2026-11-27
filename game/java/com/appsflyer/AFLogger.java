package com.appsflyer;

import android.support.annotation.NonNull;
import android.util.Log;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class AFLogger {

    /* renamed from: ˏ, reason: contains not printable characters */
    private static long f12 = System.currentTimeMillis();

    public enum LogLevel {
        NONE(0),
        ERROR(1),
        WARNING(2),
        INFO(3),
        DEBUG(4),
        VERBOSE(5);


        /* renamed from: ˊ, reason: contains not printable characters */
        private int f14;

        LogLevel(int i) {
            this.f14 = i;
        }

        public final int getLevel() {
            return this.f14;
        }
    }

    public static void afInfoLog(String str, boolean z) {
        if (LogLevel.INFO.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) {
            Log.i(AppsFlyerLib.LOG_TAG, m16(str, false));
        }
        if (z) {
            s.m186().m192("I", m16(str, true));
        }
    }

    public static void resetDeltaTime() {
        f12 = System.currentTimeMillis();
    }

    @NonNull
    /* renamed from: ˎ, reason: contains not printable characters */
    private static String m16(String str, boolean z) {
        if (z || LogLevel.VERBOSE.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) {
            return new StringBuilder("(").append(m15(System.currentTimeMillis() - f12)).append(") [").append(Thread.currentThread().getName()).append("] ").append(str).toString();
        }
        return str;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static void m18(String str, Throwable th, boolean z) {
        if ((LogLevel.ERROR.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) && z) {
            Log.e(AppsFlyerLib.LOG_TAG, m16(str, false), th);
        }
        s.m186().m197(th);
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    static void m17(String str) {
        if (LogLevel.WARNING.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) {
            Log.w(AppsFlyerLib.LOG_TAG, m16(str, false));
        }
        s.m186().m192("W", m16(str, true));
    }

    public static void afRDLog(String str) {
        if (LogLevel.VERBOSE.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) {
            Log.v(AppsFlyerLib.LOG_TAG, m16(str, false));
        }
        s.m186().m192("V", m16(str, true));
    }

    public static void afInfoLog(String str) {
        afInfoLog(str, true);
    }

    public static void afErrorLog(String str, Throwable th) {
        m18(str, th, false);
    }

    public static void afErrorLog(String str, Throwable th, boolean z) {
        m18(str, th, z);
    }

    public static void afWarnLog(String str) {
        m17(str);
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private static String m15(long j) {
        long hours = TimeUnit.MILLISECONDS.toHours(j);
        long millis = j - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long millis2 = millis - TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis2);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds), Long.valueOf(TimeUnit.MILLISECONDS.toMillis(millis2 - TimeUnit.SECONDS.toMillis(seconds))));
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    static void m19(String str) {
        if (!AppsFlyerProperties.getInstance().isLogsDisabledCompletely()) {
            Log.d(AppsFlyerLib.LOG_TAG, m16(str, false));
        }
        s.m186().m192("F", str);
    }

    public static void afDebugLog(String str) {
        if (LogLevel.DEBUG.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) {
            Log.d(AppsFlyerLib.LOG_TAG, m16(str, false));
        }
        s.m186().m192("D", m16(str, true));
    }
}
