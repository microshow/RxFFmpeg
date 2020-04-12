package io.microshow.rxffmpeg.app.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * Created by Super on 2018/12/8.
 */
public class Utils {

    //优化内存使用
    static StringBuilder mUsDurationText = new StringBuilder();

    /**
     * 微秒转换为 时分秒毫秒,如 00:00:00.000
     *
     * @param us           微秒
     * @param autoEllipsis true:如果小时为0，则这样显示00:00.000;  false:全部显示 00:00:00.000
     * @return
     */
    public static String convertUsToTime(long us, boolean autoEllipsis) {

        mUsDurationText.delete(0, mUsDurationText.length());

        long ms = us / 1000;
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        //天
        long day = ms / dd;
        //小时
        long hour = (ms - day * dd) / hh;
        //分
        long minute = (ms - day * dd - hour * hh) / mi;
        //秒
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        //毫秒
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        if (autoEllipsis) {
            if (hour > 0) {
                mUsDurationText.append(strHour).append(":");
            }
        } else {
            mUsDurationText.append(strHour).append(":");
        }
        mUsDurationText.append(strMinute).append(":")
                .append(strSecond).append(".").append(strMilliSecond);
        return mUsDurationText.toString();
    }

    public static void showDialog(Context context, String message, String runTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message + "\n\n耗时时间：" + runTime);

        builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
//        builder.setCancelable(false);
        builder.create().show();
    }

    public static ProgressDialog openProgressDialog(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        final int totalProgressTime = 100;
        mProgressDialog.setMessage("正在转换视频，请稍后...");
        mProgressDialog.setButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //中断 ffmpeg
                RxFFmpegInvoke.getInstance().exit();
            }
        });
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat("");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(totalProgressTime);
        mProgressDialog.show();
        return mProgressDialog;
    }

    /**
     * 用于解决输入法内存泄露
     * @param context context
     */
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj = f.get(imm);
                if (obj instanceof View) {
                    View vGet = (View) obj;
                    if (vGet.getContext() == context) {
                        f.set(imm, null);
                    } else {
                        break;
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
