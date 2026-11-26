package com.appsflyer.cache;

import android.content.Context;
import android.util.Log;
import com.appsflyer.AppsFlyerLib;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CacheManager {
    public static final String AF_CACHE_DIR = "AFRequestCache";
    public static final int CACHE_MAX_SIZE = 40;

    /* renamed from: ˎ, reason: contains not printable characters */
    private static CacheManager f115 = new CacheManager();

    public static CacheManager getInstance() {
        return f115;
    }

    private CacheManager() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v19 */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.io.IOException] */
    /* JADX WARN: Type inference failed for: r0v21 */
    /* JADX WARN: Type inference failed for: r0v22 */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v24 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.io.Writer] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void cacheRequest(com.appsflyer.cache.RequestCacheData r8, android.content.Context r9) {
        /*
            r7 = this;
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.io.File r2 = r9.getFilesDir()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.lang.String r3 = "AFRequestCache"
            r1.<init>(r2, r3)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            boolean r2 = r1.exists()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            if (r2 != 0) goto L16
            r1.mkdir()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
        L15:
            return
        L16:
            java.io.File[] r1 = r1.listFiles()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            if (r1 == 0) goto L39
            int r1 = r1.length     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            r2 = 40
            if (r1 <= r2) goto L39
            java.lang.String r1 = "AppsFlyer_4.8.19"
            java.lang.String r2 = "reached cache limit, not caching request"
            android.util.Log.i(r1, r2)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            goto L15
        L29:
            r1 = move-exception
        L2a:
            java.lang.String r1 = "AppsFlyer_4.8.19"
            java.lang.String r2 = "Could not cache request"
            android.util.Log.i(r1, r2)     // Catch: java.lang.Throwable -> Lb6
            if (r0 == 0) goto L15
            r0.close()     // Catch: java.io.IOException -> L37
            goto L15
        L37:
            r0 = move-exception
            goto L15
        L39:
            java.lang.String r1 = "AppsFlyer_4.8.19"
            java.lang.String r2 = "caching request..."
            android.util.Log.i(r1, r2)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.io.File r2 = new java.io.File     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.io.File r1 = new java.io.File     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.io.File r3 = r9.getFilesDir()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.lang.String r4 = "AFRequestCache"
            r1.<init>(r3, r4)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.lang.String r3 = java.lang.Long.toString(r4)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            r2.<init>(r1, r3)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            r2.createNewFile()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.io.OutputStreamWriter r1 = new java.io.OutputStreamWriter     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.lang.String r2 = r2.getPath()     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            r4 = 1
            r3.<init>(r2, r4)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            r1.<init>(r3)     // Catch: java.lang.Exception -> L29 java.lang.Throwable -> La8
            java.lang.String r0 = "version="
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            java.lang.String r0 = r8.getVersion()     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r0 = 10
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            java.lang.String r0 = "url="
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            java.lang.String r0 = r8.getRequestURL()     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r0 = 10
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            java.lang.String r0 = "data="
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            java.lang.String r0 = r8.getPostData()     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r0 = 10
            r1.write(r0)     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r1.flush()     // Catch: java.lang.Throwable -> Lb4 java.lang.Exception -> Lbb
            r1.close()     // Catch: java.io.IOException -> La5
            goto L15
        La5:
            r0 = move-exception
            goto L15
        La8:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
        Lac:
            if (r1 == 0) goto Lb1
            r1.close()     // Catch: java.io.IOException -> Lb2
        Lb1:
            throw r0
        Lb2:
            r1 = move-exception
            goto Lb1
        Lb4:
            r0 = move-exception
            goto Lac
        Lb6:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto Lac
        Lbb:
            r0 = move-exception
            r0 = r1
            goto L2a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.cache.CacheManager.cacheRequest(com.appsflyer.cache.RequestCacheData, android.content.Context):void");
    }

    public List<RequestCacheData> getCachedRequests(Context context) {
        ArrayList arrayList = new ArrayList();
        try {
            File file = new File(context.getFilesDir(), AF_CACHE_DIR);
            if (!file.exists()) {
                file.mkdir();
            } else {
                File[] listFiles = file.listFiles();
                for (File file2 : listFiles) {
                    Log.i(AppsFlyerLib.LOG_TAG, new StringBuilder("Found cached request").append(file2.getName()).toString());
                    arrayList.add(m115(file2));
                }
            }
        } catch (Exception e) {
            Log.i(AppsFlyerLib.LOG_TAG, "Could not cache request");
        }
        return arrayList;
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static RequestCacheData m115(File file) {
        FileReader fileReader;
        FileReader fileReader2;
        try {
            fileReader = new FileReader(file);
        } catch (Exception e) {
            fileReader2 = null;
        } catch (Throwable th) {
            th = th;
            fileReader = null;
        }
        try {
            char[] cArr = new char[(int) file.length()];
            fileReader.read(cArr);
            RequestCacheData requestCacheData = new RequestCacheData(cArr);
            requestCacheData.setCacheKey(file.getName());
            try {
                fileReader.close();
                return requestCacheData;
            } catch (IOException e2) {
                return requestCacheData;
            }
        } catch (Exception e3) {
            fileReader2 = fileReader;
            if (fileReader2 != null) {
                try {
                    fileReader2.close();
                } catch (IOException e4) {
                }
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }

    public void init(Context context) {
        try {
            if (!new File(context.getFilesDir(), AF_CACHE_DIR).exists()) {
                new File(context.getFilesDir(), AF_CACHE_DIR).mkdir();
            }
        } catch (Exception e) {
            Log.i(AppsFlyerLib.LOG_TAG, "Could not create cache directory");
        }
    }

    public void deleteRequest(String str, Context context) {
        File file = new File(new File(context.getFilesDir(), AF_CACHE_DIR), str);
        Log.i(AppsFlyerLib.LOG_TAG, new StringBuilder("Deleting ").append(str).append(" from cache").toString());
        if (file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                Log.i(AppsFlyerLib.LOG_TAG, new StringBuilder("Could not delete ").append(str).append(" from cache").toString(), e);
            }
        }
    }

    public void clearCache(Context context) {
        try {
            File file = new File(context.getFilesDir(), AF_CACHE_DIR);
            if (!file.exists()) {
                file.mkdir();
                return;
            }
            File[] listFiles = file.listFiles();
            for (File file2 : listFiles) {
                Log.i(AppsFlyerLib.LOG_TAG, new StringBuilder("Found cached request").append(file2.getName()).toString());
                deleteRequest(m115(file2).getCacheKey(), context);
            }
        } catch (Exception e) {
            Log.i(AppsFlyerLib.LOG_TAG, "Could not cache request");
        }
    }
}
