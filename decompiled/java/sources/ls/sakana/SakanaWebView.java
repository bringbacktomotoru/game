package ls.sakana;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.widget.ExploreByTouchHelper;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import java.net.URLEncoder;

/* loaded from: classes.dex */
public class SakanaWebView {
    private static final String SK_TAG = "SK";
    Handler handler;
    int loading;
    SakanaView skview;
    int type;
    WebView webview;
    int x = 0;
    int y = 0;
    int w = SakanaView.ACTION_PERFORMED;
    int h = SakanaView.ACTION_PERFORMED;
    JSInterface jsif = new JSInterface();

    public SakanaWebView(SakanaView v) {
        this.skview = null;
        this.skview = v;
        this.handler = v.handler;
    }

    private int setType(int t) {
        return new IRunnable() { // from class: ls.sakana.SakanaWebView.1
            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                if (SakanaWebView.this.webview != null) {
                    ((ViewGroup) SakanaWebView.this.webview.getParent()).removeView(SakanaWebView.this.webview);
                }
                SakanaWebView.this.webview = new WebView(SakanaWebView.this.skview.getContext());
                SakanaWebView.this.webview.setWebViewClient(SakanaWebView.this.new MyWebViewClient());
                SakanaWebView.this.webview.getSettings().setJavaScriptEnabled(true);
                SakanaWebView.this.webview.setWebChromeClient(SakanaWebView.this.new MyWebChromeClient());
                return 0;
            }
        }.postAndWait();
    }

    private int show() {
        return new IRunnable() { // from class: ls.sakana.SakanaWebView.2
            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                if (SakanaWebView.this.webview != null) {
                    if (SakanaWebView.this.webview.getParent() != null) {
                        ((ViewGroup) SakanaWebView.this.webview.getParent()).removeView(SakanaWebView.this.webview);
                    }
                    ViewGroup g = (ViewGroup) SakanaWebView.this.skview.getParent();
                    SakanaWebView.this.webview.setTranslationX(SakanaWebView.this.x);
                    SakanaWebView.this.webview.setTranslationY(SakanaWebView.this.y);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(SakanaWebView.this.w, SakanaWebView.this.h);
                    g.addView(SakanaWebView.this.webview, lp);
                    return 0;
                }
                return 0;
            }
        }.postAndWait();
    }

    private int hide() {
        return new IRunnable() { // from class: ls.sakana.SakanaWebView.3
            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                if (SakanaWebView.this.webview != null) {
                    SakanaWebView.this.webview.setVisibility(4);
                    return 0;
                }
                return 0;
            }
        }.postAndWait();
    }

    private int close() {
        return new IRunnable() { // from class: ls.sakana.SakanaWebView.4
            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                if (SakanaWebView.this.webview != null) {
                    ((ViewGroup) SakanaWebView.this.webview.getParent()).removeView(SakanaWebView.this.webview);
                    SakanaWebView.this.webview = null;
                    return 0;
                }
                return 0;
            }
        }.postAndWait();
    }

    private int setRect(final int _x, final int _y, final int _w, final int _h) {
        return new IRunnable() { // from class: ls.sakana.SakanaWebView.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                SakanaWebView.this.x = _x;
                SakanaWebView.this.y = _y;
                SakanaWebView.this.w = _w;
                SakanaWebView.this.h = _h;
                return 0;
            }
        }.postAndWait();
    }

    private int openURL(final String url, String headers) {
        return new IRunnable() { // from class: ls.sakana.SakanaWebView.6
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                if (SakanaWebView.this.webview != null) {
                    SakanaWebView.this.loading = 1;
                    SakanaWebView.this.webview.loadUrl(url);
                    return 0;
                }
                return 0;
            }
        }.postAndWait();
    }

    private int isLoading() {
        return this.loading;
    }

    private String eval(final String jscode) {
        new IRunnable() { // from class: ls.sakana.SakanaWebView.7
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // ls.sakana.SakanaWebView.IRunnable
            public int irun() {
                SakanaWebView.this.jsif.retobj = null;
                try {
                    SakanaWebView.this.webview.loadUrl("javascript:setResult(" + URLEncoder.encode(jscode) + ")");
                    return 0;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SakanaWebView.this.jsif.retobj = null;
                    return 0;
                }
            }
        }.postAndWait();
        Object o = this.jsif.retobj;
        this.jsif.retobj = null;
        if (o == null) {
            return "null";
        }
        if ((o instanceof Integer) || (o instanceof Float) || (o instanceof Boolean)) {
            return o.toString();
        }
        try {
            return "\"" + URLEncoder.encode(o.toString(), "UTF-8") + "\"";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "\"URLEncoder error\"";
        }
    }

    abstract class IRunnable implements Runnable {
        boolean end = false;
        int ret;

        public abstract int irun();

        IRunnable() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                this.ret = irun();
            } catch (Exception ex) {
                ex.printStackTrace();
                this.ret = ExploreByTouchHelper.INVALID_ID;
            }
            synchronized (this) {
                this.end = true;
                notifyAll();
            }
        }

        public synchronized int postAndWait() {
            int i;
            SakanaWebView.this.handler.post(this);
            while (!this.end) {
                try {
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    i = -1;
                }
            }
            i = this.ret;
            return i;
        }
    }

    class JSInterface {
        Object retobj;

        JSInterface() {
        }

        public void setResult(Object o) {
            this.retobj = o;
        }
    }

    class MyWebViewClient extends WebViewClient {
        MyWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            SakanaWebView.this.webview.addJavascriptInterface(SakanaWebView.this.jsif, "android");
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            SakanaWebView.this.loading = 0;
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        MyWebChromeClient() {
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(SakanaWebView.this.skview.getContext()).setTitle("confirm").setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: ls.sakana.SakanaWebView.MyWebChromeClient.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: ls.sakana.SakanaWebView.MyWebChromeClient.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).create().show();
            return true;
        }
    }
}
