package io.microshow.rxffmpeg.player;

import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Created by Super on 2020/5/4.
 */
public class MeasureHelper {

    private WeakReference<View> mWeakView;

    private VideoSizeInfo mVideoSizeInfo;

    private int mMeasuredWidth;
    private int mMeasuredHeight;

    /**
     * 适配模式
     */
    private FitModel mFitModel = FitModel.FM_DEFAULT;

    /**
     * 适配模式
     */
    public enum FitModel {

        /**
         * 默认
         */
        FM_DEFAULT

    }

    public static class VideoSizeInfo {

        private int mWidth;
        private int mHeight;
        private float mDar;

        public VideoSizeInfo(int width, int height, float dar) {
            this.mWidth = width;
            this.mHeight = height;
            this.mDar = dar;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public float getDar() {
            return mDar;
        }
    }

    /**
     * 设置适配模式
     *
     * @param fitModel -
     */
    public void setFitModel(FitModel fitModel) {
        this.mFitModel = fitModel;
    }

    /**
     * 获取适配模式
     *
     * @return
     */
    public FitModel getFitModel() {
        return mFitModel;
    }

    public MeasureHelper(View view) {
        mWeakView = new WeakReference<>(view);
    }

    public View getView() {
        if (mWeakView != null) {
            View view = mWeakView.get();
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    /**
     * 设置视频信息
     *
     * @param videoSizeInfo -
     */
    public void setVideoSizeInfo(VideoSizeInfo videoSizeInfo) {
        this.mVideoSizeInfo = videoSizeInfo;
    }

    public VideoSizeInfo getVideoSizeInfo() {
        return mVideoSizeInfo;
    }

    public boolean isFullScreen() {
        return false;
    }

    /**
     * 设置默认的播放器容器宽高
     */
    public void setDefaultVideoLayoutParams() {
        View view = getView();
        RxFFmpegPlayerView mPlayerView = null;
        if (view instanceof RxFFmpegPlayerView) {
            mPlayerView = (RxFFmpegPlayerView) view;
            int width;//宽
            int height;//高
            width = Helper.getScreenWidth(view.getContext());
            height = width * 9 / 16;
            setVideoSizeInfo(new MeasureHelper.VideoSizeInfo(width, height, (float) width / height));
            setVideoLayoutParams(mPlayerView.getTextureView(), mPlayerView.getContainerView());
        }
    }

    public void setVideoLayoutParams(TextureView textureView, FrameLayout container) {
        if (textureView == null || container == null || getVideoSizeInfo() == null) {
            return;
        }

        int videoWidth = getVideoSizeInfo().getWidth();
        int videoHeight = getVideoSizeInfo().getHeight();
        float dar = getVideoSizeInfo().getDar();

        //原始视频宽高比
        float videoAspect = (float) videoWidth / videoHeight;

        int viewWidth = Helper.getScreenWidth(getView().getContext());

        int viewHeight = 0;

        if (isFullScreen()) {//全屏
            //高度铺满
            viewHeight = Helper.getScreenHeight(getView().getContext());
            //宽度按比例
            viewWidth = (int) (viewHeight * videoAspect);

        } else {//非全屏
            if (videoWidth > videoHeight) {//横屏视频
                //宽铺满，高度按比例
                viewHeight = (int) (viewWidth / videoAspect);

            } else if (videoWidth < videoHeight) {//竖屏视频
                //高铺满 宽自适应
                viewHeight = viewWidth;
                viewWidth = (int) (viewHeight * videoAspect);
            } else {//正方形视频
                viewHeight = viewWidth;
            }
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewWidth, viewHeight);
        params.gravity = Gravity.CENTER;
//            LogUtils.d("Aspect: viewWith=" + viewWidth + ", viewHeight=" + viewHeight + ", dar=" + dar);
//            LogUtils.d("Aspect: w=" + videoWidth + ", h=" + videoHeight + ", videoAspect=" + videoAspect);

        textureView.setLayoutParams(params);

        //容器的宽固定铺满状态，高度跟随playerView的高
        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(Helper.getScreenWidth(getView().getContext()), viewHeight);
        container.setLayoutParams(containerParams);

        mMeasuredHeight = viewHeight;
        getView().requestLayout();
    }

    /**
     * 开始适配
     *
     * @param widthMeasureSpec  -
     * @param heightMeasureSpec -
     */
    public int[] doMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int viewWidth, viewHeight;

        if (mFitModel == FitModel.FM_DEFAULT) {
            viewWidth = widthMeasureSpec;
            viewHeight = mMeasuredHeight;

        } else {
            viewWidth = widthMeasureSpec;
            viewHeight = heightMeasureSpec;
        }

        int size[] = new int[2];
        size[0] = viewWidth;
        size[1] = viewHeight;

        return size;
    }

}
