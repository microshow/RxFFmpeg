package io.microshow.rxffmpeg.player;

import android.view.TextureView;

/**
 * 基础的 Player
 * Created by Super on 2020/7/14.
 */
public abstract class BaseMediaPlayer implements IMediaPlayer{

    public OnPreparedListener mOnPreparedListener;

    public OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    public OnLoadingListener mOnLoadingListener;

    public OnTimeUpdateListener mOnTimeUpdateListener;

    public OnErrorListener mOnErrorListener;

    public OnCompletionListener mOnCompletionListener;

    /**
     * 设置 TextureView
     *
     * @param textureView textureView
     */
    public abstract void setTextureView(TextureView textureView);

    /**
     * 播放 子类快捷实现
     *
     * @param path      path
     * @param isLooping isLooping
     */
    public abstract void play(String path, boolean isLooping);

    public abstract void repeatPlay();

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void setOnLoadingListener(OnLoadingListener listener) {
        this.mOnLoadingListener = listener;
    }

    public void setOnTimeUpdateListener(OnTimeUpdateListener listener) {
        this.mOnTimeUpdateListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setOnCompleteListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

}
