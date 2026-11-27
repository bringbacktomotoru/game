package com.sony.smartar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Size;
import android.util.SizeF;
import android.view.SurfaceView;
import com.sony.smartar.CameraDeviceInterface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TargetApi(MotionEventCompat.AXIS_WHEEL)
/* loaded from: classes.dex */
public final class CameraDevice20 implements CameraDeviceInterface {
    private static final Size h = new Size(640, 480);
    private Handler A;
    private Handler B;
    private MediaActionSound p;
    private HandlerThread x;
    private Handler y;
    private HandlerThread z;
    private final CameraCaptureSession.CaptureCallback a = new CameraCaptureSession.CaptureCallback() { // from class: com.sony.smartar.CameraDevice20.1
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j, long j2) {
            if (!CameraDevice20.this.D) {
                synchronized (com.sony.smartar.b.b) {
                    if (com.sony.smartar.b.a != 0) {
                        CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                synchronized (com.sony.smartar.b.b) {
                                    if (com.sony.smartar.b.a != 0) {
                                        CameraDevice20.onShutter(com.sony.smartar.b.a);
                                    }
                                }
                            }
                        });
                    }
                }
                CameraDevice20.this.p.play(0);
            }
        }
    };
    private final CameraCaptureSession.CaptureCallback b = new CameraCaptureSession.CaptureCallback() { // from class: com.sony.smartar.CameraDevice20.2
        private void a() {
            try {
                CaptureRequest.Builder createCaptureRequest = CameraDevice20.this.i.createCaptureRequest(2);
                createCaptureRequest.addTarget(CameraDevice20.this.s.getSurface());
                createCaptureRequest.set(CaptureRequest.CONTROL_AE_MODE, (Integer) CameraDevice20.this.o.get(CaptureRequest.CONTROL_AE_MODE));
                createCaptureRequest.set(CaptureRequest.CONTROL_AF_MODE, (Integer) CameraDevice20.this.o.get(CaptureRequest.CONTROL_AF_MODE));
                createCaptureRequest.set(CaptureRequest.CONTROL_AWB_MODE, (Integer) CameraDevice20.this.o.get(CaptureRequest.CONTROL_AWB_MODE));
                createCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, (Integer) CameraDevice20.this.o.get(CaptureRequest.JPEG_ORIENTATION));
                CameraDevice20.this.h();
                CameraDevice20.this.n.capture(createCaptureRequest.build(), CameraDevice20.this.a, CameraDevice20.this.y);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        private void a(CaptureResult captureResult) {
            final boolean z;
            Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AF_MODE);
            if (num == null || num.intValue() == 5 || num.intValue() == 0) {
                CameraDevice20.this.F = false;
                z = false;
            } else {
                Integer num2 = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                if (num2 != null) {
                    switch (num2.intValue()) {
                        case 4:
                            CameraDevice20.this.F = false;
                            z = true;
                            break;
                        case 5:
                            CameraDevice20.this.F = false;
                            z = false;
                            break;
                    }
                }
                z = true;
            }
            if (!CameraDevice20.this.F) {
                synchronized (com.sony.smartar.b.b) {
                    if (com.sony.smartar.b.a != 0) {
                        CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                synchronized (com.sony.smartar.b.b) {
                                    if (com.sony.smartar.b.a != 0) {
                                        CameraDevice20.onAutoFocus(com.sony.smartar.b.a, z);
                                    }
                                }
                            }
                        });
                    }
                }
                CameraDevice20.this.o.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
                Integer num3 = (Integer) CameraDevice20.this.o.get(CaptureRequest.CONTROL_AF_MODE);
                if (num3 != null) {
                    CameraDevice20.this.o.set(CaptureRequest.CONTROL_MODE, 1);
                    CameraDevice20.this.o.set(CaptureRequest.CONTROL_AF_MODE, num3);
                }
                try {
                    CameraDevice20.this.n.capture(CameraDevice20.this.o.build(), CameraDevice20.this.b, CameraDevice20.this.y);
                } catch (CameraAccessException e) {
                }
            }
        }

        private void b(CaptureResult captureResult) {
            if (CameraDevice20.this.L.b(a.PreviewRun)) {
                CameraDevice20.this.m = captureResult;
                if (!CameraDevice20.this.D) {
                    if (CameraDevice20.this.F) {
                        a(captureResult);
                    }
                    if (CameraDevice20.this.E && !CameraDevice20.this.C && !CameraDevice20.this.F) {
                        Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                        if (num == null || ((num.intValue() != 3 && num.intValue() != 1) || CameraDevice20.this.J || CameraDevice20.this.K >= 10)) {
                            CameraDevice20.this.K = 0;
                            CameraDevice20.this.C = true;
                            a();
                            return;
                        }
                        CameraDevice20.r(CameraDevice20.this);
                    }
                }
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j, long j2) {
            if (CameraDevice20.this.L.b(a.PreviewStarting)) {
                CameraDevice20.this.L.a(a.PreviewRun);
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureProgressed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureResult captureResult) {
            b(captureResult);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
            b(totalCaptureResult);
        }
    };
    private final CameraCaptureSession.StateCallback c = new CameraCaptureSession.StateCallback() { // from class: com.sony.smartar.CameraDevice20.3
        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            if (CameraDevice20.this.i != null) {
                CameraDevice20.this.n = cameraCaptureSession;
                try {
                    CameraDevice20.this.o = CameraDevice20.this.i.createCaptureRequest(1);
                    CameraDevice20.this.o.addTarget(CameraDevice20.this.t.getSurface());
                    CameraDevice20.this.o.set(CaptureRequest.CONTROL_MODE, 1);
                    CameraDevice20.this.o.set(CaptureRequest.CONTROL_AE_MODE, 1);
                    CameraDevice20.this.o.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(CameraDevice20.this.d()));
                    CameraDevice20.this.L.a(a.SessionReady);
                } catch (CameraAccessException e) {
                    CameraDevice20.this.a(4);
                }
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            CameraDevice20.this.a(4);
        }
    };
    private final CameraDevice.StateCallback d = new CameraDevice.StateCallback() { // from class: com.sony.smartar.CameraDevice20.4
        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            CameraDevice20.this.i = cameraDevice;
            CameraDevice20.this.L.a(a.CameraOpened);
            if (!CameraDevice20.this.e()) {
                CameraDevice20.this.a(4);
                synchronized (com.sony.smartar.c.a) {
                    CameraDevice20.this.a();
                }
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i) {
            CameraDevice20.this.a(1);
            synchronized (com.sony.smartar.c.a) {
                CameraDevice20.this.a();
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            CameraDevice20.this.a(1);
            synchronized (com.sony.smartar.c.a) {
                CameraDevice20.this.a();
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            CameraDevice20.this.L.a(a.CameraClosed);
        }
    };
    private final ImageReader.OnImageAvailableListener e = new ImageReader.OnImageAvailableListener() { // from class: com.sony.smartar.CameraDevice20.5
        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Image acquireLatestImage = imageReader.acquireLatestImage();
            if (acquireLatestImage != null) {
                if (!CameraDevice20.this.D && CameraDevice20.this.E) {
                    if (CameraDevice20.this.J) {
                        CameraDevice20.this.c();
                    }
                    Image.Plane[] planes = acquireLatestImage.getPlanes();
                    if (planes.length == 1 && acquireLatestImage.getFormat() == 256) {
                        synchronized (com.sony.smartar.c.b) {
                            CameraDevice20.this.N.b();
                            CameraDevice20.this.N.a(0).put(planes[0].getBuffer());
                            CameraDevice20.this.N.a(0, planes[0].getPixelStride());
                            CameraDevice20.this.N.b(0, planes[0].getRowStride());
                        }
                        synchronized (com.sony.smartar.b.b) {
                            if (com.sony.smartar.b.a != 0) {
                                CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.5.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        synchronized (com.sony.smartar.c.b) {
                                            if (!CameraDevice20.this.D) {
                                                synchronized (com.sony.smartar.b.b) {
                                                    if (com.sony.smartar.b.a != 0) {
                                                        CameraDevice20.onStillImage(com.sony.smartar.b.a, CameraDevice20.this.r.getWidth(), CameraDevice20.this.r.getHeight(), CameraDevice20.this.v, SystemClock.uptimeMillis(), CameraDevice20.this.N.a(0), CameraDevice20.this.N.a(0).limit());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                    CameraDevice20.this.C = false;
                    CameraDevice20.this.E = false;
                    if (CameraDevice20.this.J) {
                        CameraDevice20.this.J = false;
                    } else {
                        CameraDevice20.this.L.a(a.PreviewStarting);
                        CameraDevice20.this.g();
                    }
                }
                acquireLatestImage.close();
            }
        }
    };
    private final ImageReader.OnImageAvailableListener f = new ImageReader.OnImageAvailableListener() { // from class: com.sony.smartar.CameraDevice20.6
        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Image acquireLatestImage = imageReader.acquireLatestImage();
            if (acquireLatestImage != null) {
                if (!CameraDevice20.this.D && !CameraDevice20.this.J && !CameraDevice20.this.E && CameraDevice20.this.L.b(a.PreviewRun)) {
                    Image.Plane[] planes = acquireLatestImage.getPlanes();
                    if (planes.length == 3 && acquireLatestImage.getFormat() == 35) {
                        synchronized (com.sony.smartar.c.b) {
                            CameraDevice20.this.M.a(planes);
                        }
                        CameraDevice20.this.B.post(CameraDevice20.this.g);
                    }
                }
                acquireLatestImage.close();
            }
        }
    };
    private Runnable g = new Runnable() { // from class: com.sony.smartar.CameraDevice20.7
        @Override // java.lang.Runnable
        public void run() {
            c a2;
            synchronized (com.sony.smartar.c.b) {
                if (!CameraDevice20.this.D && !CameraDevice20.this.J && !CameraDevice20.this.E && CameraDevice20.this.L.b(a.PreviewRun) && (a2 = CameraDevice20.this.M.a()) != null) {
                    synchronized (com.sony.smartar.b.b) {
                        if (com.sony.smartar.b.a != 0) {
                            CameraDevice20.onVideoImage(com.sony.smartar.b.a, CameraDevice20.this.q.getWidth(), CameraDevice20.this.q.getHeight(), CameraDevice20.this.u, SystemClock.uptimeMillis(), a2.a(0), a2.a(0).limit(), a2.c(0), a2.b(0), a2.a(1), a2.a(1).limit(), a2.c(1), a2.b(1), a2.a(2), a2.a(2).limit(), a2.c(2), a2.b(2));
                        }
                    }
                }
            }
        }
    };
    private CameraDevice i = null;
    private String j = null;
    private int k = -1;
    private CameraManager l = null;
    private CaptureResult m = null;
    private CameraCaptureSession n = null;
    private CaptureRequest.Builder o = null;
    private Size q = h;
    private Size r = h;
    private ImageReader s = null;
    private ImageReader t = null;
    private int u = 1;
    private int v = 4;
    private Context w = null;
    private boolean C = false;
    private boolean D = false;
    private boolean E = false;
    private boolean F = false;
    private boolean G = false;
    private boolean H = false;
    private boolean I = false;
    private boolean J = false;
    private int K = 0;
    private b L = new b();
    private d M = null;
    private c N = null;

    private enum a {
        CameraClosed,
        CameraOpening,
        CameraOpened,
        SessionCreating,
        SessionReady,
        PreviewStarting,
        PreviewRun,
        PreviewStopping
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onAutoExposure(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onAutoFocus(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onAutoWhiteBalance(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onError(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onShutter(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onStillImage(long j, int i, int i2, int i3, long j2, ByteBuffer byteBuffer, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onVideoImage(long j, int i, int i2, int i3, long j2, ByteBuffer byteBuffer, int i4, int i5, int i6, ByteBuffer byteBuffer2, int i7, int i8, int i9, ByteBuffer byteBuffer3, int i10, int i11, int i12);

    static /* synthetic */ int r(CameraDevice20 cameraDevice20) {
        int i = cameraDevice20.K;
        cameraDevice20.K = i + 1;
        return i;
    }

    private class b {
        private a b;

        private b() {
            this.b = a.CameraClosed;
        }

        public synchronized void a(a aVar) {
            synchronized (com.sony.smartar.a.b) {
                this.b = aVar;
                notifyAll();
            }
        }

        public boolean b(a aVar) {
            boolean z;
            synchronized (com.sony.smartar.a.b) {
                z = this.b == aVar;
            }
            return z;
        }
    }

    private class c {
        private ByteBuffer[] b;
        private int[] c;
        private int[] d;
        private int e;

        public c(int i, int i2, int i3, int i4) {
            this.e = i3;
            this.b = new ByteBuffer[this.e];
            this.c = new int[this.e];
            this.d = new int[this.e];
            for (int i5 = 0; i5 < this.e; i5++) {
                if ((i4 == 2 || i4 == 1) && i5 > 0) {
                    this.b[i5] = ByteBuffer.allocateDirect(((i * i2) * 3) / 4);
                } else {
                    this.b[i5] = ByteBuffer.allocateDirect(((i * i2) * 3) / 2);
                }
                this.c[i5] = 0;
                this.d[i5] = 0;
            }
        }

        public void a() {
            for (int i = 0; i < this.e; i++) {
                this.b[i].clear();
                this.b[i] = null;
                this.c[i] = 0;
                this.d[i] = 0;
            }
        }

        public void b() {
            for (int i = 0; i < this.e; i++) {
                this.b[i].clear();
                this.c[i] = 0;
                this.d[i] = 0;
            }
        }

        public ByteBuffer a(int i) {
            if (i < 0 || i >= this.e) {
                return null;
            }
            return this.b[i];
        }

        public int b(int i) {
            if (i < 0 || i >= this.e) {
                return 0;
            }
            return this.c[i];
        }

        public void a(int i, int i2) {
            if (i >= 0 && i < this.e) {
                this.c[i] = i2;
            }
        }

        public int c(int i) {
            if (i < 0 || i >= this.e) {
                return 0;
            }
            return this.d[i];
        }

        public void b(int i, int i2) {
            if (i >= 0 && i < this.e) {
                this.d[i] = i2;
            }
        }

        public int c() {
            return this.e;
        }
    }

    private class d {
        private int b;
        private long c = -1;
        private long d = 0;
        private c[] e;

        public d(int i, int i2, int i3, int i4, int i5) {
            this.b = 1;
            this.e = null;
            this.b = Math.min(i, 4);
            this.e = new c[this.b];
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 < this.b) {
                    this.e[i7] = CameraDevice20.this.new c(i2, i3, i4, i5);
                    i6 = i7 + 1;
                } else {
                    return;
                }
            }
        }

        public void a(Image.Plane[] planeArr) {
            int i = (int) (this.d % this.b);
            synchronized (com.sony.smartar.a.c[i]) {
                c cVar = this.e[i];
                cVar.b();
                for (int i2 = 0; i2 < Math.min(cVar.c(), planeArr.length); i2++) {
                    cVar.a(i2).put(planeArr[i2].getBuffer());
                    cVar.a(i2, planeArr[i2].getPixelStride());
                    cVar.b(i2, planeArr[i2].getRowStride());
                }
                this.d++;
                if (this.c < 0) {
                    this.c = 0L;
                }
            }
        }

        public c a() {
            c cVar;
            int i = (int) (this.c % this.b);
            synchronized (com.sony.smartar.a.c[i]) {
                if (this.c < 0 || this.c >= this.d) {
                    cVar = null;
                } else {
                    cVar = this.e[i];
                    this.c++;
                }
            }
            return cVar;
        }

        public void b() {
            for (int i = 0; i < this.b; i++) {
                this.e[i].a();
                this.e[i] = null;
            }
            this.c = -1L;
            this.d = 0L;
        }
    }

    public CameraDevice20() {
        this.p = null;
        this.x = null;
        this.y = null;
        this.z = null;
        this.A = null;
        this.B = null;
        synchronized (com.sony.smartar.c.a) {
            synchronized (com.sony.smartar.b.b) {
                com.sony.smartar.b.a = 0L;
            }
            com.sony.smartar.a.a();
            this.p = new MediaActionSound();
            this.p.load(0);
            this.z = new HandlerThread("Cd2APIHandlerThread");
            this.z.start();
            this.A = new Handler(this.z.getLooper());
            this.B = new Handler(Looper.getMainLooper());
            this.x = new HandlerThread("Cd2HandlerThread");
            this.x.start();
            this.y = new Handler(this.x.getLooper()) { // from class: com.sony.smartar.CameraDevice20.8
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case 1:
                            CameraDevice20 cameraDevice20 = (CameraDevice20) message.obj;
                            if (!cameraDevice20.D) {
                                if (CameraDevice20.this.m == null) {
                                    CameraDevice20.this.y.sendMessage(message);
                                    return;
                                }
                                Integer num = (Integer) CameraDevice20.this.m.get(CaptureResult.CONTROL_AE_MODE);
                                if (num == null || num.intValue() == 0) {
                                    CameraDevice20.this.G = false;
                                    synchronized (com.sony.smartar.b.b) {
                                        if (com.sony.smartar.b.a != 0) {
                                            CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.8.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    synchronized (com.sony.smartar.b.b) {
                                                        if (com.sony.smartar.b.a != 0) {
                                                            CameraDevice20.onAutoExposure(com.sony.smartar.b.a, false);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    return;
                                }
                                if (CameraDevice20.this.G) {
                                    Integer num2 = (Integer) CameraDevice20.this.m.get(CaptureResult.CONTROL_AE_STATE);
                                    if (num2.intValue() != 2 && num2.intValue() != 4) {
                                        CameraDevice20.this.y.sendMessageDelayed(CameraDevice20.this.y.obtainMessage(1, cameraDevice20), 100L);
                                        return;
                                    }
                                    final boolean z = num2.intValue() != 4;
                                    CameraDevice20.this.G = false;
                                    synchronized (com.sony.smartar.b.b) {
                                        if (com.sony.smartar.b.a != 0) {
                                            CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.8.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    synchronized (com.sony.smartar.b.b) {
                                                        if (com.sony.smartar.b.a != 0) {
                                                            CameraDevice20.onAutoExposure(com.sony.smartar.b.a, z);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    if (CameraDevice20.this.o != null) {
                                        CameraDevice20.this.o.set(CaptureRequest.CONTROL_MODE, 1);
                                        CameraDevice20.this.o.set(CaptureRequest.CONTROL_AE_MODE, 1);
                                        CameraDevice20.this.o.set(CaptureRequest.CONTROL_AE_LOCK, true);
                                        CameraDevice20.this.y.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.8.3
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                CameraDevice20.this.i();
                                            }
                                        });
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            return;
                        case 2:
                            CameraDevice20 cameraDevice202 = (CameraDevice20) message.obj;
                            if (!cameraDevice202.D) {
                                if (CameraDevice20.this.m == null) {
                                    CameraDevice20.this.y.sendMessage(message);
                                    return;
                                }
                                Integer num3 = (Integer) CameraDevice20.this.m.get(CaptureResult.CONTROL_AWB_MODE);
                                if (num3 == null || num3.intValue() != 1) {
                                    CameraDevice20.this.H = false;
                                    synchronized (com.sony.smartar.b.b) {
                                        if (com.sony.smartar.b.a != 0) {
                                            CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.8.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    synchronized (com.sony.smartar.b.b) {
                                                        if (com.sony.smartar.b.a != 0) {
                                                            CameraDevice20.onAutoWhiteBalance(com.sony.smartar.b.a, false);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    return;
                                }
                                if (CameraDevice20.this.H) {
                                    if (((Integer) CameraDevice20.this.m.get(CaptureResult.CONTROL_AWB_STATE)).intValue() == 2) {
                                        CameraDevice20.this.H = false;
                                        synchronized (com.sony.smartar.b.b) {
                                            if (com.sony.smartar.b.a != 0) {
                                                CameraDevice20.this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.8.5
                                                    @Override // java.lang.Runnable
                                                    public void run() {
                                                        synchronized (com.sony.smartar.b.b) {
                                                            if (com.sony.smartar.b.a != 0) {
                                                                CameraDevice20.onAutoWhiteBalance(com.sony.smartar.b.a, true);
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                        if (CameraDevice20.this.o != null) {
                                            CameraDevice20.this.o.set(CaptureRequest.CONTROL_MODE, 1);
                                            CameraDevice20.this.o.set(CaptureRequest.CONTROL_AWB_MODE, 1);
                                            CameraDevice20.this.o.set(CaptureRequest.CONTROL_AWB_LOCK, true);
                                            CameraDevice20.this.y.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.8.6
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    CameraDevice20.this.i();
                                                }
                                            });
                                            return;
                                        }
                                        return;
                                    }
                                    CameraDevice20.this.y.sendMessageDelayed(CameraDevice20.this.y.obtainMessage(2, cameraDevice202), 100L);
                                    return;
                                }
                                return;
                            }
                            return;
                        default:
                            super.handleMessage(message);
                            return;
                    }
                }
            };
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int open(Context context, long j, int i, Object obj, boolean z) {
        int i2;
        int i3;
        String str;
        synchronized (com.sony.smartar.c.a) {
            this.L.a(a.CameraOpening);
            if ((obj != null && !(obj instanceof CameraDevice)) || context == null) {
                this.L.a(a.CameraClosed);
                i2 = -2138308603;
            } else {
                this.w = context;
                synchronized (com.sony.smartar.b.b) {
                    com.sony.smartar.b.a = j;
                }
                this.l = (CameraManager) this.w.getSystemService("camera");
                if (this.l == null) {
                    i2 = -2138308594;
                } else {
                    String[] defaultCameraId2withString = getDefaultCameraId2withString(this.w, false);
                    int parseInt = Integer.parseInt(defaultCameraId2withString[0]);
                    if (i < 0) {
                        i = parseInt;
                    }
                    String str2 = defaultCameraId2withString[1];
                    int i4 = 0;
                    try {
                        String[] cameraIdList = this.l.getCameraIdList();
                        int length = cameraIdList.length;
                        int i5 = 0;
                        while (true) {
                            if (i5 >= length) {
                                i3 = i4;
                                str = str2;
                                break;
                            }
                            String str3 = cameraIdList[i5];
                            Integer num = (Integer) this.l.getCameraCharacteristics(str3).get(CameraCharacteristics.LENS_FACING);
                            if (num != null && num.intValue() == 0) {
                                i3 = i4 + 1;
                                str = parseInt == 1 ? str3 : str2;
                                if (i != 1) {
                                    i5++;
                                    str2 = str;
                                    i4 = i3;
                                } else {
                                    this.j = str3;
                                    this.k = i;
                                    break;
                                }
                            } else if (num != null && num.intValue() == 1) {
                                i3 = i4 + 1;
                                str = parseInt == 0 ? str3 : str2;
                                if (i != 0) {
                                    i5++;
                                    str2 = str;
                                    i4 = i3;
                                } else {
                                    this.j = str3;
                                    this.k = i;
                                    break;
                                }
                            } else {
                                if (num == null || num.intValue() != 2) {
                                    i3 = i4;
                                    str = str2;
                                } else {
                                    i3 = i4 + 1;
                                    str = parseInt == i3 ? str3 : str2;
                                    if (i == i3) {
                                        this.j = str3;
                                        this.k = i;
                                        break;
                                    }
                                }
                                i5++;
                                str2 = str;
                                i4 = i3;
                            }
                        }
                        if (i3 <= i) {
                            this.j = str;
                        }
                        if (this.j == null) {
                            this.L.a(a.CameraClosed);
                            i2 = -2138308594;
                        } else {
                            try {
                                if (obj == null) {
                                    this.l.openCamera(this.j, this.d, this.y);
                                    if (q() != 0) {
                                        this.L.a(a.CameraClosed);
                                        i2 = -2138308594;
                                    }
                                    i2 = 0;
                                } else {
                                    this.i = (CameraDevice) obj;
                                    this.L.a(a.CameraOpened);
                                    if (!e()) {
                                        this.L.a(a.CameraClosed);
                                        i2 = -2138308594;
                                    }
                                    i2 = 0;
                                }
                            } catch (CameraAccessException e) {
                                this.L.a(a.CameraClosed);
                                i2 = -2138308594;
                            }
                        }
                    } catch (CameraAccessException e2) {
                        this.L.a(a.CameraClosed);
                        i2 = -2138308594;
                    }
                }
            }
        }
        return i2;
    }

    public static String[] getDefaultCameraId2withString(Context context, boolean z) {
        String[] cameraIdList;
        int length;
        int i;
        int i2;
        String[] strArr;
        String[] strArr2 = new String[2];
        CameraManager cameraManager = (CameraManager) context.getSystemService("camera");
        if (cameraManager == null) {
            strArr2[0] = String.valueOf(-1);
            strArr2[1] = null;
            return strArr2;
        }
        try {
            cameraIdList = cameraManager.getCameraIdList();
            length = cameraIdList.length;
            i = 0;
            i2 = 0;
        } catch (CameraAccessException e) {
            strArr2[0] = String.valueOf(-1);
            strArr2[1] = null;
            return strArr2;
        }
        while (true) {
            if (i < length) {
                String str = cameraIdList[i];
                Integer num = (Integer) cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.LENS_FACING);
                if (num != null && num.intValue() == 0) {
                    if (z) {
                        strArr2[0] = String.valueOf(i2);
                        strArr2[1] = str;
                        strArr = strArr2;
                        break;
                    }
                    i2++;
                    i++;
                } else {
                    if (num != null && num.intValue() == 1 && !z) {
                        strArr2[0] = String.valueOf(i2);
                        strArr2[1] = str;
                        strArr = strArr2;
                        break;
                    }
                    i2++;
                    i++;
                }
                strArr2[0] = String.valueOf(-1);
                strArr2[1] = null;
                return strArr2;
            }
            strArr2[0] = String.valueOf(-1);
            strArr2[1] = null;
            strArr = strArr2;
            break;
        }
        return strArr;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getImageSensorRotation() {
        int d2;
        synchronized (com.sony.smartar.c.a) {
            d2 = d();
        }
        return d2;
    }

    public static int getDefaultCameraId2(Context context, boolean z) {
        return Integer.parseInt(getDefaultCameraId2withString(context, z)[0]);
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public void release() {
        synchronized (com.sony.smartar.c.a) {
            synchronized (com.sony.smartar.b.b) {
                this.B.removeCallbacksAndMessages(null);
                com.sony.smartar.b.a = 0L;
            }
            a();
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setNativeVideoOutput(SurfaceView surfaceView) {
        synchronized (com.sony.smartar.c.a) {
        }
        return 0;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setVideoImageSize(int i, int i2) {
        int i3 = 0;
        synchronized (com.sony.smartar.c.a) {
            if (this.q.getWidth() != i || this.q.getHeight() != i2) {
                i3 = c();
                if (i3 == 0) {
                    this.q = null;
                    this.q = new Size(i, i2);
                    k();
                    i3 = q();
                    if (i3 == 0) {
                        if (!a(true, false, false)) {
                            i3 = -2138308594;
                        } else {
                            i3 = b();
                        }
                    }
                }
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setVideoImageFormat(int i) {
        synchronized (com.sony.smartar.c.a) {
            if (this.u == i) {
                return 0;
            }
            if (i != 1) {
                return -2138308603;
            }
            if (Utility.a(true, i) == -1) {
                return -2138308603;
            }
            int c2 = c();
            if (c2 == 0) {
                this.u = i;
                int q = q();
                if (q == 0) {
                    if (!a(true, false, false)) {
                        return -2138308594;
                    }
                    return b();
                }
                return q;
            }
            return c2;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setVideoImageFpsRange(int i, int i2) {
        synchronized (com.sony.smartar.c.a) {
        }
        return -2138308603;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setStillImageSize(int i, int i2) {
        int i3 = 0;
        synchronized (com.sony.smartar.c.a) {
            if (this.r.getWidth() != i || this.r.getHeight() != i2) {
                i3 = c();
                if (i3 == 0) {
                    this.r = null;
                    this.r = new Size(i, i2);
                    n();
                    i3 = q();
                    if (i3 == 0) {
                        if (!a(false, true, false)) {
                            i3 = -2138308594;
                        } else {
                            i3 = b();
                        }
                    }
                }
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setStillImageFormat(int i) {
        synchronized (com.sony.smartar.c.a) {
            if (this.v == i) {
                return 0;
            }
            if (i != 4) {
                return -2138308603;
            }
            if (Utility.a(true, i) == -1) {
                return -2138308603;
            }
            int c2 = c();
            if (c2 == 0) {
                this.v = i;
                int q = q();
                if (q == 0) {
                    if (!a(false, true, false)) {
                        return -2138308594;
                    }
                    return b();
                }
                return q;
            }
            return c2;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int start() {
        int b2;
        synchronized (com.sony.smartar.c.a) {
            b2 = b();
        }
        return b2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int stop() {
        int c2;
        synchronized (com.sony.smartar.c.a) {
            c2 = c();
        }
        return c2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int captureStillImage() {
        int i = -2138308594;
        synchronized (com.sony.smartar.c.a) {
            if (!this.E) {
                synchronized (com.sony.smartar.c.b) {
                    this.B.removeCallbacks(this.g);
                }
                if (this.L.b(a.PreviewStopping)) {
                    long j = 2500;
                    long nanoTime = System.nanoTime();
                    while (!this.L.b(a.SessionReady)) {
                        if (!this.L.b(a.PreviewStopping)) {
                            break;
                        }
                        j -= (System.nanoTime() - nanoTime) / 1000000;
                        if (j <= 0) {
                            break;
                        }
                        try {
                            synchronized (this.L) {
                                this.L.wait(j);
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                    this.E = true;
                    this.J = true;
                    b();
                    i = 0;
                } else {
                    if (this.L.b(a.SessionReady)) {
                        this.E = true;
                        this.J = true;
                        b();
                    } else if (this.L.b(a.PreviewRun) || this.L.b(a.PreviewStarting)) {
                        this.E = true;
                    } else {
                        i = -2138308608;
                    }
                    i = 0;
                }
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int runAutoFocus() {
        int i = -2138308603;
        synchronized (com.sony.smartar.c.a) {
            if (!this.L.b(a.PreviewRun)) {
                i = -2138308598;
            } else if (this.F) {
                i = -2138308594;
            } else {
                this.F = true;
                if (this.o != null) {
                    try {
                        this.o.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
                        this.n.capture(this.o.build(), this.b, this.y);
                        i = 0;
                    } catch (CameraAccessException e) {
                    }
                }
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int runAutoExposure() {
        synchronized (com.sony.smartar.c.a) {
            if (!this.L.b(a.PreviewRun)) {
                return -2138308598;
            }
            if (this.G) {
                return -2138308594;
            }
            this.G = true;
            if (this.o == null) {
                return -2138308603;
            }
            this.o.set(CaptureRequest.CONTROL_MODE, 1);
            this.o.set(CaptureRequest.CONTROL_AE_MODE, 1);
            this.o.set(CaptureRequest.CONTROL_AE_LOCK, false);
            if (i() != 0) {
                this.G = false;
                return -2138308603;
            }
            this.y.sendMessageDelayed(this.y.obtainMessage(1, this), 3000L);
            return 0;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int runAutoWhiteBalance() {
        synchronized (com.sony.smartar.c.a) {
            if (!this.L.b(a.PreviewRun)) {
                return -2138308598;
            }
            if (this.H) {
                return -2138308594;
            }
            this.H = true;
            if (this.o == null) {
                return -2138308603;
            }
            this.o.set(CaptureRequest.CONTROL_MODE, 1);
            this.o.set(CaptureRequest.CONTROL_AWB_MODE, 1);
            this.o.set(CaptureRequest.CONTROL_AWB_LOCK, false);
            if (i() != 0) {
                this.H = false;
                return -2138308603;
            }
            this.y.sendMessageDelayed(this.y.obtainMessage(2, this), 3000L);
            return 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004d A[Catch: all -> 0x0041, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000a, B:8:0x0011, B:9:0x0020, B:11:0x0024, B:14:0x0026, B:15:0x0029, B:16:0x003f, B:19:0x0045, B:20:0x0049, B:22:0x004d, B:23:0x0064, B:25:0x0096, B:26:0x0066, B:27:0x006c, B:28:0x0072, B:29:0x0078, B:30:0x007e, B:31:0x0090), top: B:3:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0096 A[Catch: all -> 0x0041, DONT_GENERATE, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000a, B:8:0x0011, B:9:0x0020, B:11:0x0024, B:14:0x0026, B:15:0x0029, B:16:0x003f, B:19:0x0045, B:20:0x0049, B:22:0x004d, B:23:0x0064, B:25:0x0096, B:26:0x0066, B:27:0x006c, B:28:0x0072, B:29:0x0078, B:30:0x007e, B:31:0x0090), top: B:3:0x0006 }] */
    @Override // com.sony.smartar.CameraDeviceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int setFocusMode(int r7) {
        /*
            r6 = this;
            r0 = -2138308603(0xffffffff808c0005, float:-1.2856976E-38)
            java.lang.Object r2 = com.sony.smartar.c.a
            monitor-enter(r2)
            boolean r1 = r6.F     // Catch: java.lang.Throwable -> L41
            if (r1 == 0) goto L20
            r1 = 0
            r6.F = r1     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Builder r1 = r6.o     // Catch: java.lang.Throwable -> L41
            if (r1 == 0) goto L20
            android.hardware.camera2.CaptureRequest$Builder r1 = r6.o     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Key r3 = android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER     // Catch: java.lang.Throwable -> L41
            r4 = 2
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L41
            r1.set(r3, r4)     // Catch: java.lang.Throwable -> L41
            r6.i()     // Catch: java.lang.Throwable -> L41
        L20:
            android.hardware.camera2.CameraCaptureSession r1 = r6.n     // Catch: java.lang.Throwable -> L41
            if (r1 != 0) goto L26
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L41
        L25:
            return r0
        L26:
            switch(r7) {
                case 0: goto L44;
                case 1: goto L66;
                case 2: goto L6c;
                case 3: goto L72;
                case 4: goto L78;
                case 5: goto L7e;
                case 6: goto L90;
                default: goto L29;
            }     // Catch: java.lang.Throwable -> L41
        L29:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L41
            r1.<init>()     // Catch: java.lang.Throwable -> L41
            java.lang.String r3 = "unexpected value: "
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch: java.lang.Throwable -> L41
            java.lang.StringBuilder r1 = r1.append(r7)     // Catch: java.lang.Throwable -> L41
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L41
            com.sony.smartar.d.b(r1)     // Catch: java.lang.Throwable -> L41
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L41
            goto L25
        L41:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L41
            throw r0
        L44:
            r1 = 1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
        L49:
            android.hardware.camera2.CaptureRequest$Builder r3 = r6.o     // Catch: java.lang.Throwable -> L41
            if (r3 == 0) goto L96
            android.hardware.camera2.CaptureRequest$Builder r0 = r6.o     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Key r3 = android.hardware.camera2.CaptureRequest.CONTROL_MODE     // Catch: java.lang.Throwable -> L41
            r4 = 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L41
            r0.set(r3, r4)     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Builder r0 = r6.o     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Key r3 = android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE     // Catch: java.lang.Throwable -> L41
            r0.set(r3, r1)     // Catch: java.lang.Throwable -> L41
            int r0 = r6.i()     // Catch: java.lang.Throwable -> L41
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L41
            goto L25
        L66:
            r1 = 4
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
            goto L49
        L6c:
            r1 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
            goto L49
        L72:
            r1 = 5
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
            goto L49
        L78:
            r1 = 0
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
            goto L49
        L7e:
            r1 = 0
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Builder r3 = r6.o     // Catch: java.lang.Throwable -> L41
            android.hardware.camera2.CaptureRequest$Key r4 = android.hardware.camera2.CaptureRequest.LENS_FOCUS_DISTANCE     // Catch: java.lang.Throwable -> L41
            r5 = 0
            java.lang.Float r5 = java.lang.Float.valueOf(r5)     // Catch: java.lang.Throwable -> L41
            r3.set(r4, r5)     // Catch: java.lang.Throwable -> L41
            goto L49
        L90:
            r1 = 2
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L41
            goto L49
        L96:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L41
            goto L25
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.smartar.CameraDevice20.setFocusMode(int):int");
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setFocusAreas(float[] fArr, int i) {
        synchronized (com.sony.smartar.c.a) {
            this.o.set(CaptureRequest.CONTROL_AF_REGIONS, a(fArr, i));
        }
        return 0;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setExposureMode(int i) {
        boolean z = false;
        int i2 = -2138308603;
        synchronized (com.sony.smartar.c.a) {
            if (this.n != null) {
                switch (i) {
                    case 0:
                        z = true;
                    case 1:
                        if (this.o == null) {
                            break;
                        } else {
                            this.o.set(CaptureRequest.CONTROL_AE_LOCK, Boolean.valueOf(z));
                            this.G = false;
                            i2 = i();
                            break;
                        }
                    default:
                        com.sony.smartar.d.b("unexpected value: " + i);
                        break;
                }
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setExposureAreas(float[] fArr, int i) {
        synchronized (com.sony.smartar.c.a) {
            this.o.set(CaptureRequest.CONTROL_AE_REGIONS, a(fArr, i));
        }
        return 0;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setFlashMode(int i) {
        int i2;
        synchronized (com.sony.smartar.c.a) {
            if (this.n == null) {
                return -2138308603;
            }
            if (this.o == null) {
                return -2138308603;
            }
            if (((Integer) this.o.get(CaptureRequest.FLASH_MODE)).intValue() == 2) {
                this.o.set(CaptureRequest.CONTROL_AE_MODE, 1);
                this.o.set(CaptureRequest.FLASH_MODE, 0);
                int i3 = i();
                if (i3 != 0) {
                    return i3;
                }
            }
            Integer num = null;
            switch (i) {
                case 0:
                    i2 = 2;
                    break;
                case 1:
                    i2 = 1;
                    num = 0;
                    break;
                case 2:
                    i2 = 3;
                    break;
                case 3:
                    i2 = 4;
                    break;
                case 4:
                    i2 = 1;
                    num = 2;
                    break;
                default:
                    com.sony.smartar.d.b("unexpected value: " + i);
                    return -2138308603;
            }
            this.o.set(CaptureRequest.CONTROL_AE_MODE, i2);
            if (num != null) {
                this.o.set(CaptureRequest.FLASH_MODE, num);
            }
            return i();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0035 A[Catch: all -> 0x0029, TryCatch #0 {, blocks: (B:4:0x0008, B:6:0x000c, B:9:0x000e, B:10:0x0011, B:11:0x0027, B:14:0x002d, B:15:0x0031, B:17:0x0035, B:18:0x005a, B:20:0x0090, B:21:0x005c, B:22:0x0062, B:23:0x0068, B:24:0x006e, B:25:0x0074, B:26:0x007b, B:27:0x0081, B:28:0x0087), top: B:3:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0090 A[Catch: all -> 0x0029, DONT_GENERATE, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0008, B:6:0x000c, B:9:0x000e, B:10:0x0011, B:11:0x0027, B:14:0x002d, B:15:0x0031, B:17:0x0035, B:18:0x005a, B:20:0x0090, B:21:0x005c, B:22:0x0062, B:23:0x0068, B:24:0x006e, B:25:0x0074, B:26:0x007b, B:27:0x0081, B:28:0x0087), top: B:3:0x0008 }] */
    @Override // com.sony.smartar.CameraDeviceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int setWhiteBalanceMode(int r8) {
        /*
            r7 = this;
            r2 = 1
            r1 = 0
            r0 = -2138308603(0xffffffff808c0005, float:-1.2856976E-38)
            java.lang.Object r3 = com.sony.smartar.c.a
            monitor-enter(r3)
            android.hardware.camera2.CameraCaptureSession r4 = r7.n     // Catch: java.lang.Throwable -> L29
            if (r4 != 0) goto Le
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L29
        Ld:
            return r0
        Le:
            switch(r8) {
                case 0: goto L2c;
                case 1: goto L5c;
                case 2: goto L62;
                case 3: goto L68;
                case 4: goto L6e;
                case 5: goto L74;
                case 6: goto L7b;
                case 7: goto L81;
                case 8: goto L87;
                default: goto L11;
            }     // Catch: java.lang.Throwable -> L29
        L11:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L29
            r1.<init>()     // Catch: java.lang.Throwable -> L29
            java.lang.String r2 = "unexpected value: "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L29
            java.lang.StringBuilder r1 = r1.append(r8)     // Catch: java.lang.Throwable -> L29
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L29
            com.sony.smartar.d.b(r1)     // Catch: java.lang.Throwable -> L29
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L29
            goto Ld
        L29:
            r0 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L29
            throw r0
        L2c:
            r2 = 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
        L31:
            android.hardware.camera2.CaptureRequest$Builder r4 = r7.o     // Catch: java.lang.Throwable -> L29
            if (r4 == 0) goto L90
            android.hardware.camera2.CaptureRequest$Builder r0 = r7.o     // Catch: java.lang.Throwable -> L29
            android.hardware.camera2.CaptureRequest$Key r4 = android.hardware.camera2.CaptureRequest.CONTROL_MODE     // Catch: java.lang.Throwable -> L29
            r5 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.Throwable -> L29
            r0.set(r4, r5)     // Catch: java.lang.Throwable -> L29
            android.hardware.camera2.CaptureRequest$Builder r0 = r7.o     // Catch: java.lang.Throwable -> L29
            android.hardware.camera2.CaptureRequest$Key r4 = android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE     // Catch: java.lang.Throwable -> L29
            r0.set(r4, r2)     // Catch: java.lang.Throwable -> L29
            r0 = 0
            r7.H = r0     // Catch: java.lang.Throwable -> L29
            android.hardware.camera2.CaptureRequest$Builder r0 = r7.o     // Catch: java.lang.Throwable -> L29
            android.hardware.camera2.CaptureRequest$Key r2 = android.hardware.camera2.CaptureRequest.CONTROL_AWB_LOCK     // Catch: java.lang.Throwable -> L29
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch: java.lang.Throwable -> L29
            r0.set(r2, r1)     // Catch: java.lang.Throwable -> L29
            int r0 = r7.i()     // Catch: java.lang.Throwable -> L29
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L29
            goto Ld
        L5c:
            r2 = 6
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L62:
            r2 = 5
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L68:
            r2 = 3
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L6e:
            r2 = 2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L74:
            r2 = 8
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L7b:
            r2 = 7
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L81:
            r2 = 4
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L29
            goto L31
        L87:
            r1 = 0
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L29
            r6 = r2
            r2 = r1
            r1 = r6
            goto L31
        L90:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L29
            goto Ld
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.smartar.CameraDevice20.setWhiteBalanceMode(int):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0033 A[Catch: all -> 0x0027, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000a, B:9:0x000c, B:10:0x000f, B:11:0x0025, B:14:0x002b, B:15:0x002f, B:17:0x0033, B:18:0x004a, B:20:0x00af, B:21:0x004c, B:22:0x0052, B:23:0x0059, B:24:0x0060, B:25:0x0067, B:26:0x006e, B:27:0x0074, B:28:0x007a, B:29:0x0080, B:30:0x0087, B:31:0x008d, B:32:0x0094, B:33:0x009b, B:34:0x00a2, B:35:0x00a9), top: B:3:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00af A[Catch: all -> 0x0027, DONT_GENERATE, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000a, B:9:0x000c, B:10:0x000f, B:11:0x0025, B:14:0x002b, B:15:0x002f, B:17:0x0033, B:18:0x004a, B:20:0x00af, B:21:0x004c, B:22:0x0052, B:23:0x0059, B:24:0x0060, B:25:0x0067, B:26:0x006e, B:27:0x0074, B:28:0x007a, B:29:0x0080, B:30:0x0087, B:31:0x008d, B:32:0x0094, B:33:0x009b, B:34:0x00a2, B:35:0x00a9), top: B:3:0x0006 }] */
    @Override // com.sony.smartar.CameraDeviceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int setSceneMode(int r6) {
        /*
            r5 = this;
            r0 = -2138308603(0xffffffff808c0005, float:-1.2856976E-38)
            java.lang.Object r2 = com.sony.smartar.c.a
            monitor-enter(r2)
            android.hardware.camera2.CameraCaptureSession r1 = r5.n     // Catch: java.lang.Throwable -> L27
            if (r1 != 0) goto Lc
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L27
        Lb:
            return r0
        Lc:
            switch(r6) {
                case 0: goto L2a;
                case 1: goto L4c;
                case 2: goto L52;
                case 3: goto L59;
                case 4: goto L60;
                case 5: goto L67;
                case 6: goto L6e;
                case 7: goto L74;
                case 8: goto L7a;
                case 9: goto L80;
                case 10: goto L87;
                case 11: goto L8d;
                case 12: goto L94;
                case 13: goto L9b;
                case 14: goto La2;
                case 15: goto La9;
                default: goto Lf;
            }     // Catch: java.lang.Throwable -> L27
        Lf:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L27
            r1.<init>()     // Catch: java.lang.Throwable -> L27
            java.lang.String r3 = "unexpected value: "
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch: java.lang.Throwable -> L27
            java.lang.StringBuilder r1 = r1.append(r6)     // Catch: java.lang.Throwable -> L27
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L27
            com.sony.smartar.d.b(r1)     // Catch: java.lang.Throwable -> L27
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L27
            goto Lb
        L27:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L27
            throw r0
        L2a:
            r1 = 2
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
        L2f:
            android.hardware.camera2.CaptureRequest$Builder r3 = r5.o     // Catch: java.lang.Throwable -> L27
            if (r3 == 0) goto Laf
            android.hardware.camera2.CaptureRequest$Builder r0 = r5.o     // Catch: java.lang.Throwable -> L27
            android.hardware.camera2.CaptureRequest$Key r3 = android.hardware.camera2.CaptureRequest.CONTROL_MODE     // Catch: java.lang.Throwable -> L27
            r4 = 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L27
            r0.set(r3, r4)     // Catch: java.lang.Throwable -> L27
            android.hardware.camera2.CaptureRequest$Builder r0 = r5.o     // Catch: java.lang.Throwable -> L27
            android.hardware.camera2.CaptureRequest$Key r3 = android.hardware.camera2.CaptureRequest.CONTROL_SCENE_MODE     // Catch: java.lang.Throwable -> L27
            r0.set(r3, r1)     // Catch: java.lang.Throwable -> L27
            int r0 = r5.i()     // Catch: java.lang.Throwable -> L27
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L27
            goto Lb
        L4c:
            r1 = 0
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L52:
            r1 = 16
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L59:
            r1 = 8
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L60:
            r1 = 15
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L67:
            r1 = 12
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L6e:
            r1 = 4
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L74:
            r1 = 5
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L7a:
            r1 = 6
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L80:
            r1 = 14
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L87:
            r1 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L8d:
            r1 = 9
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L94:
            r1 = 13
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        L9b:
            r1 = 11
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        La2:
            r1 = 10
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        La9:
            r1 = 7
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> L27
            goto L2f
        Laf:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L27
            goto Lb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.smartar.CameraDevice20.setSceneMode(int):int");
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedVideoImageSize(int[] iArr, int i) {
        int a2;
        synchronized (com.sony.smartar.c.a) {
            a2 = a(iArr, i);
        }
        return a2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedVideoImageFormat(int[] iArr, int i) {
        int a2;
        synchronized (com.sony.smartar.c.a) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(35);
            a2 = Utility.a(true, (List<Integer>) arrayList, iArr, i);
        }
        return a2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedVideoImageFpsRange(int[] iArr, int i) {
        synchronized (com.sony.smartar.c.a) {
            if (this.l == null) {
                return 0;
            }
            try {
                Size[] outputSizes = ((StreamConfigurationMap) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(Utility.a(true, this.u));
                int i2 = 0;
                for (int i3 = 0; i3 < outputSizes.length && i2 < i; i3++) {
                    int round = Math.round(1.0E12f / r0.getOutputMinFrameDuration(Utility.a(true, this.u), outputSizes[i3]));
                    int i4 = 0;
                    while (i4 < i2 && iArr[(i4 * 2) + 1] != round) {
                        i4++;
                    }
                    if (i4 == i2) {
                        iArr[(i2 * 2) + 0] = 1000;
                        iArr[(i2 * 2) + 1] = round;
                        i2++;
                    }
                }
                return i2;
            } catch (CameraAccessException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedStillImageSize(int[] iArr, int i) {
        int i2 = 0;
        synchronized (com.sony.smartar.c.a) {
            if (this.l != null) {
                try {
                    i2 = Utility.a(true, (Object) Arrays.asList(((StreamConfigurationMap) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(Utility.a(true, this.v))), iArr, i);
                } catch (CameraAccessException e) {
                }
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedStillImageFormat(int[] iArr, int i) {
        int a2;
        synchronized (com.sony.smartar.c.a) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(256);
            a2 = Utility.a(true, (List<Integer>) arrayList, iArr, i);
        }
        return a2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedFocusMode(int[] iArr, int i) {
        int i2;
        int i3 = 0;
        synchronized (com.sony.smartar.c.a) {
            if (this.l != null) {
                try {
                    int[] iArr2 = (int[]) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
                    if (iArr2.length != 0) {
                        int length = iArr2.length;
                        int i4 = 0;
                        while (i4 < length && i3 < i) {
                            switch (iArr2[i4]) {
                                case 0:
                                    if (this.o == null) {
                                        i2 = i3 + 1;
                                        iArr[i3] = 4;
                                        break;
                                    } else if (((Float) this.o.get(CaptureRequest.LENS_FOCUS_DISTANCE)).floatValue() == 0.0f) {
                                        i2 = i3 + 1;
                                        iArr[i3] = 5;
                                        break;
                                    } else {
                                        i2 = i3 + 1;
                                        iArr[i3] = 4;
                                        break;
                                    }
                                case 1:
                                    i2 = i3 + 1;
                                    iArr[i3] = 0;
                                    break;
                                case 2:
                                    i2 = i3 + 1;
                                    iArr[i3] = 6;
                                    break;
                                case 3:
                                    i2 = i3 + 1;
                                    iArr[i3] = 2;
                                    break;
                                case 4:
                                    i2 = i3 + 1;
                                    iArr[i3] = 1;
                                    break;
                                case 5:
                                    i2 = i3 + 1;
                                    iArr[i3] = 3;
                                    break;
                                default:
                                    i2 = i3;
                                    break;
                            }
                            i4++;
                            i3 = i2;
                        }
                    }
                } catch (CameraAccessException e) {
                }
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getMaxNumFocusAreas() {
        synchronized (com.sony.smartar.c.a) {
            if (this.n == null) {
                return 0;
            }
            if (this.o == null) {
                return 0;
            }
            MeteringRectangle[] meteringRectangleArr = (MeteringRectangle[]) this.o.get(CaptureRequest.CONTROL_AF_REGIONS);
            return meteringRectangleArr == null ? 0 : meteringRectangleArr.length;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedFlashMode(int[] iArr, int i) {
        int i2;
        int i3 = 0;
        synchronized (com.sony.smartar.c.a) {
            if (this.l != null) {
                try {
                    int[] iArr2 = (int[]) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
                    if (iArr2.length != 0) {
                        int length = iArr2.length;
                        int i4 = 0;
                        while (i4 < length && i3 < i) {
                            switch (iArr2[i4]) {
                                case 1:
                                    int i5 = i3 + 1;
                                    iArr[i3] = 1;
                                    i2 = i5 + 1;
                                    iArr[i5] = 4;
                                    break;
                                case 2:
                                    i2 = i3 + 1;
                                    iArr[i3] = 0;
                                    break;
                                case 3:
                                    i2 = i3 + 1;
                                    iArr[i3] = 2;
                                    break;
                                case 4:
                                    i2 = i3 + 1;
                                    iArr[i3] = 3;
                                    break;
                                default:
                                    i2 = i3;
                                    break;
                            }
                            i4++;
                            i3 = i2;
                        }
                    }
                } catch (CameraAccessException e) {
                }
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedExposureMode(int[] iArr, int i) {
        int i2;
        int i3 = 1;
        synchronized (com.sony.smartar.c.a) {
            if (0 < i) {
                iArr[0] = 1;
            } else {
                i3 = 0;
            }
            if (i3 < i) {
                i2 = i3 + 1;
                iArr[i3] = 0;
            } else {
                i2 = i3;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getMaxNumExposureAreas() {
        synchronized (com.sony.smartar.c.a) {
            if (this.n == null) {
                return 0;
            }
            if (this.o == null) {
                return 0;
            }
            MeteringRectangle[] meteringRectangleArr = (MeteringRectangle[]) this.o.get(CaptureRequest.CONTROL_AE_REGIONS);
            return meteringRectangleArr == null ? 0 : meteringRectangleArr.length;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedWhiteBalanceMode(int[] iArr, int i) {
        int i2;
        int i3;
        synchronized (com.sony.smartar.c.a) {
            if (this.l == null) {
                return 0;
            }
            try {
                int[] iArr2 = (int[]) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
                if (iArr2.length == 0) {
                    return 0;
                }
                int length = iArr2.length;
                int i4 = 0;
                int i5 = 0;
                while (i4 < length && i5 < i) {
                    switch (iArr2[i4]) {
                        case 1:
                            i3 = i5 + 1;
                            iArr[i5] = 0;
                            break;
                        case 2:
                            i3 = i5 + 1;
                            iArr[i5] = 4;
                            break;
                        case 3:
                            i3 = i5 + 1;
                            iArr[i5] = 3;
                            break;
                        case 4:
                            i3 = i5 + 1;
                            iArr[i5] = 7;
                            break;
                        case 5:
                            i3 = i5 + 1;
                            iArr[i5] = 2;
                            break;
                        case 6:
                            i3 = i5 + 1;
                            iArr[i5] = 1;
                            break;
                        case 7:
                            i3 = i5 + 1;
                            iArr[i5] = 6;
                            break;
                        case 8:
                            i3 = i5 + 1;
                            iArr[i5] = 5;
                            break;
                        default:
                            i3 = i5;
                            break;
                    }
                    i4++;
                    i5 = i3;
                }
                if (i5 < i) {
                    i2 = i5 + 1;
                    iArr[i5] = 8;
                } else {
                    i2 = i5;
                }
                return i2;
            } catch (CameraAccessException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedSceneMode(int[] iArr, int i) {
        int i2;
        int i3 = 0;
        synchronized (com.sony.smartar.c.a) {
            if (this.l != null) {
                try {
                    int[] iArr2 = (int[]) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES);
                    if (iArr2.length != 0) {
                        iArr[0] = 1;
                        int length = iArr2.length;
                        int i4 = 0;
                        i3 = 1;
                        while (i4 < length && i3 < i) {
                            switch (iArr2[i4]) {
                                case 0:
                                    i2 = i3;
                                    break;
                                case 1:
                                default:
                                    i2 = i3;
                                    break;
                                case 2:
                                    i2 = i3 + 1;
                                    iArr[i3] = 0;
                                    break;
                                case 3:
                                    i2 = i3 + 1;
                                    iArr[i3] = 10;
                                    break;
                                case 4:
                                    i2 = i3 + 1;
                                    iArr[i3] = 6;
                                    break;
                                case 5:
                                    i2 = i3 + 1;
                                    iArr[i3] = 7;
                                    break;
                                case 6:
                                    i2 = i3 + 1;
                                    iArr[i3] = 8;
                                    break;
                                case 7:
                                    i2 = i3 + 1;
                                    iArr[i3] = 15;
                                    break;
                                case 8:
                                    i2 = i3 + 1;
                                    iArr[i3] = 3;
                                    break;
                                case 9:
                                    i2 = i3 + 1;
                                    iArr[i3] = 11;
                                    break;
                                case 10:
                                    i2 = i3 + 1;
                                    iArr[i3] = 14;
                                    break;
                                case 11:
                                    i2 = i3 + 1;
                                    iArr[i3] = 13;
                                    break;
                                case 12:
                                    i2 = i3 + 1;
                                    iArr[i3] = 5;
                                    break;
                                case 13:
                                    i2 = i3 + 1;
                                    iArr[i3] = 12;
                                    break;
                                case 14:
                                    i2 = i3 + 1;
                                    iArr[i3] = 9;
                                    break;
                                case 15:
                                    i2 = i3 + 1;
                                    iArr[i3] = 4;
                                    break;
                                case 16:
                                    i2 = i3 + 1;
                                    iArr[i3] = 2;
                                    break;
                            }
                            i4++;
                            i3 = i2;
                        }
                    }
                } catch (CameraAccessException e) {
                }
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public float[] getDeviceInfo() {
        float[] fArr;
        synchronized (com.sony.smartar.c.a) {
            fArr = new float[15];
            fArr[0] = this.k;
            fArr[1] = this.q.getWidth();
            fArr[2] = this.q.getHeight();
            fArr[3] = this.r.getWidth();
            fArr[4] = this.r.getHeight();
            CameraDeviceInterface.a aVar = sCameraModelInfos.get(getDefaultCameraId2(this.w, false));
            if (aVar != null) {
                fArr[5] = 1.0f;
                fArr[6] = aVar.a;
                fArr[7] = aVar.b;
                fArr[8] = aVar.c;
            }
            CameraDeviceInterface.a aVar2 = sCameraModelInfos.get(getDefaultCameraId2(this.w, true));
            if (aVar2 != null) {
                fArr[9] = 1.0f;
                fArr[10] = aVar2.a;
                fArr[11] = aVar2.b;
                fArr[12] = aVar2.c;
            }
            Utility.a(this.w, new Point());
            fArr[13] = r0.x;
            fArr[14] = r0.y;
        }
        return fArr;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getFacing() {
        int i;
        synchronized (com.sony.smartar.c.a) {
            switch (this.k) {
                case 0:
                    i = 0;
                    break;
                case 1:
                    i = 1;
                    break;
                default:
                    i = -2138308603;
                    break;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getRotation() {
        synchronized (com.sony.smartar.c.a) {
            if (this.l == null) {
                return 0;
            }
            try {
                switch (((Integer) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue()) {
                    case 0:
                        return 0;
                    case 90:
                        return 90;
                    case 180:
                        return 180;
                    case 270:
                        return 270;
                    default:
                        return 0;
                }
            } catch (CameraAccessException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public Object getNativeDevice() {
        CameraDevice cameraDevice;
        synchronized (com.sony.smartar.c.a) {
            cameraDevice = this.i;
        }
        return cameraDevice;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public float getFovY() {
        float height;
        synchronized (com.sony.smartar.c.a) {
            height = j().getHeight();
        }
        return height;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getStillImageSize(int[] iArr) {
        int i = 0;
        synchronized (com.sony.smartar.c.a) {
            if (iArr.length != 2) {
                i = -2138308602;
            } else {
                iArr[0] = this.r.getWidth();
                iArr[1] = this.r.getHeight();
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public float getFocalLength() {
        float f = 0.0f;
        synchronized (com.sony.smartar.c.a) {
            if (this.l != null) {
                try {
                    f = ((float[]) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS))[0];
                } catch (CameraAccessException e) {
                }
            }
        }
        return f;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getVideoImageSize(int[] iArr) {
        int i = 0;
        synchronized (com.sony.smartar.c.a) {
            if (iArr.length != 2) {
                i = -2138308602;
            } else {
                iArr[0] = this.q.getWidth();
                iArr[1] = this.q.getHeight();
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getMaxStillImageSize(int[] iArr) {
        int a2;
        synchronized (com.sony.smartar.c.a) {
            a2 = a(iArr);
        }
        return a2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int[] getCameraAPIFeature() {
        synchronized (com.sony.smartar.c.a) {
            int[] iArr = new int[2];
            iArr[0] = 2;
            if (this.l == null) {
                iArr[1] = 2;
                return iArr;
            }
            CameraCharacteristics cameraCharacteristics = null;
            try {
                cameraCharacteristics = this.l.getCameraCharacteristics(this.j);
            } catch (CameraAccessException e) {
                iArr[1] = 2;
            }
            switch (((Integer) cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue()) {
                case 0:
                    iArr[1] = 0;
                    break;
                case 1:
                    iArr[1] = 1;
                    break;
                case 2:
                default:
                    iArr[1] = 2;
                    break;
                case 3:
                    iArr[1] = 3;
                    break;
            }
            return iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        if (!this.D) {
            this.D = true;
            if (this.L.b(a.PreviewRun) || this.L.b(a.PreviewStarting)) {
                c();
            }
            if (this.n != null) {
                this.n.close();
                this.n = null;
            }
            if (this.s != null) {
                this.s.close();
                this.s = null;
            }
            if (this.i != null) {
                this.i.close();
                this.i = null;
            }
            if (this.p != null) {
                this.p.release();
            }
            this.l = null;
            if (this.z != null) {
                if (this.z.quitSafely()) {
                    try {
                        this.z.join(250L);
                    } catch (InterruptedException e) {
                    }
                }
                this.z = null;
            }
            if (this.x != null) {
                if (this.x.quitSafely()) {
                    try {
                        this.x.join(250L);
                    } catch (InterruptedException e2) {
                    }
                }
                this.x = null;
            }
            if (!this.L.b(a.CameraClosed)) {
                this.L.a(a.CameraClosed);
            }
            l();
            o();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(final int i) {
        synchronized (com.sony.smartar.b.b) {
            if (com.sony.smartar.b.a != 0) {
                this.B.post(new Runnable() { // from class: com.sony.smartar.CameraDevice20.9
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (com.sony.smartar.b.b) {
                            if (com.sony.smartar.b.a != 0) {
                                CameraDevice20.onError(com.sony.smartar.b.a, i);
                            }
                        }
                    }
                });
            }
        }
    }

    private int a(int[] iArr) {
        if (iArr.length != 2) {
            return -2138308602;
        }
        if (this.l == null) {
            return -2138308608;
        }
        try {
            Size[] outputSizes = ((StreamConfigurationMap) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(Utility.a(true, this.v));
            iArr[0] = 0;
            iArr[1] = 0;
            for (int i = 0; i < outputSizes.length; i++) {
                if (iArr[0] <= outputSizes[i].getWidth() && iArr[1] <= outputSizes[i].getHeight()) {
                    iArr[0] = outputSizes[i].getWidth();
                    iArr[1] = outputSizes[i].getHeight();
                }
            }
            return (iArr[0] == 0 || iArr[1] == 0) ? -2138308608 : 0;
        } catch (CameraAccessException e) {
            return -2138308608;
        }
    }

    private int b() {
        if (this.L.b(a.CameraOpening)) {
            return -2138308594;
        }
        if (this.L.b(a.CameraOpened) || this.L.b(a.SessionCreating)) {
            if (q() != 0) {
                return -2138308594;
            }
            this.L.a(a.PreviewStarting);
            g();
        } else {
            if (!this.L.b(a.SessionReady) && !this.L.b(a.PreviewStopping)) {
                return (this.L.b(a.PreviewRun) || this.L.b(a.PreviewStarting)) ? 0 : -2138308608;
            }
            this.L.a(a.PreviewStarting);
            g();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int c() {
        if (!this.L.b(a.PreviewRun) && !this.L.b(a.PreviewStarting)) {
            return (this.L.b(a.SessionCreating) || this.L.b(a.SessionReady) || this.L.b(a.PreviewStopping) || this.L.b(a.CameraOpening) || this.L.b(a.CameraOpened)) ? 0 : -2138308608;
        }
        this.L.a(a.PreviewStopping);
        h();
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int d() {
        if (this.l == null) {
            return 90;
        }
        try {
            return ((Integer) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
        } catch (CameraAccessException e) {
            return 90;
        }
    }

    private int a(int[] iArr, int i) {
        if (this.l == null) {
            return 0;
        }
        try {
            Size[] outputSizes = ((StreamConfigurationMap) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(Utility.a(true, this.u));
            Point point = new Point();
            Utility.a(this.w, point);
            Point point2 = point.x < point.y ? new Point(point.y, point.x) : point;
            Size size = (point2.x < 1920 || point2.y < 1080) ? new Size(point2.x, point2.y) : new Size(1920, 1080);
            ArrayList arrayList = new ArrayList(outputSizes.length);
            for (int i2 = 0; i2 < outputSizes.length; i2++) {
                Size size2 = outputSizes[i2];
                if (size2.getWidth() <= size.getWidth() && size2.getHeight() <= size.getHeight()) {
                    arrayList.add(outputSizes[i2]);
                }
            }
            return Utility.a(true, (Object) arrayList, iArr, i);
        } catch (CameraAccessException e) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e() {
        if (!f()) {
            return false;
        }
        int[] iArr = new int[32];
        int a2 = a(iArr, 32);
        ArrayList arrayList = new ArrayList(a2);
        for (int i = 0; i < a2 * 2; i += 2) {
            arrayList.add(new Size(iArr[i], iArr[i + 1]));
        }
        this.q = (Size) Utility.a(true, (Object) arrayList, h.getWidth(), h.getHeight());
        k();
        return a(true, true, true);
    }

    private void a(int i, boolean z) {
        if (this.s != null) {
            this.s.close();
            this.s = null;
        }
        if (z) {
            int[] iArr = new int[2];
            if (a(iArr) == 0) {
                this.r = new Size(iArr[0], iArr[1]);
                n();
            }
        }
        this.s = ImageReader.newInstance(this.r.getWidth(), this.r.getHeight(), i, 2);
        this.s.setOnImageAvailableListener(this.e, this.A);
    }

    private void b(int i) {
        if (this.t != null) {
            this.t.close();
            this.t = null;
        }
        this.t = ImageReader.newInstance(this.q.getWidth(), this.q.getHeight(), i, 2);
        this.t.setOnImageAvailableListener(this.f, this.A);
    }

    private boolean f() {
        if (this.l == null) {
            return false;
        }
        CameraDeviceInterface.a aVar = new CameraDeviceInterface.a();
        try {
            CameraCharacteristics cameraCharacteristics = this.l.getCameraCharacteristics(this.j);
            float[] fArr = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            SizeF sizeF = (SizeF) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            aVar.a = fArr[0];
            aVar.b = (float) (Math.atan((sizeF.getWidth() / 2.0f) / fArr[0]) * 2.0d);
            aVar.c = (float) (Math.atan((sizeF.getHeight() / 2.0f) / fArr[0]) * 2.0d);
            sCameraModelInfos.put(this.k, aVar);
            return true;
        } catch (CameraAccessException e) {
            return false;
        }
    }

    private boolean a(boolean z, boolean z2, boolean z3) {
        synchronized (com.sony.smartar.a.a) {
            this.L.a(a.SessionCreating);
            if (z) {
                b(Utility.a(true, this.u));
            }
            if (z2) {
                a(Utility.a(true, this.v), z3);
            }
            try {
                this.i.createCaptureSession(Arrays.asList(this.t.getSurface(), this.s.getSurface()), this.c, this.y);
            } catch (CameraAccessException e) {
                this.L.a(a.CameraOpened);
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        try {
            this.n.setRepeatingRequest(this.o.build(), this.b, this.y);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (this.n != null) {
            try {
                this.n.abortCaptures();
                synchronized (this.L) {
                    this.L.a(a.SessionReady);
                }
                this.B.removeCallbacks(this.g);
            } catch (CameraAccessException e) {
            } catch (IllegalStateException e2) {
            }
        }
    }

    private MeteringRectangle[] a(float[] fArr, int i) {
        if (this.l == null) {
            return null;
        }
        try {
            Rect rect = (Rect) this.l.getCameraCharacteristics(this.j).get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            MeteringRectangle[] meteringRectangleArr = new MeteringRectangle[i];
            for (int i2 = 0; i2 < i; i2++) {
                int width = rect.width();
                int height = rect.height();
                meteringRectangleArr[i2] = new MeteringRectangle(new Rect((int) (((fArr[(i2 * 5) + 0] * width) + width) / 2.0f), (int) (((fArr[(i2 * 5) + 1] * height) + height) / 2.0f), (int) ((width + (fArr[(i2 * 5) + 2] * width)) / 2.0f), (int) ((height + (fArr[(i2 * 5) + 3] * height)) / 2.0f)), (int) (fArr[(i2 * 5) + 4] * 1000.0f));
            }
            return meteringRectangleArr;
        } catch (CameraAccessException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int i() {
        try {
            if (this.L.b(a.PreviewRun) || this.L.b(a.PreviewStarting)) {
                this.n.setRepeatingRequest(this.o.build(), this.b, this.y);
            }
            return 0;
        } catch (CameraAccessException e) {
            return -2138308603;
        }
    }

    private SizeF j() {
        Size size;
        if (this.l == null) {
            return new SizeF(0.0f, 0.0f);
        }
        try {
            CameraCharacteristics cameraCharacteristics = this.l.getCameraCharacteristics(this.j);
            float[] fArr = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            SizeF sizeF = (SizeF) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            Size size2 = (Size) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
            Rect rect = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            if (this.r.getWidth() / this.r.getHeight() > rect.width() / rect.height()) {
                size = new Size(rect.width(), (int) ((rect.width() * this.q.getHeight()) / this.q.getWidth()));
            } else if (this.r.getWidth() / this.r.getHeight() < rect.width() / rect.height()) {
                size = new Size((int) ((rect.height() * this.q.getWidth()) / this.q.getHeight()), rect.height());
            } else {
                size = new Size(rect.width(), rect.height());
            }
            SizeF sizeF2 = new SizeF((sizeF.getWidth() * size.getWidth()) / size2.getWidth(), (sizeF.getHeight() * size.getHeight()) / size2.getHeight());
            return new SizeF(((float) (Math.atan(sizeF2.getWidth() / (fArr[0] * 2.0f)) * 2.0d)) * 57.295776f, ((float) (Math.atan(sizeF2.getHeight() / (fArr[0] * 2.0f)) * 2.0d)) * 57.295776f);
        } catch (CameraAccessException e) {
            return new SizeF(0.0f, 0.0f);
        }
    }

    private void k() {
        synchronized (com.sony.smartar.c.b) {
            m();
            this.M = new d(2, this.q.getWidth(), this.q.getHeight(), 3, 1);
        }
    }

    private void l() {
        synchronized (com.sony.smartar.c.b) {
            m();
        }
    }

    private void m() {
        if (this.M != null) {
            this.M.b();
            this.M = null;
        }
    }

    private void n() {
        synchronized (com.sony.smartar.c.b) {
            p();
            this.N = new c(this.r.getWidth(), this.r.getHeight(), 1, 4);
        }
    }

    private void o() {
        synchronized (com.sony.smartar.c.b) {
            p();
        }
    }

    private void p() {
        if (this.N != null) {
            this.N.a();
            this.N = null;
        }
    }

    private int q() {
        long j = 2500;
        long nanoTime = System.nanoTime();
        while (!this.L.b(a.SessionReady)) {
            if (!this.L.b(a.CameraOpening) && !this.L.b(a.CameraOpened) && !this.L.b(a.SessionCreating)) {
                return -2138308594;
            }
            j -= (System.nanoTime() - nanoTime) / 1000000;
            if (j <= 0) {
                return -2138308594;
            }
            try {
                synchronized (this.L) {
                    this.L.wait(j);
                }
            } catch (InterruptedException e) {
            }
        }
        return 0;
    }
}
