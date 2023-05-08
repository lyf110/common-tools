package cn.lyf.tools.core.ffmpeg;

import cn.lyf.tools.core.builder.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 19:27:48
 */
@Data
public class VideoSplitParam implements Serializable {
    private static final long serialVersionUID = 7436788359394147091L;
    private String srcPath;
    private String destPath;
    private Long startTime;
    private Long splitTime;
    private Long videoTime;
    private TimeUnit unit;

    private VideoSplitParam(String srcPath, String destPath, Long startTime, Long splitTime, Long videoTime, TimeUnit unit) {
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.startTime = startTime;
        this.splitTime = splitTime;
        this.videoTime = videoTime;
        this.unit = unit;
    }

    /**
     * 创建构建器对象
     *
     * @return VideoSplitParamBuilder
     */
    public static VideoSplitParamBuilder create() {
        return new VideoSplitParamBuilder();
    }

    public static class VideoSplitParamBuilder implements Builder<VideoSplitParam> {
        private static final long serialVersionUID = 7386273336462409710L;
        private String srcPath;
        private String destPath;
        private Long startTime = 0L;
        private Long splitTime;
        private Long videoTime;
        private TimeUnit unit = TimeUnit.MILLISECONDS;

        VideoSplitParamBuilder() {
        }

        public VideoSplitParam.VideoSplitParamBuilder srcPath(String srcPath) {
            this.srcPath = srcPath;
            return this;
        }

        public VideoSplitParam.VideoSplitParamBuilder destPath(String destPath) {
            this.destPath = destPath;
            return this;
        }

        public VideoSplitParam.VideoSplitParamBuilder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        public VideoSplitParam.VideoSplitParamBuilder splitTime(Long splitTime) {
            this.splitTime = splitTime;
            return this;
        }

        public VideoSplitParam.VideoSplitParamBuilder videoTime(Long videoTime) {
            this.videoTime = videoTime;
            return this;
        }

        public VideoSplitParam.VideoSplitParamBuilder unit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        @Override
        public VideoSplitParam build() {
            return new VideoSplitParam(this.srcPath, this.destPath, this.startTime, this.splitTime, this.videoTime, this.unit);
        }


        public String toString() {
            return "VideoSplitParam.VideoSplitParamBuilder(srcPath=" + this.srcPath + ", destPath=" + this.destPath + ", startTime=" + this.startTime + ", splitTime=" + this.splitTime + ", videoTime=" + this.videoTime + ", unit=" + this.unit + ")";
        }
    }
}
