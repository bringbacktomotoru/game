package ls.sakana;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class SakanaActivity extends Activity implements SakanaViewListener {
    static SakanaView savedview;
    protected ViewGroup mainpanel;
    protected SakanaView view;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("SakanaActivity.onCreate(). view=" + this.view + " save=" + savedInstanceState);
        if (savedview != null) {
            System.out.println("SakanaView restart.");
            this.view = savedview;
            savedview = null;
            if (this.mainpanel == null || this.mainpanel.getRootView() != getWindow().getDecorView().getRootView() || this.view.getParent() != this.mainpanel) {
                if (this.view.getParent() != null) {
                    ((ViewGroup) this.view.getParent()).removeView(this.view);
                }
                createMainPanel();
                return;
            }
            return;
        }
        this.view = new SakanaView(this);
        this.view.addSakanaViewListener(this);
        createMainPanel();
        this.view.startSakanaGL(getSakanaArguments());
    }

    protected String[] getSakanaArguments() {
        return new String[0];
    }

    protected void createMainPanel() {
        FrameLayout fl = new FrameLayout(getApplicationContext());
        setContentView(fl);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        fl.addView(this.view, lp);
        this.mainpanel = fl;
    }

    @Override // android.app.Activity
    public void onDestroy() {
        System.out.println("SakanaActivity.onDestroy(). view=" + this.view);
        if (this.view != null) {
            if (savedview == this.view) {
                this.mainpanel.removeView(this.view);
            } else {
                this.view.stopSakanaGL();
                this.view = null;
                finish();
            }
        }
        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onPause() {
        System.out.println("SakanaActivity.onPause(). view=" + this.view);
        if (this.view != null) {
            this.view.onPause();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    public void onStop() {
        System.out.println("SakanaActivity.onStop(). view=" + this.view);
        if (this.view != null) {
            this.view.onStop();
        }
        super.onStop();
    }

    @Override // android.app.Activity
    public void onRestart() {
        System.out.println("SakanaActivity.onRestart(). view=" + this.view + " ... clear savedview!");
        savedview = null;
        super.onRestart();
    }

    @Override // android.app.Activity
    public void onResume() {
        System.out.println("SakanaActivity.onResume(). view=" + this.view);
        if (this.view != null) {
            this.view.onResume();
        }
        super.onResume();
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("SakanaActivity.onSaveInstanceState(). view=" + this.view);
        savedview = this.view;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.view == null || !this.view.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        System.out.println("SakanaActivity.onConfigurationChanged(). view=" + this.view);
        if (this.view != null) {
            this.mainpanel.invalidate();
            this.view.onConfigurationChanged(newConfig);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getAction() == 0) {
            switch (e.getKeyCode()) {
                case 4:
                    if (this.view != null) {
                        this.view.quitRequested();
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(e);
    }

    @Override // ls.sakana.SakanaViewListener
    public void skFinished(SakanaView v) {
        runOnUiThread(new Runnable() { // from class: ls.sakana.SakanaActivity.1
            @Override // java.lang.Runnable
            public void run() {
                SakanaActivity.this.finish();
            }
        });
    }

    @Override // ls.sakana.SakanaViewListener
    public void skOnPause(SakanaView v) {
    }

    @Override // ls.sakana.SakanaViewListener
    public void skOnResume(SakanaView v) {
    }
}
