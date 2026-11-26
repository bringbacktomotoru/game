package com.appsflyer;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes.dex */
class AFKeystoreWrapper {

    /* renamed from: ˋ, reason: contains not printable characters */
    private KeyStore f8;

    /* renamed from: ˏ, reason: contains not printable characters */
    private Context f10;

    /* renamed from: ॱ, reason: contains not printable characters */
    private final Object f11 = new Object();

    /* renamed from: ˎ, reason: contains not printable characters */
    private String f9 = "";

    /* renamed from: ˊ, reason: contains not printable characters */
    private int f7 = 0;

    public AFKeystoreWrapper(Context context) {
        this.f10 = context;
        AFLogger.afInfoLog("Initialising KeyStore..");
        try {
            this.f8 = KeyStore.getInstance("AndroidKeyStore");
            this.f8.load(null);
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            AFLogger.afErrorLog("Couldn't load keystore instance of type: AndroidKeyStore", e);
        }
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    final void m10(String str) {
        this.f9 = str;
        this.f7 = 0;
        m8(m9());
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    final void m13() {
        String m9 = m9();
        synchronized (this.f11) {
            this.f7++;
            AFLogger.afInfoLog("Deleting key with alias: ".concat(String.valueOf(m9)));
            try {
                synchronized (this.f11) {
                    this.f8.deleteEntry(m9);
                }
            } catch (KeyStoreException e) {
                AFLogger.afErrorLog(new StringBuilder("Exception ").append(e.getMessage()).append(" occurred").toString(), e);
            }
        }
        m8(m9());
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0026, code lost:
    
        r4 = r0.split(",");
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x002e, code lost:
    
        if (r4.length != 3) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0030, code lost:
    
        com.appsflyer.AFLogger.afInfoLog("Found a matching AF key with alias:\n".concat(java.lang.String.valueOf(r0)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x003e, code lost:
    
        r0 = r4[1].trim().split("=");
        r2 = r4[2].trim().split("=");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0058, code lost:
    
        if (r0.length != 2) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x005b, code lost:
    
        if (r2.length != 2) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x005d, code lost:
    
        r8.f9 = r0[1].trim();
        r8.f7 = java.lang.Integer.parseInt(r2[1].trim());
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0097, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0079, code lost:
    
        com.appsflyer.AFLogger.afErrorLog(new java.lang.StringBuilder("Couldn't list KeyStore Aliases: ").append(r0.getClass().getName()).toString(), r0);
     */
    /* renamed from: ˊ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final boolean m11() {
        /*
            r8 = this;
            r7 = 2
            r1 = 1
            r2 = 0
            java.lang.Object r3 = r8.f11
            monitor-enter(r3)
            java.security.KeyStore r0 = r8.f8     // Catch: java.lang.Throwable -> L94
            if (r0 == 0) goto L99
            java.security.KeyStore r0 = r8.f8     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            java.util.Enumeration r4 = r0.aliases()     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
        L10:
            boolean r0 = r4.hasMoreElements()     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            if (r0 == 0) goto L75
            java.lang.Object r0 = r4.nextElement()     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            if (r0 == 0) goto L10
            java.lang.String r5 = "com.appsflyer"
            boolean r5 = r0.startsWith(r5)     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            if (r5 == 0) goto L10
            java.lang.String r4 = ","
            java.lang.String[] r4 = r0.split(r4)     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            int r5 = r4.length     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            r6 = 3
            if (r5 != r6) goto L75
            java.lang.String r5 = "Found a matching AF key with alias:\n"
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            java.lang.String r0 = r5.concat(r0)     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            com.appsflyer.AFLogger.afInfoLog(r0)     // Catch: java.lang.Throwable -> L77 java.lang.Throwable -> L94
            r0 = 1
            r0 = r4[r0]     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            java.lang.String r2 = "="
            java.lang.String[] r0 = r0.split(r2)     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            r2 = 2
            r2 = r4[r2]     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            java.lang.String r2 = r2.trim()     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            java.lang.String r4 = "="
            java.lang.String[] r2 = r2.split(r4)     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            int r4 = r0.length     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            if (r4 != r7) goto L73
            int r4 = r2.length     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            if (r4 != r7) goto L73
            r4 = 1
            r0 = r0[r4]     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            r8.f9 = r0     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            r0 = 1
            r0 = r2[r0]     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
            r8.f7 = r0     // Catch: java.lang.Throwable -> L94 java.lang.Throwable -> L97
        L73:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L94
            return r1
        L75:
            r1 = r2
            goto L73
        L77:
            r0 = move-exception
            r1 = r2
        L79:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L94
            java.lang.String r4 = "Couldn't list KeyStore Aliases: "
            r2.<init>(r4)     // Catch: java.lang.Throwable -> L94
            java.lang.Class r4 = r0.getClass()     // Catch: java.lang.Throwable -> L94
            java.lang.String r4 = r4.getName()     // Catch: java.lang.Throwable -> L94
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch: java.lang.Throwable -> L94
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L94
            com.appsflyer.AFLogger.afErrorLog(r2, r0)     // Catch: java.lang.Throwable -> L94
            goto L73
        L94:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        L97:
            r0 = move-exception
            goto L79
        L99:
            r1 = r2
            goto L73
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.AFKeystoreWrapper.m11():boolean");
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private void m8(String str) {
        AFLogger.afInfoLog("Creating a new key with alias: ".concat(String.valueOf(str)));
        try {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.add(1, 5);
            AlgorithmParameterSpec algorithmParameterSpec = null;
            synchronized (this.f11) {
                if (!this.f8.containsAlias(str)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        algorithmParameterSpec = new KeyGenParameterSpec.Builder(str, 3).setCertificateSubject(new X500Principal("CN=AndroidSDK, O=AppsFlyer")).setCertificateSerialNumber(BigInteger.ONE).setCertificateNotBefore(calendar.getTime()).setCertificateNotAfter(calendar2.getTime()).build();
                    } else if (Build.VERSION.SDK_INT >= 18) {
                        if (!("OPPO".equals(Build.BRAND))) {
                            algorithmParameterSpec = new KeyPairGeneratorSpec.Builder(this.f10).setAlias(str).setSubject(new X500Principal("CN=AndroidSDK, O=AppsFlyer")).setSerialNumber(BigInteger.ONE).setStartDate(calendar.getTime()).setEndDate(calendar2.getTime()).build();
                        }
                    }
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    keyPairGenerator.initialize(algorithmParameterSpec);
                    keyPairGenerator.generateKeyPair();
                } else {
                    AFLogger.afInfoLog("Alias already exists: ".concat(String.valueOf(str)));
                }
            }
        } catch (Throwable th) {
            AFLogger.afErrorLog(new StringBuilder("Exception ").append(th.getMessage()).append(" occurred").toString(), th);
        }
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    private String m9() {
        StringBuilder sb = new StringBuilder();
        sb.append("com.appsflyer,");
        synchronized (this.f11) {
            sb.append("KSAppsFlyerId=").append(this.f9).append(",");
            sb.append("KSAppsFlyerRICounter=").append(this.f7);
        }
        return sb.toString();
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    final String m14() {
        String str;
        synchronized (this.f11) {
            str = this.f9;
        }
        return str;
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    final int m12() {
        int i;
        synchronized (this.f11) {
            i = this.f7;
        }
        return i;
    }
}
