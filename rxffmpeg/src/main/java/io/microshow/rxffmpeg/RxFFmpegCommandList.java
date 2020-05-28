package io.microshow.rxffmpeg;

import android.util.Log;

import java.util.ArrayList;

/**
 * 指令集合,
 * 默认会加上 ffmpeg -y，如果想去除默认的指令可以调用clearCommands()清除
 * <p>
 * Created by Super on 2019/4/5.
 */
public class RxFFmpegCommandList extends ArrayList<String> {

    public RxFFmpegCommandList() {
        super();
        this.add("ffmpeg");
        this.add("-y");
    }

    /**
     * 清除命令集合
     */
    public RxFFmpegCommandList clearCommands() {
        this.clear();
        return this;
    }

    /**
     * 追加命令
     *
     * @param s cmd
     * @return RxFFmpegCommandList
     */
    public RxFFmpegCommandList append(String s) {
        this.add(s);
        return this;
    }

    /**
     * 构建命令
     *
     * @return -
     */
    public String[] build() {
        String[] command = new String[this.size()];
        for (int i = 0; i < this.size(); i++) {
            command[i] = this.get(i);
        }
        return command;
    }

    /**
     * 构建命令
     *
     * @param isLog true:构建命令后 Log打印命令日志;  false :不打印命令日志
     * @return -
     */
    public String[] build(boolean isLog) {
        String[] cmds = build();
        if (isLog) {//需要打印构建后的命令
            StringBuilder cmdLogStr = new StringBuilder();
            for (int i = 0; i < cmds.length; i++) {
                cmdLogStr.append(cmds[i]);
                if (i < cmds.length - 1) {
                    cmdLogStr.append(" ");
                }
            }
            Log.e("TAG_FFMPEG", "cmd: " + cmdLogStr.toString());
        }
        return cmds;
    }

}
