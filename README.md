# FastFFmpegAndroid

基于 **( FFmpeg 4.0 + X264 + mp3lame + fdk-aac )** 编译的 FFmpeg 库，适用于 Android 平台

# 编译环节

## 编译环境
  * win10 + ubuntu 16.04 + gcc + make 

## 主要依赖以下库进行编译

| 库名        | 版本    |  下载地址  |
| :--------   | :-----   | :---- |
| FFmpeg        | 4.0      |   http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2    |
| X264        | x264-snapshot-20180212-2245-stable      |   http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2    |
| mp3lame        | 3.100      |   https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz    |
| fdk-aac        | 0.1.6      |   https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz    |
| NDK Linux版        | android-ndk-r14b-linux-x86_64      |   https://dl.google.com/android/repository/android-ndk-r14b-linux-x86_64.zip    |


## 编译脚本
编译脚本、JNI代码整理后上传

# 必杀技
* 支持任何FFmpeg命令执行
* 支持FFmpeg命令执行进度回调
* 支持中断FFmpeg命令
* 支持硬件加速，使编解码处理更快
* 代码封装成SDK的方式，方便依赖使用

## 使用方式

* 开启/关闭 debug 模式

```java
FFmpegInvoke.getInstance().setDebug(true);
```

* FFmpeg 命令执行

```java

String text = "ffmpeg -y -i /storage/emulated/0/1/qq.mp4 -vf boxblur=25:5 -preset superfast /storage/emulated/0/1/result.mp4";

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
```

* 中断 FFmpeg 命令

```java
FFmpegInvoke.getInstance().exit();
```

# 下载体验
* 扫码下载体验

<img src="/preview/apkQR.png" alt="图-1：扫码下载体验"></img> 
[点击下载](https://github.com/microshow/FastFFmpegAndroid/blob/master/preview/app-debug.apk)
