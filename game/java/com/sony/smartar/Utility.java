package com.sony.smartar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

@TargetApi(MotionEventCompat.AXIS_WHEEL)
/* loaded from: classes.dex */
class Utility {
    public static final int SDK_INT = Build.VERSION.SDK_INT;

    Utility() {
    }

    public static int a(boolean z, int i) {
        switch (i) {
            case 0:
                break;
            case 1:
                if (z) {
                }
                break;
            case 2:
                d.b("unsupported mode: " + i);
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                d.b("unexpected value: " + i);
                break;
        }
        return -1;
    }

    public static int a(boolean z, Object obj, int[] iArr, int i) {
        List list;
        List list2;
        int i2;
        if (obj == null) {
            return 0;
        }
        if (z) {
            list2 = (List) obj;
            list = null;
            i2 = Math.min(list2.size(), i);
        } else {
            List list3 = (List) obj;
            int min = Math.min(list3.size(), i);
            list = list3;
            list2 = null;
            i2 = min;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (z) {
                Size size = (Size) list2.get(i3);
                iArr[(i3 * 2) + 0] = size.getWidth();
                iArr[(i3 * 2) + 1] = size.getHeight();
            } else {
                Camera.Size size2 = (Camera.Size) list.get(i3);
                iArr[(i3 * 2) + 0] = size2.width;
                iArr[(i3 * 2) + 1] = size2.height;
            }
        }
        return i2;
    }

    public static int a(boolean z, List<Integer> list, int[] iArr, int i) {
        int i2;
        if (list == null) {
            return 0;
        }
        int size = list.size();
        int i3 = 0;
        int i4 = 0;
        while (i3 < size && i4 < i) {
            switch (list.get(i3).intValue()) {
                case 1:
                    i2 = i4 + 1;
                    iArr[i4] = 3;
                    continue;
                case 9:
                    i2 = i4 + 1;
                    iArr[i4] = 0;
                    continue;
                case MotionEventCompat.AXIS_LTRIGGER /* 17 */:
                    if (!z) {
                        i2 = i4 + 1;
                        iArr[i4] = 1;
                        continue;
                    }
                    break;
                case MotionEventCompat.AXIS_GENERIC_4 /* 35 */:
                    if (z) {
                        i2 = i4 + 1;
                        iArr[i4] = 1;
                        continue;
                    }
                    break;
                case 256:
                    i2 = i4 + 1;
                    iArr[i4] = 4;
                    continue;
            }
            i2 = i4;
            i3++;
            i4 = i2;
        }
        return i4;
    }

    public static Object a(boolean z, Object obj, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        Object obj2;
        int i8;
        float f;
        Object obj3 = null;
        float f2 = 0.0f;
        List list = (List) obj;
        int size = list.size();
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        while (i9 < size) {
            Object obj4 = list.get(i9);
            if (z) {
                int width = ((Size) obj4).getWidth();
                i3 = ((Size) obj4).getHeight();
                i4 = width;
            } else {
                int i12 = ((Camera.Size) obj4).width;
                i3 = ((Camera.Size) obj4).height;
                i4 = i12;
            }
            if (j.d && i4 == 320 && i3 == 240) {
                f = f2;
                i8 = i10;
                obj2 = obj3;
                i7 = i11;
            } else {
                float abs = (Math.abs(i3 - i2) / Math.min(i3, i2)) + (Math.abs(i4 - i) / Math.min(i4, i));
                if (obj3 == null) {
                    i5 = i10;
                    i6 = i11;
                } else if (z) {
                    i6 = ((Size) obj3).getWidth();
                    i5 = ((Size) obj3).getHeight();
                } else {
                    i6 = ((Camera.Size) obj3).width;
                    i5 = ((Camera.Size) obj3).height;
                }
                if (obj3 == null || abs < f2 || (Math.abs(abs - f2) < 1.0E-5f && i4 * i3 < i6 * i5)) {
                    i7 = i6;
                    obj2 = obj4;
                    i8 = i5;
                    f = abs;
                } else {
                    i8 = i5;
                    f = f2;
                    obj2 = obj3;
                    i7 = i6;
                }
            }
            i9++;
            i10 = i8;
            i11 = i7;
            obj3 = obj2;
            f2 = f;
        }
        if (obj3 == null) {
            throw new RuntimeException("no available preview size: " + list.size());
        }
        return obj3;
    }

    public static int isSony() {
        return Build.MANUFACTURER.toUpperCase(Locale.ENGLISH).contains("SONY") ? 1 : 0;
    }

    public static int[] isAndroidCamera2Available(Context context, boolean z) {
        int[] iArr = {0, 1, -1};
        if (SDK_INT < 21) {
            return iArr;
        }
        try {
            CameraCharacteristics cameraCharacteristics = ((CameraManager) context.getSystemService("camera")).getCameraCharacteristics(CameraDevice20.getDefaultCameraId2withString(context, z)[1]);
            iArr[1] = 2;
            switch (((Integer) cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue()) {
                case 0:
                    iArr[0] = 1;
                    iArr[2] = 0;
                    break;
                case 1:
                    iArr[0] = 1;
                    iArr[2] = 1;
                    break;
                case 3:
                    iArr[0] = 1;
                    iArr[2] = 3;
                    break;
            }
            return iArr;
        } catch (CameraAccessException e) {
            return iArr;
        } catch (Exception e2) {
            return iArr;
        }
    }

    public static void a(Context context, Point point) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Display.class.getMethod("getRealSize", Point.class).invoke(defaultDisplay, point);
                return;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RuntimeException(e3);
            }
        }
        if (Build.VERSION.SDK_INT >= 13) {
            try {
                Method method = Display.class.getMethod("getRawWidth", new Class[0]);
                Method method2 = Display.class.getMethod("getRawHeight", new Class[0]);
                point.x = ((Integer) method.invoke(defaultDisplay, new Object[0])).intValue();
                point.y = ((Integer) method2.invoke(defaultDisplay, new Object[0])).intValue();
                return;
            } catch (IllegalAccessException e4) {
                throw new RuntimeException(e4);
            } catch (NoSuchMethodException e5) {
                throw new RuntimeException(e5);
            } catch (InvocationTargetException e6) {
                throw new RuntimeException(e6);
            }
        }
        point.x = defaultDisplay.getWidth();
        point.y = defaultDisplay.getHeight();
    }
}
