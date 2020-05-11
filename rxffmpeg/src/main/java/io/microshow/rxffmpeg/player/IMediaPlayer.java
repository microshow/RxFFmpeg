package io.microshow.rxffmpeg.player;


import android.view.Surface;

/**
 * 播放器基础接口
 * Created by Super on 2020/4/26.
 */
public interface IMediaPlayer {

    /**
     * 视频画面承载
     *
     * @param surface surface
     */
    void setSurface(Surface surface);

    /**
     * 通过一个具体的路径来设置MediaPlayer的数据源，path可以是本地的一个路径，也可以是一个网络路径
     *
     * @param path -
     */
    void setDataSource(String path);

    /**
     * 装载流媒体文件
     */
    void prepare();

    /**
     * 暂停
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * 开始
     */
    void start();

    /**
     * 停止
     */
    void stop();

    /**
     * 指定播放的位置
     *
     * @param secds -
     */
    void seekTo(int secds);

    /**
     * 得到文件的时间
     *
     * @return -
     */
    int getDuration();

    /**
     * 是否循环
     *
     * @param looping -
     */
    void setLooping(boolean looping);

    /**
     * 是否循环播放
     *
     * @return -
     */
    boolean isLooping();

    /**
     * 是否正在播放
     *
     * @return -
     */
    boolean isPlaying();

    /**
     * 设置音量
     * @param percent 取值范围( 0 - 100 )； 0是静音
     */
    void setVolume(int percent);

    /**
     * 获取音量 默认100
     */
    int getVolume();

    /**
     * 设置声道；0立体声；1左声道；2右声道
     */
    void setMuteSolo(int mute);

    /**
     * 获取声道：0立体声；1左声道；2右声道；
     * 如果没有调用setMuteSolo，则返回-1 （默认没有设置）
     * @return
     */
    int getMuteSolo();

    /**
     * 回收流媒体资源
     */
    void release();


    /**
     * 装载流媒体完毕的时候回调
     */
    interface OnPreparedListener {
        void onPrepared(IMediaPlayer mediaPlayer);
    }

    /**
     * 网络流媒体播放结束时回调
     */
    interface OnCompletionListener {
        void onCompletion(IMediaPlayer mediaPlayer);
    }

    /**
     * 发生错误时回调
     */
    interface OnErrorListener {
        void onError(IMediaPlayer mediaPlayer, int err, String msg);
    }

    /**
     * 加载回调
     */
    interface OnLoadingListener {
        void onLoading(IMediaPlayer mediaPlayer, boolean isLoading);
    }

    /**
     * 视频size改变
     */
    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IMediaPlayer mediaPlayer, int width, int height, float dar);
    }

    /**
     * 时间更新回调
     */
    interface OnTimeUpdateListener {
        void onTimeUpdate(IMediaPlayer mediaPlayer, int currentTime, int totalTime);
    }

}
