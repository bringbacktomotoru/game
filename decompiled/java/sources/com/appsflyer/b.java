package com.appsflyer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

/* loaded from: classes.dex */
final class b {

    /* renamed from: ॱ, reason: contains not printable characters */
    private IntentFilter f98 = new IntentFilter("android.intent.action.BATTERY_CHANGED");

    static final class a {

        /* renamed from: ˏ, reason: contains not printable characters */
        static final b f99 = new b();
    }

    b() {
    }

    @NonNull
    /* renamed from: ˊ, reason: contains not printable characters */
    final c m95(Context context) {
        String str = null;
        float f = 0.0f;
        try {
            Intent registerReceiver = context.registerReceiver(null, this.f98);
            if (registerReceiver != null) {
                if (2 == registerReceiver.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1)) {
                    switch (registerReceiver.getIntExtra("plugged", -1)) {
                        case 1:
                            str = "ac";
                            break;
                        case 2:
                            str = "usb";
                            break;
                        case 3:
                        default:
                            str = "other";
                            break;
                        case 4:
                            str = "wireless";
                            break;
                    }
                } else {
                    str = "no";
                }
                int intExtra = registerReceiver.getIntExtra("level", -1);
                int intExtra2 = registerReceiver.getIntExtra("scale", -1);
                if (-1 != intExtra && -1 != intExtra2) {
                    f = (100.0f * intExtra) / intExtra2;
                }
            }
        } catch (Throwable th) {
        }
        return new c(f, str);
    }

    static final class c {

        /* renamed from: ˎ, reason: contains not printable characters */
        private final String f100;

        /* renamed from: ˏ, reason: contains not printable characters */
        private final float f101;

        c(float f, String str) {
            this.f101 = f;
            this.f100 = str;
        }

        /* renamed from: ˊ, reason: contains not printable characters */
        final float m96() {
            return this.f101;
        }

        /* renamed from: ˋ, reason: contains not printable characters */
        final String m97() {
            return this.f100;
        }

        c() {
        }
    }
}
