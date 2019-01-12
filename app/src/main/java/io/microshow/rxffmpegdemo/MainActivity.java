package io.microshow.rxffmpegdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.microshow.rxffmpegdemo.databinding.ActivityMainBinding;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

//    String path = Environment.getExternalStorageDirectory().getPath();

    //需要申请的权限，必须先在AndroidManifest.xml有声明，才可以动态获取权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private ActivityMainBinding binding;

    private  ProgressDialog mProgressDialog;

    //权限
    private RxPermissions rxPermissions = null;

    private long startTime;//记录开始时间
    private long endTime;//记录结束时间

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            rxPermissions.request(PERMISSIONS_STORAGE).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {// 用户同意了权限
                        runFFmpegRxJava();
                    } else {//用户拒绝了权限
                        Toast.makeText(MainActivity.this,"您拒绝了权限，请往设置里开启权限",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.editText.setText("ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=5:1 -preset superfast /storage/emulated/0/1/result.mp4");
        binding.button.setOnClickListener(this);

        rxPermissions = new RxPermissions(this);
    }

    /**
     * rxjava方式调用
     */
    private void runFFmpegRxJava () {
        openProgressDialog();

        final String text = binding.editText.getText().toString();
        String[] commands = text.split(" ");

        RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(new RxFFmpegSubscriber() {
            @Override
            public void onFinish() {
                if (mProgressDialog != null)
                    mProgressDialog.cancel();
                showDialog("处理成功");
            }

            @Override
            public void onProgress(int progress) {
                if (mProgressDialog != null)
                    mProgressDialog.setProgress(progress);
            }

            @Override
            public void onCancel() {
                if (mProgressDialog != null)
                    mProgressDialog.cancel();
                showDialog("已取消");
            }

            @Override
            public void onError(String message) {
                if (mProgressDialog != null)
                    mProgressDialog.cancel();
                showDialog("出错了 onError：" + message);
            }
        });
    }

    public void openProgressDialog() {
        startTime = System.nanoTime();
        mProgressDialog = Utils.openProgressDialog(this);
    }

    private void showDialog (String message) {
        endTime = System.nanoTime();
        Utils.showDialog(this, message, Utils.convertUsToTime((endTime-startTime)/1000, false));
    }

}