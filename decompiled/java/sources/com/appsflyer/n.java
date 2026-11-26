package com.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.lang.ref.WeakReference;
import java.util.concurrent.RejectedExecutionException;

@RequiresApi(api = 14)
/* loaded from: classes.dex */
final class n implements Application.ActivityLifecycleCallbacks {

    /* renamed from: ॱ, reason: contains not printable characters */
    private static n f180;

    /* renamed from: ˏ, reason: contains not printable characters */
    private boolean f183 = false;

    /* renamed from: ˋ, reason: contains not printable characters */
    private boolean f182 = true;

    /* renamed from: ˊ, reason: contains not printable characters */
    private a f181 = null;

    interface a {
        /* renamed from: ˊ */
        void mo81(WeakReference<Context> weakReference);

        /* renamed from: ॱ */
        void mo82(Activity activity);
    }

    n() {
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    static /* synthetic */ boolean m160(n nVar) {
        nVar.f183 = false;
        return false;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    static n m159() {
        if (f180 == null) {
            f180 = new n();
        }
        return f180;
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    public static n m161() {
        if (f180 == null) {
            throw new IllegalStateException("Foreground is not initialised - invoke at least once with parameter init/get");
        }
        return f180;
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    public final void m165(Application application, a aVar) {
        this.f181 = aVar;
        if (Build.VERSION.SDK_INT >= 14) {
            application.registerActivityLifecycleCallbacks(f180);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityResumed(Activity activity) {
        this.f182 = false;
        boolean z = this.f183 ? false : true;
        this.f183 = true;
        if (z) {
            try {
                this.f181.mo82(activity);
            } catch (Exception e) {
                AFLogger.afErrorLog("Listener threw exception! ", e);
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityPaused(Activity activity) {
        this.f182 = true;
        try {
            new b(new WeakReference(activity.getApplicationContext())).executeOnExecutor(AFExecutor.getInstance().getThreadPoolExecutor(), new Void[0]);
        } catch (RejectedExecutionException e) {
            AFLogger.afErrorLog("backgroundTask.executeOnExecutor failed with RejectedExecutionException Exception", e);
        } catch (Throwable th) {
            AFLogger.afErrorLog("backgroundTask.executeOnExecutor failed with Exception", th);
        }
    }

    class b extends AsyncTask<Void, Void, Void> {

        /* renamed from: ˋ, reason: contains not printable characters */
        private WeakReference<Context> f184;

        @Override // android.os.AsyncTask
        protected final /* synthetic */ Void doInBackground(Void[] voidArr) {
            return m166();
        }

        public b(WeakReference<Context> weakReference) {
            this.f184 = weakReference;
        }

        /* renamed from: ˎ, reason: contains not printable characters */
        private Void m166() {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                AFLogger.afErrorLog("Sleeping attempt failed (essential for background state verification)\n", e);
            }
            if (n.this.f183 && n.this.f182) {
                n.m160(n.this);
                try {
                    n.this.f181.mo81(this.f184);
                } catch (Exception e2) {
                    AFLogger.afErrorLog("Listener threw exception! ", e2);
                    cancel(true);
                }
            }
            this.f184.clear();
            return null;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        AFDeepLinkManager.getInstance().collectIntentsFromActivities(activity.getIntent());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
    }
}
