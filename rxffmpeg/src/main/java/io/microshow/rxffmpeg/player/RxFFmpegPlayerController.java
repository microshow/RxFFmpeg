package io.microshow.rxffmpeg.player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 抽象的控制层容器
 * Created by Super on 2020/5/4.
 */
public abstract class RxFFmpegPlayerController extends FrameLayout {

    protected RxFFmpegPlayerView mPlayerView;

    protected RxFFmpegPlayer mPlayer;

    public RxFFmpegPlayerController(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        initView();
    }

    public void setPlayerView(RxFFmpegPlayerView playerView) {
        if (playerView != null) {
            this.mPlayerView = playerView;
            this.mPlayer = mPlayerView.mPlayer;
            initListener();
        }
    }

    /**
     * 子类实现 提供 layout id
     *
     * @return -
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 设置播放器动作Listener
     */
    protected abstract void initListener();

    /**
     * 播放器触发了 Pause
     */
    protected abstract void onPause();

    /**
     * 播放器触发了 Resume
     */
    protected abstract void onResume();

}
