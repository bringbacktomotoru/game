package com.appsflyer;

import java.security.MessageDigest;
import java.util.Formatter;

/* renamed from: com.appsflyer.r, reason: case insensitive filesystem */
/* loaded from: classes.dex */
final class C0006r {
    C0006r() {
    }

    /* renamed from: ॱ, reason: contains not printable characters */
    public static String m179(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            return m177(messageDigest.digest());
        } catch (Exception e) {
            AFLogger.afErrorLog(new StringBuilder("Error turning ").append(str.substring(0, 6)).append(".. to SHA1").toString(), e);
            return null;
        }
    }

    /* renamed from: ˏ, reason: contains not printable characters */
    public static String m178(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            return m177(messageDigest.digest());
        } catch (Exception e) {
            AFLogger.afErrorLog(new StringBuilder("Error turning ").append(str.substring(0, 6)).append(".. to MD5").toString(), e);
            return null;
        }
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    public static String m176(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            AFLogger.afErrorLog(new StringBuilder("Error turning ").append(str.substring(0, 6)).append(".. to SHA-256").toString(), e);
            return null;
        }
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private static String m177(byte[] bArr) {
        Formatter formatter = new Formatter();
        for (byte b : bArr) {
            formatter.format("%02x", Byte.valueOf(b));
        }
        String obj = formatter.toString();
        formatter.close();
        return obj;
    }
}
