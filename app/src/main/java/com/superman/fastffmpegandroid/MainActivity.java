package com.superman.fastffmpegandroid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.superman.ffmpeg.FFmpegInvoke;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ExecutorService es = Executors.newSingleThreadExecutor();
    String path = Environment.getExternalStorageDirectory().getPath();

    //需要申请的权限，必须先在AndroidManifest.xml有声明，才可以动态获取权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private TextView tv;

    private EditText editText;

    private  ProgressDialog mProgressDialog;

    //权限
    private RxPermissions rxPermissions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxPermissions = new RxPermissions(this);

        tv = (TextView)findViewById(R.id.sample_text);
//        tv.setText(path);

        editText = (EditText)findViewById(R.id.editText);
        editText.setText("ffmpeg -y -i /storage/emulated/0/1/qq.mp4 -vf boxblur=5:1 -preset superfast /storage/emulated/0/1/result.mp4");

        FFmpegInvoke.getInstance().setDebug(true);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rxPermissions.request(PERMISSIONS_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {// 用户同意了权限
                            runFFmpeg ();

                        } else {//用户拒绝了权限
                            Toast.makeText(MainActivity.this,"您拒绝了权限，请往设置里开启权限",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

    private void runFFmpeg () {
        openProgressDialog();
        final String text = editText.getText().toString();
        String[] commands = text.split(" ");

        FFmpegInvoke.getInstance().runCommand(commands, new FFmpegInvoke.IFFmpegListener() {
            @Override
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null)
                            mProgressDialog.cancel();
                        tv.setText("处理成功");
                        Toast.makeText(MainActivity.this, "处理成功", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onProgress(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null)
                            mProgressDialog.setProgress(progress);
                    }
                });
            }

            @Override
            public void onCancel() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null)
                            mProgressDialog.cancel();
                        tv.setText("onCancel");
                        Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null)
                            mProgressDialog.cancel();
                        tv.setText("onError=" + message);
                        Toast.makeText(MainActivity.this, "onError=" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void openProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        final int totalProgressTime = 100;
        mProgressDialog.setMessage("正在转换视频，请勿取消");
        mProgressDialog.setButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                FFmpegInvoke.getInstance().exit();

            }
        });
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(totalProgressTime);
        mProgressDialog.show();

    }

}
