package io.microshow.rxffmpeg.player;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 辅助类
 * Created by Super on 2020/4/30.
 */
public class Helper {

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 通过 context 找到 activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 获取DecorView
     */
    public static ViewGroup getDecorView(Context mContext) {
        Activity activity = Helper.scanForActivity(mContext);
        if (activity == null) return null;
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    /**
     * 获取DecorView
     */
    public static ViewGroup getDecorView(Activity _activity) {
        Activity activity = _activity;
        if (activity == null) return null;
        return (ViewGroup) activity.getWindow().getDecorView();
    }


    public static void showSysBar(Activity activity, ViewGroup decorView) {
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void hideSysBar(Activity activity, ViewGroup decorView) {
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置全屏
     *
     * @param context      context
     * @param isFullScreen true: 全屏； false:非全屏
     * @return decorView
     */
    public static ViewGroup setFullScreen(Context context, boolean isFullScreen) {

        Activity activity = Helper.scanForActivity(context);
        ViewGroup decorView = Helper.getDecorView(activity);
        if (decorView == null) {
            return null;
        }

        if (isFullScreen) {//全屏
            // 隐藏ActionBar、状态栏，并横屏
            hideSysBar(activity, decorView);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {//非全屏
            // 展示ActionBar、状态栏，并竖屏
            showSysBar(activity, decorView);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        return decorView;
    }


    public static String secdsToDateFormat(int secds, int totalsecds) {
        long hours = secds / (60 * 60);
        long minutes = (secds % (60 * 60)) / (60);
        long seconds = secds % (60);

        String sh = "00";
        if (hours > 0) {
            if (hours < 10) {
                sh = "0" + hours;
            } else {
                sh = hours + "";
            }
        }
        String sm = "00";
        if (minutes > 0) {
            if (minutes < 10) {
                sm = "0" + minutes;
            } else {
                sm = minutes + "";
            }
        }

        String ss = "00";
        if (seconds > 0) {
            if (seconds < 10) {
                ss = "0" + seconds;
            } else {
                ss = seconds + "";
            }
        }
        if (totalsecds >= 3600) {
            return sh + ":" + sm + ":" + ss;
        }
        return sm + ":" + ss;

    }

    private static long lastClickTime;

    /**
     * 防止快速点击
     *
     * @return true:是快速点击； false:否
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
