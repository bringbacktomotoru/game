package ls.sakana;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.FileInputStream;

/* loaded from: classes.dex */
public class SakanaMoviePlayer implements SakanaViewListener {
    static final int STATE_CLOSE = 6;
    static final int STATE_ERROR = -1;
    static final int STATE_INIT = 0;
    static final int STATE_LOAD = 2;
    static final int STATE_OPEN = 1;
    static final int STATE_PAUSE = 5;
    static final int STATE_PLAY = 4;
    static final int STATE_READY = 3;
    FileDescriptor fd;
    volatile int framecount;
    Handler handler;
    MediaPlayer player = new MediaPlayer();
    SakanaView skview;
    volatile int state;
    Surface surf;
    float[] surfmat;
    SurfaceTexture surftex;
    boolean viewpaused;

    public SakanaMoviePlayer(SakanaView v, int texid) {
        this.skview = v;
        this.handler = v.handler;
        this.surftex = new SurfaceTexture(texid);
        this.surf = new Surface(this.surftex);
        this.player.setSurface(this.surf);
        this.surfmat = new float[16];
        this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: ls.sakana.SakanaMoviePlayer.1
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer p) {
                if (SakanaMoviePlayer.this.state == 2) {
                    SakanaMoviePlayer.this.state = 3;
                    SakanaMoviePlayer.this.framecount = 0;
                }
            }
        });
        this.player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() { // from class: ls.sakana.SakanaMoviePlayer.2
            @Override // android.media.MediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(MediaPlayer p) {
                if (SakanaMoviePlayer.this.state == 2) {
                    SakanaMoviePlayer.this.state = 3;
                    SakanaMoviePlayer.this.framecount = 0;
                }
            }
        });
        this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: ls.sakana.SakanaMoviePlayer.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer p) {
                if (SakanaMoviePlayer.this.state == 4) {
                    SakanaMoviePlayer.this.state = 5;
                    SakanaMoviePlayer.this.skview.removeSakanaViewListener(SakanaMoviePlayer.this);
                }
            }
        });
        this.surftex.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: ls.sakana.SakanaMoviePlayer.4
            @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                SakanaMoviePlayer.this.framecount++;
            }
        });
    }

    private int setSource(String path, long offs, long len) {
        if (this.state != 0) {
            System.out.println("SakanaMoviePlayer setSource invalid state : " + this.state);
            return 0;
        }
        try {
            AssetFileDescriptor afd = this.skview.getContext().getResources().getAssets().openFd(path);
            this.fd = afd.getFileDescriptor();
            this.player.setDataSource(this.fd, afd.getStartOffset(), afd.getLength());
            this.state = 1;
            System.out.println("SakanaMoviePlayer setSource from assets : " + path);
            return 1;
        } catch (Exception e) {
            try {
                FileInputStream fis = new FileInputStream(path);
                this.fd = fis.getFD();
                this.player.setDataSource(this.fd, offs, len);
                this.state = 1;
                System.out.println("SakanaMoviePlayer setSource from file : " + path);
                return 1;
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            }
        }
    }

    private int prepare() {
        if (this.state == 1) {
            this.state = 2;
            this.player.prepareAsync();
            System.out.println("SakanaMoviePlayer prepareAsync");
            return 1;
        }
        System.out.println("SakanaMoviePlayer prepare invalid state : " + this.state);
        return 0;
    }

    private int getState() {
        return this.state;
    }

    private int[] getMovieInfo() {
        int[] i = {3, this.player.getDuration(), 0, 0, 0, this.player.getVideoWidth(), this.player.getVideoHeight(), 30, 0, 0, 44100, 2, 16, 0, 0, 0};
        return i;
    }

    private void setLoop(boolean b) {
        this.player.setLooping(b);
    }

    private void setAudioVolume(int vol) {
        float fvol = vol / 100.0f;
        this.player.setVolume(fvol, fvol);
    }

    private int seek(int f) {
        if (this.state != 5 && this.state != 3 && this.state != 1) {
            return 0;
        }
        if (this.player.isPlaying()) {
            this.player.pause();
        }
        this.skview.removeSakanaViewListener(this);
        try {
            this.state = 2;
            this.player.seekTo(f);
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    private int tell() {
        return this.player.getCurrentPosition();
    }

    private int play() {
        if (this.state == 3 || this.state == 5) {
            this.state = 4;
            this.player.start();
            this.skview.addSakanaViewListener(this);
            return 1;
        }
        System.out.println("SakanaMoviePlayer play invalid state : " + this.state);
        return 0;
    }

    private int pause() {
        if (this.state != 4) {
            return 0;
        }
        this.state = 5;
        this.player.pause();
        this.skview.removeSakanaViewListener(this);
        return 1;
    }

    private void close() {
        this.player.stop();
        this.player.release();
        this.surftex.release();
        this.state = 6;
        this.skview.removeSakanaViewListener(this);
    }

    private float[] capture() {
        if (this.framecount <= 0) {
            return null;
        }
        this.surftex.updateTexImage();
        this.surftex.getTransformMatrix(this.surfmat);
        this.framecount--;
        return this.surfmat;
    }

    @Override // ls.sakana.SakanaViewListener
    public void skFinished(SakanaView v) {
    }

    @Override // ls.sakana.SakanaViewListener
    public void skOnPause(SakanaView v) {
        this.viewpaused = true;
        if (this.state == 4) {
            this.player.pause();
        }
    }

    @Override // ls.sakana.SakanaViewListener
    public void skOnResume(SakanaView v) {
        this.viewpaused = false;
        if (this.state == 4) {
            this.player.start();
        }
    }
}
