package io.microshow.rxffmpeg.app.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mobstat.StatService;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.databinding.FragmentHomeBinding;
import io.microshow.rxffmpeg.app.utils.CPUUtils;
import io.microshow.rxffmpeg.app.utils.Utils;

/**
 * 首页
 * Created by Super on 2019/12/7.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    private long startTime;//记录开始时间
    private long endTime;//记录结束时间

    private ProgressDialog mProgressDialog;

    private MyRxFFmpegSubscriber myRxFFmpegSubscriber;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            runFFmpegRxJava();
        }
    }

    private void openProgressDialog() {
        //统计开始时间
        startTime = System.nanoTime();
        mProgressDialog = Utils.openProgressDialog(getActivity());
    }

    /**
     * 取消进度条
     *
     * @param dialogTitle Title
     */
    private void cancelProgressDialog(String dialogTitle) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
        if (!TextUtils.isEmpty(dialogTitle)) {
            showDialog(dialogTitle);
        }
    }

    /**
     * 设置进度条
     */
    private void setProgressDialog(int progress, long progressTime) {
        if (mProgressDialog != null) {
            mProgressDialog.setProgress(progress);
            //progressTime 可以在结合视频总时长去计算合适的进度值
            mProgressDialog.setMessage("已处理progressTime=" + (double) progressTime / 1000000 + "秒");
        }
    }

    private void showDialog(String message) {
        //统计结束时间
        endTime = System.nanoTime();
        Utils.showDialog(getActivity(), message, Utils.convertUsToTime((endTime - startTime) / 1000, false));
        StatService.onEventDuration(getActivity(), "RunFFmpegCommand", binding.editText.getText().toString(), (endTime - startTime) / (1000 * 1000));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initData() {
        init();
    }

    private void init() {
        binding.editText.setText("ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=5:1 -preset superfast /storage/emulated/0/1/result.mp4");
        binding.button.setOnClickListener(this);

        binding.tvCpu.setText("正在使用 " + CPUUtils.getCPUAbi() + " 架构");
    }

    /**
     * rxjava方式调用
     */
    private void runFFmpegRxJava() {
        openProgressDialog();

        final String text = binding.editText.getText().toString();
        String[] commands = text.split(" ");

        myRxFFmpegSubscriber = new MyRxFFmpegSubscriber(this);

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
                .runCommandRxJava(commands)
                .subscribe(myRxFFmpegSubscriber);

    }

    // 这里设为静态内部类，防止内存泄露
    public static class MyRxFFmpegSubscriber extends RxFFmpegSubscriber {

        private WeakReference<HomeFragment> mWeakReference;

        public MyRxFFmpegSubscriber(HomeFragment homeFragment) {
            mWeakReference = new WeakReference<>(homeFragment);
        }

        @Override
        public void onFinish() {
            final HomeFragment mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                mHomeFragment.cancelProgressDialog("处理成功");
            }
        }

        @Override
        public void onProgress(int progress, long progressTime) {
            final HomeFragment mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                //progressTime 可以在结合视频总时长去计算合适的进度值
                mHomeFragment.setProgressDialog(progress, progressTime);
            }
        }

        @Override
        public void onCancel() {
            final HomeFragment mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                mHomeFragment.cancelProgressDialog("已取消");
            }
        }

        @Override
        public void onError(String message) {
            final HomeFragment mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                mHomeFragment.cancelProgressDialog("出错了 onError：" + message);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myRxFFmpegSubscriber != null) {
            myRxFFmpegSubscriber.dispose();
        }
    }

}
