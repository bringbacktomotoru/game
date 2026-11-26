package ls.sakana;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class URLRequest implements Runnable {
    public int contentlength;
    public ByteBuffer data;
    int flags;
    FileOutputStream fos;
    public String header;
    HttpURLConnection http;
    InputStream is;
    public long lastmodified;
    int lockcount;
    int method;
    File outfile;
    byte[] post;
    public int procmode;
    String[] proplist;
    public int readsize;
    public int status;
    Thread thread;
    String url;

    public void startWithParameters(String _url, int _flags, int _method, String[] _proplist, byte[] _post, String _tofilepath) {
        if (!lock()) {
            this.procmode = -1;
            this.status = -1;
            return;
        }
        this.url = _url;
        this.flags = _flags;
        this.method = _method;
        this.proplist = _proplist;
        this.post = _post;
        if (_tofilepath != null) {
            this.outfile = new File(_tofilepath);
        }
        unlock();
        start();
    }

    public void start() {
        if (!lock()) {
            this.procmode = -1;
            this.status = -1;
        } else {
            this.procmode = 1;
            this.thread = new Thread(this, "URLResutst Thread[" + this.url + "]");
            this.thread.start();
            unlock();
        }
    }

    public void stop() {
        if (!lock()) {
            this.procmode = -1;
            this.status = -1;
        } else {
            if (this.thread != null) {
                this.thread.interrupt();
                this.thread = null;
            }
            unlock();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.http = (HttpURLConnection) new URL(this.url).openConnection();
            switch (this.method) {
                case 0:
                    this.http.setRequestMethod("GET");
                    break;
                case 1:
                    this.http.setRequestMethod("PUT");
                    break;
                case 2:
                    this.http.setRequestMethod("HEADER");
                    break;
            }
            if ((this.flags & 1) != 0) {
                this.http.setUseCaches(false);
            }
            if (this.proplist != null) {
                for (int l = 0; l < this.proplist.length; l += 2) {
                    this.http.addRequestProperty(this.proplist[l], this.proplist[l + 1]);
                }
            }
            if (this.post != null) {
                this.http.setDoOutput(true);
                this.http.connect();
                OutputStream os = this.http.getOutputStream();
                os.write(this.post);
            } else {
                this.http.connect();
            }
            this.header = this.http.getHeaderFields().toString();
            this.status = this.http.getResponseCode();
            this.lastmodified = this.http.getDate();
            this.contentlength = this.http.getContentLength();
            if (!lock()) {
                this.status = -1;
                this.procmode = -1;
                return;
            }
            if (this.method == 2) {
                this.procmode = 4;
                unlock();
                return;
            }
            this.procmode = 2;
            unlock();
            byte[] buf = new byte[32768];
            try {
                this.is = this.http.getInputStream();
                if (this.outfile != null) {
                    this.fos = new FileOutputStream(this.outfile);
                } else {
                    this.data = ByteBuffer.allocateDirect(this.contentlength);
                }
                while (true) {
                    int read = this.is.read(buf);
                    if (read > 0) {
                        if (!lock()) {
                            this.procmode = -1;
                            this.status = -1;
                        } else {
                            if (this.fos != null) {
                                this.fos.write(buf, 0, read);
                            } else {
                                this.data.put(buf, 0, read);
                            }
                            this.readsize += read;
                            if (this.readsize > this.contentlength) {
                                this.contentlength += this.readsize;
                            }
                            unlock();
                        }
                    }
                }
                if (this.fos != null) {
                    this.fos.close();
                }
                this.status = this.http.getResponseCode();
                this.procmode = 3;
                if (lock()) {
                    this.procmode = 4;
                    unlock();
                } else {
                    this.procmode = -1;
                    this.status = -1;
                }
            } catch (Exception e) {
                this.status = -1;
                this.procmode = -1;
            }
        } catch (IOException e2) {
            this.status = -1;
            this.procmode = -1;
        }
    }

    public synchronized boolean lock() {
        boolean z;
        while (this.lockcount > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                z = false;
            }
        }
        z = true;
        return z;
    }

    public synchronized void unlock() {
        int i = this.lockcount - 1;
        this.lockcount = i;
        if (i <= 0) {
            notifyAll();
        }
    }
}
