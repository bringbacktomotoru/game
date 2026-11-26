package com.sony.smartar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import com.sony.smartar.CameraDeviceInterface;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class CameraDevice10 implements CameraDeviceInterface {
    private static boolean H = false;
    private static final Handler I = new Handler(Looper.getMainLooper()) { // from class: com.sony.smartar.CameraDevice10.7
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    CameraDevice10 cameraDevice10 = (CameraDevice10) message.obj;
                    if (!cameraDevice10.A) {
                        Camera.Parameters parameters = cameraDevice10.n.getParameters();
                        f.c(parameters, true);
                        cameraDevice10.n.setParameters(parameters);
                        synchronized (b.b) {
                            if (b.a != 0) {
                                CameraDevice10.onAutoExposure(b.a, true);
                            }
                        }
                        cameraDevice10.D = false;
                        return;
                    }
                    return;
                case 2:
                    CameraDevice10 cameraDevice102 = (CameraDevice10) message.obj;
                    if (!cameraDevice102.A) {
                        Camera.Parameters parameters2 = cameraDevice102.n.getParameters();
                        f.b(parameters2, true);
                        cameraDevice102.n.setParameters(parameters2);
                        synchronized (b.b) {
                            if (b.a != 0) {
                                CameraDevice10.onAutoWhiteBalance(b.a, true);
                            }
                        }
                        cameraDevice102.E = false;
                        return;
                    }
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    };
    private final int a;
    private final int b;
    private String c;
    private String d;
    private String e;
    private String f;
    private int g;
    private final boolean h;
    private final boolean i;
    private final boolean j;
    private final boolean k;
    private final boolean l;
    private Context m;
    private Camera n;
    private int o;
    private SurfaceView p = null;
    private SurfaceTexture q = null;
    private int r = -1;
    private ArrayList<byte[]> s = new ArrayList<>(2);
    private int t = 1;
    private int u = 640;
    private int v = 480;
    private SurfaceView w = null;
    private boolean x = false;
    private boolean y = false;
    private boolean z = false;
    private boolean A = false;
    private boolean B = false;
    private boolean C = false;
    private boolean D = false;
    private boolean E = false;
    private boolean F = false;
    private boolean G = false;
    private final SurfaceHolder.Callback J = new SurfaceHolder.Callback() { // from class: com.sony.smartar.CameraDevice10.8
        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            CameraDevice10.this.z = true;
            CameraDevice10.this.d();
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            CameraDevice10.this.z = false;
            CameraDevice10.this.e();
        }
    };
    private final Camera.PreviewCallback K = new Camera.PreviewCallback() { // from class: com.sony.smartar.CameraDevice10.9
        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.hardware.Camera.PreviewCallback
        public void onPreviewFrame(byte[] bArr, Camera camera) {
            long uptimeMillis = SystemClock.uptimeMillis();
            if (CameraDevice10.this.y) {
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 < 2) {
                        if (bArr == CameraDevice10.this.s.get(i2)) {
                            synchronized (b.b) {
                                if (b.a != 0) {
                                    CameraDevice10.onVideoImage(b.a, bArr, CameraDevice10.this.u, CameraDevice10.this.v, CameraDevice10.this.t, uptimeMillis);
                                }
                            }
                            CameraDevice10.this.n.addCallbackBuffer(bArr);
                        }
                        i = i2 + 1;
                    } else {
                        return;
                    }
                }
            }
        }
    };
    private final Camera.ShutterCallback L = new Camera.ShutterCallback() { // from class: com.sony.smartar.CameraDevice10.10
        @Override // android.hardware.Camera.ShutterCallback
        public void onShutter() {
            if (!CameraDevice10.this.A) {
                synchronized (b.b) {
                    if (b.a != 0) {
                        CameraDevice10.onShutter(b.a);
                    }
                }
            }
        }
    };
    private final Camera.PictureCallback M = new Camera.PictureCallback() { // from class: com.sony.smartar.CameraDevice10.11
        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] bArr, Camera camera) {
            if (!CameraDevice10.this.A) {
                Camera.Parameters parameters = CameraDevice10.this.n.getParameters();
                if (CameraDevice10.this.k && Build.VERSION.SDK_INT >= 14) {
                    f.a(parameters, true);
                    CameraDevice10.this.n.setParameters(parameters);
                }
                CameraDevice10.this.d();
                Camera.Size pictureSize = parameters.getPictureSize();
                int[] iArr = new int[1];
                Utility.a(false, (List<Integer>) Collections.singletonList(Integer.valueOf(parameters.getPictureFormat())), iArr, 1);
                long uptimeMillis = SystemClock.uptimeMillis();
                synchronized (b.b) {
                    if (b.a != 0) {
                        CameraDevice10.onStillImage(b.a, bArr, pictureSize.width, pictureSize.height, iArr[0], uptimeMillis);
                    }
                }
                CameraDevice10.this.B = false;
            }
        }
    };
    private final Camera.AutoFocusCallback N = new Camera.AutoFocusCallback() { // from class: com.sony.smartar.CameraDevice10.2
        @Override // android.hardware.Camera.AutoFocusCallback
        public void onAutoFocus(boolean z, Camera camera) {
            if (!CameraDevice10.this.A) {
                synchronized (b.b) {
                    if (b.a != 0) {
                        CameraDevice10.onAutoFocus(b.a, z);
                    }
                }
                String focusMode = camera.getParameters().getFocusMode();
                if (focusMode.compareTo("continuous-video") == 0 || focusMode.compareTo("continuous-picture") == 0) {
                    camera.cancelAutoFocus();
                }
                CameraDevice10.this.C = false;
            }
        }
    };
    private final Camera.ErrorCallback O = new Camera.ErrorCallback() { // from class: com.sony.smartar.CameraDevice10.3
        @Override // android.hardware.Camera.ErrorCallback
        public void onError(int i, Camera camera) {
            synchronized (b.b) {
                if (b.a != 0) {
                    CameraDevice10.onError(b.a, i);
                }
            }
        }
    };

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
    public static native void onStillImage(long j, byte[] bArr, int i, int i2, int i3, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void onVideoImage(long j, byte[] bArr, int i, int i2, int i3, long j2);

    public CameraDevice10(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        boolean z6 = true;
        synchronized (c.a) {
            synchronized (b.b) {
                b.a = 0L;
            }
            this.a = z ? 640 : 480;
            this.b = z ? 480 : 320;
            this.l = z2;
            this.h = z3;
            this.i = z4;
            this.j = z5;
            if (!j.a || !this.h || Build.VERSION.SDK_INT < 14 || (Build.VERSION.SDK_INT >= 17 && (Build.VERSION.SDK_INT != 17 || this.i))) {
                z6 = false;
            }
            this.k = z6;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int open(Context context, long j, int i, Object obj, boolean z) {
        Context context2;
        Class<?> cls;
        synchronized (c.a) {
            if (obj != null) {
                try {
                    if (!(obj instanceof Camera)) {
                        return -2138308603;
                    }
                } catch (RuntimeException e) {
                    return -2138308594;
                }
            }
            this.m = context;
            synchronized (b.b) {
                b.a = j;
            }
            int a2 = Build.VERSION.SDK_INT >= 9 ? f.a() : 1;
            if (i < 0 || i >= a2) {
                this.o = getDefaultCameraId(false);
            } else {
                this.o = i;
            }
            H = false;
            if (obj != null) {
                this.n = (Camera) obj;
            } else {
                if (a2 > 1) {
                    Object b = f.b();
                    f.a(this.o, b);
                    a(getDefaultCameraId(f.a(b) == 1 ? false : true), (Camera) null);
                }
                if (z) {
                    this.n = b(this.o);
                } else if (Build.VERSION.SDK_INT >= 9) {
                    this.n = f.a(this.o);
                } else {
                    this.n = Camera.open();
                }
                if (this.n == null) {
                    return -2138308594;
                }
            }
            try {
                a(this.o, this.n);
                try {
                    context2 = context.createPackageContext("com.sonyericsson.android.camera", 3);
                } catch (PackageManager.NameNotFoundException e2) {
                    context2 = null;
                }
                if (context2 != null) {
                    try {
                        cls = i.a("com.sonyericsson.android.camera.device.CameraExtensionValues", false, context2.getClassLoader());
                    } catch (h e3) {
                        cls = null;
                    }
                } else {
                    cls = null;
                }
                if (cls != null) {
                    this.c = (String) a(cls, "KEY_EX_VIDEO_MODE");
                    this.d = (String) a(cls, "EX_ON");
                    this.e = (String) a(cls, "KEY_EX_FOCUS_AREA");
                    this.f = (String) a(cls, "EX_FOCUS_AREA_CENTER");
                } else {
                    this.c = null;
                    this.d = null;
                    this.e = null;
                    this.f = null;
                }
                this.g = 0;
                c();
                return 0;
            } catch (RuntimeException e4) {
                this.n.release();
                return -2138308594;
            }
        }
    }

    public static int getDefaultCameraId(boolean z) {
        try {
            if (Build.VERSION.SDK_INT >= 9) {
                int i = z ? 1 : 0;
                Object b = f.b();
                int a2 = f.a();
                for (int i2 = 0; i2 < a2; i2++) {
                    f.a(i2, b);
                    if (f.a(b) == i) {
                        return i2;
                    }
                }
            }
            return -1;
        } catch (RuntimeException e) {
            return -1;
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public void release() {
        synchronized (c.a) {
            if (!this.A) {
                synchronized (b.b) {
                    I.removeCallbacksAndMessages(null);
                    b.a = 0L;
                }
                this.A = true;
                if (this.p != null) {
                    this.p.getHolder().removeCallback(this.J);
                }
                this.n.release();
                this.s.clear();
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setNativeVideoOutput(SurfaceView surfaceView) {
        String str;
        synchronized (c.a) {
            try {
                if (this.p == surfaceView) {
                    return 0;
                }
                e();
                if (this.p != null) {
                    this.p.getHolder().removeCallback(this.J);
                }
                this.p = surfaceView;
                if (this.p != null) {
                    this.p.getHolder().addCallback(this.J);
                    if (Build.VERSION.SDK_INT >= 11) {
                        this.z = this.p.getHolder().getSurface().isValid();
                    } else {
                        try {
                            if (Build.VERSION.SDK_INT >= 9) {
                                str = "mNativeSurface";
                            } else {
                                str = "mSurface";
                            }
                            Field declaredField = Surface.class.getDeclaredField(str);
                            declaredField.setAccessible(true);
                            this.z = declaredField.getInt(this.p.getHolder().getSurface()) != 0;
                        } catch (IllegalAccessException e) {
                            return -2138308603;
                        } catch (NoSuchFieldException e2) {
                            return -2138308603;
                        }
                    }
                } else {
                    this.z = false;
                }
                d();
                return 0;
            } catch (RuntimeException e3) {
                return -2138308594;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setVideoImageSize(int i, int i2) {
        int i3 = 0;
        synchronized (c.a) {
            try {
                Camera.Parameters parameters = this.n.getParameters();
                Camera.Size previewSize = parameters.getPreviewSize();
                if (previewSize.width != i || previewSize.height != i2) {
                    e();
                    parameters.setPreviewSize(i, i2);
                    this.n.setParameters(parameters);
                    d();
                }
            } catch (RuntimeException e) {
                i3 = -2138308594;
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setVideoImageFormat(int i) {
        int i2 = 0;
        synchronized (c.a) {
            try {
                int a2 = Utility.a(false, i);
                if (a2 == -1) {
                    i2 = -2138308603;
                } else {
                    Camera.Parameters parameters = this.n.getParameters();
                    if (parameters.getPreviewFormat() != a2) {
                        e();
                        parameters.setPreviewFormat(a2);
                        this.n.setParameters(parameters);
                        d();
                    }
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setVideoImageFpsRange(int i, int i2) {
        int i3;
        synchronized (c.a) {
            try {
                e();
                Camera.Parameters parameters = this.n.getParameters();
                if (Build.VERSION.SDK_INT >= 9) {
                    f.a(parameters, i, i2);
                } else {
                    a(parameters, i2 / 1000);
                }
                this.n.setParameters(parameters);
                d();
                i3 = 0;
            } catch (RuntimeException e) {
                i3 = -2138308594;
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setStillImageSize(int i, int i2) {
        int i3 = 0;
        synchronized (c.a) {
            try {
                Camera.Parameters parameters = this.n.getParameters();
                Camera.Size pictureSize = parameters.getPictureSize();
                if (pictureSize.width != i || pictureSize.height != i2) {
                    parameters.setPictureSize(i, i2);
                    this.n.setParameters(parameters);
                }
            } catch (RuntimeException e) {
                i3 = -2138308594;
            }
        }
        return i3;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setStillImageFormat(int i) {
        int i2 = 0;
        synchronized (c.a) {
            try {
                int a2 = Utility.a(false, i);
                if (a2 == -1) {
                    i2 = -2138308603;
                } else {
                    Camera.Parameters parameters = this.n.getParameters();
                    if (parameters.getPictureFormat() != a2) {
                        parameters.setPictureFormat(a2);
                        this.n.setParameters(parameters);
                    }
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int start() {
        int i;
        synchronized (c.a) {
            try {
                if (j.b && Build.VERSION.SDK_INT < 11) {
                    final Object obj = new Object();
                    Runnable runnable = new Runnable() { // from class: com.sony.smartar.CameraDevice10.1
                        private int c = 0;

                        @Override // java.lang.Runnable
                        public void run() {
                            if (CameraDevice10.this.w == null && CameraDevice10.this.p == null) {
                                GLSurfaceView gLSurfaceView = (GLSurfaceView) j.a(((Activity) CameraDevice10.this.m).getWindow().getDecorView(), GLSurfaceView.class);
                                if (gLSurfaceView == null) {
                                    int i2 = this.c;
                                    this.c = i2 + 1;
                                    if (i2 < 20) {
                                        CameraDevice10.I.postDelayed(this, 100L);
                                        return;
                                    }
                                    return;
                                }
                                ViewGroup viewGroup = (ViewGroup) gLSurfaceView.getParent();
                                CameraDevice10.this.w = new SurfaceView(CameraDevice10.this.m);
                                CameraDevice10.this.w.setBackgroundColor(0);
                                if (Build.VERSION.SDK_INT < 11) {
                                    CameraDevice10.b(CameraDevice10.this.w.getHolder());
                                }
                                viewGroup.addView(CameraDevice10.this.w, 480, 320);
                                CameraDevice10.this.a(CameraDevice10.this.w);
                                gLSurfaceView.setZOrderMediaOverlay(true);
                                d.a("add dummy view");
                            }
                            synchronized (obj) {
                                obj.notifyAll();
                            }
                        }
                    };
                    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                        runnable.run();
                    } else {
                        I.post(runnable);
                        synchronized (obj) {
                            try {
                                obj.wait(2000L);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
                this.x = true;
                d();
                i = 0;
            } catch (RuntimeException e2) {
                i = -2138308594;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int stop() {
        int i = 0;
        synchronized (c.a) {
            try {
                this.x = false;
                e();
                if (j.b && Build.VERSION.SDK_INT < 11) {
                    Runnable runnable = new Runnable() { // from class: com.sony.smartar.CameraDevice10.4
                        @Override // java.lang.Runnable
                        public void run() {
                            if (CameraDevice10.this.w != null) {
                                ViewGroup viewGroup = (ViewGroup) CameraDevice10.this.w.getParent();
                                CameraDevice10.this.a((SurfaceView) null);
                                viewGroup.removeView(CameraDevice10.this.w);
                                CameraDevice10.this.w = null;
                                d.a("remove dummy view");
                            }
                        }
                    };
                    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                        runnable.run();
                    } else {
                        I.post(runnable);
                    }
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int captureStillImage() {
        synchronized (c.a) {
            try {
                if (this.B) {
                    return -2138308594;
                }
                this.B = true;
                e();
                if (this.k && Build.VERSION.SDK_INT >= 14) {
                    Camera.Parameters parameters = this.n.getParameters();
                    f.a(parameters, false);
                    this.n.setParameters(parameters);
                }
                if (this.j && Build.VERSION.SDK_INT >= 16) {
                    b();
                }
                f();
                this.n.setOneShotPreviewCallback(new Camera.PreviewCallback() { // from class: com.sony.smartar.CameraDevice10.5
                    @Override // android.hardware.Camera.PreviewCallback
                    public void onPreviewFrame(byte[] bArr, Camera camera) {
                        if (!CameraDevice10.this.A) {
                            if (CameraDevice10.this.j) {
                                CameraDevice10.I.postDelayed(new Runnable() { // from class: com.sony.smartar.CameraDevice10.5.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (!CameraDevice10.this.A) {
                                            if (CameraDevice10.this.F) {
                                                CameraDevice10.this.G = true;
                                            } else {
                                                CameraDevice10.this.n.takePicture(CameraDevice10.this.L, null, null, CameraDevice10.this.M);
                                            }
                                        }
                                    }
                                }, 300L);
                            } else {
                                CameraDevice10.this.n.takePicture(CameraDevice10.this.L, null, null, CameraDevice10.this.M);
                            }
                        }
                    }
                });
                this.n.startPreview();
                return 0;
            } catch (RuntimeException e) {
                return -2138308594;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int runAutoFocus() {
        int i = -2138308594;
        synchronized (c.a) {
            try {
                if (!this.C) {
                    this.C = true;
                    this.n.autoFocus(this.N);
                    i = 0;
                }
            } catch (RuntimeException e) {
            }
        }
        return i;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x004e -> B:8:0x0016). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int runAutoExposure() {
        int i = -2138308603;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT < 14) {
                    d.b("unsupported API on this device");
                } else if (this.D) {
                    i = -2138308594;
                } else {
                    Camera.Parameters parameters = this.n.getParameters();
                    if (f.f(parameters)) {
                        this.D = true;
                        f.c(parameters, false);
                        this.n.setParameters(parameters);
                        I.sendMessageDelayed(I.obtainMessage(1, this), 3000L);
                        i = 0;
                    }
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x004e -> B:8:0x0016). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int runAutoWhiteBalance() {
        int i = -2138308603;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT < 14) {
                    d.b("unsupported API on this device");
                } else if (this.E) {
                    i = -2138308594;
                } else {
                    Camera.Parameters parameters = this.n.getParameters();
                    if (f.e(parameters)) {
                        this.E = true;
                        f.b(parameters, false);
                        this.n.setParameters(parameters);
                        I.sendMessageDelayed(I.obtainMessage(2, this), 3000L);
                        i = 0;
                    }
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setFocusMode(int i) {
        String str;
        int i2 = 0;
        synchronized (c.a) {
            try {
                if (this.C) {
                    this.C = false;
                    this.n.cancelAutoFocus();
                }
                switch (i) {
                    case 0:
                        str = "auto";
                        Camera.Parameters parameters = this.n.getParameters();
                        parameters.setFocusMode(str);
                        this.n.setParameters(parameters);
                        break;
                    case 1:
                        str = "continuous-picture";
                        Camera.Parameters parameters2 = this.n.getParameters();
                        parameters2.setFocusMode(str);
                        this.n.setParameters(parameters2);
                        break;
                    case 2:
                        str = "continuous-video";
                        Camera.Parameters parameters22 = this.n.getParameters();
                        parameters22.setFocusMode(str);
                        this.n.setParameters(parameters22);
                        break;
                    case 3:
                        str = "edof";
                        Camera.Parameters parameters222 = this.n.getParameters();
                        parameters222.setFocusMode(str);
                        this.n.setParameters(parameters222);
                        break;
                    case 4:
                        str = "fixed";
                        Camera.Parameters parameters2222 = this.n.getParameters();
                        parameters2222.setFocusMode(str);
                        this.n.setParameters(parameters2222);
                        break;
                    case 5:
                        str = "infinity";
                        Camera.Parameters parameters22222 = this.n.getParameters();
                        parameters22222.setFocusMode(str);
                        this.n.setParameters(parameters22222);
                        break;
                    case 6:
                        str = "macro";
                        Camera.Parameters parameters222222 = this.n.getParameters();
                        parameters222222.setFocusMode(str);
                        this.n.setParameters(parameters222222);
                        break;
                    default:
                        d.b("unexpected value: " + i);
                        i2 = -2138308603;
                        break;
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x002f -> B:9:0x0012). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int setFocusAreas(float[] fArr, int i) {
        int i2;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT < 14) {
                    d.b("unsupported API on this device");
                    i2 = -2138308603;
                } else {
                    List<Object> a2 = a(fArr, i);
                    Camera.Parameters parameters = this.n.getParameters();
                    f.a(parameters, a2);
                    this.n.setParameters(parameters);
                    i2 = 0;
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setExposureMode(int i) {
        boolean z;
        synchronized (c.a) {
            try {
                switch (i) {
                    case 0:
                        if (Build.VERSION.SDK_INT >= 14) {
                            z = true;
                            break;
                        } else {
                            d.b("unsupported mode: " + i);
                            return -2138308603;
                        }
                    case 1:
                        z = false;
                        break;
                    default:
                        d.b("unexpected value: " + i);
                        return -2138308603;
                }
                Camera.Parameters parameters = this.n.getParameters();
                if (Build.VERSION.SDK_INT >= 14) {
                    I.removeMessages(1);
                    this.D = false;
                    f.c(parameters, z);
                }
                this.n.setParameters(parameters);
                return 0;
            } catch (RuntimeException e) {
                return -2138308594;
            }
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x002f -> B:9:0x0012). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int setExposureAreas(float[] fArr, int i) {
        int i2;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT < 14) {
                    d.b("unsupported API on this device");
                    i2 = -2138308603;
                } else {
                    List<Object> a2 = a(fArr, i);
                    Camera.Parameters parameters = this.n.getParameters();
                    f.b(parameters, a2);
                    this.n.setParameters(parameters);
                    i2 = 0;
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0047 -> B:8:0x0020). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int setFlashMode(int i) {
        int i2;
        String str;
        synchronized (c.a) {
            try {
                switch (i) {
                    case 0:
                        str = "auto";
                        Camera.Parameters parameters = this.n.getParameters();
                        parameters.setFlashMode(str);
                        this.n.setParameters(parameters);
                        i2 = 0;
                        break;
                    case 1:
                        str = "off";
                        Camera.Parameters parameters2 = this.n.getParameters();
                        parameters2.setFlashMode(str);
                        this.n.setParameters(parameters2);
                        i2 = 0;
                        break;
                    case 2:
                        str = "on";
                        Camera.Parameters parameters22 = this.n.getParameters();
                        parameters22.setFlashMode(str);
                        this.n.setParameters(parameters22);
                        i2 = 0;
                        break;
                    case 3:
                        str = "red-eye";
                        Camera.Parameters parameters222 = this.n.getParameters();
                        parameters222.setFlashMode(str);
                        this.n.setParameters(parameters222);
                        i2 = 0;
                        break;
                    case 4:
                        str = "torch";
                        Camera.Parameters parameters2222 = this.n.getParameters();
                        parameters2222.setFlashMode(str);
                        this.n.setParameters(parameters2222);
                        i2 = 0;
                        break;
                    default:
                        d.b("unexpected value: " + i);
                        i2 = -2138308603;
                        break;
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int setWhiteBalanceMode(int i) {
        String str;
        boolean z;
        synchronized (c.a) {
            try {
                switch (i) {
                    case 0:
                        str = "auto";
                        z = false;
                        break;
                    case 1:
                        str = "cloudy-daylight";
                        z = false;
                        break;
                    case 2:
                        str = "daylight";
                        z = false;
                        break;
                    case 3:
                        str = "fluorescent";
                        z = false;
                        break;
                    case 4:
                        str = "incandescent";
                        z = false;
                        break;
                    case 5:
                        str = "shade";
                        z = false;
                        break;
                    case 6:
                        str = "twilight";
                        z = false;
                        break;
                    case 7:
                        str = "warm-fluorescent";
                        z = false;
                        break;
                    case 8:
                        if (Build.VERSION.SDK_INT < 14) {
                            d.b("unsupported mode: " + i);
                            return -2138308603;
                        }
                        str = null;
                        z = true;
                        break;
                    default:
                        d.b("unexpected value: " + i);
                        return -2138308603;
                }
                Camera.Parameters parameters = this.n.getParameters();
                if (str != null) {
                    parameters.setWhiteBalance(str);
                }
                if (Build.VERSION.SDK_INT >= 14) {
                    I.removeMessages(2);
                    this.E = false;
                    f.b(parameters, z);
                }
                this.n.setParameters(parameters);
                return 0;
            } catch (RuntimeException e) {
                return -2138308594;
            }
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x0068 -> B:8:0x0020). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int setSceneMode(int i) {
        int i2;
        String str;
        synchronized (c.a) {
            try {
                switch (i) {
                    case 0:
                        str = "action";
                        Camera.Parameters parameters = this.n.getParameters();
                        parameters.setSceneMode(str);
                        this.n.setParameters(parameters);
                        i2 = 0;
                        break;
                    case 1:
                        str = "auto";
                        Camera.Parameters parameters2 = this.n.getParameters();
                        parameters2.setSceneMode(str);
                        this.n.setParameters(parameters2);
                        i2 = 0;
                        break;
                    case 2:
                        str = "barcode";
                        Camera.Parameters parameters22 = this.n.getParameters();
                        parameters22.setSceneMode(str);
                        this.n.setParameters(parameters22);
                        i2 = 0;
                        break;
                    case 3:
                        str = "beach";
                        Camera.Parameters parameters222 = this.n.getParameters();
                        parameters222.setSceneMode(str);
                        this.n.setParameters(parameters222);
                        i2 = 0;
                        break;
                    case 4:
                        str = "candlelight";
                        Camera.Parameters parameters2222 = this.n.getParameters();
                        parameters2222.setSceneMode(str);
                        this.n.setParameters(parameters2222);
                        i2 = 0;
                        break;
                    case 5:
                        str = "fireworks";
                        Camera.Parameters parameters22222 = this.n.getParameters();
                        parameters22222.setSceneMode(str);
                        this.n.setParameters(parameters22222);
                        i2 = 0;
                        break;
                    case 6:
                        str = "landscape";
                        Camera.Parameters parameters222222 = this.n.getParameters();
                        parameters222222.setSceneMode(str);
                        this.n.setParameters(parameters222222);
                        i2 = 0;
                        break;
                    case 7:
                        str = "night";
                        Camera.Parameters parameters2222222 = this.n.getParameters();
                        parameters2222222.setSceneMode(str);
                        this.n.setParameters(parameters2222222);
                        i2 = 0;
                        break;
                    case 8:
                        str = "night-portrait";
                        Camera.Parameters parameters22222222 = this.n.getParameters();
                        parameters22222222.setSceneMode(str);
                        this.n.setParameters(parameters22222222);
                        i2 = 0;
                        break;
                    case 9:
                        str = "party";
                        Camera.Parameters parameters222222222 = this.n.getParameters();
                        parameters222222222.setSceneMode(str);
                        this.n.setParameters(parameters222222222);
                        i2 = 0;
                        break;
                    case 10:
                        str = "portrait";
                        Camera.Parameters parameters2222222222 = this.n.getParameters();
                        parameters2222222222.setSceneMode(str);
                        this.n.setParameters(parameters2222222222);
                        i2 = 0;
                        break;
                    case 11:
                        str = "snow";
                        Camera.Parameters parameters22222222222 = this.n.getParameters();
                        parameters22222222222.setSceneMode(str);
                        this.n.setParameters(parameters22222222222);
                        i2 = 0;
                        break;
                    case 12:
                        str = "sports";
                        Camera.Parameters parameters222222222222 = this.n.getParameters();
                        parameters222222222222.setSceneMode(str);
                        this.n.setParameters(parameters222222222222);
                        i2 = 0;
                        break;
                    case 13:
                        str = "steadyphoto";
                        Camera.Parameters parameters2222222222222 = this.n.getParameters();
                        parameters2222222222222.setSceneMode(str);
                        this.n.setParameters(parameters2222222222222);
                        i2 = 0;
                        break;
                    case 14:
                        str = "sunset";
                        Camera.Parameters parameters22222222222222 = this.n.getParameters();
                        parameters22222222222222.setSceneMode(str);
                        this.n.setParameters(parameters22222222222222);
                        i2 = 0;
                        break;
                    case 15:
                        str = "theatre";
                        Camera.Parameters parameters222222222222222 = this.n.getParameters();
                        parameters222222222222222.setSceneMode(str);
                        this.n.setParameters(parameters222222222222222);
                        i2 = 0;
                        break;
                    default:
                        d.b("unexpected value: " + i);
                        i2 = -2138308603;
                        break;
                }
            } catch (RuntimeException e) {
                i2 = -2138308594;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedVideoImageSize(int[] iArr, int i) {
        List<Camera.Size> list;
        int a2;
        synchronized (c.a) {
            try {
                List<Camera.Size> supportedPreviewSizes = this.n.getParameters().getSupportedPreviewSizes();
                if (j.d && supportedPreviewSizes != null) {
                    int size = supportedPreviewSizes.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        Camera.Size size2 = supportedPreviewSizes.get(i2);
                        if (size2.width == 320 && size2.height == 240) {
                            list = new ArrayList<>(supportedPreviewSizes);
                            list.remove(i2);
                            break;
                        }
                    }
                }
                list = supportedPreviewSizes;
                a2 = Utility.a(false, (Object) list, iArr, i);
            } catch (RuntimeException e) {
                return 0;
            }
        }
        return a2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedVideoImageFormat(int[] iArr, int i) {
        int i2 = 0;
        synchronized (c.a) {
            try {
                i2 = Utility.a(false, this.n.getParameters().getSupportedPreviewFormats(), iArr, i);
            } catch (RuntimeException e) {
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedVideoImageFpsRange(int[] iArr, int i) {
        synchronized (c.a) {
            try {
                Camera.Parameters parameters = this.n.getParameters();
                if (Build.VERSION.SDK_INT >= 9) {
                    List<int[]> a2 = f.a(parameters);
                    if (a2 == null) {
                        return 0;
                    }
                    int min = Math.min(a2.size(), i);
                    for (int i2 = 0; i2 < min; i2++) {
                        int[] iArr2 = a2.get(i2);
                        iArr[(i2 * 2) + 0] = iArr2[0];
                        iArr[(i2 * 2) + 1] = iArr2[1];
                    }
                    return min;
                }
                List<Integer> b = b(parameters);
                if (b == null) {
                    return 0;
                }
                int min2 = Math.min(b.size(), i);
                for (int i3 = 0; i3 < min2; i3++) {
                    int intValue = b.get(i3).intValue();
                    iArr[(i3 * 2) + 0] = 1000;
                    iArr[(i3 * 2) + 1] = intValue * 1000;
                }
                return min2;
            } catch (RuntimeException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedStillImageSize(int[] iArr, int i) {
        int i2 = 0;
        synchronized (c.a) {
            try {
                i2 = Utility.a(false, (Object) this.n.getParameters().getSupportedPictureSizes(), iArr, i);
            } catch (RuntimeException e) {
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedStillImageFormat(int[] iArr, int i) {
        int i2 = 0;
        synchronized (c.a) {
            try {
                i2 = Utility.a(false, this.n.getParameters().getSupportedPictureFormats(), iArr, i);
            } catch (RuntimeException e) {
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedFocusMode(int[] iArr, int i) {
        int i2;
        synchronized (c.a) {
            try {
                List<String> supportedFocusModes = this.n.getParameters().getSupportedFocusModes();
                if (supportedFocusModes == null) {
                    return 0;
                }
                int size = supportedFocusModes.size();
                int i3 = 0;
                int i4 = 0;
                while (i3 < size && i4 < i) {
                    String str = supportedFocusModes.get(i3);
                    if ("auto".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 0;
                    } else if ("continuous-picture".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 1;
                    } else if ("continuous-video".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 2;
                    } else if ("edof".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 3;
                    } else if ("fixed".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 4;
                    } else if ("infinity".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 5;
                    } else if ("macro".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 6;
                    } else {
                        i2 = i4;
                    }
                    i3++;
                    i4 = i2;
                }
                return i4;
            } catch (RuntimeException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    @TargetApi(14)
    public int getMaxNumFocusAreas() {
        int i = 0;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT >= 14) {
                    Camera.Parameters parameters = this.n.getParameters();
                    List<Camera.Area> focusAreas = parameters.getFocusAreas();
                    i = Math.min(focusAreas == null ? 0 : focusAreas.size(), f.b(parameters));
                }
            } catch (RuntimeException e) {
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedFlashMode(int[] iArr, int i) {
        int i2;
        synchronized (c.a) {
            try {
                List<String> supportedFlashModes = this.n.getParameters().getSupportedFlashModes();
                if (supportedFlashModes == null) {
                    return 0;
                }
                int size = supportedFlashModes.size();
                int i3 = 0;
                int i4 = 0;
                while (i3 < size && i4 < i) {
                    String str = supportedFlashModes.get(i3);
                    if ("auto".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 0;
                    } else if ("off".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 1;
                    } else if ("on".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 2;
                    } else if ("red-eye".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 3;
                    } else if ("torch".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 4;
                    } else {
                        i2 = i4;
                    }
                    i3++;
                    i4 = i2;
                }
                return i4;
            } catch (RuntimeException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedExposureMode(int[] iArr, int i) {
        int i2;
        int i3 = 1;
        synchronized (c.a) {
            try {
                Camera.Parameters parameters = this.n.getParameters();
                if (0 < i) {
                    iArr[0] = 1;
                } else {
                    i3 = 0;
                }
                if (Build.VERSION.SDK_INT < 14 || i3 >= i || !f.g(parameters)) {
                    i2 = i3;
                } else {
                    i2 = i3 + 1;
                    iArr[i3] = 0;
                }
            } catch (RuntimeException e) {
                return 0;
            }
        }
        return i2;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    @TargetApi(14)
    public int getMaxNumExposureAreas() {
        int i = 0;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT >= 14) {
                    Camera.Parameters parameters = this.n.getParameters();
                    List<Camera.Area> meteringAreas = parameters.getMeteringAreas();
                    i = Math.min(meteringAreas == null ? 0 : meteringAreas.size(), f.d(parameters));
                }
            } catch (RuntimeException e) {
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedWhiteBalanceMode(int[] iArr, int i) {
        int i2;
        synchronized (c.a) {
            try {
                Camera.Parameters parameters = this.n.getParameters();
                List<String> supportedWhiteBalance = parameters.getSupportedWhiteBalance();
                if (supportedWhiteBalance == null) {
                    return 0;
                }
                int size = supportedWhiteBalance.size();
                int i3 = 0;
                int i4 = 0;
                while (i3 < size && i4 < i) {
                    String str = supportedWhiteBalance.get(i3);
                    if ("auto".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 0;
                    } else if ("cloudy-daylight".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 1;
                    } else if ("daylight".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 2;
                    } else if ("fluorescent".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 3;
                    } else if ("incandescent".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 4;
                    } else if ("shade".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 5;
                    } else if ("twilight".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 6;
                    } else if ("warm-fluorescent".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 7;
                    } else {
                        i2 = i4;
                    }
                    i3++;
                    i4 = i2;
                }
                if (Build.VERSION.SDK_INT >= 14 && i4 < i && f.h(parameters)) {
                    int i5 = i4 + 1;
                    iArr[i4] = 8;
                    i4 = i5;
                }
                return i4;
            } catch (RuntimeException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getSupportedSceneMode(int[] iArr, int i) {
        int i2;
        synchronized (c.a) {
            try {
                List<String> supportedSceneModes = this.n.getParameters().getSupportedSceneModes();
                if (supportedSceneModes == null) {
                    return 0;
                }
                if (j.c) {
                    return 0;
                }
                int size = supportedSceneModes.size();
                int i3 = 0;
                int i4 = 0;
                while (i3 < size && i4 < i) {
                    String str = supportedSceneModes.get(i3);
                    if ("action".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 0;
                    } else if ("auto".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 1;
                    } else if ("barcode".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 2;
                    } else if ("beach".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 3;
                    } else if ("candlelight".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 4;
                    } else if ("fireworks".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 5;
                    } else if ("landscape".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 6;
                    } else if ("night".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 7;
                    } else if ("night-portrait".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 8;
                    } else if ("party".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 9;
                    } else if ("portrait".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 10;
                    } else if ("snow".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 11;
                    } else if ("sports".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 12;
                    } else if ("steadyphoto".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 13;
                    } else if ("sunset".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 14;
                    } else if ("theatre".equals(str)) {
                        i2 = i4 + 1;
                        iArr[i4] = 15;
                    } else {
                        i2 = i4;
                    }
                    i3++;
                    i4 = i2;
                }
                return i4;
            } catch (RuntimeException e) {
                return 0;
            }
        }
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public float[] getDeviceInfo() {
        float[] fArr;
        synchronized (c.a) {
            try {
                Camera.Parameters parameters = this.n.getParameters();
                Camera.Size previewSize = parameters.getPreviewSize();
                Camera.Size pictureSize = parameters.getPictureSize();
                fArr = new float[15];
                fArr[0] = this.o;
                fArr[1] = previewSize.width;
                fArr[2] = previewSize.height;
                fArr[3] = pictureSize.width;
                fArr[4] = pictureSize.height;
                CameraDeviceInterface.a aVar = sCameraModelInfos.get(getDefaultCameraId(false));
                if (aVar != null) {
                    fArr[5] = 1.0f;
                    fArr[6] = aVar.a;
                    fArr[7] = aVar.b;
                    fArr[8] = aVar.c;
                }
                CameraDeviceInterface.a aVar2 = sCameraModelInfos.get(getDefaultCameraId(true));
                if (aVar2 != null) {
                    fArr[9] = 1.0f;
                    fArr[10] = aVar2.a;
                    fArr[11] = aVar2.b;
                    fArr[12] = aVar2.c;
                }
                Utility.a(this.m, new Point());
                fArr[13] = r0.x;
                fArr[14] = r0.y;
            } catch (RuntimeException e) {
                return null;
            }
        }
        return fArr;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getFacing() {
        int i = 0;
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT >= 9) {
                    Object b = f.b();
                    f.a(this.o, b);
                    switch (f.a(b)) {
                        case 0:
                            break;
                        case 1:
                            i = 1;
                            break;
                        default:
                            i = -2138308603;
                            break;
                    }
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x004a -> B:9:0x001d). Please report as a decompilation issue!!! */
    @Override // com.sony.smartar.CameraDeviceInterface
    public int getRotation() {
        synchronized (c.a) {
            try {
                if (Build.VERSION.SDK_INT >= 9) {
                    Object b = f.b();
                    f.a(this.o, b);
                    switch (f.b(b)) {
                        case 0:
                            break;
                        case 90:
                            r0 = 90;
                            break;
                        case 180:
                            r0 = 180;
                            break;
                        case 270:
                            r0 = 270;
                            break;
                    }
                } else {
                    int b2 = d.b(this.m);
                    boolean a2 = d.a(this.m);
                    switch (b2) {
                        case 0:
                        case 2:
                            r0 = a2 ? 90 : 0;
                            break;
                        case 1:
                        case 3:
                            r0 = a2 ? 0 : 90;
                            break;
                    }
                }
            } catch (RuntimeException e) {
            }
        }
        return r0;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public Camera getNativeDevice() {
        Camera camera;
        synchronized (c.a) {
            camera = this.n;
        }
        return camera;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public float getFovY() {
        float f = 0.0f;
        synchronized (c.a) {
            try {
                if (this.n != null) {
                    Camera.Parameters parameters = this.n.getParameters();
                    if (parameters != null) {
                        f = parameters.getVerticalViewAngle();
                    }
                }
            } catch (RuntimeException e) {
            }
        }
        return f;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getStillImageSize(int[] iArr) {
        Camera.Parameters parameters;
        int i = 0;
        synchronized (c.a) {
            try {
                if (iArr.length != 2) {
                    i = -2138308602;
                } else {
                    iArr[0] = 0;
                    iArr[1] = 0;
                    if (this.n != null && (parameters = this.n.getParameters()) != null) {
                        Camera.Size pictureSize = parameters.getPictureSize();
                        iArr[0] = pictureSize.width;
                        iArr[1] = pictureSize.height;
                    } else {
                        i = -2138308608;
                    }
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public float getFocalLength() {
        float f = 0.0f;
        synchronized (c.a) {
            try {
                if (this.n != null) {
                    Camera.Parameters parameters = this.n.getParameters();
                    if (parameters != null) {
                        f = parameters.getFocalLength();
                    }
                }
            } catch (RuntimeException e) {
                f = -2.1383086E9f;
            }
        }
        return f;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getVideoImageSize(int[] iArr) {
        Camera.Parameters parameters;
        int i = 0;
        synchronized (c.a) {
            try {
                if (iArr.length != 2) {
                    i = -2138308602;
                } else {
                    iArr[0] = 0;
                    iArr[1] = 0;
                    if (this.n != null && (parameters = this.n.getParameters()) != null) {
                        Camera.Size previewSize = parameters.getPreviewSize();
                        iArr[0] = previewSize.width;
                        iArr[1] = previewSize.height;
                    } else {
                        i = -2138308608;
                    }
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getMaxStillImageSize(int[] iArr) {
        int i = 0;
        synchronized (c.a) {
            try {
                if (iArr.length != 2) {
                    i = -2138308602;
                } else {
                    iArr[0] = 0;
                    iArr[1] = 0;
                    int[] iArr2 = new int[64];
                    int supportedStillImageSize = getSupportedStillImageSize(iArr2, 64);
                    for (int i2 = 0; i2 < supportedStillImageSize; i2 += 2) {
                        if (iArr2[i2] >= iArr[0] && iArr2[i2 + 1] >= iArr[1]) {
                            iArr[0] = iArr2[i2];
                            iArr[1] = iArr2[i2 + 1];
                        }
                    }
                    if (iArr[0] != 0) {
                        if (iArr[1] != 0) {
                        }
                    }
                    i = -2138308608;
                }
            } catch (RuntimeException e) {
                i = -2138308594;
            }
        }
        return i;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int[] getCameraAPIFeature() {
        int[] iArr;
        synchronized (c.a) {
            iArr = new int[2];
            iArr[0] = 1;
            iArr[1] = H ? 2 : -1;
        }
        return iArr;
    }

    @Override // com.sony.smartar.CameraDeviceInterface
    public int getImageSensorRotation() {
        int i;
        synchronized (c.a) {
            i = getFacing() == 1 ? 270 : 90;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(SurfaceView surfaceView) {
        String str;
        if (this.p == surfaceView) {
            return 0;
        }
        e();
        if (this.p != null) {
            this.p.getHolder().removeCallback(this.J);
        }
        this.p = surfaceView;
        if (this.p != null) {
            this.p.getHolder().addCallback(this.J);
            if (Build.VERSION.SDK_INT >= 11) {
                this.z = this.p.getHolder().getSurface().isValid();
            } else {
                try {
                    if (Build.VERSION.SDK_INT >= 9) {
                        str = "mNativeSurface";
                    } else {
                        str = "mSurface";
                    }
                    Field declaredField = Surface.class.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    this.z = declaredField.getInt(this.p.getHolder().getSurface()) != 0;
                } catch (IllegalAccessException e) {
                    return -2138308603;
                } catch (NoSuchFieldException e2) {
                    return -2138308603;
                }
            }
        } else {
            this.z = false;
        }
        d();
        return 0;
    }

    private static List<Object> a(float[] fArr, int i) {
        Object[] objArr = new Object[i];
        for (int i2 = 0; i2 < i; i2++) {
            Rect rect = new Rect((int) (fArr[(i2 * 5) + 0] * 1000.0f), (int) (fArr[(i2 * 5) + 1] * 1000.0f), (int) (fArr[(i2 * 5) + 2] * 1000.0f), (int) (fArr[(i2 * 5) + 3] * 1000.0f));
            int i3 = (int) (fArr[(i2 * 5) + 4] * 1000.0f);
            Object a2 = f.a(rect, i3);
            f.a(a2, rect);
            f.a(a2, i3);
            objArr[i2] = a2;
        }
        return Arrays.asList(objArr);
    }

    @TargetApi(16)
    private void b() {
        this.F = false;
        this.n.setAutoFocusMoveCallback(new Camera.AutoFocusMoveCallback() { // from class: com.sony.smartar.CameraDevice10.6
            @Override // android.hardware.Camera.AutoFocusMoveCallback
            public void onAutoFocusMoving(boolean z, Camera camera) {
                if (!CameraDevice10.this.A) {
                    CameraDevice10.this.F = z;
                    if (CameraDevice10.this.G && !z) {
                        CameraDevice10.this.G = false;
                        CameraDevice10.this.n.takePicture(CameraDevice10.this.L, null, null, CameraDevice10.this.M);
                    }
                }
            }
        });
    }

    private void c() {
        int a2;
        Camera.Parameters parameters = this.n.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Integer> supportedPreviewFormats = parameters.getSupportedPreviewFormats();
        List<Integer> b = b(parameters);
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        List<String> supportedSceneModes = parameters.getSupportedSceneModes();
        List<String> supportedWhiteBalance = parameters.getSupportedWhiteBalance();
        if (supportedPreviewFormats != null && supportedPreviewFormats.indexOf(17) != -1) {
            parameters.setPreviewFormat(17);
        }
        if (supportedWhiteBalance != null && supportedWhiteBalance.indexOf("auto") != -1) {
            parameters.setWhiteBalance("auto");
        }
        if (supportedSceneModes != null && supportedSceneModes.indexOf("sports") != -1 && !j.c) {
            parameters.setSceneMode("sports");
        }
        if (supportedPreviewSizes != null) {
            Camera.Size size = (Camera.Size) Utility.a(false, (Object) supportedPreviewSizes, this.a, this.b);
            parameters.setPreviewSize(size.width, size.height);
        }
        if (b != null && (a2 = a(b, 30)) != 0) {
            a(parameters, a2);
        }
        if (supportedFocusModes.indexOf("continuous-video") != -1) {
            parameters.setFocusMode("continuous-video");
        } else if (supportedFocusModes.indexOf("infinity") != -1) {
            parameters.setFocusMode("infinity");
        }
        if (this.k && Build.VERSION.SDK_INT >= 14) {
            f.a(parameters, true);
        }
        if (this.c != null && this.d != null) {
            parameters.set(this.c, this.d);
        }
        if (this.e != null && this.f != null) {
            parameters.set(this.e, this.f);
            if (Build.VERSION.SDK_INT >= 14 && f.b(parameters) > 0) {
                List c = f.c(parameters);
                if (c == null) {
                    Object a3 = f.a(new Rect(), this.g);
                    c = new ArrayList();
                    c.add(a3);
                } else {
                    Object obj = c.get(0);
                    f.a(obj, new Rect());
                    f.a(obj, this.g);
                }
                f.a(parameters, (List<Object>) c);
            }
        }
        this.n.setParameters(parameters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        boolean z;
        List<Integer> b;
        if (this.y || !this.x) {
            return;
        }
        if (this.p == null || this.z) {
            this.y = true;
            Camera.Parameters parameters = this.n.getParameters();
            f();
            Camera.Size previewSize = parameters.getPreviewSize();
            this.u = previewSize.width;
            this.v = previewSize.height;
            int previewFormat = parameters.getPreviewFormat();
            int[] iArr = new int[1];
            Utility.a(false, (List<Integer>) Collections.singletonList(Integer.valueOf(previewFormat)), iArr, 1);
            this.t = iArr[0];
            int a2 = g.a(previewSize.width, previewSize.height, previewFormat) + 1 + 4095;
            int i = a2 - (a2 & 4095);
            if (this.s.size() != 2) {
                z = true;
            } else if (this.s.get(0).length == i) {
                for (int i2 = 0; i2 < 2; i2++) {
                    this.n.addCallbackBuffer(this.s.get(i2));
                }
                z = false;
            } else {
                z = true;
            }
            if (z) {
                this.s.clear();
                for (int i3 = 0; i3 < 2; i3++) {
                    this.s.add(i3, new byte[i]);
                    this.n.addCallbackBuffer(this.s.get(i3));
                }
            }
            this.n.setPreviewCallbackWithBuffer(this.K);
            this.n.setErrorCallback(this.O);
            this.n.startPreview();
            if ((!Build.MANUFACTURER.equals("FUJITSU") || !this.l) && (b = b(parameters)) != null) {
                int a3 = a(parameters);
                int size = b.size();
                for (int i4 = 0; i4 < size; i4++) {
                    int intValue = b.get(i4).intValue();
                    if (intValue != a3) {
                        a(parameters, intValue);
                        this.n.setParameters(parameters);
                        a(parameters, a3);
                        this.n.setParameters(parameters);
                        return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        if (this.y) {
            this.y = false;
            this.n.stopPreview();
            this.n.setPreviewCallbackWithBuffer(null);
            this.n.setErrorCallback(null);
            g();
        }
    }

    private void f() {
        if (this.p != null) {
            try {
                this.n.setPreviewDisplay(this.p.getHolder());
            } catch (IOException e) {
            }
        } else if (Build.VERSION.SDK_INT >= 11) {
            this.r = h();
            this.q = f.b(this.r);
            f.a(this.n, this.q);
        }
    }

    private static int a(List<Integer> list, int i) {
        int i2;
        int size = list.size();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < size) {
            int intValue = list.get(i3).intValue();
            int abs = Math.abs(intValue - i);
            if (i5 == 0 || abs < i4) {
                i2 = intValue;
            } else {
                abs = i4;
                i2 = i5;
            }
            i3++;
            i5 = i2;
            i4 = abs;
        }
        return i5;
    }

    private void g() {
        try {
            this.n.setPreviewDisplay(null);
            if (Build.VERSION.SDK_INT >= 11 && this.q != null) {
                if (Build.VERSION.SDK_INT >= 14) {
                    f.c(this.q);
                }
                this.q = null;
                a(this.r);
                this.r = -1;
            }
        } catch (IOException e) {
        }
    }

    private static Object a(Class<?> cls, String str) {
        try {
            return i.b(cls, str);
        } catch (h e) {
            return null;
        }
    }

    private static void a(int i, Camera camera) {
        if (sCameraModelInfos.get(i) == null) {
            boolean z = camera == null;
            if (z) {
                if (Build.VERSION.SDK_INT >= 9) {
                    camera = f.a(i);
                } else {
                    camera = Camera.open();
                }
                if (camera == null) {
                    return;
                }
            }
            Camera.Parameters parameters = camera.getParameters();
            CameraDeviceInterface.a aVar = new CameraDeviceInterface.a();
            aVar.a = parameters.getFocalLength();
            aVar.b = parameters.getHorizontalViewAngle();
            aVar.c = parameters.getVerticalViewAngle();
            sCameraModelInfos.put(i, aVar);
            if (z) {
                camera.release();
            }
        }
    }

    private static void a(Camera.Parameters parameters, int i) {
        parameters.setPreviewFrameRate(i);
    }

    private static int a(Camera.Parameters parameters) {
        return parameters.getPreviewFrameRate();
    }

    private static List<Integer> b(Camera.Parameters parameters) {
        return parameters.getSupportedPreviewFrameRates();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(SurfaceHolder surfaceHolder) {
        surfaceHolder.setType(3);
    }

    @SuppressLint({"all"})
    private static int h() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(36197, iArr[0]);
        GLES20.glTexParameterf(36197, 10241, 9728.0f);
        GLES20.glTexParameterf(36197, 10240, 9728.0f);
        GLES20.glTexParameterf(36197, 10242, 33071.0f);
        GLES20.glTexParameterf(36197, 10243, 33071.0f);
        GLES20.glBindTexture(36197, 0);
        return iArr[0];
    }

    private static void a(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:5:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.hardware.Camera b(int r7) {
        /*
            r1 = 0
            r6 = 1
            boolean r0 = i()
            if (r0 == 0) goto L62
            java.lang.Class<android.hardware.Camera> r0 = android.hardware.Camera.class
            java.lang.String r2 = "openLegacy"
            r3 = 2
            java.lang.Class[] r3 = new java.lang.Class[r3]     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r4 = 0
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r3[r4] = r5     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r4 = 1
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r3[r4] = r5     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            java.lang.reflect.Method r0 = r0.getMethod(r2, r3)     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r2 = 0
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r4 = 0
            java.lang.Integer r5 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r3[r4] = r5     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r4 = 1
            int r5 = j()     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            r3[r4] = r5     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            java.lang.Object r0 = r0.invoke(r2, r3)     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
            android.hardware.Camera r0 = (android.hardware.Camera) r0     // Catch: java.lang.NoSuchMethodException -> L46 java.lang.IllegalAccessException -> L4c java.lang.IllegalArgumentException -> L52 java.lang.reflect.InvocationTargetException -> L58 java.lang.Exception -> L5e
        L39:
            if (r0 != 0) goto L69
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 9
            if (r0 < r1) goto L64
            android.hardware.Camera r0 = com.sony.smartar.f.a(r7)
        L45:
            return r0
        L46:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r1
            goto L39
        L4c:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r1
            goto L39
        L52:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r1
            goto L39
        L58:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r1
            goto L39
        L5e:
            r0 = move-exception
            r0.printStackTrace()
        L62:
            r0 = r1
            goto L39
        L64:
            android.hardware.Camera r0 = android.hardware.Camera.open()
            goto L45
        L69:
            com.sony.smartar.CameraDevice10.H = r6
            goto L45
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.smartar.CameraDevice10.b(int):android.hardware.Camera");
    }

    private static boolean i() {
        return a.b;
    }

    private static int j() {
        return a.a;
    }

    private static class a {
        private static final int a = d();
        private static final boolean b = c();

        private static boolean c() {
            try {
                for (Method method : Camera.class.getMethods()) {
                    if (method.getName().equals("openLegacy") && d() != -1) {
                        return true;
                    }
                }
                return false;
            } catch (RuntimeException e) {
                return false;
            }
        }

        private static int d() {
            int i = -1;
            try {
                for (Field field : Camera.class.getFields()) {
                    if (field.getName().equals("CAMERA_HAL_API_VERSION_1_0")) {
                        try {
                            i = field.getInt(null);
                            break;
                        } catch (IllegalAccessException e) {
                        } catch (IllegalArgumentException e2) {
                        }
                    }
                }
            } catch (RuntimeException e3) {
            }
            return i;
        }
    }
}
