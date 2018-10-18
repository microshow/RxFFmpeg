package com.superman.ffmpeg;

/**
 * Created by Super on 2018/6/14.
 */

public class FFmpegInvoke {

    public static final String TAG = FFmpegInvoke.class.getSimpleName();

    static {
        System.loadLibrary("ffmpeg-core");
        System.loadLibrary("ffmpeg-invoke");
    }

    private static FFmpegInvoke instance;

    public IFFmpegListener ffmpegListener;

    public static FFmpegInvoke getInstance(){
        if (instance == null) {
            synchronized (FFmpegInvoke.class) {
                if (instance == null) {
                    instance = new FFmpegInvoke();
                }
            }
        }
        return instance;
    }

    public void runCommand(final String[] command, IFFmpegListener mffmpegListener){
        ffmpegListener = mffmpegListener;
        synchronized (FFmpegInvoke.class){
            // 不允许多线程访问
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int ret = runFFmpegCmd(command);
                }
            }).start();
        }
    }

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

    public interface IFFmpegListener {
        void onFinish();
        void onProgress(int progress);
        void onCancel();
        void onError(String message);
    }

}
