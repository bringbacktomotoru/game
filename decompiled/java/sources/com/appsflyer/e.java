package com.appsflyer;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.appsflyer.f;

/* loaded from: classes.dex */
final class e {

    static final class d {

        /* renamed from: ˊ, reason: contains not printable characters */
        static final e f122 = new e();
    }

    e() {
    }

    @Nullable
    /* renamed from: ॱ, reason: contains not printable characters */
    static Location m118(@NonNull Context context) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            Location lastKnownLocation = m117(context, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}) ? locationManager.getLastKnownLocation("network") : null;
            Location lastKnownLocation2 = m117(context, new String[]{"android.permission.ACCESS_FINE_LOCATION"}) ? locationManager.getLastKnownLocation("gps") : null;
            if (lastKnownLocation2 == null && lastKnownLocation == null) {
                lastKnownLocation2 = null;
            } else if (lastKnownLocation2 == null && lastKnownLocation != null) {
                lastKnownLocation2 = lastKnownLocation;
            } else if (lastKnownLocation != null || lastKnownLocation2 == null) {
                if (60000 < lastKnownLocation.getTime() - lastKnownLocation2.getTime()) {
                    lastKnownLocation2 = lastKnownLocation;
                }
            }
            if (lastKnownLocation2 != null) {
                return lastKnownLocation2;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private static boolean m117(@NonNull Context context, @NonNull String[] strArr) {
        for (String str : strArr) {
            if (f.AnonymousClass1.m124(context, str)) {
                return true;
            }
        }
        return false;
    }
}
