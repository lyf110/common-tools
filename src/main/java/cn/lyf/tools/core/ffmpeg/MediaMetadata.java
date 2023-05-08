package cn.lyf.tools.core.ffmpeg;

import cn.lyf.tools.io.file.FileUtil;
import it.sauronsoftware.jave.AudioInfo;
import it.sauronsoftware.jave.VideoInfo;
import it.sauronsoftware.jave.*;
import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * @author lyf
 * @description 媒体资源的元数据信息，主要是音频和视频
 * @since 2023/5/4 18:47:36
 */
@Data
public class MediaMetadata implements Serializable {
    private static final double DIVIDE_NUM = 1024.0d;
    private static final long serialVersionUID = -4452322495347595513L;
    private String format = null;
    private long duration = -1L;
    private AudioInfo audio = null;
    private VideoInfo video = null;
    /**
     * 文件大小，单位字节
     */
    private long fileLength;

    /**
     * 文件大小，单位gb
     */
    private double fileGbSize;

    /**
     * 文件大小，单位mb
     */
    private double fileMbSize;

    /**
     * 文件大小，单位kb
     */
    private double fileKbSize;

    /**
     * 文件名，不带后缀
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String fileType;

    public MediaMetadata(String videoPath) throws EncoderException {
        File file = new File(videoPath);
        Encoder encoder = new Encoder();
        MultimediaInfo multimediaInfo = encoder.getInfo(file);
        this.format = multimediaInfo.getFormat();
        this.duration = multimediaInfo.getDuration();
        this.audio = multimediaInfo.getAudio();
        this.video = multimediaInfo.getVideo();
        this.fileLength = file.length();
        this.fileName = FileUtil.getFileName(file);
        this.fileType = FileUtil.getFileType(file);


        double fileLen = fileLength * 1.0d;

        this.fileKbSize = fileLen / DIVIDE_NUM;
        this.fileMbSize = fileKbSize / DIVIDE_NUM;
        this.fileGbSize = fileMbSize / DIVIDE_NUM;
    }
}
