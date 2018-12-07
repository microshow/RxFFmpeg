package com.superman.fastffmpegandroid;

import android.app.Application;

import com.superman.ffmpeg.FFmpegInvoke;

/**
 * Created by Super on 2018/12/7.
 */

public class FFmpegApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //开启debug模式，正式环境设为false即可
        FFmpegInvoke.getInstance().setDebug(true);

    }
}
