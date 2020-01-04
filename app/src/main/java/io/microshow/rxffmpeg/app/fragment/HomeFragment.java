package io.microshow.rxffmpeg.app.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.Nullable;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.microshow.rxffmpeg.app.R;
import io.microshow.rxffmpeg.app.databinding.FragmentHomeBinding;
import io.microshow.rxffmpeg.app.utils.Utils;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 首页
 * Created by Super on 2019/12/7.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    //需要申请的权限，必须先在AndroidManifest.xml有声明，才可以动态获取权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //权限
    private RxPermissions rxPermissions = null;

    private Disposable mDisposable;

    private long startTime;//记录开始时间
    private long endTime;//记录结束时间

    private ProgressDialog mProgressDialog;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            mDisposable = rxPermissions.request(PERMISSIONS_STORAGE).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {// 用户同意了权限
                        runFFmpegRxJava();
                    } else {//用户拒绝了权限
                        Toast.makeText(getActivity(), "您拒绝了权限，请往设置里开启权限", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
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
        rxPermissions = new RxPermissions(this);
        binding.editText.setText("ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=5:1 -preset superfast /storage/emulated/0/1/result.mp4");
        binding.button.setOnClickListener(this);
    }

    /**
     * rxjava方式调用
     */
    private void runFFmpegRxJava() {
        openProgressDialog();

        final String text = binding.editText.getText().toString();
        String[] commands = text.split(" ");

        RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(new RxFFmpegSubscriber() {
            @Override
            public void onFinish() {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }
                showDialog("处理成功");
            }

            @Override
            public void onProgress(int progress, long progressTime) {
                if (mProgressDialog != null) {
                    mProgressDialog.setProgress(progress);
                    //progressTime 可以在结合视频总时长去计算合适的进度值
                    mProgressDialog.setMessage("已处理progressTime=" + (double) progressTime / 1000000 + "秒");
                }
            }

            @Override
            public void onCancel() {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }
                showDialog("已取消");
            }

            @Override
            public void onError(String message) {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }
                showDialog("出错了 onError：" + message);
            }
        });
    }

    private void openProgressDialog() {
        //统计开始时间
        startTime = System.nanoTime();
        mProgressDialog = Utils.openProgressDialog(getActivity());
    }

    private void showDialog(String message) {
        //统计结束时间
        endTime = System.nanoTime();
        Utils.showDialog(getActivity(), message, Utils.convertUsToTime((endTime - startTime) / 1000, false));
        StatService.onEventDuration(getActivity(), "RunFFmpegCommand", binding.editText.getText().toString(), (endTime - startTime) / (1000 * 1000));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}
