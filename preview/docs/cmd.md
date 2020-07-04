
# 常用命令

# 视频处理

*  视频压缩

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -b 2097k -r 30 -vcodec libx264 -preset superfast /storage/emulated/0/1/result.mp4

```

*  视频拼接

```java

ffmpeg -y -f concat -safe 0 -i Cam01.txt -c copy Cam01.mp4

```

这种合并方式的适用场景是：当容器格式不支持文件层次的合并，而又不想（不需要）进行再编码的操作的时候。这种方式对源视频同样有【同格式同性质】的要求

Cam01.txt文件里面的内容类似如下(要改成全路径形式)

file 'input1.mp4'

file 'input2.mp4'

file 'input3.mp4'


* 视频转图片(每隔一秒截取一张图)

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -f image2 -r 1 -q:v 10 -preset superfast /storage/emulated/0/1/%3d.jpg

```

* 截取指定时间的一张图)

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -f image2 -ss 00:00:03 -vframes 1 -preset superfast /storage/emulated/0/1/result.jpg

```

* 添加背景音乐（支持调节原音和配乐的音量）

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -i /storage/emulated/0/1/input.mp3 -filter_complex [0:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=0.2[a0];[1:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=1[a1];[a0][a1]amix=inputs=2:duration=first[aout] -map [aout] -ac 2 -c:v copy -map 0:v:0 -preset superfast /storage/emulated/0/1/result.mp4

```

* 添加水印

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -i /storage/emulated/0/1/input.png -filter_complex [0:v]scale=iw:ih[outv0];[1:0]scale=0.0:0.0[outv1];[outv0][outv1]overlay=0:0 -preset superfast /storage/emulated/0/1/result.mp4

```

* Gif转视频

```java

ffmpeg -y -i /storage/emulated/0/1/input.gif -pix_fmt yuv420p -preset superfast /storage/emulated/0/1/result.mp4

```

* 视频转Gif

```java

ffmpeg -y -ss 0 -t 7 -i /storage/emulated/0/1/input.mp4 -r 5 -s 280x606 -preset superfast /storage/emulated/0/1/result.gif

```

* 图片合成视频（带动画）

```java

ffmpeg -y -loop 1 -r 25 -i /storage/emulated/0/1/input.png -vf zoompan=z=1.1:x='if(eq(x,0),100,x-1)':s='960*540' -t 10 -pix_fmt yuv420p /storage/emulated/0/1/result.mp4

```

* 视频去水印

```java

//x:y 离左上角的坐标 值必须>=1;  w:h logo的宽和高;  show：设为1有一个绿色的矩形边框，默认值0
ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf delogo=x=1:y=1:w=200:h=200:show=1 -preset superfast /storage/emulated/0/1/result.mp4

```

* 视频变速

```java

// X = 取值范围 [0.5 - 2.0] ; 1.0为标准速度， 替换 X 的值即可
ffmpeg -y -i /storage/emulated/0/1/input.mp4 -filter_complex [0:v]setpts=PTS/X[v];[0:a]atempo=X[a] -map [v] -map [a] -preset superfast /storage/emulated/0/1/result.mp4

```

# 音频处理

* 音频拼接

```java

ffmpeg -y -i concat:123.mp3|124.mp3 -acodec copy output.mp3

```

-i代表输入参数

contact:123.mp3|124.mp3代表着需要连接到一起的音频文件
      
-acodec copy output.mp3 重新编码并复制到新文件中

* 音频混音

```java

ffmpeg -y -i 124.mp3 -i 123.mp3 -filter_complex amix=inputs=2:duration=first:dropout_transition=2 -f mp3 remix.mp3

```

-i代表输入参数

-filter_complex ffmpeg滤镜功能，非常强大，详细请查看文档

amix是混合多个音频到单个音频输出

inputs=2代表是2个音频文件，如果更多则代表对应数字

duration 确定最终输出文件的长度

longest(最长)|shortest（最短）|first（第一个文件）

dropout_transition  The transition time, in seconds, for volume renormalization when an input stream ends. The default value is 2 seconds.

-f mp3  输出文件格式
            
* 音频裁剪          

```java

