package cn.lyf.tools.ffmpeg;

import cn.lyf.tools.core.ffmpeg.FileSizeEnum;
import cn.lyf.tools.core.ffmpeg.MediaMetadata;
import cn.lyf.tools.core.ffmpeg.VideoSplitParam;
import cn.lyf.tools.str.StringUtil;
import cn.lyf.tools.system.CmdUtil;
import cn.lyf.tools.time.SimpleDateTimeUtil;
import it.sauronsoftware.jave.EncoderException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.lyf.tools.core.constant.CommonConstant.FFMPEG_PATH;

/**
 * Ffmpeg工具类
 *
 * @author lyf
 * @since 2023-05-04
 */
@Slf4j
public final class FfmpegUtil {

    /**
     * 对超过baseGB的视频进行切割
     *
     * @param srcPath   源文件
     * @param outputDir 输出文件夹
     * @param splitSize 切割的大小, 单位GB
     */
    public static void spiltVideoWithSize(String srcPath, String outputDir, long splitSize, FileSizeEnum fileSizeEnum) throws EncoderException {
        MediaMetadata mediaMetadata = new MediaMetadata(srcPath);

        // 获取视频时长
        long duration = mediaMetadata.getDuration();
        // 获取视频大小
        double fileSize;

        // 视频需要分配的个数
        int num;

        switch (fileSizeEnum) {
            case GB_SIZE:
                // 获取视频大小
                fileSize = mediaMetadata.getFileGbSize();

                // 视频需要分配的个数
                num = (int) Math.ceil(fileSize / splitSize);
                break;
            case MB_SIZE:
                // 获取视频大小
                fileSize = mediaMetadata.getFileMbSize();

                // 视频需要分配的个数
                num = (int) Math.ceil(fileSize / splitSize);
                break;

            default:
                // 获取视频大小
                fileSize = mediaMetadata.getFileKbSize();
                // 视频需要分配的个数
                num = (int) Math.ceil(fileSize / splitSize);
                break;
        }

        if (num == 1) {
            log.info("视频: {}, 视频元信息: {}", srcPath, mediaMetadata);
            return;
        }

        // 计算出分割时间
        long splitDuration = duration / num;


        String destVideoPath;
        long startTime = 0;
        for (int i = 0; i < num; i++) {
            startTime = i * splitDuration;
            destVideoPath = outputDir + File.separator + mediaMetadata.getFileName() + "-" + i + "." + mediaMetadata.getFileType();
            videoSplit(VideoSplitParam.create()
                    .srcPath(srcPath)
                    .destPath(destVideoPath)
                    .startTime(startTime)
                    .splitTime(splitDuration)
                    .videoTime(duration)
                    .unit(TimeUnit.MILLISECONDS)
                    .build());
        }
    }


    /**
     * 视频切割的方法
     *
     * @param param 视频切割的参数
     */
    public static void videoSplit(VideoSplitParam param
    ) {

        // 先获取视频的元数据信息
        String srcVideoPath = param.getSrcPath();
        if (StringUtil.isEmpty(srcVideoPath)) {
            throw new IllegalArgumentException("srcVideoPath not null");
        }
        String destPath = param.getDestPath();

        destPath = StringUtil.isEmpty(destPath) ? null : destPath;


        Long duration = param.getVideoTime();
        if (duration == null || duration <= 0L) {
            log.error("视频: {}, 的时间长度为0", srcVideoPath);
            return;
        }

        // 将视频时长转成毫秒时间的时长
        TimeUnit unit = param.getUnit();
        long videoMillisSeconds = unit.toMillis(duration);

        // 校验开始时间和结束时间
        long startTime = unit.toMillis(param.getStartTime());

        if (startTime >= videoMillisSeconds) {
            log.info("视频: {}, 视频时长为: {}秒, 开始时间为: {}秒, 无需切割", srcVideoPath, videoMillisSeconds, startTime);
            return;
        }

        Long splitTime = param.getSplitTime();
        if (splitTime != null && splitTime > 0L) {
            splitTime = unit.toMillis(splitTime);
            if ((startTime + splitTime) > videoMillisSeconds) {
                splitTime = null;
            }
        }

        List<String> commandList = new ArrayList<>();
        // 从00:00:45秒开始切割
        // ffmpeg -y -ss 00:00:45.232 -i "input.mp4" -c:v copy -c:a copy "output.mp4"
        // 从视频中截取00:00:00 到 02:00:00时间的视频
        // ffmpeg -y -ss 00:00:00 -to 02:00:00 -i "input.mp4" -c:v copy -c:a copy "output.mp4"
        commandList.add(FFMPEG_PATH);
        commandList.add("-y");
        commandList.add("-ss");


        String startTimeStr = SimpleDateTimeUtil.formatForVideoTime(startTime);
        if (splitTime != null && splitTime > 0) {
            commandList.add(startTimeStr);
            commandList.add("-to");
            String endTimeStr = SimpleDateTimeUtil.formatForVideoTime(startTime + splitTime);
            commandList.add(endTimeStr);
        } else {
            commandList.add(startTimeStr);
        }

        commandList.add("-i");
        commandList.add(srcVideoPath);
        commandList.add("-c:v");
        commandList.add("copy");
        commandList.add("-c:a");
        commandList.add("copy");
        commandList.add(destPath);

        String result = CmdUtil.invokeCommand(commandList);
        log.debug("result: \n{}\n", result);
        log.info("源文件: {}, 目标文件: {} 切割完成", srcVideoPath, destPath);
    }


    /**
     * 将字幕集成到视频中（挂载字幕，非硬压）
     *
     * @param srcVideoPath  源视频路径
     * @param destVideoPath 目标视频路径
     * @param subtitlesPath 字幕文件路径
     */
    public static void integrateSubtitlesIntoTheVideo(String srcVideoPath, String destVideoPath, String subtitlesPath) {
        List<String> commandList = new ArrayList<>();
        // ffmpeg -y -i "input.mp4" -i input.srt -c copy -c:s mov_text "output.mp4"
        commandList.add(FFMPEG_PATH);
        commandList.add("-y");
        commandList.add("-i");
        commandList.add(srcVideoPath);
        commandList.add("-i");
        commandList.add(subtitlesPath);
        commandList.add("-c");
        commandList.add("copy");
        commandList.add("-c:s");
        commandList.add("mov_text");
        commandList.add(destVideoPath);

        String result = CmdUtil.invokeCommand(commandList);
        log.debug("result:\n{}\n", result);
    }

    /**
     * 将源目标格式的字幕转换为目标格式的字幕
     *
     * @param srcSubPath  源目标格式路径
     * @param destSubPath 目标格式路径
     */
    public static void subtitleConversion(String srcSubPath, String destSubPath) {
        List<String> commandList = new ArrayList<>();
        // ffmpeg -i a.ass b.srt
        commandList.add(FFMPEG_PATH);
        commandList.add("-i");
        commandList.add(srcSubPath);
        commandList.add(destSubPath);

        String result = CmdUtil.invokeCommand(commandList);
        log.debug("result:\n{}\n", result);
    }
}
