package com.appsflyer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class f {

    /* renamed from: ʻ, reason: contains not printable characters */
    private static volatile f f123;

    /* renamed from: ˋ, reason: contains not printable characters */
    boolean f128;

    /* renamed from: ˋॱ, reason: contains not printable characters */
    private boolean f129;

    /* renamed from: ˎ, reason: contains not printable characters */
    final Handler f130;

    /* renamed from: ͺ, reason: contains not printable characters */
    private final SensorManager f132;

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private static final BitSet f125 = new BitSet(6);

    /* renamed from: ʼ, reason: contains not printable characters */
    private static final Handler f124 = new Handler(Looper.getMainLooper());

    /* renamed from: ˏ, reason: contains not printable characters */
    final Object f131 = new Object();

    /* renamed from: ʽ, reason: contains not printable characters */
    private final Map<i, i> f126 = new HashMap(f125.size());

    /* renamed from: ॱˊ, reason: contains not printable characters */
    private final Map<i, Map<String, Object>> f134 = new HashMap(f125.size());

    /* renamed from: ॱ, reason: contains not printable characters */
    final Runnable f133 = new AnonymousClass1();

    /* renamed from: ˊ, reason: contains not printable characters */
    final Runnable f127 = new AnonymousClass4();

    /* renamed from: ᐝ, reason: contains not printable characters */
    final Runnable f135 = new Runnable() { // from class: com.appsflyer.f.5
        @Override // java.lang.Runnable
        public final void run() {
            synchronized (f.this.f131) {
                if (f.this.f128) {
                    f.this.f130.removeCallbacks(f.this.f127);
                    f.this.f130.removeCallbacks(f.this.f133);
                    f.this.m122();
                    f.this.f128 = false;
                }
            }
        }
    };

    static {
        f125.set(1);
        f125.set(2);
        f125.set(4);
    }

    /* renamed from: com.appsflyer.f$1, reason: invalid class name */
    final class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (f.this.f131) {
                f.this.m122();
                f.this.f130.postDelayed(f.this.f127, 1800000L);
            }
        }

        AnonymousClass1() {
        }

        /* renamed from: ˋ, reason: contains not printable characters */
        static boolean m124(Context context, String str) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(context, str);
            AFLogger.afRDLog(new StringBuilder("is Permission Available: ").append(str).append("; res: ").append(checkSelfPermission).toString());
            return checkSelfPermission == 0;
        }
    }

    /* renamed from: com.appsflyer.f$4, reason: invalid class name */
    final class AnonymousClass4 implements Runnable {

        /* renamed from: ˊ, reason: contains not printable characters */
        private static String f137;

        /* renamed from: ॱ, reason: contains not printable characters */
        private static String f138;

        AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (f.this.f131) {
                f.this.m123();
                f.this.f130.postDelayed(f.this.f133, 500L);
                f.this.f128 = true;
            }
        }

        AnonymousClass4() {
        }

        /* renamed from: ˊ, reason: contains not printable characters */
        static void m125(String str) {
            f137 = str;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                if (i == 0 || i == str.length() - 1) {
                    sb.append(str.charAt(i));
                } else {
                    sb.append("*");
                }
            }
            f138 = sb.toString();
        }

        /* renamed from: ॱ, reason: contains not printable characters */
        static void m126(String str) {
            if (f137 == null) {
                m125(AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.AF_KEY));
            }
            if (f137 != null && str.contains(f137)) {
                AFLogger.afInfoLog(str.replace(f137, f138));
            }
        }
    }

    private f(@NonNull SensorManager sensorManager, Handler handler) {
        this.f132 = sensorManager;
        this.f130 = handler;
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    static f m119(Context context) {
        return m120((SensorManager) context.getApplicationContext().getSystemService("sensor"), f124);
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    private static f m120(SensorManager sensorManager, Handler handler) {
        if (f123 == null) {
            synchronized (f.class) {
                if (f123 == null) {
                    f123 = new f(sensorManager, handler);
                }
            }
        }
        return f123;
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m123() {
        try {
            for (Sensor sensor : this.f132.getSensorList(-1)) {
                int type = sensor.getType();
                if (type >= 0 && f125.get(type)) {
                    i m140 = i.m140(sensor);
                    if (!this.f126.containsKey(m140)) {
                        this.f126.put(m140, m140);
                    }
                    this.f132.registerListener(this.f126.get(m140), sensor, 0);
                }
            }
        } catch (Throwable th) {
        }
        this.f129 = true;
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    final void m122() {
        try {
            if (!this.f126.isEmpty()) {
                for (i iVar : this.f126.values()) {
                    this.f132.unregisterListener(iVar);
                    iVar.m146(this.f134);
                }
            }
        } catch (Throwable th) {
        }
        this.f129 = false;
    }

    @NonNull
    /* renamed from: ˎ, reason: contains not printable characters */
    final List<Map<String, Object>> m121() {
        List<Map<String, Object>> emptyList;
        synchronized (this.f131) {
            if (!this.f126.isEmpty() && this.f129) {
                Iterator<i> it = this.f126.values().iterator();
                while (it.hasNext()) {
                    it.next().m145(this.f134);
                }
            }
            emptyList = this.f134.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.f134.values());
        }
        return emptyList;
    }
}
