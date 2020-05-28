package io.microshow.rxffmpeg;

import android.text.TextUtils;

/**
 * RxFFmpeg 命令支持
 * Created by Super on 2019/4/5.
 */
public class RxFFmpegCommandSupport {

    /**
     * 给视频加毛玻璃效果
     *
     * @param inputPath  输入视频文件
     * @param outputPath 输出视频文件
     * @param boxblur    blur效果调节，默认 "5:1"
     * @param isLog      true: 构建命令后 Log打印命令日志;  false :不打印命令日志
     * @return cmds
     */
    public static String[] getBoxblur(String inputPath, String outputPath, String boxblur, boolean isLog) {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append(inputPath);
        cmdlist.append("-vf");
        cmdlist.append("boxblur=" + (TextUtils.isEmpty(boxblur) ? "5:1" : boxblur));
        cmdlist.append("-preset");
        cmdlist.append("superfast");
        cmdlist.append(outputPath);
        return cmdlist.build(isLog);
    }


}
