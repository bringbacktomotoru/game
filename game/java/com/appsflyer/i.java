package com.appsflyer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class i implements SensorEventListener {

    /* renamed from: ʽ, reason: contains not printable characters */
    private double f150;

    /* renamed from: ˋ, reason: contains not printable characters */
    @NonNull
    private final String f152;

    /* renamed from: ˎ, reason: contains not printable characters */
    @NonNull
    private final String f153;

    /* renamed from: ˏ, reason: contains not printable characters */
    private final int f154;

    /* renamed from: ॱॱ, reason: contains not printable characters */
    private long f156;

    /* renamed from: ᐝ, reason: contains not printable characters */
    private final int f157;

    /* renamed from: ॱ, reason: contains not printable characters */
    private final float[][] f155 = new float[2][];

    /* renamed from: ˊ, reason: contains not printable characters */
    private final long[] f151 = new long[2];

    private i(int i, @Nullable String str, @Nullable String str2) {
        this.f154 = i;
        this.f153 = str == null ? "" : str;
        this.f152 = str2 == null ? "" : str2;
        this.f157 = ((this.f153.hashCode() + ((i + 31) * 31)) * 31) + this.f152.hashCode();
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    static i m140(Sensor sensor) {
        return new i(sensor.getType(), sensor.getName(), sensor.getVendor());
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private static double m143(@NonNull float[] fArr, @NonNull float[] fArr2) {
        int min = Math.min(fArr.length, fArr2.length);
        double d = 0.0d;
        for (int i = 0; i < min; i++) {
            d += StrictMath.pow(fArr[i] - fArr2[i], 2.0d);
        }
        return Math.sqrt(d);
    }

    @NonNull
    /* renamed from: ˊ, reason: contains not printable characters */
    private static List<Float> m138(@NonNull float[] fArr) {
        ArrayList arrayList = new ArrayList(fArr.length);
        for (float f : fArr) {
            arrayList.add(Float.valueOf(f));
        }
        return arrayList;
    }

    @Override // android.hardware.SensorEventListener
    public final void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent != null && sensorEvent.values != null) {
            Sensor sensor = sensorEvent.sensor;
            if ((sensor == null || sensor.getName() == null || sensor.getVendor() == null) ? false : true) {
                int type = sensorEvent.sensor.getType();
                String name = sensorEvent.sensor.getName();
                String vendor = sensorEvent.sensor.getVendor();
                long j = sensorEvent.timestamp;
                float[] fArr = sensorEvent.values;
                if (m144(type, name, vendor)) {
                    long currentTimeMillis = System.currentTimeMillis();
                    float[] fArr2 = this.f155[0];
                    if (fArr2 == null) {
                        this.f155[0] = Arrays.copyOf(fArr, fArr.length);
                        this.f151[0] = currentTimeMillis;
                        return;
                    }
                    float[] fArr3 = this.f155[1];
                    if (fArr3 == null) {
                        float[] copyOf = Arrays.copyOf(fArr, fArr.length);
                        this.f155[1] = copyOf;
                        this.f151[1] = currentTimeMillis;
                        this.f150 = m143(fArr2, copyOf);
                        return;
                    }
                    if (50000000 <= j - this.f156) {
                        this.f156 = j;
                        if (Arrays.equals(fArr3, fArr)) {
                            this.f151[1] = currentTimeMillis;
                            return;
                        }
                        double m143 = m143(fArr2, fArr);
                        if (m143 <= this.f150) {
                            return;
                        }
                        this.f155[1] = Arrays.copyOf(fArr, fArr.length);
                        this.f151[1] = currentTimeMillis;
                        this.f150 = m143;
                    }
                }
            }
        }
    }

    @Override // android.hardware.SensorEventListener
    public final void onAccuracyChanged(Sensor sensor, int i) {
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m146(@NonNull Map<i, Map<String, Object>> map) {
        m142(map, true);
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    public final void m145(Map<i, Map<String, Object>> map) {
        m142(map, false);
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private boolean m144(int i, @NonNull String str, @NonNull String str2) {
        return this.f154 == i && this.f153.equals(str) && this.f152.equals(str2);
    }

    @NonNull
    /* renamed from: ˎ, reason: contains not printable characters */
    private Map<String, Object> m141() {
        HashMap hashMap = new HashMap(7);
        hashMap.put("sT", Integer.valueOf(this.f154));
        hashMap.put("sN", this.f153);
        hashMap.put("sV", this.f152);
        float[] fArr = this.f155[0];
        if (fArr != null) {
            hashMap.put("sVS", m138(fArr));
        }
        float[] fArr2 = this.f155[1];
        if (fArr2 != null) {
            hashMap.put("sVE", m138(fArr2));
        }
        return hashMap;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private void m139() {
        for (int i = 0; i < 2; i++) {
            this.f155[i] = null;
        }
        for (int i2 = 0; i2 < 2; i2++) {
            this.f151[i2] = 0;
        }
        this.f150 = 0.0d;
        this.f156 = 0L;
    }

    public final int hashCode() {
        return this.f157;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            return m144(iVar.f154, iVar.f153, iVar.f152);
        }
        return false;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private void m142(@NonNull Map<i, Map<String, Object>> map, boolean z) {
        if (this.f155[0] != null) {
            map.put(this, m141());
            if (z) {
                m139();
                return;
            }
            return;
        }
        if (!map.containsKey(this)) {
            map.put(this, m141());
        }
    }
}
