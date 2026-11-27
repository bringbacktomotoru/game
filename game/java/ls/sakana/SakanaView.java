package ls.sakana;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.appsflyer.share.Constants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import ls.sakana.iap.IabHelper;
import ls.sakana.iap.IabResult;
import ls.sakana.iap.Inventory;
import ls.sakana.iap.Purchase;
import ls.sakana.iap.SkuDetails;

/* loaded from: classes.dex */
public class SakanaView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    public static final int ACCEL_CHANGED = 1400;
    public static final int ACTION_EVENT = 4;
    public static final int ACTION_PERFORMED = 400;
    public static final int ACTIVATED = 2;
    public static final int ALT_GRAPH_MASK = 32;
    public static final int ALT_MASK = 8;
    public static final int APPACTIVE_ACTIVE = 1;
    public static final int APPACTIVE_BACKGROUND = 3;
    public static final int APPACTIVE_INACTIVE = 2;
    public static final int APPACTIVE_NOTLAUNCH = 0;
    public static final int APPACTIVE_SUSPENDED = 4;
    public static final int APP_EVENT = 0;
    public static final int BUTTON1_MASK = 64;
    public static final int BUTTON2_MASK = 128;
    public static final int BUTTON3_MASK = 256;
    public static final int BUTTON4_MASK = 512;
    public static final int CTRL_MASK = 2;
    public static final int DEACTIVATED = 3;
    public static final int DRAGSOURCE_EVENT = 8;
    public static final int DROPTARGET_EVENT = 9;
    public static final int FOCUS_GAINED = 700;
    public static final int FOCUS_LOST = 701;
    public static final int GYRO_CHANGED = 1401;
    public static final int IAP_CONSUME_FAIL = 1109;
    public static final int IAP_CONSUME_SUCCESS = 1108;
    public static final int IAP_INFO_FAIL = 1103;
    public static final int IAP_INFO_SUCCESS = 1102;
    public static final int IAP_INIT_FAIL = 1101;
    public static final int IAP_INIT_SUCCESS = 1100;
    public static final int IAP_OWN_FAIL = 1105;
    public static final int IAP_OWN_SUCCESS = 1104;
    public static final int IAP_PURCHASE_FAIL = 1107;
    public static final int IAP_PURCHASE_SUCCESS = 1106;
    public static final int IMTEXT_CHANGED = 300;
    public static final int IM_EVENT = 3;
    public static final int ITEM_EVENT = 5;
    public static final int ITEM_SELECTED = 500;
    public static final int KEY_EVENT = 2;
    public static final int KEY_PRESSED = 201;
    public static final int KEY_RELEASED = 203;
    public static final int KEY_REPEATED = 202;
    public static final int KEY_TYPED = 200;
    public static final int KWT_EVENT = 7;
    public static final int META_MASK = 4;
    public static final int MIDI_CLOSE = 2001;
    public static final int MIDI_DATA = 2002;
    public static final int MIDI_OPEN = 2000;
    public static final int MODAL_HMOUSE_WHEEL_MOVED = 116;
    public static final int MODAL_MOUSE_CLICKED = 110;
    public static final int MODAL_MOUSE_DRAGGED = 114;
    public static final int MODAL_MOUSE_MOVED = 113;
    public static final int MODAL_MOUSE_OFFSET = 10;
    public static final int MODAL_MOUSE_PRESSED = 111;
    public static final int MODAL_MOUSE_RELEASED = 112;
    public static final int MODAL_MOUSE_WHEEL_MOVED = 115;
    public static final int MOUSE_CANCELLED = 109;
    public static final int MOUSE_CLICKED = 100;
    public static final int MOUSE_DRAGGED = 104;
    public static final int MOUSE_ENTERED = 107;
    public static final int MOUSE_EVENT = 1;
    public static final int MOUSE_EXITED = 108;
    public static final int MOUSE_HWHEEL_MOVED = 106;
    public static final int MOUSE_MOVED = 103;
    public static final int MOUSE_PRESSED = 101;
    public static final int MOUSE_RELEASED = 102;
    public static final int MOUSE_WHEEL_MOVED = 105;
    public static final int QUIT_REQUESTED = 1;
    public static final int REPAINT = 5;
    public static final int RESIZED = 4;
    public static final int SHIFT_MASK = 1;
    public static final int SKSTATE_END = 4;
    public static final int SKSTATE_ENDING = 3;
    public static final int SKSTATE_ERROR = -1;
    public static final int SKSTATE_INIT = 0;
    public static final int SKSTATE_RUNNING = 2;
    public static final int SKSTATE_STARTING = 1;
    private static final String SK_TAG = "SK";
    public static final int SOURCE_DRAG_ENTER = 800;
    public static final int SOURCE_DRAG_EXIT = 801;
    public static final int SOURCE_DRAG_OVER = 802;
    public static final int SOURCE_DROP = 803;
    public static final int SOURCE_DROP_ACTION_CHANGED = 804;
    public static final int TARGET_DRAG_ENTER = 900;
    public static final int TARGET_DRAG_EXIT = 901;
    public static final int TARGET_DRAG_OVER = 902;
    public static final int TARGET_DROP = 903;
    public static final int TARGET_DROP_ACTION_CHANGED = 904;
    public static final int TEXTINPUT_CANCELED = 1001;
    public static final int TEXTINPUT_DONE = 1000;
    public static final int TEXT_CHANGED = 600;
    public static final int TEXT_EVENT = 6;
    private static boolean firstinitialized = false;
    Sensor accel;
    private String[] appargs;
    EditText et;
    Sensor gyro;
    Handler handler;
    private IabHelper iab;
    private HashMap<String, ArrayList<Purchase>> iabmap;
    private int iapstate;
    private ArrayList<SakanaViewListener> listeners;
    SensorManager sensorman;
    private int sk;
    SKThread skthread;
    private HashMap<Integer, TouchInfo> touchmap;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skcalleduithread(int i, long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skchangeAppActiveState(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean skchecksxr(int i, String str, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skdisableGL(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skenableGL(int i);

    private static native int skexecute(int i, String str);

    private static native void skinit(String str, String str2, String str3, String str4, String str5);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skkill(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int skloop(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean skmain(int i, SakanaView sakanaView, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int skmakewindow(int i, int i2, int i3, float f, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int sknew();

    private static native void skpushAppEvent(int i, int i2);

    private static native void skpushKeyEvent(int i, int i2, int i3, int i4, int i5, int i6);

    private static native void skpushMouseEvent(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native void skpushSkuStringArrayEvent(int i, int i2, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skpushStringEvent(int i, int i2, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skregfunc(int i, String str, SakanaFunction sakanaFunction);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void skresized(int i, int i2, int i3, float f, float f2);

    void copyasset(String path, String name, String dir, byte[] buf) throws IOException {
        AssetManager man = getContext().getAssets();
        File dirf = new File(dir);
        dirf.mkdirs();
        BufferedInputStream is = new BufferedInputStream(man.open(path));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dir + Constants.URL_PATH_DELIMITER + name));
        while (true) {
            int n = is.read(buf);
            if (-1 == n) {
                os.close();
                is.close();
                return;
            }
            os.write(buf, 0, n);
        }
    }

    void copyassets(String assetpath, String outdirpath) throws IOException {
        byte[] buf = new byte[262144];
        AssetManager man = getContext().getAssets();
        String[] fileList = man.list(assetpath);
        for (String str : fileList) {
            String path = assetpath + Constants.URL_PATH_DELIMITER + str;
            String name = path.substring(path.lastIndexOf(Constants.URL_PATH_DELIMITER) + 1);
            System.out.println("copyasset : " + path + " -> " + outdirpath);
            try {
                copyasset(path, name, outdirpath, buf);
            } catch (IOException ioe) {
                System.err.println("!copyasset failure : " + path + " -> " + outdirpath);
                throw ioe;
            }
        }
    }

    int getPackageVersion() throws IOException {
        AssetManager man = getContext().getAssets();
        BufferedReader r = new BufferedReader(new InputStreamReader(man.open("sxr/version")));
        String s = r.readLine();
        return Integer.parseInt(s);
    }

    static boolean deleteFiles(File[] list) {
        for (File f : list) {
            if (f.isDirectory()) {
                boolean success = deleteFiles(f.listFiles());
                if (!success) {
                    return false;
                }
            }
            boolean success2 = f.delete();
            if (!success2) {
                return false;
            }
        }
        return true;
    }

    public SakanaView(Context context) {
        super(context);
        this.listeners = new ArrayList<>();
        this.iapstate = -1;
        this.iabmap = new HashMap<>();
        this.touchmap = new HashMap<>();
        synchronized (SakanaView.class) {
            if (!firstinitialized) {
                File datadir = context.getFilesDir();
                File docdir = Environment.getDataDirectory();
                File cachedir = context.getCacheDir();
                String osver = Build.VERSION.RELEASE;
                Locale loc = Locale.getDefault();
                String lang = loc.getLanguage();
                String co = loc.getCountry();
                if (lang.equals("zh")) {
                    if (co.equals("CN")) {
                        lang = "cn";
                    } else {
                        lang = "tw";
                    }
                }
                getContext().getApplicationInfo();
                System.loadLibrary("sakanagl");
                skinit(datadir.getPath(), docdir.getPath(), cachedir.getPath(), osver, lang);
            }
        }
        this.handler = new Handler();
        getHolder().addCallback(this);
        this.skthread = new SKThread();
    }

    public synchronized void addSakanaViewListener(SakanaViewListener l) {
        if (!this.listeners.contains(l)) {
            this.listeners.add(l);
        }
    }

    public synchronized void removeSakanaViewListener(SakanaViewListener l) {
        int i = this.listeners.indexOf(l);
        if (i >= 0) {
            this.listeners.remove(i);
        }
    }

    public void startSakanaGL(String[] args) {
        this.appargs = args;
        this.skthread.startAndWait();
    }

    public void stopSakanaGL() {
        if (this.iab != null) {
            this.iab.dispose();
            this.iab = null;
        }
        this.skthread.exitAndWait();
    }

    public void registerSakanaFunction(String name, SakanaFunction func) {
        this.skthread.registerSakanaFunction(name, func);
    }

    public void quitRequested() {
        synchronized (this.skthread) {
            if (this.sk != 0) {
                skpushAppEvent(this.sk, 1);
            }
        }
    }

    public void onPause() {
        SakanaViewListener[] a;
        _skchangeAppActiveState(2);
        synchronized (this) {
            a = (SakanaViewListener[]) this.listeners.toArray(new SakanaViewListener[this.listeners.size()]);
        }
        for (SakanaViewListener sakanaViewListener : a) {
            sakanaViewListener.skOnPause(this);
        }
    }

    public void onStop() {
        _skchangeAppActiveState(3);
    }

    public void onResume() {
        SakanaViewListener[] a;
        _skchangeAppActiveState(1);
        synchronized (this.skthread) {
            if (this.sk != 0) {
                skpushAppEvent(this.sk, 5);
            }
        }
        synchronized (this) {
            a = (SakanaViewListener[]) this.listeners.toArray(new SakanaViewListener[this.listeners.size()]);
        }
        for (SakanaViewListener sakanaViewListener : a) {
            sakanaViewListener.skOnResume(this);
        }
    }

    private void _skchangeAppActiveState(final int st) {
        try {
            this.skthread.invokeAndWait(new Runnable() { // from class: ls.sakana.SakanaView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (SakanaView.this.sk != 0) {
                        int i = SakanaView.this.sk;
                        SKThread sKThread = SakanaView.this.skthread;
                        int i2 = st;
                        sKThread.appactivestate = i2;
                        SakanaView.skchangeAppActiveState(i, i2);
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println("skchangeAppActiveState failure");
            ex.printStackTrace();
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        synchronized (this.skthread) {
            if (this.sk != 0) {
                skpushAppEvent(this.sk, 5);
            }
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("SAKANA onActivityResult(" + requestCode + "," + resultCode + "," + data + ")");
        return this.iab != null && this.iab.handleActivityResult(requestCode, resultCode, data);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.skthread.surfaceChanged(holder, format, width, height);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        this.skthread.surfaceCreated(holder);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.skthread.surfaceDestroyed(holder);
    }

    TouchInfo getTouchInfo(int pid) {
        Integer i = new Integer(pid & 255);
        TouchInfo ti = this.touchmap.get(i);
        if (ti == null) {
            TouchInfo ti2 = new TouchInfo();
            this.touchmap.put(i, ti2);
            return ti2;
        }
        return ti;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int count = event.getPointerCount();
        int id = (65280 & action) >> 8;
        if (this.skthread == null) {
            return super.onTouchEvent(event);
        }
        synchronized (this.skthread) {
            if (this.sk != 0) {
                if (this.skthread.skstate == 2) {
                    switch (action & 255) {
                        case 0:
                        case 5:
                            if (this.et != null) {
                                endTextEdit();
                            }
                            int x = (int) event.getX(id);
                            int y = (int) event.getY(id);
                            int pid = event.getPointerId(id);
                            TouchInfo ti = getTouchInfo(pid);
                            ti.presstime = event.getEventTime();
                            ti.pressx = x;
                            ti.x = x;
                            ti.pressy = y;
                            ti.y = y;
                            skpushMouseEvent(this.sk, MOUSE_PRESSED, pid, x, y, 64, 1);
                            return true;
                        case 1:
                        case 6:
                            int x2 = (int) event.getX(id);
                            int y2 = (int) event.getY(id);
                            int pid2 = event.getPointerId(id);
                            TouchInfo ti2 = getTouchInfo(pid2);
                            if (event.getEventTime() - ti2.presstime < 300 && ((x2 - ti2.pressx) * (x2 - ti2.pressx)) + ((y2 - ti2.pressy) * (y2 - ti2.pressy)) < 144) {
                                skpushMouseEvent(this.sk, 100, pid2, x2, y2, 64, 1);
                            }
                            skpushMouseEvent(this.sk, MOUSE_RELEASED, pid2, x2, y2, 64, 1);
                            break;
                        case 2:
                            for (int i = 0; i < count; i++) {
                                int x3 = (int) event.getX(i);
                                int y3 = (int) event.getY(i);
                                int pid3 = event.getPointerId(i);
                                TouchInfo ti3 = getTouchInfo(pid3);
                                if (x3 != ti3.x || y3 != ti3.y) {
                                    ti3.x = x3;
                                    ti3.y = y3;
                                    skpushMouseEvent(this.sk, MOUSE_DRAGGED, pid3, x3, y3, 64, 1);
                                }
                            }
                            break;
                    }
                    return super.onTouchEvent(event);
                }
                return super.onTouchEvent(event);
            }
            return super.onTouchEvent(event);
        }
    }

    private boolean skSwapBuffers() {
        try {
            return this.skthread.ctx.swap();
        } catch (Exception ex) {
            System.out.println("skSwapBuffers caused an Exception : " + ex);
            return false;
        }
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private String getAdvertisingID() {
        try {
            return Settings.Secure.getString(getContext().getContentResolver(), "android_id");
        } catch (Exception ex) {
            ex.printStackTrace();
            return getUUID();
        }
    }

    private String getUserName(int option) {
        Account[] accounts = AccountManager.get(getContext()).getAccounts();
        for (int l = 0; l < accounts.length; l++) {
            System.out.println("account[" + l + "] : " + accounts[0].name + " " + accounts[0].type);
        }
        return accounts.length > 0 ? accounts[0].name : "unknown-username";
    }

    private void initializeIAP(String pubkey, boolean debug) {
        if (this.iab != null) {
            skpushStringEvent(this.sk, IAP_INIT_SUCCESS, "IAP already initialized");
            return;
        }
        this.iab = new IabHelper((Activity) getContext(), pubkey);
        if (debug) {
            this.iab.enableDebugLogging(true);
        }
        this.iab.startSetup(new IabHelper.OnIabSetupFinishedListener() { // from class: ls.sakana.SakanaView.2
            @Override // ls.sakana.iap.IabHelper.OnIabSetupFinishedListener
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    if (SakanaView.this.iab == null) {
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_INIT_FAIL, "IAP already disposed");
                        return;
                    } else {
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_INIT_SUCCESS, result.getMessage());
                        return;
                    }
                }
                System.out.println("onIabSetupFinished failure : " + result.getMessage());
                if (SakanaView.this.iab != null) {
                    SakanaView.this.iab.dispose();
                    SakanaView.this.iab = null;
                }
                SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_INIT_FAIL, result.getMessage());
            }
        });
    }

    private void queryIAPItemsInfo(String idstr) {
        if (this.iab == null) {
            skpushStringEvent(this.sk, IAP_INFO_FAIL, "IAP is not initialized / avalilable");
            return;
        }
        final ArrayList<String> moreSkus = new ArrayList<>();
        System.out.println("queryIAPItemsInfo : " + idstr);
        String[] idlist = idstr.split(",", 0);
        for (String str : idlist) {
            moreSkus.add(str.trim());
        }
        final IabHelper.QueryInventoryFinishedListener listener = new IabHelper.QueryInventoryFinishedListener() { // from class: ls.sakana.SakanaView.3
            @Override // ls.sakana.iap.IabHelper.QueryInventoryFinishedListener
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if (result.isFailure()) {
                    synchronized (SakanaView.this.skthread) {
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_INFO_FAIL, result.getMessage());
                    }
                } else {
                    synchronized (SakanaView.this.skthread) {
                        System.out.println("queryIAPItemsInfo success. num=" + inv.getSkuDetailsList().length);
                        SakanaView.skpushSkuDetailsArrayEvent(SakanaView.this.sk, SakanaView.IAP_INFO_SUCCESS, inv.getSkuDetailsList());
                    }
                }
            }
        };
        this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.4
            @Override // java.lang.Runnable
            public void run() {
                SakanaView.this.iab.querySkuDetailsAsync(moreSkus, listener);
            }
        });
    }

    private void queryIAPOwnItems() {
        if (this.iab == null) {
            skpushStringEvent(this.sk, IAP_OWN_FAIL, "IAP is not initialized / avalilable");
        } else {
            final IabHelper.QueryInventoryFinishedListener listener = new IabHelper.QueryInventoryFinishedListener() { // from class: ls.sakana.SakanaView.5
                @Override // ls.sakana.iap.IabHelper.QueryInventoryFinishedListener
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        synchronized (SakanaView.this.skthread) {
                            SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_OWN_FAIL, result.getMessage());
                        }
                        return;
                    }
                    StringBuffer sb = new StringBuffer();
                    sb.append("[");
                    synchronized (SakanaView.this.iabmap) {
                        String[] pa = inv.getPurchasedIDList();
                        for (int l = 0; l < pa.length; l++) {
                            String sku = pa[l];
                            Purchase purchase = inv.getPurchase(sku);
                            if (purchase != null) {
                                ArrayList<Purchase> a = (ArrayList) SakanaView.this.iabmap.get(sku);
                                if (a == null) {
                                    a = new ArrayList<>();
                                    SakanaView.this.iabmap.put(sku, a);
                                }
                                a.add(purchase);
                                byte[] b = purchase.getOriginalJson().getBytes();
                                String receipt64 = Base64.encodeToString(b, 2);
                                String receiptdata = "{\"itemid\":\"" + sku + "\",\"orderid\":\"" + purchase.getOrderId() + "\",\"signature\":\"" + purchase.getSignature() + "\",\"receipt\":\"" + receipt64 + "\"}";
                                if (l > 0) {
                                    sb.append(",");
                                }
                                sb.append(receiptdata);
                            }
                        }
                    }
                    sb.append("]");
                    synchronized (SakanaView.this.skthread) {
                        System.out.println("queryIAPOwnItems success. num=" + inv.getSkuDetailsList().length);
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_OWN_SUCCESS, sb.toString());
                    }
                }
            };
            this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.6
                @Override // java.lang.Runnable
                public void run() {
                    SakanaView.this.iab.queryInventoryAsync(listener);
                }
            });
        }
    }

    private void requestIAP(final String itemid) {
        System.out.println("requestIAP : " + itemid);
        if (this.iab == null) {
            skpushStringEvent(this.sk, IAP_PURCHASE_FAIL, "IAP is not initialized / avalilable");
        } else {
            final IabHelper.OnIabPurchaseFinishedListener listener = new IabHelper.OnIabPurchaseFinishedListener() { // from class: ls.sakana.SakanaView.7
                @Override // ls.sakana.iap.IabHelper.OnIabPurchaseFinishedListener
                public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                    System.out.println("onIabPurchaseFinished : " + result + ", " + purchase);
                    if (result.isFailure()) {
                        synchronized (SakanaView.this.skthread) {
                            SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_PURCHASE_FAIL, result.getMessage());
                        }
                        return;
                    }
                    String sku = purchase.getSku();
                    synchronized (SakanaView.this.iabmap) {
                        ArrayList<Purchase> a = (ArrayList) SakanaView.this.iabmap.get(sku);
                        if (a == null) {
                            a = new ArrayList<>();
                            SakanaView.this.iabmap.put(sku, a);
                        }
                        a.add(purchase);
                    }
                    byte[] b = purchase.getOriginalJson().getBytes();
                    String receipt64 = Base64.encodeToString(b, 2);
                    String receiptdata = "{\"itemid\":\"" + itemid + "\",\"orderid\":\"" + purchase.getOrderId() + "\",\"signature\":\"" + purchase.getSignature() + "\",\"receipt\":\"" + receipt64 + "\"}";
                    synchronized (SakanaView.this.skthread) {
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_PURCHASE_SUCCESS, receiptdata);
                    }
                }
            };
            this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.8
                @Override // java.lang.Runnable
                public void run() {
                    SakanaView.this.iab.launchPurchaseFlow((Activity) SakanaView.this.getContext(), itemid, 131423, listener, "AJDJAWIJDWJ");
                }
            });
        }
    }

    private void consumeIAP(final String id) {
        if (this.iab == null) {
            skpushStringEvent(this.sk, IAP_CONSUME_FAIL, "IAP is not initialized / avalilable");
            return;
        }
        final IabHelper.OnConsumeFinishedListener listener = new IabHelper.OnConsumeFinishedListener() { // from class: ls.sakana.SakanaView.9
            @Override // ls.sakana.iap.IabHelper.OnConsumeFinishedListener
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    synchronized (SakanaView.this.skthread) {
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_CONSUME_FAIL, result.getMessage());
                    }
                } else {
                    synchronized (SakanaView.this.skthread) {
                        SakanaView.skpushStringEvent(SakanaView.this.sk, SakanaView.IAP_CONSUME_SUCCESS, id);
                    }
                }
            }
        };
        Purchase purchase = null;
        synchronized (this.iabmap) {
            ArrayList<Purchase> a = this.iabmap.get(id);
            if (a != null && !a.isEmpty()) {
                purchase = a.remove(0);
            }
        }
        if (purchase != null) {
            final Purchase _p = purchase;
            this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.10
                @Override // java.lang.Runnable
                public void run() {
                    SakanaView.this.iab.consumeAsync(_p, listener);
                }
            });
        } else {
            synchronized (this.skthread) {
                skpushStringEvent(this.sk, IAP_CONSUME_FAIL, "consumable item [" + id + "] not found");
            }
        }
    }

    private void startTextEdit(final String str, int flags_len) {
        this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.11
            @Override // java.lang.Runnable
            public void run() {
                if (SakanaView.this.et != null) {
                    ((ViewGroup) SakanaView.this.et.getParent()).removeView(SakanaView.this.et);
                }
                SakanaView.this.et = new EditText(SakanaView.this.getContext());
                SakanaView.this.et.setTranslationY(0.0f);
                SakanaView.this.et.setBackgroundColor(-1);
                ViewGroup g = (ViewGroup) SakanaView.this.getParent();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(g.getWidth(), 80);
                g.addView(SakanaView.this.et, lp);
                SakanaView.this.et.requestFocus();
                SakanaView.this.et.setInputType(1);
                if (str != null) {
                    SakanaView.this.et.setText(str);
                    SakanaView.this.et.setSelection(0, str.length());
                }
                InputMethodManager imm = (InputMethodManager) SakanaView.this.getContext().getSystemService("input_method");
                imm.showSoftInput(SakanaView.this.et, 2);
                SakanaView.this.et.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: ls.sakana.SakanaView.11.1
                    @Override // android.widget.TextView.OnEditorActionListener
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == 6) {
                            System.out.println("IME_ACTION_DONE " + ((Object) v.getText()));
                            synchronized (SakanaView.this.skthread) {
                                SakanaView.skpushStringEvent(SakanaView.this.sk, 1000, v.getText().toString());
                            }
                            SakanaView.this.endTextEdit();
                            SakanaView.this.hideNavigationBar();
                            return false;
                        }
                        System.out.println("actionId == " + actionId);
                        return true;
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideNavigationBar() {
        Activity a = (Activity) getContext();
        View decor = a.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 18) {
            decor.setSystemUiVisibility(4102);
        } else if (Build.VERSION.SDK_INT > 15) {
            decor.setSystemUiVisibility(6);
        } else if (Build.VERSION.SDK_INT > 13) {
            decor.setSystemUiVisibility(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endTextEdit() {
        this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.12
            @Override // java.lang.Runnable
            public void run() {
                if (SakanaView.this.et != null) {
                    ((ViewGroup) SakanaView.this.et.getParent()).removeView(SakanaView.this.et);
                    SakanaView.this.et = null;
                }
            }
        });
    }

    private int startAcceleroMeter() {
        if (this.sensorman == null) {
            this.sensorman = (SensorManager) getContext().getSystemService("sensor");
        }
        if (this.accel == null) {
            this.accel = this.sensorman.getDefaultSensor(1);
        }
        this.sensorman.registerListener(this, this.accel, 2);
        return 0;
    }

    private int stopAcceleroMeter() {
        if (this.accel != null) {
            this.sensorman.unregisterListener(this, this.accel);
            return 0;
        }
        return 0;
    }

    private int startGyroScope() {
        if (this.sensorman == null) {
            this.sensorman = (SensorManager) getContext().getSystemService("sensor");
        }
        if (this.gyro == null) {
            this.gyro = this.sensorman.getDefaultSensor(4);
        }
        this.sensorman.registerListener(this, this.gyro, 2);
        return 0;
    }

    private int stopGyroScope() {
        if (this.gyro != null) {
            this.sensorman.unregisterListener(this, this.gyro);
            return 0;
        }
        return 0;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent e) {
        switch (e.sensor.getType()) {
            case 1:
            case 4:
                int eid = e.sensor.getType() == 1 ? ACCEL_CHANGED : GYRO_CHANGED;
                synchronized (this.skthread) {
                    if (this.sk != 0) {
                        String s = new StringBuffer().append("[").append(e.values[0]).append(", ").append(e.values[1]).append(", ").append(e.values[2]).append("]").toString();
                        skpushStringEvent(this.sk, eid, s);
                    }
                }
                return;
            case 2:
            case 3:
            default:
                return;
        }
    }

    public SakanaWebView createWebView() {
        return new SakanaWebView(this);
    }

    public SakanaMoviePlayer createMoviePlayer(int tex) {
        return new SakanaMoviePlayer(this, tex);
    }

    private void callInUIThread(final long ptr) {
        this.handler.post(new Runnable() { // from class: ls.sakana.SakanaView.13
            @Override // java.lang.Runnable
            public void run() {
                synchronized (SakanaView.this.skthread) {
                    SakanaView.skcalleduithread(SakanaView.this.sk, ptr);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void skpushSkuDetailsArrayEvent(int sk, int eid, SkuDetails[] skus) {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        for (int l = 0; l < skus.length; l++) {
            String desc = NotificationCompat.CATEGORY_ERROR;
            SkuDetails s = skus[l];
            try {
                desc = URLEncoder.encode(s.getDescription(), "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            sb.append("{\"itemid\":\"");
            sb.append(s.getSku());
            sb.append("\",\"desc\":\"");
            sb.append(desc);
            sb.append("\",\"type\":\"");
            sb.append(s.getType());
            sb.append("\",\"pricetxt\":\"");
            sb.append(s.getPrice());
            sb.append("\",\"priceamt\":");
            sb.append(getPriceAmount(s.getPrice()));
            if (l < skus.length - 1) {
                sb.append("},\n");
            } else {
                sb.append("}\n");
            }
        }
        sb.append("]\n");
        String s2 = sb.toString();
        System.out.println("sku_details = " + s2);
        skpushSkuStringArrayEvent(sk, eid, s2);
    }

    private static int getPriceAmount(String s) {
        int i = 0;
        for (int l = 0; l < s.length(); l++) {
            char c = s.charAt(l);
            if (c >= '0' && c <= '9') {
                i = (i * 10) + (c - '0');
            }
        }
        return i * 100;
    }

    private void checksk() {
        if (this.sk == 0) {
            throw new IllegalStateException("SAKANAGL IS NULL!!!!!!!");
        }
    }

    class SKThread extends Thread {
        int appactivestate;
        SakanaContext ctx;
        int loopi;
        Runnable proc;
        HashMap<String, SakanaFunction> regfunc;
        int skstate;
        final int[] sleeptime;

        SKThread() {
            super("SKThread");
            this.sleeptime = new int[]{0, 0, 40, 50, 100};
            this.ctx = new SakanaContext();
            this.skstate = 0;
            this.regfunc = new HashMap<>();
            this.appactivestate = 1;
        }

        synchronized int getSKState() {
            return this.skstate;
        }

        synchronized void setSKState(int s) {
            if (s != this.skstate) {
                System.out.println("skstate change from:" + this.skstate + " to:" + s);
                this.skstate = s;
            }
            notifyAll();
        }

        synchronized void startAndWait() {
            setSKState(1);
            super.start();
            while (this.skstate == 1) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }

        synchronized void exitAndWait() {
            if (this.skstate != 4 && this.skstate != -1) {
                setSKState(3);
                while (this.skstate == 3) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        synchronized void registerSakanaFunction(final String name, final SakanaFunction func) {
            if (this.skstate != 2) {
                this.regfunc.put(name, func);
            } else {
                invokeAndWait(new Runnable() { // from class: ls.sakana.SakanaView.SKThread.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SKThread.this.regfunc.put(name, func);
                        SakanaView.skregfunc(SakanaView.this.sk, name, func);
                    }
                });
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            String[] _appargs;
            try {
                this.ctx.createContext();
                System.out.println("EGL Context created. es version=" + this.ctx.esver);
                SakanaView.this.sk = SakanaView.sknew();
                int version = SakanaView.this.getPackageVersion();
                System.out.println("sxr package check. version=" + version);
                String mainsxrpath = SakanaView.this.getContext().getCacheDir().getPath() + "/main.sxr";
                if (!SakanaView.skchecksxr(SakanaView.this.sk, mainsxrpath, version)) {
                    System.out.println("Copying sxr packages to cache dir.");
                    SakanaView.deleteFiles(SakanaView.this.getContext().getCacheDir().listFiles());
                    SakanaView.this.copyassets("sxr", SakanaView.this.getContext().getCacheDir().getPath());
                } else {
                    System.out.println("sxr package check ok.");
                }
                if (SakanaView.this.appargs == null) {
                    _appargs = new String[]{"-main", mainsxrpath};
                } else {
                    ArrayList<String> a = new ArrayList<>();
                    a.add("-main");
                    a.add(mainsxrpath);
                    for (int l = 0; l < SakanaView.this.appargs.length; l++) {
                        a.add(SakanaView.this.appargs[l]);
                    }
                    _appargs = (String[]) a.toArray(new String[a.size()]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("WARNING : SakanaGL main thread abnormally finished...");
                setSKState(-1);
            }
            synchronized (this) {
                for (Map.Entry<String, SakanaFunction> e2 : this.regfunc.entrySet()) {
                    SakanaView.skregfunc(SakanaView.this.sk, e2.getKey(), e2.getValue());
                }
                setSKState(2);
                while (getSKState() == 2) {
                    synchronized (this) {
                        if (this.proc != null) {
                            this.proc.run();
                            this.proc = null;
                            notifyAll();
                        }
                    }
                    if (this.ctx.surface != null) {
                        break;
                    } else {
                        Thread.sleep(10L);
                    }
                }
                DisplayMetrics met = new DisplayMetrics();
                WindowManager wm = (WindowManager) SakanaView.this.getContext().getSystemService("window");
                wm.getDefaultDisplay().getRealMetrics(met);
                SakanaView.skmakewindow(SakanaView.this.sk, SakanaView.this.getWidth(), SakanaView.this.getHeight(), met.xdpi, met.ydpi);
                System.out.println("sakana arguments : " + _appargs.length);
                for (int l2 = 0; l2 < _appargs.length; l2++) {
                    System.out.println("[" + l2 + "] : " + _appargs[l2]);
                }
                SakanaView.skmain(SakanaView.this.sk, SakanaView.this, _appargs);
                System.out.println("sakana loop start!");
                while (true) {
                    if (getSKState() != 2) {
                        break;
                    }
                    synchronized (this) {
                        if (this.proc != null) {
                            this.proc.run();
                            this.proc = null;
                            notifyAll();
                        }
                    }
                    int frameflag = SakanaView.skloop(SakanaView.this.sk);
                    if (frameflag == Integer.MIN_VALUE) {
                        System.out.println("sakana loop done.");
                        break;
                    }
                    int st = (this.appactivestate != 1 || (frameflag & 1) == 0) ? this.sleeptime[this.appactivestate] : 2;
                    if (st > 0) {
                        Thread.sleep(st);
                    } else {
                        Thread.yield();
                    }
                }
                System.out.println("SakanaGL main thread normally finished.");
                setSKState(3);
                this.ctx.destroySurface();
                this.ctx.finish();
                synchronized (this) {
                    SakanaView.skkill(SakanaView.this.sk);
                    SakanaView.this.sk = 0;
                }
            }
            synchronized (SakanaView.this) {
                for (SakanaViewListener sakanaViewListener : (SakanaViewListener[]) SakanaView.this.listeners.toArray(new SakanaViewListener[SakanaView.this.listeners.size()])) {
                    sakanaViewListener.skFinished(SakanaView.this);
                }
            }
            setSKState(4);
            System.out.println("SakanaGL thread terminated.");
        }

        synchronized void invokeAndWait(Runnable p) {
            if (this.skstate != 2) {
                throw new IllegalStateException("SakanaGL is not running. state=" + this.skstate);
            }
            if (Thread.currentThread() == this) {
                p.run();
            } else {
                this.proc = p;
                do {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (this.proc == p);
            }
        }

        synchronized void surfaceChanged(SurfaceHolder holder, int format, final int width, final int height) {
            System.out.println("ctx.surfaceChanged. " + getSKState() + "  (" + width + "," + height + ") v(" + SakanaView.this.getWidth() + "," + SakanaView.this.getHeight() + ") : " + format);
            invokeAndWait(new Runnable() { // from class: ls.sakana.SakanaView.SKThread.2
                @Override // java.lang.Runnable
                public void run() {
                    if (SakanaView.this.sk != 0) {
                        DisplayMetrics met = new DisplayMetrics();
                        WindowManager wm = (WindowManager) SakanaView.this.getContext().getSystemService("window");
                        wm.getDefaultDisplay().getRealMetrics(met);
                        SakanaView.skresized(SakanaView.this.sk, width, height, met.xdpi, met.ydpi);
                    }
                }
            });
        }

        synchronized void surfaceCreated(final SurfaceHolder holder) {
            System.out.println("ctx.createSurface. " + getSKState() + "  " + SakanaView.this.getWidth() + " , " + SakanaView.this.getHeight());
            if (this.skstate == 2) {
                invokeAndWait(new Runnable() { // from class: ls.sakana.SakanaView.SKThread.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (SakanaView.this.sk != 0) {
                            SKThread.this.ctx.createSurface(SakanaView.this, holder);
                            SakanaView.skenableGL(SakanaView.this.sk);
                        }
                    }
                });
            }
        }

        synchronized void surfaceDestroyed(SurfaceHolder holder) {
            System.out.println("ctx.surfaceDestroyed. skstate=" + getSKState());
            if (this.skstate == 2) {
                invokeAndWait(new Runnable() { // from class: ls.sakana.SakanaView.SKThread.4
                    @Override // java.lang.Runnable
                    public void run() {
                        if (SakanaView.this.sk != 0) {
                            SakanaView.skdisableGL(SakanaView.this.sk);
                        }
                        SKThread.this.ctx.destroySurface();
                    }
                });
            }
        }
    }

    class TouchInfo {
        int clicks;
        long presstime;
        int pressx;
        int pressy;
        int x;
        int y;

        TouchInfo() {
        }
    }
}
