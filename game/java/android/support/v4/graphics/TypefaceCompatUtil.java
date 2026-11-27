package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static File getTempFile(Context context) {
        String prefix = CACHE_FILE_PREFIX + Process.myPid() + "-" + Process.myTid() + "-";
        for (int i = 0; i < 100; i++) {
            File file = new File(context.getCacheDir(), prefix + i);
            if (file.createNewFile()) {
                return file;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[Catch: IOException -> 0x0024, SYNTHETIC, TRY_ENTER, TryCatch #3 {IOException -> 0x0024, blocks: (B:3:0x0001, B:13:0x001b, B:9:0x0027, B:17:0x0020, B:32:0x0035, B:29:0x003e, B:36:0x003a, B:33:0x0038), top: B:2:0x0001, inners: #2, #5 }] */
    @android.support.annotation.RequiresApi(19)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.nio.ByteBuffer mmap(java.io.File r11) {
        /*
            r8 = 0
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch: java.io.IOException -> L24
            r7.<init>(r11)     // Catch: java.io.IOException -> L24
            r9 = 0
            java.nio.channels.FileChannel r0 = r7.getChannel()     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L42
            long r4 = r0.size()     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L42
            java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L42
            r2 = 0
            java.nio.MappedByteBuffer r1 = r0.map(r1, r2, r4)     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L42
            if (r7 == 0) goto L1e
            if (r8 == 0) goto L27
            r7.close()     // Catch: java.lang.Throwable -> L1f java.io.IOException -> L24
        L1e:
            return r1
        L1f:
            r2 = move-exception
            r9.addSuppressed(r2)     // Catch: java.io.IOException -> L24
            goto L1e
        L24:
            r6 = move-exception
            r1 = r8
            goto L1e
        L27:
            r7.close()     // Catch: java.io.IOException -> L24
            goto L1e
        L2b:
            r1 = move-exception
            throw r1     // Catch: java.lang.Throwable -> L2d
        L2d:
            r2 = move-exception
            r10 = r2
            r2 = r1
            r1 = r10
        L31:
            if (r7 == 0) goto L38
            if (r2 == 0) goto L3e
            r7.close()     // Catch: java.io.IOException -> L24 java.lang.Throwable -> L39
        L38:
            throw r1     // Catch: java.io.IOException -> L24
        L39:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: java.io.IOException -> L24
            goto L38
        L3e:
            r7.close()     // Catch: java.io.IOException -> L24
            goto L38
        L42:
            r1 = move-exception
            r2 = r8
            goto L31
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(java.io.File):java.nio.ByteBuffer");
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:49:? A[Catch: Throwable -> 0x0039, all -> 0x004e, SYNTHETIC, TRY_ENTER, TryCatch #8 {all -> 0x004e, blocks: (B:6:0x000b, B:14:0x0029, B:12:0x004a, B:17:0x0035, B:43:0x0064, B:40:0x006d, B:47:0x0069, B:44:0x0067), top: B:5:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:65:? A[Catch: IOException -> 0x0047, SYNTHETIC, TRY_ENTER, TRY_LEAVE, TryCatch #1 {IOException -> 0x0047, blocks: (B:3:0x0004, B:24:0x0030, B:20:0x0056, B:28:0x0052, B:59:0x0043, B:56:0x0076, B:63:0x0072, B:60:0x0046), top: B:2:0x0004, inners: #2, #3 }] */
    @android.support.annotation.RequiresApi(19)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.nio.ByteBuffer mmap(android.content.Context r13, android.os.CancellationSignal r14, android.net.Uri r15) {
        /*
            android.content.ContentResolver r9 = r13.getContentResolver()
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r8 = r9.openFileDescriptor(r15, r1, r14)     // Catch: java.io.IOException -> L47
            r11 = 0
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            java.io.FileDescriptor r1 = r8.getFileDescriptor()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            r7.<init>(r1)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            r10 = 0
            java.nio.channels.FileChannel r0 = r7.getChannel()     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L7a
            long r4 = r0.size()     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L7a
            java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L7a
            r2 = 0
            java.nio.MappedByteBuffer r1 = r0.map(r1, r2, r4)     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L7a
            if (r7 == 0) goto L2c
            if (r10 == 0) goto L4a
            r7.close()     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L4e
        L2c:
            if (r8 == 0) goto L33
            if (r11 == 0) goto L56
            r8.close()     // Catch: java.io.IOException -> L47 java.lang.Throwable -> L51
        L33:
            return r1
        L34:
            r2 = move-exception
            r10.addSuppressed(r2)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            goto L2c
        L39:
            r1 = move-exception
            throw r1     // Catch: java.lang.Throwable -> L3b
        L3b:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L3f:
            if (r8 == 0) goto L46
            if (r2 == 0) goto L76
            r8.close()     // Catch: java.io.IOException -> L47 java.lang.Throwable -> L71
        L46:
            throw r1     // Catch: java.io.IOException -> L47
        L47:
            r6 = move-exception
            r1 = 0
            goto L33
        L4a:
            r7.close()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            goto L2c
        L4e:
            r1 = move-exception
            r2 = r11
            goto L3f
        L51:
            r2 = move-exception
            r11.addSuppressed(r2)     // Catch: java.io.IOException -> L47
            goto L33
        L56:
            r8.close()     // Catch: java.io.IOException -> L47
            goto L33
        L5a:
            r1 = move-exception
            throw r1     // Catch: java.lang.Throwable -> L5c
        L5c:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L60:
            if (r7 == 0) goto L67
            if (r2 == 0) goto L6d
            r7.close()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L68
        L67:
            throw r1     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
        L68:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            goto L67
        L6d:
            r7.close()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L4e
            goto L67
        L71:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: java.io.IOException -> L47
            goto L46
        L76:
            r8.close()     // Catch: java.io.IOException -> L47
            goto L46
        L7a:
            r1 = move-exception
            r2 = r10
            goto L60
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources res, int id) {
        ByteBuffer byteBuffer = null;
        File tmpFile = getTempFile(context);
        if (tmpFile != null) {
            try {
                if (copyToFile(tmpFile, res, id)) {
                    byteBuffer = mmap(tmpFile);
                }
            } finally {
                tmpFile.delete();
            }
        }
        return byteBuffer;
    }

    public static boolean copyToFile(File file, InputStream is) {
        FileOutputStream os;
        boolean z = false;
        FileOutputStream os2 = null;
        try {
            try {
                os = new FileOutputStream(file, false);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int readLen = is.read(buffer);
                if (readLen == -1) {
                    break;
                }
                os.write(buffer, 0, readLen);
            }
            z = true;
            closeQuietly(os);
            os2 = os;
        } catch (IOException e2) {
            e = e2;
            os2 = os;
            Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
            closeQuietly(os2);
            return z;
        } catch (Throwable th2) {
            th = th2;
            os2 = os;
            closeQuietly(os2);
            throw th;
        }
        return z;
    }

    public static boolean copyToFile(File file, Resources res, int id) {
        InputStream is = null;
        try {
            is = res.openRawResource(id);
            return copyToFile(file, is);
        } finally {
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
