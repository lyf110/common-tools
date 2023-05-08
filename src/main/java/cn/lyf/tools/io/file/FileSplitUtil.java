package cn.lyf.tools.io.file;


import cn.lyf.tools.crypto.Md5Util;
import cn.lyf.tools.str.ObjectUtil;
import cn.lyf.tools.str.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * @author lyf
 * @description 文件的分片和合并工具类
 * @since 2023/5/5 11:18:58
 */
@Slf4j
public final class FileSplitUtil {
    /**
     * 最大的分片大小，5M
     */
    public static final int max_slice_size = 5 * 1024 * 1024;

    private FileSplitUtil() {
    }

    /**
     * 使用NIO完成大文件的切片工作
     *
     * @param srcFile     源文件
     * @param chunkFolder 分块所在的临时目录
     * @param sliceSize   分片大小
     */
    public static void sliceFile(File srcFile, File chunkFolder, long sliceSize) throws IOException, IllegalArgumentException {
        // 校验源文件
        if (srcFile == null || srcFile.isDirectory()) {
            throw new IllegalArgumentException("分块文件文件为null");
        }

        // 校验分块目录
        // 如果分块所在的文件夹不存在的话，那么就创建该文件夹
        if (chunkFolder == null) {
            throw new IllegalArgumentException("传入的chunkFolder为空");
        }

        if (chunkFolder.isFile()) {
            throw new IllegalArgumentException(chunkFolder.getCanonicalPath() + ", 不是目录");
        }

        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }

        // 分片的最大值为5M
        if (sliceSize > max_slice_size) {
            sliceSize = max_slice_size;
        }

        try (FileInputStream fis = new FileInputStream(srcFile);
             FileChannel inputChannel = fis.getChannel()) {

            long srcFileLength = srcFile.length();
            // 分片数
            int splitNum = (int) Math.ceil(srcFileLength * 1.0d / sliceSize);

            // 文件的开始位置
            long startPoint = 0;
            // 当前的分片大小
            long currentSliceSize;

            for (int i = 0; i < splitNum; i++) {
                File chunkFile = new File(chunkFolder, String.valueOf(i + 1));
                if (chunkFile.exists()) {
                    // 如果分片文件存在的话，那么先删除旧的分片文件
                    chunkFile.delete();
                }

                // 分块逻辑
                try (FileOutputStream fileOutputStream = new FileOutputStream(chunkFile);
                     FileChannel outputChannel = fileOutputStream.getChannel()) {
                    // 当前的分片大小
                    currentSliceSize = Math.min(sliceSize, srcFileLength - i * sliceSize);
                    // 分片的核心方法
                    inputChannel.transferTo(startPoint, currentSliceSize, outputChannel);
                    // 计算下一个分片的起始位置
                    startPoint += currentSliceSize;
                    log.debug("源文件: {}, 分片文件: {}, 已经分片完成", srcFile.getCanonicalPath(), chunkFile.getCanonicalPath());
                }
            }
        }
    }

    /**
     * 使用NIO对文件进行合并操作
     *
     * @param chunkFolder    分块所在的文件目录
     * @param srcFileMd5     源文件的md5值
     * @param chunkMergeFile 分块合并之后的文件
     */
    public static void mergeFile(File chunkFolder, File chunkMergeFile, String srcFileMd5) throws IOException, IllegalArgumentException {
        if (chunkFolder == null || chunkFolder.isFile()) {
            throw new IllegalArgumentException("分块文件目录不存在");
        }

        File[] files = chunkFolder.listFiles();
        if (ObjectUtil.isEmpty(files)) {
            throw new IllegalArgumentException(chunkFolder.getCanonicalPath() + ", 不存在分块文件");
        }

        if (StringUtil.isEmpty(srcFileMd5)) {
            throw new IllegalArgumentException("源文件的md5值不能为空");
        }

        if (chunkMergeFile == null || chunkMergeFile.isDirectory()) {
            throw new IllegalArgumentException("非法的目标文件");
        }


        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        // 对文件进行排序
        fileList.sort(Comparator.comparingInt(file -> Integer.parseInt(file.getName())));
        // 分块合并逻辑
        if (chunkMergeFile.exists()) {
            boolean delete = chunkMergeFile.delete();
            if (delete) {
                chunkMergeFile.createNewFile();
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(chunkMergeFile, true);
             FileChannel outFileChannel = fileOutputStream.getChannel()
        ) {
            for (File file : fileList) {
                try (FileChannel chunkFileChannel = new FileInputStream(file).getChannel()) {
                    outFileChannel.transferFrom(chunkFileChannel, outFileChannel.size(), chunkFileChannel.size());
                }
            }

            // 合并完成之后需要校验文件的md5
            String chunkMergeFileMd5 = Md5Util.fileMd5(new FileInputStream(chunkMergeFile));
            log.info("合并后的文件: {}, 源文件的md5: {}, 目标文件的md5: {}", chunkMergeFile.getCanonicalPath(), srcFileMd5, chunkMergeFileMd5);
            if (Objects.equals(srcFileMd5, chunkMergeFileMd5)) {
                log.info("分块文件夹: {},目标文件: {}, 已经合并完成", chunkFolder.getCanonicalPath(), chunkMergeFile.getCanonicalPath());
                // 然后再删除分块文件夹
                FileUtils.deleteDirectory(chunkFolder);
            } else {
                log.info("源文件的md5: {}, 分片合并之后的文件md5值: {}", srcFileMd5, chunkMergeFileMd5);
            }
        }
    }
}
