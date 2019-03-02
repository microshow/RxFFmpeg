

# RxFFmpeg

[![](https://img.shields.io/badge/minSdkVersion-19-green.svg)](https://developer.android.google.cn)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)
[![](https://img.shields.io/badge/X264-20180212.2245-red.svg)](http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2)
[![](https://img.shields.io/badge/mp3lame-3.100-blue.svg)](https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz)
[![](https://img.shields.io/badge/fdkaac-0.1.6-orange.svg)](https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz)

>RxFFmpeg 是基于 ( FFmpeg 4.0 + X264 + mp3lame + fdk-aac ) 编译的适用于 Android 平台的音视频编辑、视频剪辑的快速处理框架，包含以下功能（视频拼接，转码，压缩，裁剪，片头片尾，分离音视频，变速，添加静态贴纸和gif动态贴纸，添加字幕，添加滤镜，添加背景音乐，加速减速视频，倒放音视频，音频裁剪，变声，混音，图片合成视频，视频解码图片等主流特色功能……

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
| ndk        | android-ndk-r14b-linux-x86_64      |   https://dl.google.com/android/repository/android-ndk-r14b-linux-x86_64.zip  |


## 编译脚本

* [编译脚本](preview/docs/build.md)

# 特色功能

* **支持任何FFmpeg命令执行**

* **支持FFmpeg命令执行进度回调**

* **支持中断FFmpeg命令**

* **支持同步/异步执行**

* **支持开启/关闭 debug 模式**

* **支持硬件加速，使编解码处理更快(已开启MediaCodec)**

* **代码封装成SDK的方式，方便依赖使用**

* **支持把FFmpeg的各子模块libavutil 
libavcodec 
libavformat 
libavdevice 
libavfilter 
libswscale 
libswresample 
libpostproc 最终打包成一个libffmpeg-core.so核心库方便依赖使用，无需导入七八个so库**

* **支持libx264编码库，可以使压缩后的视频体积变的极小，清晰度还保持着很高清，简单的压缩命令: ffmpeg -y -i /storage/emulated/0/1/input.mp4 -b 2097k -r 30 -vcodec libx264 -preset superfast /storage/emulated/0/1/result.mp4**

* **支持添加 mp3、aac、wav 等主流格式的背景音乐**

* **支持主流视频格式转换，如: avi > mp4 > avi** 


# 使用方式

* 开启/关闭 debug 模式，建议在 Application 初始化调用

```java

RxFFmpegInvoke.getInstance().setDebug(true);

```

* FFmpeg 命令执行 (RxJava2优雅的调用)

```java

String text = "ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=25:5 -preset superfast /storage/emulated/0/1/result.mp4";

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
```

* FFmpeg 命令执行 (同步方式)

```java

RxFFmpegInvoke.getInstance().runCommand(command, null);

```

* 中断 FFmpeg 命令

```java

RxFFmpegInvoke.getInstance().exit();

```
# 常用命令

* [常用命令汇总](preview/docs/cmd.md)

# 下载体验

* 扫码下载体验 [点击下载](/preview/app-debug.apk)

<img src="/preview/apkQR.png" alt="图-1：扫码下载体验"></img> 

* **注意**：体验App时，需要把预设的视频SD卡路径，改为你本地实际的视频SD卡路径。

# License
```text
Copyright 2019 Super

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
