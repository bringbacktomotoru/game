package com.appsflyer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.io.File;
import java.lang.ref.WeakReference;
import java.security.SecureRandom;
import java.util.UUID;

/* loaded from: classes.dex */
final class t {

    /* renamed from: ˋ, reason: contains not printable characters */
    private static String f255 = null;

    t() {
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    public static synchronized String m210(WeakReference<Context> weakReference) {
        String str;
        String obj;
        synchronized (t.class) {
            if (weakReference.get() == null) {
                str = f255;
            } else {
                if (f255 == null) {
                    String string = weakReference.get() != null ? weakReference.get().getSharedPreferences("appsflyer-data", 0).getString("AF_INSTALLATION", null) : null;
                    if (string != null) {
                        f255 = string;
                    } else {
                        try {
                            File file = new File(weakReference.get().getFilesDir(), "AF_INSTALLATION");
                            if (file.exists()) {
                                f255 = m209(file);
                                file.delete();
                            } else {
                                if (Build.VERSION.SDK_INT >= 9) {
                                    obj = new StringBuilder().append(System.currentTimeMillis()).append("-").append(Math.abs(new SecureRandom().nextLong())).toString();
                                } else {
                                    obj = UUID.randomUUID().toString();
                                }
                                f255 = obj;
                            }
                            String str2 = f255;
                            SharedPreferences.Editor edit = weakReference.get().getSharedPreferences("appsflyer-data", 0).edit();
                            edit.putString("AF_INSTALLATION", str2);
                            if (Build.VERSION.SDK_INT >= 9) {
                                edit.apply();
                            } else {
                                edit.commit();
                            }
                        } catch (Exception e) {
                            AFLogger.afErrorLog("Error getting AF unique ID", e);
                        }
                    }
                    if (f255 != null) {
                        AppsFlyerProperties.getInstance().set("uid", f255);
                    }
                }
                str = f255;
            }
        }
        return str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0042 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v9, types: [java.lang.String] */
    /* renamed from: ˊ, reason: contains not printable characters */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String m209(java.io.File r7) {
        /*
            r1 = 0
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch: java.io.IOException -> L27 java.lang.Throwable -> L3e
            java.lang.String r0 = "r"
            r2.<init>(r7, r0)     // Catch: java.io.IOException -> L27 java.lang.Throwable -> L3e
            long r4 = r2.length()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L53
            int r0 = (int) r4     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L53
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L53
            r2.readFully(r0)     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L58
            r2.close()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L58
            r2.close()     // Catch: java.io.IOException -> L20
        L18:
            java.lang.String r1 = new java.lang.String
            if (r0 == 0) goto L4d
        L1c:
            r1.<init>(r0)
            return r1
        L20:
            r1 = move-exception
            java.lang.String r2 = "Exception while trying to close the InstallationFile"
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L18
        L27:
            r0 = move-exception
            r2 = r1
            r6 = r1
            r1 = r0
            r0 = r6
        L2c:
            java.lang.String r3 = "Exception while reading InstallationFile: "
            com.appsflyer.AFLogger.afErrorLog(r3, r1)     // Catch: java.lang.Throwable -> L51
            if (r2 == 0) goto L18
            r2.close()     // Catch: java.io.IOException -> L37
            goto L18
        L37:
            r1 = move-exception
            java.lang.String r2 = "Exception while trying to close the InstallationFile"
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L18
        L3e:
            r0 = move-exception
            r2 = r1
        L40:
            if (r2 == 0) goto L45
            r2.close()     // Catch: java.io.IOException -> L46
        L45:
            throw r0
        L46:
            r1 = move-exception
            java.lang.String r2 = "Exception while trying to close the InstallationFile"
            com.appsflyer.AFLogger.afErrorLog(r2, r1)
            goto L45
        L4d:
            r0 = 0
            byte[] r0 = new byte[r0]
            goto L1c
        L51:
            r0 = move-exception
            goto L40
        L53:
            r0 = move-exception
            r6 = r0
            r0 = r1
            r1 = r6
            goto L2c
        L58:
            r1 = move-exception
            goto L2c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.t.m209(java.io.File):java.lang.String");
    }
}
