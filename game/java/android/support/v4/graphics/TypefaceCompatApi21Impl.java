package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.view.MotionEventCompat;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

@RequiresApi(MotionEventCompat.AXIS_WHEEL)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String TAG = "TypefaceCompatApi21Impl";

    TypefaceCompatApi21Impl() {
    }

    private File getFile(ParcelFileDescriptor fd) {
        try {
            String path = Os.readlink("/proc/self/fd/" + fd.getFd());
            if (OsConstants.S_ISREG(Os.stat(path).st_mode)) {
                return new File(path);
            }
            return null;
        } catch (ErrnoException e) {
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:77:? A[Catch: IOException -> 0x0047, SYNTHETIC, TRY_ENTER, TRY_LEAVE, TryCatch #3 {IOException -> 0x0047, blocks: (B:7:0x000e, B:20:0x0084, B:17:0x008f, B:24:0x008a, B:44:0x003e, B:41:0x0064, B:48:0x0043, B:71:0x0059, B:68:0x0099, B:75:0x0095, B:72:0x005c), top: B:6:0x000e, inners: #6, #8, #9 }] */
    @Override // android.support.v4.graphics.TypefaceCompatBaseImpl, android.support.v4.graphics.TypefaceCompat.TypefaceCompatImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r12, android.os.CancellationSignal r13, @android.support.annotation.NonNull android.support.v4.provider.FontsContractCompat.FontInfo[] r14, int r15) {
        /*
            r11 = this;
            int r6 = r14.length
            r7 = 1
            if (r6 >= r7) goto L6
            r6 = 0
        L5:
            return r6
        L6:
            android.support.v4.provider.FontsContractCompat$FontInfo r0 = r11.findBestInfo(r14, r15)
            android.content.ContentResolver r5 = r12.getContentResolver()
            android.net.Uri r6 = r0.getUri()     // Catch: java.io.IOException -> L47
            java.lang.String r7 = "r"
            android.os.ParcelFileDescriptor r4 = r5.openFileDescriptor(r6, r7, r13)     // Catch: java.io.IOException -> L47
            r8 = 0
            java.io.File r2 = r11.getFile(r4)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            if (r2 == 0) goto L25
            boolean r6 = r2.canRead()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            if (r6 != 0) goto L7c
        L25:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            java.io.FileDescriptor r6 = r4.getFileDescriptor()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            r3.<init>(r6)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            r7 = 0
            android.graphics.Typeface r6 = super.createFromInputStream(r12, r3)     // Catch: java.lang.Throwable -> L68 java.lang.Throwable -> L6a
            if (r3 == 0) goto L3a
            if (r7 == 0) goto L5d
            r3.close()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L61
        L3a:
            if (r4 == 0) goto L5
            if (r8 == 0) goto L64
            r4.close()     // Catch: java.lang.Throwable -> L42 java.io.IOException -> L47
            goto L5
        L42:
            r7 = move-exception
            r8.addSuppressed(r7)     // Catch: java.io.IOException -> L47
            goto L5
        L47:
            r1 = move-exception
            r6 = 0
            goto L5
        L4a:
            r9 = move-exception
            r7.addSuppressed(r9)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            goto L3a
        L4f:
            r6 = move-exception
            throw r6     // Catch: java.lang.Throwable -> L51
        L51:
            r7 = move-exception
            r10 = r7
            r7 = r6
            r6 = r10
        L55:
            if (r4 == 0) goto L5c
            if (r7 == 0) goto L99
            r4.close()     // Catch: java.io.IOException -> L47 java.lang.Throwable -> L94
        L5c:
            throw r6     // Catch: java.io.IOException -> L47
        L5d:
            r3.close()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            goto L3a
        L61:
            r6 = move-exception
            r7 = r8
            goto L55
        L64:
            r4.close()     // Catch: java.io.IOException -> L47
            goto L5
        L68:
            r7 = move-exception
            throw r7     // Catch: java.lang.Throwable -> L6a
        L6a:
            r6 = move-exception
            if (r3 == 0) goto L72
            if (r7 == 0) goto L78
            r3.close()     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L73
        L72:
            throw r6     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
        L73:
            r9 = move-exception
            r7.addSuppressed(r9)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            goto L72
        L78:
            r3.close()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            goto L72
        L7c:
            android.graphics.Typeface r6 = android.graphics.Typeface.createFromFile(r2)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L61
            if (r4 == 0) goto L5
            if (r8 == 0) goto L8f
            r4.close()     // Catch: java.io.IOException -> L47 java.lang.Throwable -> L89
            goto L5
        L89:
            r7 = move-exception
            r8.addSuppressed(r7)     // Catch: java.io.IOException -> L47
            goto L5
        L8f:
            r4.close()     // Catch: java.io.IOException -> L47
            goto L5
        L94:
            r8 = move-exception
            r7.addSuppressed(r8)     // Catch: java.io.IOException -> L47
            goto L5c
        L99:
            r4.close()     // Catch: java.io.IOException -> L47
            goto L5c
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }
}
