package ls.sakana;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/* loaded from: classes.dex */
public class SakanaApplication extends Application implements Application.ActivityLifecycleCallbacks {
    static int running = 0;

    @Override // android.app.Application
    public void onCreate() {
        registerActivityLifecycleCallbacks(this);
        System.out.println("ls.sakana.SakanaApplication started.");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        synchronized (SakanaApplication.class) {
            running--;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        synchronized (SakanaApplication.class) {
            running--;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    public static boolean isForeground() {
        boolean z;
        synchronized (SakanaApplication.class) {
            z = running > 0;
        }
        return z;
    }
}
