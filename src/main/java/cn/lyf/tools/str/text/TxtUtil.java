package cn.lyf.tools.str.text;

import cn.lyf.tools.io.file.FileUtil;
import cn.lyf.tools.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyf
 * @description 文本操作的工具类
 * @since 2023/5/2 15:49:59
 */
public final class TxtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TxtUtil.class);


    /**
     * 读取Txt文本
     *
     * @param filePath 文件路径
     * @return list
     */
    public static List<String> readTxtFile(String filePath) {
        if (FileUtil.isAbsolutePath(filePath)) {
            try {
                InputStream inputStream = new FileInputStream(new File(filePath));
                return readTxtFile(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return readTxtFile(IoUtil.getInputStream(filePath));
    }

    /**
     * 读取Txt文本
     *
     * @param inputStream 文件输入流
     * @return list
     */
    public static List<String> readTxtFile(InputStream inputStream) {
        List<String> list = new ArrayList<>();
        if (inputStream == null) {
            LOGGER.error("inputStream not be null");
            return list;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String urlStr;
            while ((urlStr = br.readLine()) != null) {
                list.add(urlStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeStream(inputStream);
            IoUtil.closeStream(br);
        }
        return list;
    }

    /**
     * 读取txt文件，并使用java1.8新特性去重
     *
     * @param filePath 相对路径
     * @return Set
     */
    public static List<String> readTxtFileAndDistinct(String filePath) {
        if (FileUtil.isAbsolutePath(filePath)) {
            try {
                InputStream inputStream = new FileInputStream(new File(filePath));
                return readTxtFile(inputStream).stream().distinct().collect(Collectors.toList());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        List<String> list = readTxtFile(filePath);
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 读取txt文件，并去重
     *
     * @param inputStream inputStream
     * @return Set
     */
    public static List<String> readTxtFileAndDistinct(InputStream inputStream) {
        List<String> list = readTxtFile(inputStream);
        return list.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 获取两个文本之间不同的值
     *
     * @param basePath  basePath
     * @param otherPath otherPath
     * @return list
     */
    public static List<String> getDiffFromTwoTxt(String basePath, String otherPath) {
        List<String> baseList = readTxtFile(basePath);
        List<String> otherList = readTxtFile(otherPath);
        List<String> list = new ArrayList<>();

        for (String s : baseList) {
            if (!otherList.contains(s)) {
                list.add(s);
            }
        }

        return list;
    }
}
