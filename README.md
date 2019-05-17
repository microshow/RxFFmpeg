
Language: [English](README_EN.md)

# RxFFmpeg

[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)
[![](https://img.shields.io/badge/X264-20180212.2245-red.svg)](http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2)
[![](https://img.shields.io/badge/mp3lame-3.100-blue.svg)](https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz)
[![](https://img.shields.io/badge/fdkaac-0.1.6-orange.svg)](https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz)

<img src="/preview/icon/logo-v1.gif" alt="图-1：logo" width="460px"></img>

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

* **每隔一秒截取一张图 ffmpeg -y -i /storage/emulated/0/1/input.mp4 -f image2 -r 1 -q:v 10 -preset superfast /storage/emulated/0/1/%3d.jpg**


# 使用方式

## Gradle

在根目录下的build.gradle里添加maven仓库

```groovy

allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}

```

添加依赖，最新版本[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)

```groovy

dependencies {
    implementation 'com.github.microshow:RxFFmpeg:1.2.4'
}

```

**注意**：如果你的项目存在其它平台的so库，如arm64-v8a，为了不影响使用需要在build.gradle添加如下配置

```groovy

defaultConfig {
    .
    .
    .
    ndk {
        abiFilters "armeabi-v7a"
    }
}

```

## 开始

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

# 代码混淆

```text
-dontwarn io.microshow.rxffmpeg.**
-keep class io.microshow.rxffmpeg.**{*;}
```

# 常用命令

* [常用命令汇总](preview/docs/cmd.md)
* [FFmpeg文档汇总](https://ffmpeg.org/documentation.html)
* [FFmpeg Filters文档](https://ffmpeg.org/ffmpeg-filters.html)

# ScreenShot

<img src="/preview/icon/screen-shot.gif" alt="图-1：screenShot.gif" width="320px"></img>

# 下载体验

| 扫码 or [点击下载](https://github.com/microshow/RxFFmpeg/raw/master/preview/app-debug.apk)  | 
| :--------:   |
| <img src="/preview/icon/apk-qr.png" alt="图-1：扫码下载体验" width="260px" />       | 

# 谁在用？

>按登记顺序排序，更多接入APP，欢迎在https://github.com/microshow/RxFFmpeg/issues/37 登记（供开源用户参考）

[![](https://pp.myapp.com/ma_icon/0/icon_42333639_1555789191/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.hndnews.main)
[![](http://appimg.hicloud.com/hwmarket/files/application/icon144/c3a7534396f04d9fa5bc2e5803c83fdc.png)](https://shouji.baidu.com/software/25866977.html)
<img src="https://www.hndnews.com/static/images/logo.png" height="96px" />
<img src="https://www.lgstatic.com/thumbnail_300x300/i/image2/M00/16/EC/CgoB5ln4FriAVfL6AAAU0vZMT60031.jpg" height="96px" />
<img src="https://www.kid17.com/static/kid17-web/kid17_logo01.svg" height="96px" />
[![](https://pp.myapp.com/ma_icon/0/icon_52610077_1556520138/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.kmb2b.KmMall)
[![](https://pp.myapp.com/ma_icon/0/icon_42274023_1557972414/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.yiqizhangda.parent)


# 沟通无限

QQ Email: 986386695@qq.com

| QQ群：799934185  | 
| :--------:   |
| <img src="/preview/icon/qqGroup.jpg" alt="图-1：qqGroup" width="260px" />  | 

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
