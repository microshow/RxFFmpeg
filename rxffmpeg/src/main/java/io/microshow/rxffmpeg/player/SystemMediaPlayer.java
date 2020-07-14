package io.microshow.rxffmpeg.player;

import android.media.MediaPlayer;
import android.os.Build;
import android.view.Surface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 系统播放器 MediaPlayer
 * Created by Super on 2020/7/14.
 */
public abstract class SystemMediaPlayer extends BaseMediaPlayer {

    public MediaPlayer mMediaPlayer;

    /**
     * 视频路径
     */
    protected String path;

    /**
     * 音量
     */
    public int volumePercent = -1;

    private CompositeDisposable mCompositeDisposable;

    /**
     * Disposable 播放进度更新
     */
    private Disposable mTimeUpdateDisposable;

    public SystemMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setSurface(Surface surface) {
        if (surface != null) {
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void setDataSource(String path) {
        try {
            this.path = path;
            mMediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare() {
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        cancelTimeUpdateDisposable();
    }

    @Override
    public void resume() {
        start();
    }

    @Override
    public void start() {
        mMediaPlayer.start();
        startTimeUpdateDisposable();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
    }

    @Override
    public void seekTo(int secds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mMediaPlayer.seekTo(secds, MediaPlayer.SEEK_CLOSEST);
        } else {
            mMediaPlayer.seekTo(secds);
        }
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void setVolume(int percent) {
        this.volumePercent = percent;
        mMediaPlayer.setVolume((float) percent / 100, (float) percent / 100);
    }

    @Override
    public int getVolume() {
        return volumePercent;
    }

    @Override
    public void setMuteSolo(int mute) {
    }

    @Override
    public int getMuteSolo() {
        return 0;
    }

    @Override
    public void release() {
        setOnPreparedListener(null);
        setOnVideoSizeChangedListener(null);
        setOnLoadingListener(null);
        setOnTimeUpdateListener(null);
        setOnErrorListener(null);
        setOnCompleteListener(null);
        cancelTimeUpdateDisposable();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        mMediaPlayer.release();
    }

    @Override
    public void setOnPreparedListener(final OnPreparedListener listener) {
        super.setOnPreparedListener(listener);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (listener != null) {
                    listener.onPrepared(SystemMediaPlayer.this);
                }
            }
        });
    }

    @Override
    public void setOnVideoSizeChangedListener(final OnVideoSizeChangedListener listener) {
        super.setOnVideoSizeChangedListener(listener);
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                if (listener != null) {
                    listener.onVideoSizeChanged(SystemMediaPlayer.this, width, height, (float) width / height);
                }
            }
        });
    }

    @Override
    public void setOnLoadingListener(final OnLoadingListener listener) {
        super.setOnLoadingListener(listener);
//        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//            @Override
//            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//                if (listener != null) {
//                    listener.onLoading(SystemMediaPlayer.this, i != 100);
//                }
//            }
//        });
    }

    @Override
    public void setOnErrorListener(final OnErrorListener listener) {
        super.setOnErrorListener(listener);
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                if (listener != null && what != -38) {
                    listener.onError(SystemMediaPlayer.this, what, extra + "");
                }
                return true;
            }
        });
    }

    @Override
    public void setOnCompleteListener(final OnCompletionListener listener) {
        super.setOnCompleteListener(listener);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (listener != null) {
                    listener.onCompletion(SystemMediaPlayer.this);
                }
            }
        });
    }

    @Override
    public void setOnTimeUpdateListener(final OnTimeUpdateListener listener) {
        super.setOnTimeUpdateListener(listener);
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
                        || what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
                    if (mOnLoadingListener != null) {//隐藏加载圈
                        mOnLoadingListener.onLoading(SystemMediaPlayer.this, false);
                    }
                    return true;
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    if (mOnLoadingListener != null) {//显示加载圈
                        mOnLoadingListener.onLoading(SystemMediaPlayer.this, true);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void repeatPlay() {
        play(this.path, mMediaPlayer.isLooping());
    }

    /**
     * 取消 时间更新 Disposable
     */
    private void cancelTimeUpdateDisposable() {
        if (mTimeUpdateDisposable != null && !mTimeUpdateDisposable.isDisposed()) {
            mTimeUpdateDisposable.dispose();
        }
    }

    /**
     * 开始执行时间更新
     */
    public void startTimeUpdateDisposable() {
        cancelTimeUpdateDisposable();
        //每 200 毫秒执行进度更新
        mTimeUpdateDisposable = Flowable.interval(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (mOnTimeUpdateListener != null) {
                            mOnTimeUpdateListener.onTimeUpdate(SystemMediaPlayer.this, mMediaPlayer.getCurrentPosition() / 1000, getDuration() / 1000);
                        }
                    }
                });
        mCompositeDisposable.add(mTimeUpdateDisposable);
    }

}
