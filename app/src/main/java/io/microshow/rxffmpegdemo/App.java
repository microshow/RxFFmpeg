package io.microshow.rxffmpegdemo;

import android.app.Application;

import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * Application
 * Created by Super on 2018/12/7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //开启debug模式，正式环境设为false即可
        RxFFmpegInvoke.getInstance().setDebug(BuildConfig.DEBUG);

    }
}