ffmpeg -y -i 124.mp3 -vn -acodec copy -ss 00:00:00 -t 00:01:32 output.mp3

```

-i代表输入参数

-acodec copy output.mp3 重新编码并复制到新文件中

-ss 开始截取的时间点

-t 截取音频时间长度

* 音频格式转换

```java

ffmpeg -y -i null.ape -ar 44100 -ac 2 -ab 16k -vol 50 -f mp3 null.mp3

```

-i代表输入参数

-acodec aac（音频编码用AAC） 

-ar 设置音频采样频率

-ac  设置音频通道数

-ab 设定声音比特率

-vol <百分比> 设定音量



* 音频的某段时间设为静音

```java


ffmpeg -y -i /storage/emulated/0/1/input.mp3 -af volume=enable='between(t,0,2)':volume=0,volume=enable='between(t,15,20)':volume=0 /storage/emulated/0/1/result.mp3


```


# 在线视频处理(支持 http / https / rtmp / hls / m3u8...等等)

*  下载rtmp(湖南卫视)直播流的10秒视频转成mp4,并保存到本地

```java

ffmpeg -y -i rtmp://58.200.131.2:1935/livetv/hunantv -t 10 -preset superfast /storage/emulated/0/1/result.mp4

```




# 高级用法

*  图片+视频+视频 混合拼接

```java

ffmpeg -y -loop 1 -framerate 25 -t 10.0 -i /storage/emulated/0/1/input.jpg -ss 5.0 -t 5.04 -accurate_seek -i /storage/emulated/0/1/input.mp4 -ss 0.0 -t 5.921 -accurate_seek -i /storage/emulated/0/1/input2.mp4 -f lavfi -t 10.0 -i anullsrc=channel_layout=stereo:sample_rate=44100 -filter_complex [0:v]scale=260.0:260.0,pad=320:260:30.0:0.0,setdar=320/260[outv0];[1:v]scale=320.0:256.0,pad=320:260:0.0:2.0,setdar=320/260[outv1];[2:v]scale=320.0:180.0,pad=320:260:0.0:40.0,setdar=320/260[outv2];[outv0][outv1][outv2]concat=n=3:v=1:a=0:unsafe=1[outv];[3:a][1:a][2:a]concat=n=3:v=0:a=1[outa] -map [outv] -map [outa] -r 25 -b 1M -f mp4 -t 20.961 -vcodec libx264 -c:a aac -pix_fmt yuv420p -s 320x260 -preset superfast /storage/emulated/0/1/result.mp4

```

*  视频[片尾]添加3秒的图片视频

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -loop 1 -framerate 25 -t 3.0 -i /storage/emulated/0/1/input.png -f lavfi -t 3.0 -i anullsrc=channel_layout=stereo:sample_rate=44100 -filter_complex [0:v]scale=iw:ih[outv0];[1:v]scale=iw:ih[outv1];[outv0][outv1]concat=n=2:v=1:a=0:unsafe=1[outv];[0:a][2:a]concat=n=2:v=0:a=1[outa] -map [outv] -map [outa] -r 25 -b 1M -f mp4 -vcodec libx264 -c:a aac -pix_fmt yuv420p -s 960x540 -preset superfast /storage/emulated/0/1/result.mp4

```

*  视频[片头]添加3秒的图片视频

```java

ffmpeg -y -loop 1 -framerate 1 -t 3 -i /storage/emulated/0/1/input.png -i /storage/emulated/0/1/input.mp4 -f lavfi -t 3.0 -i anullsrc=channel_layout=stereo:sample_rate=44100 -filter_complex [0:v]scale=iw:ih[outv0];[1:v]scale=iw:ih[outv1];[outv0][outv1]concat=n=2:v=1:a=0:unsafe=1[outv];[2:a][1:a]concat=n=2:v=0:a=1[outa] -map [outv] -map [outa] -r 15 -b 1M -f mp4 -vcodec libx264 -c:a aac -pix_fmt yuv420p -s 960x540 -preset superfast /storage/emulated/0/1/result.mp4

```


# 其它

* FFmpeg文档汇总：https://ffmpeg.org/documentation.html
* FFmpeg filters文档：https://ffmpeg.org/ffmpeg-filters.html


