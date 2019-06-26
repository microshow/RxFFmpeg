package io.microshow.rxffmpeg;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;

/**
 * 音视频工具类
 * Created by Super on 2019/4/5.
 */
public class AudioVideoUtils {

    /**
     * 根据视频的宽X高计算出一个合适的码率
     *
     * @param wxh 宽X高的值
     * @return
     */
    public static int getFitBitRate(int wxh) {
        if (wxh <= 480 * 480) {
            return 1000 * 1024;
        } else if (wxh <= 640 * 480) {
            return 1500 * 1024;
        } else if (wxh <= 800 * 480) {
            return 1800 * 1024;
        } else if (wxh <= 960 * 544) {
            return 2 * 1024 * 1024;
        } else if (wxh <= 1280 * 720) {
            return (int) (2.5 * 1024 * 1024);
        } else if (wxh <= 1920 * 1088) {
            return 3 * 1024 * 1024;
        } else {
            return 3500 * 1024;
        }
    }

    /**
     * 查找视频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectVideoTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
//                Log.d(TAG, "Extractor selected track " + i + " (" + mime + "): " + format);
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找音频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectAudioTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
//                Log.d(TAG, "Extractor selected track " + i + " (" + mime + "): " + format);
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取视频信息 时长 微秒 us
     *
     * @param url
     * @return
     */
    public static long getDuration(String url) {
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(url);
            int videoExt = selectVideoTrack(mediaExtractor);
            MediaFormat mediaFormat = mediaExtractor.getTrackFormat(videoExt);
            long res = mediaFormat.containsKey(MediaFormat.KEY_DURATION) ? mediaFormat.getLong(MediaFormat.KEY_DURATION) : 0;//时长
            mediaExtractor.release();
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取视频宽
     *
     * @param videoPath
     * @return
     */
    public static int getVideoWidth(String videoPath) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath);
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if ("90".equals(rotation) || "270".equals(rotation)) {//当视频是竖屏的时候 orientation = 90，横屏 orientation = 0 ，正确转换宽高
            width = height;
        }
        retr.release();
        return Integer.parseInt(width);
    }

    /**
     * 获取视频高
     *
     * @param videoPath
     * @return
     */
    public static int getVideoHeight(String videoPath) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath);
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if ("90".equals(rotation) || "270".equals(rotation)) {//当视频是竖屏的时候 orientation = 90，横屏 orientation = 0 ，正确转换宽高
            height = width;
        }
        retr.release();
        return Integer.parseInt(height);
    }

    /**
     * 获取视频旋转方向
     *
     * @param videoPath
     * @return
     */
    public static int getVideoRotation(String videoPath) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath);
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        retr.release();
        return Integer.parseInt(rotation);
    }

    /**
     * 获取视频时长 单位：秒
     *
     * @param videoPath
     * @return
     */
    public static int getVideoDuration(String videoPath) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath);
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 视频时长 毫秒
        retr.release();
        return Integer.parseInt(rotation) / 1000;//转为秒
    }

    /**
     * 判断是否是横屏的视频
     *
     * @param videoPath
     * @return
     */
    public static boolean isHorizontalVideo(String videoPath) {
        if (getVideoWidth(videoPath) >= getVideoHeight(videoPath)) {
            return true;
        } else {
            return false;
        }
    }

}
