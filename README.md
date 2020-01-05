
Language: [English](README_EN.md)

# RxFFmpeg

[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)
[![](https://img.shields.io/badge/X264-20180212.2245-red.svg)](http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2)
[![](https://img.shields.io/badge/mp3lame-3.100-blue.svg)](https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz)
[![](https://img.shields.io/badge/fdkaac-0.1.6-orange.svg)](https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz)

<img src="/preview/icon/logo-v1.gif" alt="图-1：logo" width="460px"></img>

>RxFFmpeg 是基于 ( FFmpeg 4.0 + X264 + mp3lame + fdk-aac ) 编译的适用于 Android 平台的音视频编辑、视频剪辑的快速处理框架，包含以下功能（视频拼接，转码，压缩，裁剪，片头片尾，分离音视频，变速，添加静态贴纸和gif动态贴纸，添加字幕，添加滤镜，添加背景音乐，加速减速视频，倒放音视频，音频裁剪，**[##百变魔音##](https://github.com/microshow/AiSound)**，混音，图片合成视频，视频解码图片等主流特色功能…… 

# 【官方App】下载体验

| 扫码 or [点击下载](https://github.com/microshow/RxFFmpeg/raw/master/preview/app-release.apk)  |
| :--------:   |
| <img src="/preview/icon/apk-qr.png" alt="图-1：扫码下载体验" width="260px" />       |


# TODO

为响应Google的号召，Google Play从2019年8月1日起，在Google Play上发布app必须支持64位体系。从021年8月1日起，Google Play将停掉尚未支持64位体系的APP。

同时因广大开发者要求此项目提供arm64-v8a平台的支持，本项目在近期会提供arm64-v8a的支持，同时对代码及性能做了不少的优化，性能大大提升，在这里感谢大家一如既往的支持！

**已发布 V2.0.0 版本，新增支持arm64-v8a平台，同时对代码及性能做了不少的优化，转码效率大大提升【强力推荐更新】** 


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
libpostproc 最终打包成一个librxffmpeg-core.so核心库方便依赖使用，无需导入七八个so库**

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
    implementation 'com.github.microshow:RxFFmpeg:2.2.0'
}

```

如果你的App只要v7a平台，可以只保留armeabi-v7a，不过推荐加上arm64-v8a架构，这样性能会大大提升

```groovy

defaultConfig {
    .
    .
    .
    ndk {
        abiFilters "armeabi-v7a","arm64-v8a"
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
                    mProgressDialog.setMessage("已处理progressTime="+(double)progressTime/1000000+"秒");
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
```

* FFmpeg 命令执行 (同步方式)

```java

RxFFmpegInvoke.getInstance().runCommand(command, null);

```

* 中断 FFmpeg 命令 

```java

RxFFmpegInvoke.getInstance().exit();

```

* 构建命令，使用RxFFmpegCommandList构建，可以有效避免路径带有空格等问题

```java

public static String[] getBoxblur() {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append("/storage/emulated/0/1/input.mp4");
        cmdlist.append("-vf");
        cmdlist.append("boxblur=5:1");
        cmdlist.append("-preset");
        cmdlist.append("superfast");
        cmdlist.append("/storage/emulated/0/1/result.mp4");
        return cmdlist.build();
    }

```

* 获取媒体文件信息

```java

RxFFmpegInvoke.getInstance().getMediaInfo(String filePath);

```


# 代码混淆

```text
-dontwarn io.microshow.rxffmpeg.**
-keep class io.microshow.rxffmpeg.**{*;}
```

# 优质项目

### * [GSYVideoPlayer (Android端的视频播放器神器)](https://github.com/CarGuo/GSYVideoPlayer)

### * [AiSound百变魔音 (一个神奇的魔法声音)](https://github.com/microshow/AiSound)

### * [RetrofitGO (轻量级的网络请求库-支持缓存配置)](https://github.com/microshow/RetrofitGO)


# 常用命令

### * [常用命令汇总](preview/docs/cmd.md)
### * [FFmpeg文档汇总](https://ffmpeg.org/documentation.html)
### * [FFmpeg Filters文档](https://ffmpeg.org/ffmpeg-filters.html)

# ScreenShot

<img src="/preview/icon/screen-shot.gif" alt="图-1：screenShot.gif" width="320px"></img>

## 性能比对：arm64-v8a VS armeabi-v7a
<img src="/preview/icon/run_time_arm64-v8a.jpg" alt="图-2：arm64-v8a" width="320px"></img>
<img src="/preview/icon/run_time_armeabi-v7a.jpg" alt="图-1：armeabi-v7a" width="320px"></img>

>总结：可以看出arm64-v8a架构的运行效率远大于armeabi-v7a，强烈建议在你的App添加arm64-v8a架构的so,同时也是响应Google的号召。

# 谁在用？

>按登记顺序排序，更多接入APP，欢迎在https://github.com/microshow/RxFFmpeg/issues/37 登记（供开源用户参考）

[![](https://pp.myapp.com/ma_icon/0/icon_42333639_1555789191/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.hndnews.main)
[![](https://pp.myapp.com/ma_icon/0/icon_53292925_1558869088/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.jianyi.watermarkdog)
[![](https://pp.myapp.com/ma_icon/0/icon_52859659_1558810527/96)](https://a.app.qq.com/o/simple.jsp?pkgname=com.jmhy.tool)
[![](https://pp.myapp.com/ma_icon/0/icon_52610077_1556520138/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.kmb2b.KmMall)
[![](https://pp.myapp.com/ma_icon/0/icon_42274023_1557972414/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.yiqizhangda.parent)
[![](https://pp.myapp.com/ma_icon/0/icon_53871621_1563941814/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.soubo.zimujun)
[![](https://pp.myapp.com/ma_icon/0/icon_52821569_1564999429/96)](https://sj.qq.com/myapp/detail.htm?apkName=net.youqu.dev.android.miyu)
[![](https://pp.myapp.com/ma_icon/0/icon_53260423_1565165298/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.aiso.tea)
[![](http://www.aniic.com/public/l/18.4d1e77d4.png)](http://www.aniic.com/share/download?tdsourcetag=s_pcqq_aiomsg)
[![](https://pp.myapp.com/ma_icon/0/icon_42373340_1567047517/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.jycj.juyuanmeihui)
[![](https://pp.myapp.com/ma_icon/0/icon_52743084_1555772496/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.peace.calligraphy)
[![](https://pp.myapp.com/ma_icon/0/icon_12205109_1570448862/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.peace.guitarmusic)
[![](https://pp.myapp.com/ma_icon/0/icon_52873563_1570726608/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.queen.oa.xt)
[![](https://pp.myapp.com/ma_icon/0/icon_53899619_1572422746/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.shifenkexue.appscience)
[![](https://pp.myapp.com/ma_icon/0/icon_53923023_1570761579/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.sixsix.call)



# 沟通无限

QQ Email: 986386695@qq.com

| QQ群：799934185  | 
| :--------:   |
| <img src="/preview/icon/qqGroup.jpg" alt="图-1：qqGroup" width="260px" />  | 

# 感谢

撸码不易，如果觉得帮您节省了大量的开发时间，对您有所帮助，欢迎您的赞赏！

| 微信赞赏  |
| :--------:   |
| <img src="/preview/icon/weixinPay.png" alt="图-1：weixin" width="260px" />  |


# License

```text
Copyright 2020 Super

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
