package com.appsflyer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ls.sakana.SakanaView;

/* loaded from: classes.dex */
final class c {

    /* renamed from: ˎ, reason: contains not printable characters */
    private static int f103 = 0;

    /* renamed from: ॱ, reason: contains not printable characters */
    private static int f105 = 1;

    /* renamed from: ˋ, reason: contains not printable characters */
    private static char[] f102 = {12953, 46792, 14880, 49040, 9199, 42823, 10408, 44100, 4220, 38355, 6456, 33431, 1760, 35414, 4019, 62228, 30561, 63720, 31792, 57447, 26060, 59691, 21148, 55010, 23111, 57272, 17165, 51039, 18641, 52272, 45469, 13821, 47453, 8852, 36797, '0', 47217, 15407, 45261, 13675, 43357, 11667, 41585, 9911, 39580, 7970, 37883, 2122, 35880, 163, 34113, 31225, 64923, 29292, 'b', 33855, 2270, 36223, 4377, 38323, 6660, 40615, 8905, 42794, 11161, 45113, 13321, 47268, 15645, 49585, 17876, 51800, 20136, 53919, 22380, 56275, 24608, 58434, 26804, 60695, 29117, 62860, 31354, 65174, 33589, 1875, 'a', 33840, 2264, 36200, 4375, 38335, 6736, 40636, 8863, 42813, 11138, 45128, 13321, 47282, 15696, 49639, 17810, 51783, 20177, 53915, 22326, 56279, 24691, 58391, 26786, 'a', 33840, 2264, 36200, 4375, 38335, 6736, 40636, 8856, 42799, 11230, 45166, 13343, 47271, 15702, 49639, 17870, 51821, 20217, 53908, 22315, 56281, 24678, 'a', 33840, 2264, 36200, 4375, 38335, 6736, 40636, 8863, 42813, 11138, 45131, 13339, 47295, 15690, 49633, 17844, 51807, 20207, 53905, 46291, 12418, 48234, 14810, 42405, 8461, 44770, 10766, 38444, 5017, 40810, 1174, 32911, 3078, 35327, '.', '\\', 33904, 'j', 33855, 2250, 36219, 4438, 38335, 6747, 40636, 8836, 42787, 11228, 45166, 13313, 47284, '/', 33853, 2269, 36217, 4368, 38323, '.', 33908, 2195, 36166, 4363, 38396, 6684, 40636, 8922, 42855, 'C', 33846, 2265, 36217, 4371, 38309, 6721, 40703, 8885, 42806, 11215, 45167, 13336, 47282, 15693, 49645, 17806, 'F', 33855, 2261, 36214, 4381, 38322, 6676, 40678, 8863, 42862, 11211, 45167, 13340, 47334, 15687, 49635, 17795, 51798, 20217, 53978, 22312, 56279, 24672, 58394, 26864, 60761, 29157, 62878, 31264, 65158, 33633, 1818, 35747, 4219, 37900, 6318, 40273, 8697, 42394, 10856, 44688, 'g', 33851, 2248, 36179, 4374, 38309, 6720, 40691, 8860, 42786, 11209, 45166, 13353, 47286, 15700, 49646, 17801, 51805, 20221, 53902, 22321, 56281, 24698, 58369, 'C', 33846, 2265, 36217, 4371, 38309, 6721, 40703, 8866, 42795, 11210, 45158, 13325, 47269, 15696, 49607, 17816, 51805, 20217, 53898, 22316, 56287, 24699, 58396, 37970, 4139, 40129, 6498, 34057, 422, 36352, 2799, 46730, 13100, 49111, 9333, 40985, 11506, 43330, 22003, 53650, 24134, 56045, 18061, 49976, 20423, 62564, 28742, 64681, 31071, 58860, 24982, 60979, 27350, 5936, 37633, 8125, 33918, 0, 36078, 2377, 46586, 12675, 48675, 15060, 42862, 8977, 44977, 21586, 53416, 23760};

    /* renamed from: ˏ, reason: contains not printable characters */
    private static long f104 = 2406303584873383006L;

    c() {
    }

