package io.microshow.rxffmpeg;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * RxFFmpegSubscriber rxjava 回调
 * Created by Super on 2019/1/12.
 */
public abstract class RxFFmpegSubscriber extends DisposableSubscriber<RxFFmpegProgress> implements RxFFmpegInvoke.IFFmpegListener {

    /**
     * 取消状态
     */
    public static int STATE_CANCEL = -100;

    /**
     * 正常执行
     */
    public static int STATE_PROGRESS = 100;

    @Override
    public void onNext(RxFFmpegProgress mProgress) {
        if (mProgress.state == STATE_CANCEL) {//取消状态
            onCancel();
        } else {//进度回调
            onProgress(mProgress.progress, mProgress.progressTime);
        }
    }

    @Override
    public void onError(Throwable t) {
        onError(t.getMessage());
    }

    @Override
    public void onComplete() {
        onFinish();
    }

}
