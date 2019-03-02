
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


* 视频转图片

```java

ffmpeg -y -i /storage/emulated/0/1/input.mp4 -preset superfast /storage/emulated/0/1/output_frames_%05d.jpg

```




# 音频处理

* 音频拼接

```java

ffmpeg -y -i "concat:123.mp3|124.mp3" -acodec copy output.mp3

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



# 其它


