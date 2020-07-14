package io.microshow.rxffmpeg.player;

import android.text.TextUtils;
import android.view.Surface;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * RxFFmpegPlayer 播放器内核
 * Created by Super on 2020/4/26.
 */
public abstract class RxFFmpegPlayer extends BaseMediaPlayer {

    static {
        System.loadLibrary("rxffmpeg-core");
        System.loadLibrary("rxffmpeg-player");
    }

    private native void nativeSetSurface(Surface surface);

    private native void nativePrepare(String url);

    private native void nativeStart();

    private native void nativePause();

    private native void nativeResume();

    private native void nativeStop();

    private native void nativeRelease();

    private native void nativeSeekTo(int position);

    private native boolean nativeIsPlaying();

    private native void nativeSetVolume(int percent);

    private native int nativeGetVolume();

    private native void nativeSetMuteSolo(int mute);

    private native int nativeGetMuteSolo();

    /**
     * 视频路径
     */
    protected String path;

    /**
     * 总时长
     */
    protected int mDuration = 0;

    /**
     * 循环标志
     */
    protected boolean looping;

    private CompositeDisposable mCompositeDisposable;

    /**
     * 延迟时间的 Disposable 用于循环播放
     */
    private Disposable mTimeDisposable;

    public RxFFmpegPlayer() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setSurface(Surface surface) {
        if (surface != null) {
            nativeSetSurface(surface);
        }
    }

    @Override
    public void setDataSource(String path) {
        this.path = path;
    }

    @Override
    public void prepare() {
        if (!TextUtils.isEmpty(path)) {
            nativePrepare(path);
        }
    }

    @Override
    public void pause() {
        nativePause();
    }

    @Override
    public void resume() {
        nativeResume();
    }

    @Override
    public void start() {
        if (!TextUtils.isEmpty(path)) {
            nativeStart();
        }
    }

    @Override
    public void stop() {
        cancelTimeDisposable();
        nativeStop();
    }

    @Override
    public void seekTo(int secds) {
        nativeSeekTo(secds);
    }

    @Override
    public int getDuration() {
        return mDuration;
    }

    @Override
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    @Override
    public boolean isLooping() {
        return looping;
    }

    @Override
    public boolean isPlaying() {
        return nativeIsPlaying();
    }

    @Override
    public void setVolume(int percent) {
        nativeSetVolume(percent);
    }

    @Override
    public int getVolume() {
        return nativeGetVolume();
    }

    @Override
    public void setMuteSolo(int mute) {
        nativeSetMuteSolo(mute);
    }

    @Override
    public int getMuteSolo() {
        return nativeGetMuteSolo();
    }

    @Override
    public void release() {
        setOnPreparedListener(null);
        setOnVideoSizeChangedListener(null);
        setOnLoadingListener(null);
        setOnTimeUpdateListener(null);
        setOnErrorListener(null);
        setOnCompleteListener(null);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        nativeRelease();
    }

    /**
     * 重新播放
     */
    @Override
    public void repeatPlay() {
        play(path, looping);
    }

    /**
     * 取消 延迟时间的 Disposable
     */
    private void cancelTimeDisposable() {
        if (mTimeDisposable != null && !mTimeDisposable.isDisposed()) {
            mTimeDisposable.dispose();
        }
    }

    /**
     * 准备状态  由native层回调
     */
    public void onPreparedNative() {
        if (mOnPreparedListener != null) {
            mOnPreparedListener.onPrepared(this);
        }
    }

    /**
     * 视频尺寸回调  由native层回调
     *
     * @param width  宽
     * @param height 高
     * @param dar    比例
     */
    public void onVideoSizeChangedNative(int width, int height, float dar) {
        if (mOnVideoSizeChangedListener != null) {
            mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, dar);
        }
    }

    /**
     * 加载状态 由native层回调
     *
     * @param load -
     */
    public void onLoadingNative(boolean load) {
        if (mOnLoadingListener != null) {
            mOnLoadingListener.onLoading(this, load);
        }
    }

    /**
     * 时间更新 由native层回调
     *
     * @param currentTime
     * @param totalTime
     */
    public void onTimeUpdateNative(int currentTime, int totalTime) {
        if (mOnTimeUpdateListener != null) {
            mDuration = totalTime;
            mOnTimeUpdateListener.onTimeUpdate(this, currentTime, totalTime);
        }
    }

    /**
     * 错误回调 由native层回调
     *
     * @param code -
     * @param msg  -
     */
    public void onErrorNative(int code, String msg) {
        if (mOnErrorListener != null) {
            mOnErrorListener.onError(this, code, msg);
        }
    }

    /**
     * 播放完成 由native层回调
     */
    public void onCompletionNative() {
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(this);
        }
        if (isLooping()) {//循环状态，则延迟重播
            mTimeDisposable = Flowable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            play(path, looping);
                        }
                    });
            mCompositeDisposable.add(mTimeDisposable);
        }
    }

}
