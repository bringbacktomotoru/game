package com.appsflyer.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/* loaded from: classes.dex */
final class c {

    /* renamed from: ˋ, reason: contains not printable characters */
    private String f254;

    c() {
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final void m208(String str) {
        this.f254 = str;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final void m207(Context context) {
        if (this.f254 != null) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.f254)).setFlags(268435456));
        }
    }
}
