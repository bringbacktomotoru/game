package com.cyberstep.sanriocharacters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ls.sakana.SakanaActivity;

/* loaded from: classes.dex */
public class SanrioActivity extends SakanaActivity {
    private static final String CONTENT_PROVIDER_AUTHORITY = "com.cyberstep.sanriocharacters.fileprovider";
    private static final int REQUEST_CODE_PERMISSIONS = 1;

    @Override // ls.sakana.SakanaActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(1024);
        super.onCreate(savedInstanceState);
        System.loadLibrary("smartar");
    }

    private boolean checkARPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return PermissionChecker.checkSelfPermission(this, "android.permission.CAMERA") == 0 && PermissionChecker.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return PermissionChecker.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void confirmARPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissionList = new ArrayList<>();
            if (PermissionChecker.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
                permissionList.add("android.permission.CAMERA");
            }
            if (PermissionChecker.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
                permissionList.add("android.permission.READ_EXTERNAL_STORAGE");
            }
            if (PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                permissionList.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }
            if (permissionList.size() > 0) {
                ActivityCompat.requestPermissions(this, (String[]) permissionList.toArray(new String[0]), 1);
            }
        }
    }

    private void confirmStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissionList = new ArrayList<>();
            if (PermissionChecker.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
                permissionList.add("android.permission.READ_EXTERNAL_STORAGE");
            }
            if (PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                permissionList.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }
            if (permissionList.size() > 0) {
                ActivityCompat.requestPermissions(this, (String[]) permissionList.toArray(new String[0]), 1);
            }
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.mainpanel.getWidth();
        this.mainpanel.getHeight();
        hideNavigationBar();
    }

    @Override // ls.sakana.SakanaActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void hideNavigationBar() {
        View decor = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 18) {
            decor.setSystemUiVisibility(4102);
        } else if (Build.VERSION.SDK_INT > 15) {
            decor.setSystemUiVisibility(6);
        } else if (Build.VERSION.SDK_INT > 13) {
            decor.setSystemUiVisibility(2);
        }
    }

    @Override // ls.sakana.SakanaActivity
    protected void createMainPanel() {
        hideNavigationBar();
        Display dis = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        dis.getRealSize(p);
        FrameLayout fl = new FrameLayout(getApplicationContext());
        setContentView(fl);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        fl.addView(this.view, lp);
        this.mainpanel = fl;
    }

    public static String getExternalStrageDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static void scanPhotoGallery(String filename, Context context) {
        Uri contentUri = Uri.fromFile(new File(filename));
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void startExternalIntent(String filename, String postStr, Context context) {
        Uri contentUri = FileProvider.getUriForFile(context, CONTENT_PROVIDER_AUTHORITY, new File(filename));
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(filename.endsWith("mp4") ? "video/*" : "image/*");
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            HashMap<String, Intent> shareIntentMap = new HashMap<>();
            for (ResolveInfo info : resInfo) {
                Intent shareIntent = (Intent) intent.clone();
                shareIntent.putExtra("android.intent.extra.TEXT", postStr);
                shareIntent.putExtra("android.intent.extra.STREAM", contentUri);
                shareIntent.addFlags(3);
                shareIntent.setPackage(info.activityInfo.packageName);
                if (shareIntentMap.get(info.activityInfo.packageName) == null) {
                    shareIntentMap.put(info.activityInfo.packageName, shareIntent);
                }
            }
            List<Intent> shareIntentList = new ArrayList<>(shareIntentMap.values());
            Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "select");
            chooserIntent.addFlags(268435456);
            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) shareIntentList.toArray(new Parcelable[0]));
            context.startActivity(chooserIntent);
        }
    }

    public static boolean sarCheckARPermission(Context cont) {
        SanrioActivity mine = (SanrioActivity) cont;
        return mine.checkARPermission();
    }

    public static boolean sarCheckStoragePermission(Context cont) {
        SanrioActivity mine = (SanrioActivity) cont;
        return mine.checkStoragePermission();
    }

    public static void sarConfirmARPermission(Context cont) {
        SanrioActivity mine = (SanrioActivity) cont;
        mine.confirmARPermission();
    }

    public static void sarConfirmStoragePermission(Context cont) {
        SanrioActivity mine = (SanrioActivity) cont;
        mine.confirmStoragePermission();
    }

    public static void openGooglePlayStoreApp(Context cont) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=com.cyberstep.sanriocharacters"));
        intent.setPackage("com.android.vending");
        cont.startActivity(intent);
    }
}
