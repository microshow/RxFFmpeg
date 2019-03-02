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
        System.loadLibrary("ffmpeg-core");
        System.loadLibrary("ffmpeg-invoke");
    }

    private static RxFFmpegInvoke instance;

    /**
     * ffmpeg 回调监听
     */
    private IFFmpegListener ffmpegListener;

    public static RxFFmpegInvoke getInstance(){
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
     * @param command
     * @param mffmpegListener
     */
    public void runCommandAsync(final String[] command, IFFmpegListener mffmpegListener){
        setFFmpegListener (mffmpegListener);
        synchronized (RxFFmpegInvoke.class){
            // 不允许多线程访问
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int ret = runFFmpegCmd(command);
                }
            }).start();
        }
    }

    /**
     * 同步执行 (可以结合RxJava)
     * @param command
     * @param mffmpegListener
     * @return
     */
    public int runCommand(final String[] command, IFFmpegListener mffmpegListener){
        setFFmpegListener (mffmpegListener);
        int ret;
        synchronized (RxFFmpegInvoke.class){
            ret = runFFmpegCmd(command);
            return ret;
        }
    }

    /**
     * [推荐使用]
     * 同步执行 RxJava 形式
     * @param command
     * @return
     */
    public Flowable<Integer> runCommandRxJava(final String[] command){
        return Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) {
                setFFmpegListener (new RxFFmpegInvoke.IFFmpegListener() {
                    @Override
                    public void onFinish() {
                        emitter.onComplete();
                    }

                    @Override
                    public void onProgress(int progress) {
                        emitter.onNext(progress);
                    }

                    @Override
                    public void onCancel() {
                        //设为-100 作为取消状态
                        emitter.onNext(RxFFmpegSubscriber.STATE_CANCEL);
                    }

                    @Override
                    public void onError(String message) {
                        emitter.onError(new Throwable(message));
                    }
                });

                int ret = runFFmpegCmd(command);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 执行ffmpeg cmd
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
     * @param debug
     */
    public native void setDebug(boolean debug);

    public void onProgress(int progress) {
        if (ffmpegListener != null)
            ffmpegListener.onProgress(progress);
    }

    public void onFinish() {
        if (ffmpegListener != null)
            ffmpegListener.onFinish();
    }

    public void onCancel() {
        if (ffmpegListener != null)
            ffmpegListener.onCancel();
    }

    public void onError(String message) {
        if (ffmpegListener != null)
            ffmpegListener.onError(message);
    }

    public IFFmpegListener getFFmpegListener() {
        return ffmpegListener;
    }

    /**
     * 设置执行监听
     * @param ffmpegListener
     */
    public void setFFmpegListener(IFFmpegListener ffmpegListener) {
        this.ffmpegListener = ffmpegListener;
    }

    /**
     * IFFmpegListener监听接口
     */
    public interface IFFmpegListener {
        void onFinish();
        void onProgress(int progress);
        void onCancel();
        void onError(String message);
    }

}
