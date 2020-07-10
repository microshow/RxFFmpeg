package io.microshow.rxffmpeg.app.utils;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * UmengHelper 友盟
 */
public class UmengHelper {

    private static final String TAG = UmengHelper.class.getSimpleName();

    private static final String UMENG_APP_KEY = "5f086f8c978eea0850afa1df";
    private static final String UMENG_MESSAGE_SECRET = "daf7b72fb35a2691870dc2143f79931d";

    /**
     * 初始化友盟
     */
    public static void initUMConfigure(Context context) {
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(false);
        UMConfigure.init(context, UMENG_APP_KEY, "", UMConfigure.DEVICE_TYPE_PHONE, UMENG_MESSAGE_SECRET);
        //友盟push
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setDisplayNotificationNumber(0);
        // 注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e(TAG, "mPushAgent.register onSuccess ======deviceToken======" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "mPushAgent.register onFailure " + s + s1);
            }
        });
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

}
