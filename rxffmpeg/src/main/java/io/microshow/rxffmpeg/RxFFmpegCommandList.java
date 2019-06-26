package io.microshow.rxffmpeg;

import java.util.ArrayList;

/**
 * 指令集合
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
    public void clearCommands() {
        this.clear();
    }

    public RxFFmpegCommandList append(String s) {
        this.add(s);
        return this;
    }

    public String[] build() {
        String[] command = new String[this.size()];
        for (int i = 0; i < this.size(); i++) {
            command[i] = this.get(i);
        }
        return command;
    }

}
