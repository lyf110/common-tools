package cn.lyf.tools.ffmpeg;

import cn.lyf.tools.str.text.TxtUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.lyf.tools.io.file.FileUtil.getFileType;

/**
 * 视频处理的工具类
 *
 * @author lyf
 * @since 2023-05-04
 */
@Slf4j
public final class VideoUtil {
    private static final String VIDEO_TYPE_TXT_PATH = "video/video.type.txt";

    /**
     * 根据文件后缀判断文件是否为视频
     *
     * @param filePath filePath
     * @return 判断文件是否为视频
     */
    public static boolean isVideo(String filePath) {
        try {
            File file = new File(".", VIDEO_TYPE_TXT_PATH);
            List<String> list;
            if (file.exists() && file.isFile()) {
                list = TxtUtil.readTxtFileAndDistinct(file.getCanonicalPath());
            } else {
                list = TxtUtil.readTxtFileAndDistinct(VIDEO_TYPE_TXT_PATH);
            }

            String fileType = getFileType(filePath);
            for (String videoType : list) {
                if (videoType.equalsIgnoreCase(fileType)) {
                    return true;
                }
            }
        } catch (IOException e) {
            log.error("IOException", e);
        }
        return false;
    }

    /**
     * 根据文件后缀判断文件是否为视频
     *
     * @param file file
     * @return 判断文件是否为视频
     */
    public static boolean isVideo(File file) {
        if (file == null) {
            return false;
        }
        String filePath = null;
        try {
            filePath = file.getCanonicalPath();
        } catch (IOException e) {
            log.error("", e);
            return false;
        }
        return isVideo(filePath);
    }
}
