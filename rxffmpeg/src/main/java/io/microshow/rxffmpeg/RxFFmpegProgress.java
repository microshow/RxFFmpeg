package io.microshow.rxffmpeg;

/**
 * 进度封装
 */
public class RxFFmpegProgress {

    public int state = 0;

    /**
     * 执行进度
     */
    public int progress;

    /**
     * 执行的时间，相对于总时间 单位：微秒
     */
    public long progressTime;

    public RxFFmpegProgress(int state, int progress, long progressTime) {
        this.state = state;
        this.progress = progress;
        this.progressTime = progressTime;
    }

    public RxFFmpegProgress(int state) {
        this(state, 0, 0);
    }

}
