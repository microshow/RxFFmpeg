package io.microshow.rxffmpeg.player;

import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;

import java.lang.ref.WeakReference;

/**
 * 系统播放器 MediaPlayer 实现类
 * Created by Super on 2020/4/28.
 */
public class SystemMediaPlayerImpl extends SystemMediaPlayer implements TextureView.SurfaceTextureListener {

    private WeakReference<TextureView> mWeakTextureView;

    private static SurfaceTexture mSurfaceTexture;

    @Override
    public void setTextureView(TextureView textureView) {
        if (textureView != null) {
            mWeakTextureView = new WeakReference<>(textureView);
            textureView.setSurfaceTextureListener(this);
        }
    }

    private TextureView getTextureView() {
        if (mWeakTextureView != null) {
            TextureView view = mWeakTextureView.get();
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    @Override
    public void play(String path, boolean isLooping) {
        if (!TextUtils.isEmpty(path)) {
            mMediaPlayer.reset();
            setDataSource(path);
            setLooping(isLooping);
            setOnPreparedListener(new RxFFmpegPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mediaPlayer) {
                    //已准备好，立即就位 start
                    start();
                }
            });
            prepare();
        }
    }

    @Override
    public void release() {
        super.release();
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if (getTextureView() == null) {
            return;
        }
        // 解决 暂停状态下，旋转屏幕出现黑屏情况，
        // 因旋转屏幕后会重新生成一个surfaceTexture，
        // 这里把mSurfaceTexture保存起来，旋转后直接恢复第一个surfaceTexture  mTextureView.setSurfaceTexture(mSurfaceTexture);
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            setSurface(new Surface(mSurfaceTexture));
        } else {
            if (getTextureView() != null) {
                getTextureView().setSurfaceTexture(mSurfaceTexture);
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

}
