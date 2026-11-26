package com.appsflyer;

/* loaded from: classes.dex */
final class o {

    /* renamed from: ˊ, reason: contains not printable characters */
    private boolean f186;

    /* renamed from: ˋ, reason: contains not printable characters */
    private a f187;

    /* renamed from: ˎ, reason: contains not printable characters */
    private String f188;

    o(a aVar, String str, boolean z) {
        this.f187 = aVar;
        this.f188 = str;
        this.f186 = z;
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    final String m167() {
        return this.f188;
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final boolean m168() {
        return this.f186;
    }

    public final String toString() {
        return String.format("%s,%s", this.f188, Boolean.valueOf(this.f186));
    }

    enum a {
        GOOGLE(0),
        AMAZON(1);


        /* renamed from: ॱ, reason: contains not printable characters */
        private int f192;

        a(int i) {
            this.f192 = i;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return String.valueOf(this.f192);
        }
    }
}
