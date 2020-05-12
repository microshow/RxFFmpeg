package io.microshow.rxffmpeg.player;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * 播放器view
 * Created by Super on 2020/5/4.
 */
public class RxFFmpegPlayerView extends FrameLayout {

    private Context mContext;

    private MeasureHelper mMeasureHelper;

    private FrameLayout mContainer;

    private RxFFmpegPlayerController mPlayerController;

    private TextureView mTextureView;

    public RxFFmpegPlayer mPlayer;

    /**
     * 普通模式
     **/
    public static final int MODE_NORMAL = 0;

    /**
     * 全屏模式
     **/
    public static final int MODE_FULL_SCREEN = 1;

    private int mCurrentMode = MODE_NORMAL;

    public RxFFmpegPlayerView(Context context) {
        this(context, null);
    }

    public RxFFmpegPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mMeasureHelper = new MeasureHelper(this) {
            @Override
            public boolean isFullScreen() {
                return mCurrentMode == MODE_FULL_SCREEN;
            }
        };
        initContainer();
        initPlayer();
        setKeepScreenOn(true);//设置屏幕保持常亮
    }

    private void initPlayer() {
        if (mTextureView == null) {
            mTextureView = new ScaleTextureView(mContext);
        }
        mPlayer = new RxFFmpegPlayerImpl();
        mPlayer.setTextureView(mTextureView);
        mPlayer.setOnVideoSizeChangedListener(new VideoSizeChangedListener(this));
    }

    //更新view尺寸
    private void updateVideoLayoutParams(final int width, final int height, final float dar) {
        post(new Runnable() {
            @Override
            public void run() {
                mMeasureHelper.setVideoSizeInfo(new MeasureHelper.VideoSizeInfo(width, height, dar));
                mMeasureHelper.setVideoLayoutParams(mTextureView, mContainer);
            }
        });
    }

    /**
     * 视频size改变 监听
     */
    public static class VideoSizeChangedListener implements IMediaPlayer.OnVideoSizeChangedListener {

        private WeakReference<RxFFmpegPlayerView> mWeakReference;

        VideoSizeChangedListener(RxFFmpegPlayerView mRxFFmpegPlayerView) {
            mWeakReference = new WeakReference<>(mRxFFmpegPlayerView);
        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer mediaPlayer, int width, int height, float dar) {
            final RxFFmpegPlayerView mRxFFmpegPlayerView = mWeakReference.get();
            if (mRxFFmpegPlayerView != null) {
                mRxFFmpegPlayerView.updateVideoLayoutParams(width, height, dar);
            }
        }

    }

    private void initContainer() {
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer, params);
    }

    /**
     * 设置播放器背景颜色
     * @param color
     */
    public void setPlayerBackgroundColor(int color) {
        if (mContainer != null) {
            mContainer.setBackgroundColor(color);
        }
    }

    /**
     * 设置设置控制层容器
     *
     * @param playerController 控制层容器
     * @param fitModel         设置视频尺寸适配模式
     */
    public void setController(RxFFmpegPlayerController playerController, MeasureHelper.FitModel fitModel) {
        setFitModel(fitModel);
        mContainer.removeView(mPlayerController);
        mPlayerController = playerController;
        mPlayerController.setPlayerView(RxFFmpegPlayerView.this);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mPlayerController, params);

        addTextureView();
    }

    private void addTextureView() {
        mContainer.removeView(mTextureView);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mContainer.addView(mTextureView, 0, params);
    }

    public TextureView getTextureView() {
        return mTextureView;
    }

    public FrameLayout getContainerView() {
        return mContainer;
    }

    /**
     * 设置适配模式
     *
     * @param fitModel -
     */
    public void setFitModel(MeasureHelper.FitModel fitModel) {
        if (mMeasureHelper != null && fitModel != null) {
            mMeasureHelper.setFitModel(fitModel);
            //设置默认的 宽高
            mMeasureHelper.setDefaultVideoLayoutParams();
        }
    }

    /**
     * 播放
     *
     * @param videoPath -
     * @param isLooping -
     */
    public void play(String videoPath, boolean isLooping) {
        if (mPlayer != null && !Helper.isFastClick()) {
            mPlayer.play(videoPath, isLooping);
        }
    }

    /**
     * 重新播放
     */
    public void repeatPlay() {
        if (mPlayer != null) {
            mPlayer.repeatPlay();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            if (mPlayerController != null) {
                mPlayerController.onPause();
            }
        }
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (mPlayer != null) {
            mPlayer.resume();
            if (mPlayerController != null) {
                mPlayerController.onResume();
            }
        }
    }

    /**
     * 是否在播放
     *
     * @return -
     */
    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    /**
     * 是否循环
     *
     * @return -
     */
    public boolean isLooping() {
        return mPlayer != null && mPlayer.isLooping();
    }

//    /**
//     * 停止
//     */
//    public void stop() {
//        mPlayer.stop();
//    }

    /**
     * 设置音量 (需要在play方法之前调用)
     * @param percent 取值范围( 0 - 100 )； 0是静音
     */
    public void setVolume(int percent) {
        if (mPlayer != null) {
            mPlayer.setVolume(percent);
        }
    }

    /**
     * 获取音量
     * @return volume
     */
    public int getVolume() {
        if (mPlayer != null) {
            return mPlayer.getVolume() != -1 ? mPlayer.getVolume() : 100;
        } else {
            return 100;
        }
    }

    /**
     * 设置声道；0立体声；1左声道；2右声道
     */
    public void setMuteSolo(int mute) {
        if (mPlayer != null) {
            mPlayer.setMuteSolo(mute);
        }
    }

    /**
     * 获取声道：0立体声；1左声道；2右声道；
     */
    public int getMuteSolo() {
        if (mPlayer != null) {
            return mPlayer.getMuteSolo() != -1 ? mPlayer.getMuteSolo() : 0;
        } else {
            return 0;
        }
    }

    /**
     * 销毁
     */
    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        setKeepScreenOn(false);//设置屏幕保持常亮
    }

    /**
     * 当前是否是全屏
     * @return true:是；false:否
     */
    public boolean isFullScreenModel() {
        return mCurrentMode == MODE_FULL_SCREEN;
    }

    /**
     * 切换全屏或关闭全屏
     * @return true已经进入到全屏
     */
    public boolean switchScreen() {
        if (isFullScreenModel()) {
            //是全屏 则退出全屏
            return exitFullScreen();//退出全屏
        } else {
            return enterFullScreen();//进入全屏
        }
    }

    /**
     * 进入全屏
     */
    public boolean enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) return false;

        ViewGroup decorView = Helper.setFullScreen(mContext, true);
        if (decorView == null)
            return false;

        this.removeView(mContainer);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        decorView.addView(mContainer, params);

        mCurrentMode = MODE_FULL_SCREEN;

        return true;
    }

    /**
     * 退出全屏
     */
    public boolean exitFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {

            ViewGroup decorView = Helper.setFullScreen(mContext, false);
            if (decorView == null)
                return false;

            decorView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mCurrentMode = MODE_NORMAL;
        }
        return false;
    }

    //屏幕旋转后改变
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //屏幕旋转后更新layout尺寸
        if (mMeasureHelper != null) {
            mMeasureHelper.setVideoLayoutParams(mTextureView, mContainer);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size[] = mMeasureHelper.doMeasure(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size[0], size[1]);
    }

}
