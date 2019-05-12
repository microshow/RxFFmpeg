

# RxFFmpeg

[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)
[![](https://img.shields.io/badge/FFmpeg-4.0-yellow.svg)](http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2)
[![](https://img.shields.io/badge/X264-20180212.2245-red.svg)](http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2)
[![](https://img.shields.io/badge/mp3lame-3.100-blue.svg)](https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz)
[![](https://img.shields.io/badge/fdkaac-0.1.6-orange.svg)](https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz)

<img src="/preview/icon/logo.jpg" alt="image-1：logo" width="260px"></img>

>RxFFmpeg is a fast processing framework for audio and video editing and video editing based on (FFmpeg 4.0 + X264 + mp3lame + fdk-aac) compiled for Android platform. It includes the following functions (video stitching, transcoding, compression, clipping, film head and tail, separation of audio and video, speed change, adding static stickers and GIF dynamic stickers, adding subtitles, adding filters, adding background music, adding Speed deceleration video, rewind audio video, audio clipping, voice change, mixing, picture synthesis video, video decoding pictures and other mainstream features...

# Compile

## Compiling Environment
  * win10 + ubuntu 16.04 + gcc + make

## Compiling mainly depends on the following Libraries

| Library        | Version    |  Download  |
| :--------   | :-----   | :---- |
| FFmpeg        | 4.0      |   http://ffmpeg.org/releases/ffmpeg-4.0.tar.bz2    |
| X264        | x264-snapshot-20180212-2245-stable      |   http://download.videolan.org/x264/snapshots/x264-snapshot-20180212-2245-stable.tar.bz2    |
| mp3lame        | 3.100      |   https://jaist.dl.sourceforge.net/project/lame/lame/3.100/lame-3.100.tar.gz    |
| fdk-aac        | 0.1.6      |   https://jaist.dl.sourceforge.net/project/opencore-amr/fdk-aac/fdk-aac-0.1.6.tar.gz    |
| ndk        | android-ndk-r14b-linux-x86_64      |   https://dl.google.com/android/repository/android-ndk-r14b-linux-x86_64.zip  |


## Compiler Script

* [Compiler script](preview/docs/build.md)

# Features

* **Supporting any FFmpeg command execution**

* **Support FFmpeg command execution schedule callback**

* **Supporting interruption of FFmpeg commands**

* **Supporting synchronous/asynchronous execution**

* **Support debug mode on/off**

* **Supports hardware acceleration to make codec processing faster (MediaCodec is turned on)**

* **Code encapsulation into SDK, easy to rely on**

* **Support sub-modules of FFmpeg libavutil
libavcodec 
libavformat 
libavdevice 
libavfilter 
libswscale 
libswresample 
libpostproc Ultimately packaged into a libffmpeg-core.so core library for easy dependency use**

* **Supporting libx264 encoding library, can make the compressed video volume become very small, the clarity remains very high, simple compression commands: ffmpeg -y -i /storage/emulated/0/1/input.mp4 -b 2097k -r 30 -vcodec libx264 -preset superfast /storage/emulated/0/1/result.mp4**

* **Background music that supports the addition of mp3, aac, wav and other mainstream formats**

* **Support mainstream video format conversion, such as: avi > mp4 > avi**

* **Intercept a graph every second: ffmpeg -y -i /storage/emulated/0/1/input.mp4 -f image2 -r 1 -q:v 10 -preset superfast /storage/emulated/0/1/%3d.jpg**


# Usage Mode

## Gradle

Add the Maven repository to build. gradle under the root directory

```groovy

allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}

```

Adding dependencies, latest version[![](https://www.jitpack.io/v/microshow/RxFFmpeg.svg)](https://www.jitpack.io/#microshow/RxFFmpeg)

```groovy

dependencies {
    implementation 'com.github.microshow:RxFFmpeg:1.2.4'
}

```

**Be careful**：If your project has so libraries on other platforms, such as arm64-v8a, add the following configuration to build. gradle to keep usage intact

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

## Start

* Open/close debug mode. It is recommended to initialize the call in Application

```java

RxFFmpegInvoke.getInstance().setDebug(true);

```

* FFmpeg command execution (RxJava2 elegant call)

```java

String text = "ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=25:5 -preset superfast /storage/emulated/0/1/result.mp4";

String[] commands = text.split(" ");

RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(new RxFFmpegSubscriber() {
            @Override
            public void onFinish() {
                if (mProgressDialog != null)
                    mProgressDialog.cancel();
                showDialog("Successful");
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
                showDialog("Cancelled");
            }

            @Override
            public void onError(String message) {
                if (mProgressDialog != null)
                    mProgressDialog.cancel();
                showDialog("onError：" + message);
            }
        });
```

* FFmpeg command execution (synchronization)

```java

RxFFmpegInvoke.getInstance().runCommand(command, null);

```

* Interrupt FFmpeg command

```java

RxFFmpegInvoke.getInstance().exit();

```

# Code Confusion

```text
-dontwarn io.microshow.rxffmpeg.**
-keep class io.microshow.rxffmpeg.**{*;}
```

# Frequently used commands

* [Summary of Common Commands](preview/docs/cmd.md)
* [FFmpeg Documentation](https://ffmpeg.org/documentation.html)
* [FFmpeg Filters](https://ffmpeg.org/ffmpeg-filters.html)

# Download Experience

| Scanning or [Download](https://github.com/microshow/RxFFmpeg/raw/master/preview/app-debug.apk)  | 
| :--------:   |
| <img src="/preview/icon/apk-qr.png" alt="image-1：Scanning Download Experience" width="260px" />       | 

# Unlimited Communication

QQ Email: 986386695@qq.com

| QQ Group：799934185  | 
| :--------:   |
| <img src="/preview/icon/qqGroup.jpg" alt="image-1：qqGroup" width="260px" />  | 

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
