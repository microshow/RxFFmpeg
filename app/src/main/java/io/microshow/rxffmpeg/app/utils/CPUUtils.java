package io.microshow.rxffmpeg.app.utils;

import android.os.Build;

/**
 * Created by Super on 2020/4/16.
 */
public class CPUUtils {

    public static String getCPUAbi() {
        return Build.CPU_ABI;
    }

}
