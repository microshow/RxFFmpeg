package io.microshow.rxffmpeg.app;

import android.app.Application;

import com.baidu.mobstat.StatService;
import com.squareup.leakcanary.LeakCanary;

import io.microshow.rxffmpeg.BuildConfig;
import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * Application
 * Created by Super on 2018/12/7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        //baidu mtj-sdk 崩溃日志
        StatService.autoTrace(this);

        //开启debug模式，正式环境设为false即可
        RxFFmpegInvoke.getInstance().setDebug(BuildConfig.DEBUG);

    }
}