    @Nullable
    /* renamed from: ˊ, reason: contains not printable characters */
    static String m99(Context context, long j) {
        String intern;
        String intern2;
        String intern3;
        String intern4;
        String intern5;
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        if (m103(m101(34, 0, (char) 13048).intern())) {
            int i = f103 + SakanaView.MOUSE_MOVED;
            f105 = i % 128;
            if (i % 2 == 0) {
            }
            intern = m101(1, 34, (char) 36748).intern();
        } else {
            intern = m101(1, 35, (char) 0).intern();
        }
        sb2.append(intern);
        StringBuilder sb4 = new StringBuilder();
        String packageName = context.getPackageName();
        String m104 = m104(packageName);
        sb2.append(m101(1, 34, (char) 36748).intern());
        sb4.append(m104);
        switch (m105(context) == null ? 'Z' : '!') {
            case 'Z':
                sb2.append(m101(1, 35, (char) 0).intern());
                sb4.append(packageName);
                break;
            default:
                sb2.append(m101(1, 34, (char) 36748).intern());
                sb4.append(packageName);
                break;
        }
        String m98 = m98(context);
        if (m98 == null) {
            sb2.append(m101(1, 35, (char) 0).intern());
            sb4.append(packageName);
        } else {
            sb2.append(m101(1, 34, (char) 36748).intern());
            sb4.append(m98);
        }
        sb4.append(m102(context, packageName));
        sb.append(sb4.toString());
        try {
            long j2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(m101(18, 36, (char) 47112).intern(), Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            sb.append(simpleDateFormat.format(new Date(j2)));
            sb.append(j);
            if (m103(m101(25, 86, (char) 0).intern())) {
                int i2 = f105 + 13;
                f103 = i2 % 128;
                intern2 = i2 % 2 != 0 ? m101(1, 99, (char) 36748).intern() : m101(1, 34, (char) 36748).intern();
            } else {
                intern2 = m101(1, 35, (char) 0).intern();
            }
            sb3.append(intern2);
            switch (m103(m101(23, SakanaView.MODAL_MOUSE_PRESSED, (char) 0).intern()) ? 'O' : 'D') {
                case 'D':
                    intern3 = m101(1, 35, (char) 0).intern();
                    break;
                default:
                    int i3 = f103 + 1;
                    f105 = i3 % 128;
                    switch (i3 % 2 == 0) {
                        case false:
                            intern3 = m101(1, 34, (char) 36748).intern();
                            break;
                        default:
                            intern3 = m101(1, 45, (char) 36748).intern();
                            break;
                    }
            }
            sb3.append(intern3);
            if (m103(m101(20, 134, (char) 0).intern())) {
                int i4 = f103 + 87;
                f105 = i4 % 128;
                if (i4 % 2 == 0) {
                }
                intern4 = m101(1, 34, (char) 36748).intern();
            } else {
                intern4 = m101(1, 35, (char) 0).intern();
            }
            sb3.append(intern4);
            switch (m103(m101(15, 154, (char) 46258).intern()) ? ')' : 'H') {
                case 'H':
                    intern5 = m101(1, 35, (char) 0).intern();
                    break;
                default:
                    intern5 = m101(1, 34, (char) 36748).intern();
                    break;
            }
            sb3.append(intern5);
            String m178 = C0006r.m178(C0006r.m176(sb.toString()));
            String obj = sb2.toString();
            StringBuilder sb5 = new StringBuilder(m178);
            sb5.setCharAt(17, Integer.toString(Integer.parseInt(obj, 2), 16).charAt(0));
            String obj2 = sb5.toString();
            String obj3 = sb3.toString();
            StringBuilder sb6 = new StringBuilder(obj2);
            sb6.setCharAt(27, Integer.toString(Integer.parseInt(obj3, 2), 16).charAt(0));
            return m100(sb6.toString(), Long.valueOf(j));
        } catch (PackageManager.NameNotFoundException e) {
            return m101(32, 54, (char) 0).intern();
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:202)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:115)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:92)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:115)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:92)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:100)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:49)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    /* renamed from: ˊ, reason: contains not printable characters */
    private static java.lang.String m100(java.lang.String r12, java.lang.Long r13) {
        /*
            Method dump skipped, instructions count: 278
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.c.m100(java.lang.String, java.lang.Long):java.lang.String");
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private static boolean m103(String str) {
        int i = f103 + 39;
        f105 = i % 128;
        if (i % 2 == 0) {
        }
        try {
            Class.forName(str);
            int i2 = f103 + 3;
            f105 = i2 % 128;
            switch (i2 % 2 == 0 ? (char) 11 : (char) 26) {
                case 11:
                    Object[] objArr = null;
                    int length = objArr.length;
                    break;
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /* renamed from: ˎ, reason: contains not printable characters */
    private static String m104(String str) {
        int i;
        switch (str.contains(m101(1, 169, (char) 0).intern())) {
            case false:
                return str;
            default:
                String[] split = str.split(m101(2, 170, (char) 0).intern());
                int length = split.length;
                StringBuilder sb = new StringBuilder();
                sb.append(split[length - 1]).append(m101(1, 169, (char) 0).intern());
                int i2 = f105 + 67;
                f103 = i2 % 128;
                switch (i2 % 2 != 0) {
                    case false:
                        i = 1;
                        break;
                    default:
                        i = 1;
                        break;
                }
                while (i < length - 1) {
                    int i3 = f103 + 27;
                    f105 = i3 % 128;
                    switch (i3 % 2 == 0 ? '>' : '?') {
                        case '?':
                            sb.append(split[i]).append(m101(1, 169, (char) 0).intern());
                            i++;
                            break;
                        default:
                            sb.append(split[i]).append(m101(1, 4461, (char) 0).intern());
                            i += 54;
                            break;
                    }
                }
                sb.append(split[0]);
                return sb.toString();
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x0070. Please report as an issue. */
    /* renamed from: ˏ, reason: contains not printable characters */
    private static String m105(Context context) {
        String str = null;
        switch (System.getProperties().containsKey(m101(14, 172, (char) 0).intern()) ? 'O' : 'T') {
            default:
                int i = f105 + 55;
                f103 = i % 128;
                if (i % 2 != 0) {
                }
                try {
                    Matcher matcher = Pattern.compile(m101(10, 192, (char) 0).intern()).matcher(context.getCacheDir().getPath().replace(m101(6, 186, (char) 0).intern(), ""));
                    if (matcher.find()) {
                        str = matcher.group(1);
                        int i2 = f105 + 53;
                        f103 = i2 % 128;
                        switch (i2 % 2 != 0) {
                        }
                    }
                } catch (Exception e) {
                    s.m186().m192(m101(17, SakanaView.KEY_REPEATED, (char) 0).intern(), new StringBuilder().append(m101(41, 219, (char) 0).intern()).append(e).toString());
                }
            case 'T':
                return str;
        }
    }

    /* renamed from: ˊ, reason: contains not printable characters */
    private static String m98(Context context) {
        String str;
        int i = f103 + 43;
        f105 = i % 128;
        try {
            switch (i % 2 == 0) {
                case true:
                    str = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).packageName;
                    break;
                default:
                    str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
                    break;
            }
            int i2 = f105 + 69;
            f103 = i2 % 128;
            if (i2 % 2 != 0) {
            }
            return str;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /* renamed from: ˋ, reason: contains not printable characters */
    private static String m102(Context context, String str) {
        try {
            Iterator it = ((List) PackageManager.class.getDeclaredMethod(m101(24, 260, (char) 0).intern(), Integer.TYPE).invoke(context.getPackageManager(), 0)).iterator();
            int i = f105 + 49;
            f103 = i % 128;
            if (i % 2 != 0) {
            }
            while (true) {
                switch (it.hasNext() ? (char) 3 : (char) 23) {
                    case MotionEventCompat.AXIS_BRAKE /* 23 */:
                        int i2 = f103 + 57;
                        f105 = i2 % 128;
                        if (i2 % 2 == 0) {
                        }
                        break;
                    default:
                        switch (((PackageItemInfo) ((ApplicationInfo) it.next())).packageName.equals(str) ? (char) 27 : (char) 31) {
                            case MotionEventCompat.AXIS_RELATIVE_X /* 27 */:
                                int i3 = f105 + 21;
                                f103 = i3 % 128;
                                if (i3 % 2 != 0) {
                                }
                                return Boolean.TRUE.toString();
                        }
                }
            }
        } catch (IllegalAccessException e) {
            s.m186().m192(m101(24, 284, (char) 0).intern(), new StringBuilder().append(m101(47, 308, (char) 37908).intern()).append(e).toString());
        } catch (NoSuchMethodException e2) {
            s.m186().m192(m101(24, 284, (char) 0).intern(), new StringBuilder().append(m101(47, 308, (char) 37908).intern()).append(e2).toString());
        } catch (InvocationTargetException e3) {
            s.m186().m192(m101(24, 284, (char) 0).intern(), new StringBuilder().append(m101(47, 308, (char) 37908).intern()).append(e3).toString());
        }
        return Boolean.FALSE.toString();
    }

    static class a extends HashMap<String, Object> {

        /* renamed from: ˎ, reason: contains not printable characters */
        private final Map<String, Object> f110;

        /* renamed from: ॱ, reason: contains not printable characters */
        private final Context f111;

        /* renamed from: ˋ, reason: contains not printable characters */
        private static int f108 = 0;

        /* renamed from: ʽ, reason: contains not printable characters */
        private static int f106 = 1;

        /* renamed from: ˊ, reason: contains not printable characters */
        private static char[] f107 = {'a', 31764, 63675, 29986, 61857, 28247, 60105, 26477, 58340, 24675, 56345, 22678, 'b', 31744, 63621, 30008, 61868, 14269, 19406, 53064, 17127, 50793, 22920, 56593, 20649, 11773, 20865, 54548, 'f', 31763, 63629, 30010, 61869, 28254, 60044, 26489, 58357, 24684, 56337, 22676, 54585, 20926, 52821, 19136, 51015, 17330, 49263, 15379, 47246, 13690, 45479, 11867, 43721, 9986, 41955, 8303, 39948, 6274, 38268, 4523, 36408, 2769, 34625, 998, 32892, 64531, 30851, 62768, 29162, 61026, 36821, 62377, 30524, 64209, 32335, 57789, 25899, 42970, 56231, 24362, 53913, 22016, 51658, 19825, 49367, 17474, 51165, 31648, 65310, 29317, 62978, 27109, 42728, 55936, 24064, 54180, 22309, 51423, 59431, 37970, 4300, 40315, 6636, 34335, 717, 36664, 2996, 34861, 13392, 45269, 15736, 47615, 9748, 41601, 12038, 44019, 10286, 54354, 20687, 56635, 23035, 50718, 17053, 53014, 19376, 51303, 29774, 61634, 32105, 63975, 26145, 58006, 28445, 60340, 26668, 5195, 37081, 7542, 39422, 1645, 33487, 3911, 42136, 55529, 23676, 53705, 21843, 51877, 20070, 50129, 18270, 50376, 30954, 64549, 29075, 62806, 27380, 61028, 25579, 59216, 48523, 49654, 17770, 51406, 19533, 54201, 22306, 56026, 24083, 56710, 25066, 58729, 26844, 60500, 29688, 63269, 31401, 65036, 32135, 33267, 1388, 34974, 3172, 37781, 5902, 39580, 7739, 40382, 8651, 42335, 10485, 44140, 13291, 46870, 14985, 48697, 15782, 52335, 45068, 13458, 47421, 15798, 41555, 9942, 43889, 12286, 44139, 4106, 'x', 31818, 63698, 's', 31767, 63626, 29989, 61863, 28232, 'b', 64601, 32885, 45947, 53084, 18781, 13689, 43277, 54569, 20990, 56333, 22738, 51040, 17335, 52804, 19083, 51547, 29997, 61934, 31835, 63701, 26422, 58280};

        /* renamed from: ˏ, reason: contains not printable characters */
        private static long f109 = 2756019177251896434L;

        a(Map<String, Object> map, Context context) {
            this.f110 = map;
            this.f111 = context;
            put(m107(), m109());
        }

        /* renamed from: ˏ, reason: contains not printable characters */
        private static StringBuilder m108(@NonNull String... strArr) throws Exception {
            int i;
            String[] strArr2;
            int i2;
            ArrayList arrayList = new ArrayList();
            int i3 = 0;
            while (i3 < 3) {
                int i4 = f108 + 63;
                f106 = i4 % 128;
                switch (i4 % 2 == 0) {
                    case false:
                        arrayList.add(Integer.valueOf(strArr[i3].length()));
                        i2 = i3 + 1;
                        break;
                    default:
                        arrayList.add(Integer.valueOf(strArr[i3].length()));
                        i2 = i3 + 31;
                        break;
                }
                i3 = i2;
            }
            Collections.sort(arrayList);
            int intValue = ((Integer) arrayList.get(0)).intValue();
            StringBuilder sb = new StringBuilder();
            for (int i5 = 0; i5 < intValue; i5++) {
                int i6 = f106 + 99;
                f108 = i6 % 128;
                switch (i6 % 2 != 0 ? '9' : 'S') {
                    case 'S':
                        i = 0;
                        strArr2 = strArr;
                        break;
                    default:
                        i = 1;
                        strArr2 = strArr;
                        break;
                }
                int i7 = f108 + 67;
                f106 = i7 % 128;
                if (i7 % 2 == 0) {
                }
                int i8 = i;
                Integer num = null;
                while (i8 < 3) {
                    int i9 = f108 + 87;
                    f106 = i9 % 128;
                    if (i9 % 2 == 0) {
                    }
                    int charAt = strArr2[i8].charAt(i5);
                    switch (num == null ? '\\' : '\'') {
                        case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                            charAt ^= num.intValue();
                            break;
                        default:
                            int i10 = f108 + 55;
                            f106 = i10 % 128;
                            if (i10 % 2 == 0) {
                            }
                            int i11 = f106 + 39;
                            f108 = i11 % 128;
                            if (i11 % 2 != 0) {
                            }
                            break;
                    }
                    i8++;
                    num = Integer.valueOf(charAt);
                }
                sb.append(Integer.toHexString(num.intValue()));
            }
            return sb;
        }

        @NonNull
        /* renamed from: ˏ, reason: contains not printable characters */
        private String m107() {
            int i;
            try {
                String num = Integer.toString(Build.VERSION.SDK_INT);
                String obj = this.f110.get(m106(12, 0, (char) 0).intern()).toString();
                String obj2 = this.f110.get(m106(5, 12, (char) 0).intern()).toString();
                if (obj2 == null) {
                    obj2 = m106(8, 17, (char) 14323).intern();
                    int i2 = f106 + 119;
                    f108 = i2 % 128;
                    if (i2 % 2 != 0) {
                    }
                }
                StringBuilder sb = new StringBuilder(obj);
                sb.reverse();
                StringBuilder m108 = m108(num, obj2, sb.toString());
                int length = m108.length();
                if (length > 4) {
                    int i3 = f106 + 65;
                    f108 = i3 % 128;
                    switch (i3 % 2 != 0) {
                        case true:
                            m108.delete(5, length);
                            break;
                        default:
                            m108.delete(4, length);
                            break;
                    }
                } else {
                    while (length < 4) {
                        int i4 = f106 + 119;
                        f108 = i4 % 128;
                        switch (i4 % 2 != 0 ? (char) 16 : ']') {
                            case 16:
                                i = length + 66;
                                m108.append('H');
                                break;
                            default:
                                i = length + 1;
                                m108.append('1');
                                break;
                        }
                        int i5 = f106 + SakanaView.MOUSE_CANCELLED;
                        f108 = i5 % 128;
                        if (i5 % 2 != 0) {
                        }
                        length = i;
                    }
                }
                m108.insert(0, m106(3, 25, (char) 11670).intern());
                return m108.toString();
            } catch (Exception e) {
                AFLogger.afRDLog(new StringBuilder().append(m106(42, 28, (char) 0).intern()).append(e).toString());
                return m106(7, 70, (char) 36798).intern();
            }
        }

        /* renamed from: ॱ, reason: contains not printable characters */
        private String m109() {
            String obj;
            int intExtra;
            int i = -2700;
            int i2 = 1;
            try {
                obj = new StringBuilder().append("").append(C0006r.m176(new StringBuilder().append(this.f110.get(m106(12, 0, (char) 0).intern()).toString()).append(this.f110.get(m106(15, 77, (char) 42940).intern()).toString()).append(m106(6, 92, (char) 42716).intern()).toString()).substring(0, 16)).toString();
            } catch (Exception e) {
                AFLogger.afRDLog(new StringBuilder().append(m106(44, 98, (char) 59457).intern()).append(e).toString());
                obj = new StringBuilder().append("").append(m106(18, 142, (char) 42234).intern()).toString();
            }
            try {
                Intent registerReceiver = this.f111.registerReceiver(null, new IntentFilter(m106(37, 160, (char) 48618).intern()));
                switch (registerReceiver != null) {
                    case false:
                        break;
                    default:
                        int i3 = f106 + 9;
                        f108 = i3 % 128;
                        switch (i3 % 2 == 0) {
                            case false:
                                intExtra = registerReceiver.getIntExtra(m106(26, 19887, (char) 52251).intern(), 22337);
                                break;
                            default:
                                intExtra = registerReceiver.getIntExtra(m106(11, 197, (char) 52251).intern(), -2700);
                                break;
                        }
                        i = intExtra;
                        break;
                }
                switch (this.f111.getApplicationInfo().nativeLibraryDir.contains(m106(3, 208, (char) 0).intern())) {
                    case false:
                        i2 = 0;
                        break;
                    default:
                        int i4 = f108 + 37;
                        f106 = i4 % 128;
                        if (i4 % 2 == 0) {
                        }
                        break;
                }
                return new StringBuilder().append(obj).append(C0004c.m110(new StringBuilder().append(m106(1, 217, (char) 0).intern()).append(i).append(m106(2, 218, (char) 64639).intern()).append(i2).append(m106(2, 220, (char) 45917).intern()).append(((SensorManager) this.f111.getSystemService(m106(6, 211, (char) 0).intern())).getSensorList(-1).size()).append(m106(2, 222, (char) 18811).intern()).append(this.f110.size()).toString())).toString();
            } catch (Exception e2) {
                AFLogger.afRDLog(new StringBuilder().append(m106(44, 98, (char) 59457).intern()).append(e2).toString());
                return new StringBuilder().append(obj).append(m106(16, 224, (char) 43371).intern()).toString();
            }
        }

        /* renamed from: com.appsflyer.c$a$c, reason: collision with other inner class name */
        static class C0004c {

            /* renamed from: ˊ, reason: contains not printable characters */
            private long f112;

            /* renamed from: ˏ, reason: contains not printable characters */
            private final Object f113;

            /* renamed from: ॱ, reason: contains not printable characters */
            private String f114;

            C0004c() {
            }

            @NonNull
            /* renamed from: ˋ, reason: contains not printable characters */
            static String m110(String str) throws Exception {
                byte[] bytes = str.getBytes();
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) (bytes[i] ^ ((i % 2) + 42));
                }
                StringBuilder sb = new StringBuilder();
                for (byte b : bytes) {
                    String hexString = Integer.toHexString(b);
                    if (hexString.length() == 1) {
                        hexString = "0".concat(String.valueOf(hexString));
                    }
                    sb.append(hexString);
                }
                return sb.toString();
            }

            C0004c(long j, String str) {
                this.f113 = new Object();
                this.f112 = 0L;
                this.f114 = "";
                this.f112 = j;
                this.f114 = str;
            }

            C0004c(String str) {
                this(System.currentTimeMillis(), str);
            }

            @NonNull
            /* renamed from: ˏ, reason: contains not printable characters */
            static C0004c m112(String str) {
                if (str == null) {
                    return new C0004c(0L, "");
                }
                String[] split = str.split(",");
                if (split.length >= 2) {
                    return new C0004c(Long.parseLong(split[0]), split[1]);
                }
                return new C0004c(0L, "");
            }

            /* renamed from: ˏ, reason: contains not printable characters */
            final boolean m114(C0004c c0004c) {
                return m111(c0004c.f112, c0004c.f114);
            }

            /* renamed from: ˋ, reason: contains not printable characters */
            private boolean m111(long j, String str) {
                synchronized (this.f113) {
                    if (str != null) {
                        if (!str.equals(this.f114)) {
                            if (j - this.f112 > 2000) {
                                this.f112 = j;
                                this.f114 = str;
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }

            public final String toString() {
                return new StringBuilder().append(this.f112).append(",").append(this.f114).toString();
            }

            /* renamed from: ˏ, reason: contains not printable characters */
            final String m113() {
                return this.f114;
            }
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:202)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:115)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
            	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
            	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:92)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:49)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
            */
        /* renamed from: ˊ, reason: contains not printable characters */
        private static java.lang.String m106(int r10, int r11, char r12) {
            /*
                r1 = 0
                int r0 = com.appsflyer.c.a.f106
                int r0 = r0 + 109
                int r2 = r0 % 128
                com.appsflyer.c.a.f108 = r2
                int r0 = r0 % 2
                if (r0 == 0) goto Ld
            Ld:
                char[] r3 = new char[r10]
                r2 = r1
            L10:
                if (r2 >= r10) goto L69
                r0 = r1
            L13:
                switch(r0) {
                    case 1: goto L3d;
                    default: goto L16;
                }
            L16:
                int r0 = com.appsflyer.c.a.f108
                int r0 = r0 + 13
                int r4 = r0 % 128
                com.appsflyer.c.a.f106 = r4
                int r0 = r0 % 2
                if (r0 != 0) goto L66
                r0 = 56
            L24:
                switch(r0) {
                    case 56: goto L50;
                    default: goto L27;
                }
            L27:
                char[] r0 = com.appsflyer.c.a.f107
                int r4 = r11 + r2
                char r0 = r0[r4]
                long r4 = (long) r0
                long r6 = (long) r2
                long r8 = com.appsflyer.c.a.f109
                long r6 = r6 * r8
                long r4 = r4 ^ r6
                long r6 = (long) r12
                long r4 = r4 ^ r6
                int r0 = (int) r4
                char r0 = (char) r0
                r3[r2] = r0
                int r0 = r2 + 1
                r2 = r0
                goto L10
            L3d:
                java.lang.String r0 = new java.lang.String
                r0.<init>(r3)
                int r1 = com.appsflyer.c.a.f106
                int r1 = r1 + 69
                int r2 = r1 % 128
                com.appsflyer.c.a.f108 = r2
                int r1 = r1 % 2
                if (r1 == 0) goto L4e
            L4e:
            L4f:
                return r0
            L50:
                char[] r0 = com.appsflyer.c.a.f107
                int r4 = r11 + r2
                char r0 = r0[r4]
                long r4 = (long) r0
                long r6 = (long) r2
                long r8 = com.appsflyer.c.a.f109
                long r6 = r6 ^ r8
                long r4 = r4 - r6
                long r6 = (long) r12
                long r4 = r4 / r6
                int r0 = (int) r4
                char r0 = (char) r0
                r3[r2] = r0
                int r0 = r2 + 73
                r2 = r0
                goto L10
            L66:
                r0 = 52
                goto L24
            L69:
                r0 = 1
                goto L13
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.c.a.m106(int, int, char):java.lang.String");
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:202)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:115)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:92)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:49)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    /* renamed from: ˋ, reason: contains not printable characters */
    private static java.lang.String m101(int r10, int r11, char r12) {
        /*
            r1 = 0
            char[] r3 = new char[r10]
            int r0 = com.appsflyer.c.f105
            int r0 = r0 + 91
            int r2 = r0 % 128
            com.appsflyer.c.f103 = r2
            int r0 = r0 % 2
            if (r0 == 0) goto L62
            r0 = 16
        L11:
            switch(r0) {
                case 16: goto L48;
                default: goto L14;
            }
        L14:
            r2 = r1
        L15:
            if (r2 >= r10) goto L60
            r0 = r1
        L18:
            switch(r0) {
                case 7: goto L42;
                default: goto L1b;
            }
        L1b:
            int r0 = com.appsflyer.c.f103
            int r0 = r0 + 73
            int r4 = r0 % 128
            com.appsflyer.c.f105 = r4
            int r0 = r0 % 2
            if (r0 != 0) goto L65
            r0 = 10
        L29:
            switch(r0) {
                case 10: goto L4a;
                default: goto L2c;
            }
        L2c:
            char[] r0 = com.appsflyer.c.f102
            int r4 = r11 + r2
            char r0 = r0[r4]
            long r4 = (long) r0
            long r6 = (long) r2
            long r8 = com.appsflyer.c.f104
            long r6 = r6 * r8
            long r4 = r4 ^ r6
            long r6 = (long) r12
            long r4 = r4 ^ r6
            int r0 = (int) r4
            char r0 = (char) r0
            r3[r2] = r0
            int r0 = r2 + 1
            r2 = r0
            goto L15
        L42:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r3)
            return r0
        L48:
            r2 = r1
            goto L15
        L4a:
            char[] r0 = com.appsflyer.c.f102
            int r4 = r11 - r2
            char r0 = r0[r4]
            long r4 = (long) r0
            long r6 = (long) r2
            long r8 = com.appsflyer.c.f104
            long r6 = r6 & r8
            long r4 = r4 * r6
            long r6 = (long) r12
            long r4 = r4 ^ r6
            int r0 = (int) r4
            char r0 = (char) r0
            r3[r2] = r0
            int r0 = r2 + 119
            r2 = r0
            goto L15
        L60:
            r0 = 7
            goto L18
        L62:
            r0 = 66
            goto L11
        L65:
            r0 = 8
            goto L29
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsflyer.c.m101(int, int, char):java.lang.String");
    }
}
