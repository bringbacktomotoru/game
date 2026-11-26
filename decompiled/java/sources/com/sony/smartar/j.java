package com.sony.smartar;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/* compiled from: GuUtils.java */
/* loaded from: classes.dex */
final class j {
    public static final boolean a;
    public static final boolean b;
    public static final boolean c;
    public static final boolean d;

    static {
        Class<?> cls;
        a = Build.MANUFACTURER.indexOf("Sony") != -1;
        try {
            cls = Class.forName("com.unity3d.player.UnityPlayer", false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            cls = null;
        }
        b = cls != null;
        c = Build.MODEL.equals("P-01D");
        d = Build.MODEL.equals("SC-01C");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> E a(View view, Class<E> cls) {
        if (!cls.isInstance(view)) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    E e = (E) a(viewGroup.getChildAt(i), cls);
                    if (e != null) {
                        return e;
                    }
                }
            }
            return null;
        }
        return view;
    }
}
