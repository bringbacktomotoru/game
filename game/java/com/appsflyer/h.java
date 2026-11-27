package com.appsflyer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
final class h {
    h() {
    }

    static final class e {

        /* renamed from: ˎ, reason: contains not printable characters */
        private final boolean f148;

        /* renamed from: ˏ, reason: contains not printable characters */
        private final String f149;

        e(String str, boolean z) {
            this.f149 = str;
            this.f148 = z;
        }

        /* renamed from: ˎ, reason: contains not printable characters */
        public final String m137() {
            return this.f149;
        }

        /* renamed from: ˋ, reason: contains not printable characters */
        final boolean m136() {
            return this.f148;
        }
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    static e m132(Context context) throws Exception {
        byte b2 = 0;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from the main thread");
        }
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            b bVar = new b(b2);
            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage("com.google.android.gms");
            try {
                try {
                    if (context.bindService(intent, bVar, 1)) {
                        a aVar = new a(bVar.m135());
                        return new e(aVar.m134(), aVar.m133());
                    }
                    if (context != null) {
                        context.unbindService(bVar);
                    }
                    throw new IOException("Google Play connection failed");
                } catch (Exception e2) {
                    throw e2;
                }
            } finally {
                if (context != null) {
                    context.unbindService(bVar);
                }
            }
        } catch (Exception e3) {
            throw e3;
        }
    }

    static final class b implements ServiceConnection {

        /* renamed from: ˎ, reason: contains not printable characters */
        private final LinkedBlockingQueue<IBinder> f146;

        /* renamed from: ॱ, reason: contains not printable characters */
        private boolean f147;

        private b() {
            this.f147 = false;
            this.f146 = new LinkedBlockingQueue<>(1);
        }

        /* synthetic */ b(byte b) {
            this();
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.f146.put(iBinder);
            } catch (InterruptedException e) {
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
        }

        /* renamed from: ˊ, reason: contains not printable characters */
        public final IBinder m135() throws InterruptedException {
            if (this.f147) {
                throw new IllegalStateException();
            }
            this.f147 = true;
            return this.f146.take();
        }
    }

    static final class a implements IInterface {

        /* renamed from: ˎ, reason: contains not printable characters */
        private IBinder f145;

        a(IBinder iBinder) {
            this.f145 = iBinder;
        }

        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this.f145;
        }

        /* renamed from: ॱ, reason: contains not printable characters */
        public final String m134() throws RemoteException {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.f145.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        /* renamed from: ˋ, reason: contains not printable characters */
        final boolean m133() throws RemoteException {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                obtain.writeInt(1);
                this.f145.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readInt() != 0;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }
}
