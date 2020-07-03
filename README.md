
Language: [English](README_EN.md)

# RxFFmpeg

[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)
[![](https://img.shields.io/badge/X264-20180212.2245-red.svg)](http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2)
[![](https://img.shields.io/badge/mp3lame-3.100-blue.svg)](https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz)
[![](https://img.shields.io/badge/fdkaac-0.1.6-orange.svg)](https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz)

<img src="/preview/icon/logo-v1.gif" alt="图-1：logo" width="460px"></img>

>RxFFmpeg 是基于 ( FFmpeg 4.0 + X264 + mp3lame + fdk-aac ) 编译的适用于 Android 平台的音视频编辑、视频剪辑的快速处理框架，包含以下功能：视频拼接，转码，压缩，裁剪，片头片尾，分离音视频，变速，添加静态贴纸和gif动态贴纸，添加字幕，添加滤镜，添加背景音乐，加速减速视频，倒放音视频，**[#百变魔音#](https://github.com/microshow/AiSound)**，音频裁剪，混音，图片合成视频，视频解码图片，抖音首页，**[#视频播放器#](preview/docs/player.md)**，及支持 OpenSSL https 等主流特色功能
# 【官方App】下载体验

| 扫码 or [点击下载](https://github.com/microshow/RxFFmpeg/raw/master/preview/app-release.apk)  |
| :--------:   |
| <img src="/preview/icon/apk-qr.png" alt="图-1：扫码下载体验" width="260px" />       |


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
| ndk        | android-ndk-r10e-linux-x86_64      |   https://dl.google.com/android/repository/android-ndk-r10e-linux-x86_64.zip  |


## 编译脚本

* [编译脚本](preview/docs/build.md)

# 特色功能

| 功能        | 是否支持    |  简述  |
| :--------   | :-----:   | :---- |
| FFmpeg命令        | √  |   支持任何FFmpeg命令执行    |
| FFmpeg进度回调    | √  |   支持FFmpeg命令执行进度回调    |
| FFmpeg中断        | √  |   支持中断进行中的FFmpeg命令    |
| 同步执行          | √  |   适用于多条命令分步执行   |
| 异步执行          | √  |   适用于单条命令执行   |
| Debug模式         | √  |   支持开启/关闭 Debug 模式    |
| 平台架构          | √  |   支持 armeabi-v7a, arm64-v8a, x86, x86_64    |
| 硬件加速          | √  |   支持硬件加速，使编解码处理更快(已开启MediaCodec)    |
| 单个so打包        | √  |   支持把FFmpeg的各子模块打包成一个librxffmpeg-core.so核心库，无需导入七八个so库    |
| X264              | √  |  支持libx264编码库，可以使压缩后的视频体积变的极小，清晰度还保持着很高清    |
| mp3lame           | √  |  支持MP3音频编解码    |
| fdk-aac           | √  |  支持AAC音频编解码    |
| 格式转换          | √  |   如: avi > mp4 > avi;  mp3 > aac > mp3 等    |
| https             |  √  |   version >= 3.1.0  支持 https  |
| [自研播放器](preview/docs/player.md)           | √  |   RxFFmpegPlayer播放器主打轻量、高效、低功耗、视频秒开等特色   |
| [常用命令](preview/docs/cmd.md)           | √  |   持续更新 [点击查看](preview/docs/cmd.md)    |


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

添加依赖，最新版本[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)，[版本更新记录](https://github.com/microshow/RxFFmpeg/releases)

```groovy

dependencies {
    //以下两个选一个即可
    
    //完整版
    implementation 'com.github.microshow:RxFFmpeg:4.3.0'
    
    //极速版 (预计占用 4M 左右空间)
    implementation 'com.github.microshow:RxFFmpeg:4.3.0-lite'
    
}

```

如果你的App只要v7a平台，可以只保留armeabi-v7a，不过推荐加上arm64-v8a架构，这样性能会大大提升

```groovy

defaultConfig {
    .
    .
    .
    ndk {
        //目前已支持 "armeabi-v7a","arm64-v8a","x86","x86_64" 平台架构
        abiFilters "armeabi-v7a","arm64-v8a","x86","x86_64"
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

    private void runFFmpegRxJava() {

        String text = "ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=25:5 -preset superfast /storage/emulated/0/1/result.mp4";

        String[] commands = text.split(" ");

        myRxFFmpegSubscriber = new MyRxFFmpegSubscriber(this);

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
                .runCommandRxJava(commands)
                .subscribe(myRxFFmpegSubscriber);

    }

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

```java

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

<img src="/preview/icon/screen-shot.gif" alt="图-1：screenShot.gif" width="240px"></img>

## 性能比对：arm64-v8a VS armeabi-v7a
<img src="/preview/icon/run_time_arm64-v8a.jpg" alt="图-2：arm64-v8a" width="240px"></img>
<img src="/preview/icon/run_time_armeabi-v7a.jpg" alt="图-1：armeabi-v7a" width="240px"></img>

>总结：可以看出arm64-v8a架构的运行效率远大于armeabi-v7a，强烈建议在你的App添加arm64-v8a架构的so,同时也是响应Google的号召。

# 实验室

## [自研视频播放器RxFFmpegPlayer](preview/docs/player.md)

* 自研RxFFmpegPlayer播放器内核基于(FFmpeg OpenGL OpenSL)

* 主打轻量级、高效、低功耗，播放本地视频秒开等特色

* 涵盖播放器基本的 播放、暂停、快进、快退、音量控制、声道控制、循环播放、播放进度、加载状态等功能

* 支持播放本地视频文件、在线视频、直播流(hls、rtmp、m3u8…)等

* 项目里已集成OpenSSL所以也支持Https访问

* 视频画面支持双指缩放、旋转、移动等手势操作

<img src="/preview/icon/screen_player.gif" alt="图-1：screen_player.gif" width="240px"></img>


# 谁在用？

>按登记顺序排序，更多接入APP，欢迎在https://github.com/microshow/RxFFmpeg/issues/37 登记（供开源用户参考）

[![](https://pp.myapp.com/ma_icon/0/icon_42333639_1555789191/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.hndnews.main)
[![](https://www.hndnews.com/static/images/logo.png)](https://www.hndnews.com)
[![](https://pp.myapp.com/ma_icon/0/icon_53292925_1558869088/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.jianyi.watermarkdog)
[![](https://pp.myapp.com/ma_icon/0/icon_53929550_1579661691/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.cqjy.eyeschacar)
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
[![](https://pp.myapp.com/ma_icon/0/icon_54022753_1585815499/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.bigtotoro.shuiyincamera)
[![](https://pp.myapp.com/ma_icon/0/icon_54017775_1585724965/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.yuanli.mvpaudioextraction)
[![](https://pp.myapp.com/ma_icon/0/icon_52517100_1581646762/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.yuan_li_network.texttospeechyuanli)
[![](https://pp.myapp.com/ma_icon/0/icon_52745305_1585818590/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.zhouzining.yyxc)
[![](https://pp.myapp.com/ma_icon/0/icon_53332875_1586397821/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.shockwv.nevermore)
[![](https://pp.myapp.com/ma_icon/0/icon_52762584_1586228655/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.yz.live)
[![](https://pp.myapp.com/ma_icon/0/icon_52649928_1583549532/96)](https://sj.qq.com/myapp/detail.htm?apkName=scs.com.crosscountry)
[![](https://upload.jianshu.io/collections/images/1866835/80.png?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240)](https://www.vivhist.com/)
[![](https://pp.myapp.com/ma_icon/0/icon_52761428_1591599723/96)](https://sj.qq.com/myapp/detail.htm?apkName=sulisong.ShelledPro.Screenshots)
<img src="http://image.coolapk.com/apk_logo/2019/0317/22/500px-219205-o_1d662al3g3ufvge8v51odk1iimq-uid-1030107@500x500.png" alt="https://www.coolapk.com/apk/yanyan.com.tochar" width="96px"></img>
[![](https://pp.myapp.com/ma_icon/0/icon_53947191_1592382950/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.koki.callshow)
[![](https://pp.myapp.com/ma_icon/0/icon_54014901_1589092180/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.mzxf.funchild)
[![](https://pp.myapp.com/ma_icon/0/icon_53800886_1593572867/96)](https://sj.qq.com/myapp/detail.htm?apkName=com.smallyin.fastcompre)



# 沟通无限

欢迎加入高质量音视频交流群，RxFFmpeg助力您学习移动端音视频！

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
