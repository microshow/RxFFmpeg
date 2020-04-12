package io.microshow.rxffmpeg;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxFFmpegInvoke 核心类
 * Created by Super on 2018/6/14.
 */

public class RxFFmpegInvoke {

    public static final String TAG = RxFFmpegInvoke.class.getSimpleName();

    static {
        System.loadLibrary("rxffmpeg-core");
        System.loadLibrary("rxffmpeg-invoke");
    }

    private static volatile RxFFmpegInvoke instance;

    /**
     * ffmpeg 回调监听
     */
    private IFFmpegListener ffmpegListener;

    private RxFFmpegInvoke() {

    }

    public static RxFFmpegInvoke getInstance() {
        if (instance == null) {
            synchronized (RxFFmpegInvoke.class) {
                if (instance == null) {
                    instance = new RxFFmpegInvoke();
                }
            }
        }
        return instance;
    }

    /**
     * 异步执行
     *
     * @param command
     * @param mffmpegListener
     */
    public void runCommandAsync(final String[] command, IFFmpegListener mffmpegListener) {
        setFFmpegListener(mffmpegListener);
        synchronized (RxFFmpegInvoke.class) {
            // 不允许多线程访问
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int ret = runFFmpegCmd(command);
                    onClean();
                }
            }).start();
        }
    }

    /**
     * 同步执行 (可以结合RxJava)
     *
     * @param command
     * @param mffmpegListener
     * @return
     */
    public int runCommand(final String[] command, IFFmpegListener mffmpegListener) {
        setFFmpegListener(mffmpegListener);
        int ret;
        synchronized (RxFFmpegInvoke.class) {
            ret = runFFmpegCmd(command);
            onClean();
            return ret;
        }
    }

    /**
     * [推荐使用]
     * 同步执行 RxJava 形式
     *
     * @param command
     * @return
     */
    public Flowable<RxFFmpegProgress> runCommandRxJava(final String[] command) {
        return Flowable.create(new FlowableOnSubscribe<RxFFmpegProgress>() {
            @Override
            public void subscribe(final FlowableEmitter<RxFFmpegProgress> emitter) {
                setFFmpegListener(new RxFFmpegInvoke.IFFmpegListener() {
                    @Override
                    public void onFinish() {
                        emitter.onComplete();
                    }

                    @Override
                    public void onProgress(int progress, long progressTime) {
                        emitter.onNext(new RxFFmpegProgress(RxFFmpegSubscriber.STATE_PROGRESS, progress, progressTime));
                    }

                    @Override
                    public void onCancel() {
                        //设为-100 作为取消状态
                        emitter.onNext(new RxFFmpegProgress(RxFFmpegSubscriber.STATE_CANCEL));
                    }

                    @Override
                    public void onError(String message) {
                        emitter.onError(new Throwable(message));
                    }
                });

                int ret = runFFmpegCmd(command);
                onClean();//解决内存泄露
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 执行ffmpeg cmd
     *
     * @param commands
     * @return
     */
    public native int runFFmpegCmd(String[] commands);

    /**
     * 退出，中断当前执行的cmd
     */
    public native void exit();

    /**
     * 设置是否处于调试状态
     *
     * @param debug
     */
    public native void setDebug(boolean debug);

    /**
     * 获取媒体文件信息
     *
     * @param filePath 音视频路径
     * @return info
     */
    public native String getMediaInfo(String filePath);

    /**
     * 内部进度回调
     *
     * @param progress     执行进度
     * @param progressTime 执行的时间，相对于总时间 单位：微秒
     */
    public void onProgress(int progress, long progressTime) {
        if (ffmpegListener != null) {
            ffmpegListener.onProgress(progress, progressTime);
        }
    }

    /**
     * 执行完成
     */
    public void onFinish() {
        if (ffmpegListener != null) {
            ffmpegListener.onFinish();
        }
    }

    /**
     * 执行取消
     */
    public void onCancel() {
        if (ffmpegListener != null) {
            ffmpegListener.onCancel();
        }
    }

    /**
     * 执行出错
     *
     * @param message
     */
    public void onError(String message) {
        if (ffmpegListener != null) {
            ffmpegListener.onError(message);
        }
    }

    /**
     * 清除
     */
    public void onClean() {
        //解决内存泄露
        if (ffmpegListener != null) {
            ffmpegListener = null;
        }
    }

    /**
     * 销毁实例
     */
    public void onDestroy() {
        if (ffmpegListener != null) {
            ffmpegListener = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    public IFFmpegListener getFFmpegListener() {
        return ffmpegListener;
    }

    /**
     * 设置执行监听
     *
     * @param ffmpegListener
     */
    public void setFFmpegListener(IFFmpegListener ffmpegListener) {
        this.ffmpegListener = ffmpegListener;
    }

    /**
     * IFFmpegListener监听接口
     */
    public interface IFFmpegListener {

        /**
         * 执行完成
         */
        void onFinish();

        /**
         * 进度回调
         *
         * @param progress     执行进度
         * @param progressTime 执行的时间，相对于总时间 单位：微秒
         */
        void onProgress(int progress, long progressTime);

        /**
         * 执行取消
         */
        void onCancel();

        /**
         * 执行出错
         *
         * @param message
         */
        void onError(String message);

    }

}
