package android.support.customtabs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.customtabs.ICustomTabsCallback;
import android.support.customtabs.IPostMessageService;

/* loaded from: classes.dex */
public abstract class PostMessageServiceConnection implements ServiceConnection {
    private final Object mLock = new Object();
    private IPostMessageService mService;
    private final ICustomTabsCallback mSessionBinder;

    public PostMessageServiceConnection(CustomTabsSessionToken session) {
        this.mSessionBinder = ICustomTabsCallback.Stub.asInterface(session.getCallbackBinder());
    }

    public boolean bindSessionToPostMessageService(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setClassName(packageName, PostMessageService.class.getName());
        return context.bindService(intent, this, 1);
    }

    public void unbindFromContext(Context context) {
        context.unbindService(this);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName name, IBinder service) {
        this.mService = IPostMessageService.Stub.asInterface(service);
        onPostMessageServiceConnected();
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName name) {
        this.mService = null;
        onPostMessageServiceDisconnected();
    }

    public final boolean notifyMessageChannelReady(Bundle extras) {
        boolean z = false;
        if (this.mService != null) {
            synchronized (this.mLock) {
                try {
                    this.mService.onMessageChannelReady(this.mSessionBinder, extras);
                } catch (RemoteException e) {
                }
            }
            z = true;
        }
        return z;
    }

    public final boolean postMessage(String message, Bundle extras) {
        boolean z = false;
        if (this.mService != null) {
            synchronized (this.mLock) {
                try {
                    this.mService.onPostMessage(this.mSessionBinder, message, extras);
                } catch (RemoteException e) {
                }
            }
            z = true;
        }
        return z;
    }

    public void onPostMessageServiceConnected() {
    }

    public void onPostMessageServiceDisconnected() {
    }
}
