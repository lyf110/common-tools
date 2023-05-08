package cn.lyf.tools.io.file;

import cn.lyf.tools.crypto.Md5Util;
import cn.lyf.tools.method.MethodUtil;
import cn.lyf.tools.str.StringUtil;
import cn.lyf.tools.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static cn.lyf.tools.core.constant.CommonConstant.*;

/**
 * 文件处理工具类
 *
 * @author liu yang fang
 * @since 2023-04-24 10:04
 **/
@Slf4j
public final class FileUtil {
    /**
     * 判断路径是否是绝对路径
     *
     * @param path 路径
     * @return 绝对路径
     */
    public static boolean isAbsolutePath(String path) {
        File file = new File(path);
        return file.isAbsolute();
    }

    /**
     * 判断地址是否是网络资源路径
     *
     * @param path path
     * @return 是否是网络资源路径
     */
    public static boolean isUrl(String path) {
        if (StringUtil.isEmpty(path)) {
            throw new IllegalArgumentException("path not be null");
        }
        String upPath = StringUtil.toUpperCase(path);
        return upPath.startsWith(HTTP) || upPath.startsWith(HTTPS);
    }


    /**
     * 获取文件的扩展类型
     *
     * @param file 文件
     * @return 扩展类型，可能为null，调用时需处理
     */
    public static String getFileType(File file) {
        if (file == null) {
            return null;
        }
        try {
            String canonicalPath = file.getCanonicalPath();
            int index = canonicalPath.lastIndexOf(".");

            if (index > 0 && index < canonicalPath.length() - 1) {
                return canonicalPath.substring(index + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件的扩展类型
     *
     * @param path 输入的文件名
     * @return 扩展类型，可能为null，调用时需处理
     */
    public static String getFileType(String path) {
        if (StringUtil.isNotEmpty(path)) {
            path = StringUtil.trim(path);
            String canonicalPath = path;
            if (StringUtil.isNotContains(path, HTTP) && StringUtil.isNotContains(path, HTTPS)) {
                File file = new File(path);
                return getFileType(file);
            }

        }
        log.info("fileType is null");
        return null;
    }

    /**
     * 获取文件名 不包含后缀名
     *
     * @param file 文件
     * @return 不包含后缀名的文件名称
     */
    public static String getFileName(File file) {
        try {
            String fileName = file.getCanonicalFile().getName();

            // 获取最后一个/
            int index = fileName.lastIndexOf(".");
            if (index > 0 && index < fileName.length() - 1) {
                return fileName.substring(0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件名 不包含后缀名
     *
     * @param path 路径
     * @return 不包含后缀名的文件名称
     */
    public static String getFileName(String path) {
        String trimPath = StringUtil.trim(path);
        if (StringUtil.isNotEmpty(trimPath)) {
            if (!FileUtil.isUrl(trimPath)) {
                File file = new File(path);
                return getFileName(file);
            }
        }
        return null;
    }

    public static String getFileNameMeet115Regular(String path) throws IOException {
        String fileType = FileUtil.getFileType(path);
        String fileName = FileUtil.getFileName(path);
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(fileName) && StringUtil.isNotEmpty(fileType)) {
            char[] charArray = fileName.toCharArray();

            for (char arr : charArray) {
                sb.append(arr).append(SPLIT_STR);
            }
            sb.append(".").append(fileType);
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        String trimStr = StringUtil.trim(str);
        return StringUtil.isEmpty(trimStr);
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldName 原来的文件名
     * @param newName 新文件名
     */
    public static void renameFile(String path, String oldName, String newName) {
        // 新的文件名和以前文件名不同时,才有必要进行重命名
        if (!oldName.equals(newName)) {
            File oldFile = new File(path, oldName);
            File newFile = new File(path, newName);

            // 重命名文件不存在
            if (!oldFile.exists()) {
                return;
            }

            // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
            if (newFile.exists()) {
                log.info("{} 已经存在！", newName);
            } else {
                boolean isRenameSuccess = oldFile.renameTo(newFile);
                log.info("{} isRenameSuccess: {}", oldFile.getName(), isRenameSuccess);
            }
        } else {
            log.info("新文件名和旧文件名相同...");
        }
    }


    /**
     * 获取项目的路径
     *
     * @return 项目绝对路径
     */
    private static String getProjectPath() {
        try {
            URL resource = FileUtil.class.getResource("/");
            if (resource == null) {
                throw new FileNotFoundException("\"/\" 目录不存在");
            }
            String path = resource.getPath();
            return new File(path).getCanonicalPath().replace(TARGET_CLASSES, "");
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Resource目录路径
     *
     * @return Resource目录路径
     */
    public static String getResourcesPath() {
        return getProjectPath() + SRC_MAIN_RESOURCES;
    }

    /**
     * 获取Resource目录下指定文件的路径
     *
     * @return Resource文件夹下面文件路径
     */
    public static String getResourcesPath(String fileName) {
        return getProjectPath() + SRC_MAIN_RESOURCES + fileName;
    }


    /**
     * 文件分片方法
     *
     * @param file       文件对象
     * @param sliceIndex 第几片，从1开始
     * @param sliceSize  每片大小（byte）
     * @return 字节数组
     */
    public static byte[] sliceFile(File file, int sliceIndex, int sliceSize) {
        try (RandomAccessFile readAccessFile = new RandomAccessFile(file, "r")) {
            // 创建读模式的随机访问文件模式

            // 获取文件的大小
            long fileLength = file.length();
            // 分片数
            int numSlices = (int) Math.ceil(fileLength * 1.0d / sliceSize);
            //限制参数越界
            if (sliceIndex <= 0 || sliceIndex > numSlices) {
                return null;
            }

            // 获取分片大小
            int sliceLength = (int) Math.min(sliceSize, fileLength - (long) (sliceIndex - 1) * sliceSize);
            byte[] data = new byte[sliceLength];
            readAccessFile.seek((long) (sliceIndex - 1) * sliceSize);
            readAccessFile.readFully(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String videoPath = "C:/DISH/tmp/video/input.mp4";
        File file = new File(videoPath);
        long fileLength = file.length();
        int sliceSize = 5 * 1024 * 1024; // 5M
        String outPutDir = "C:/DISH/tmp/video/temp";
//        MethodUtil.costTime(() -> {
//            sliceFile(videoPath, sliceSize, outPutDir);
//        });
        //mergeFile(outPutDir,  "C:/DISH/tmp/video/myoutput.mp4");
        MethodUtil.costTime(() -> {
            // chunk(file, "C:/DISH/tmp/video/temp1", sliceSize);
            // chunk();
           // mergeFile("C:/DISH/tmp/video/temp", "C:/DISH/tmp/video/input-" + TimeUtil.getCurrentTimeMillis() + ".mp4");
        });

    }




}
