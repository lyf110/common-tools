package cn.lyf.tools.io;

import cn.lyf.tools.core.constant.CommonConstant;
import cn.lyf.tools.io.file.FileTypeUtil;
import cn.lyf.tools.io.file.FileUtil;
import cn.lyf.tools.io.http.HttpClientUtil;
import cn.lyf.tools.str.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author lyf
 * @description 操作IO、流的工具类
 * @since 2023/5/2 15:49:59
 */
@Slf4j
public final class IoUtil {
    private static final int BYTE_BUFFER_LENGTH = 8 * 1024;

    /**
     * 获取文件输入流
     *
     * @param path 相对路径，一般相对于resource或者java目录
     * @return InputStream
     */
    public static InputStream getInputStream(String path) {
        boolean isAbsolutePath = FileUtil.isAbsolutePath(path);
        InputStream inputStream = null;
        if (isAbsolutePath) {
            try {
                inputStream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                log.error("", e);
            }
        } else {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        }
        return inputStream;
    }

    /**
     * 将文件输入流转成字节数组
     *
     * @param inputStream 文件输入流
     * @return 字节数组
     * @throws IOException IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            byte[] byteBuffer = new byte[BYTE_BUFFER_LENGTH];
            int length = -1;
            while ((length = bufferedInputStream.read(byteBuffer)) != -1) {
                byteArrayOutputStream.write(byteBuffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 从网络资源上获取文件输入流
     *
     * @param urlPath 资源路径，一般为http或者https
     * @return InputStream
     */
    public static InputStream getInputStreamFromUrl(String urlPath) {
        if (StringUtil.isEmpty(urlPath)) {
            log.info("urlPath is not be null");
            return null;
        }

        // 如果资源类型为https
        InputStream inputStream = null;
        try {
            if (urlPath.toUpperCase(Locale.ENGLISH).startsWith(CommonConstant.HTTPS)) {
                inputStream = getInputStreamFromHttpsUrl(urlPath);
            } else {
                // 资源类型为http
                inputStream = getInputStreamFromHttpUrl(urlPath);
            }
        } catch (IOException e) {
            log.info("IOException:", e);
        }
        return inputStream;
    }

    private static InputStream getInputStreamFromHttpUrl(String urlPath) throws IOException {
        HttpURLConnection httpUrlConnection = HttpClientUtil.getHttpUrlConnection(urlPath);
        if (httpUrlConnection == null) {
            log.info("httpUrlConnection is null");
            return null;
        }

        return httpUrlConnection.getInputStream();
    }

    private static InputStream getInputStreamFromHttpsUrl(String urlPath) throws IOException {
        HttpsURLConnection httpsUrlConnection = HttpClientUtil.getHttpsUrlConnection(urlPath);
        if (httpsUrlConnection == null) {
            log.info("httpsUrlConnection is null");
            return null;
        }

        return httpsUrlConnection.getInputStream();
    }

    /**
     * 释放资源
     *
     * @param stream stream
     */
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("IOException:", e);
            }
        }
    }

    /**
     * 获取输出流
     *
     * @param path 相对于项目的resource路径
     * @return OutputStream
     */
    public static OutputStream getOutputStream(String path) throws IOException {
        if (StringUtil.isEmpty(path)) {
            return null;
        }

        // File.pathSeparator指的是分隔连续多个路径字符串的分隔符，例如: java   -cp   test.jar;abc.jar   HelloWorld 就是指“;”
        // File.separator才是用来分隔同一个路径字符串中的目录的，例如： C:/Program Files/Common Files 就是指“/”
        File file = new File(path);
        OutputStream outputStream = null;
        file = file.getCanonicalFile();
        outputStream = new FileOutputStream(file);
        return outputStream;
    }

    /**
     * getPrintStream
     *
     * @param path 相对于resource路径
     * @return PrintStream
     */
    public static PrintStream getPrintStream(String path) {
        OutputStream outputStream;
        PrintStream printStream = null;
        try {
            outputStream = getOutputStream(path);
            if (outputStream == null) {
                log.error("outputStream not null");
                throw new IllegalArgumentException("outputStream not null");
            }
            printStream = new PrintStream(outputStream);
        } catch (IOException e) {
            log.error("IOException", e);
        }
        return printStream;
    }

    public static void copyFile(File srcFile, File destFile) {
        if (srcFile == null || !srcFile.exists() || !srcFile.isFile()) {
            log.debug("srcFile not exists");
            return;
        }

        if (destFile == null) {
            log.debug("destFile not exists");
            return;
        }

        try (
                FileChannel srcFileChannel = new FileInputStream(srcFile).getChannel();
                FileChannel destFileChannel = new FileOutputStream(destFile).getChannel()
        ) {
            srcFileChannel.transferTo(0, srcFileChannel.size(), destFileChannel);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream inputStream
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] readInputStream(InputStream inputStream)
            throws IOException {
        byte[] b = new byte[1024 * 8];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 自动下载资源
     *
     * @param urlPath       url地址
     * @param saveDirectory 保存资源的目录
     */
    public static String autoDownload(String urlPath, String saveDirectory) {
        return autoDownload(urlPath, saveDirectory, null);
    }

    /**
     * 自动下载资源
     *
     * @param urlPath       url地址
     * @param saveDirectory 保存资源的目录
     */
    public static String autoDownload(String urlPath, String saveDirectory, String saveName) {
        InputStream inputStream = null;
        String fileName = null;
        try {
            inputStream = getInputStreamFromUrl(urlPath);
            if (inputStream == null) {
                log.info("inputStream is null");
                return null;
            }
            byte[] bytes = readInputStream(inputStream);
            String fileType = FileTypeUtil.getRealFileType(bytes);
            fileType = StringUtil.isEmpty(fileType) ? "unknown" : fileType;
            String fileSaveName = saveName;
            if (StringUtil.isEmpty(fileSaveName)) {
                fileSaveName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss SSS"));
            }
            fileName = saveDirectory + File.separator
                    + fileSaveName + "." + fileType;
            download(bytes, fileName);
        } catch (IOException e) {
            log.error("autoDownload failed", e);
            return null;
        } finally {
            IoUtil.closeStream(inputStream);
        }
        return fileName;
    }

    /**
     * 下载资源
     *
     * @param bytes        资源字节数组
     * @param destFilePath 目标文件路径
     */
    public static void download(byte[] bytes, String destFilePath) {
        File file = new File(destFilePath);
        download(bytes, file);
    }

    public static void downloadByNio(InputStream inputStream, File destFile) {
        try (FileChannel outChannel = new FileOutputStream(destFile).getChannel();
             ReadableByteChannel inChannel = Channels.newChannel(inputStream);
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) != -1) {
                // 将buffer 从读模式转为写模式
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
    }

    /**
     * 下载资源
     *
     * @param bytes    资源字节数组
     * @param destFile 目标文件
     */
    public static void download(byte[] bytes, File destFile) {
        if (bytes == null || bytes.length == 0) {
            log.info("download failed, bytes is null");
            return;
        }

        if (destFile == null) {
            log.info("destFile is null");
            return;
        }

        if (destFile.exists()) {
            log.info("{} is exists, not need download", destFile.getName());
            return;
        }
        try (OutputStream outputStream = new FileOutputStream(destFile);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {
            bos.write(bytes);
            log.info("{} download is success", destFile.getName());
        } catch (IOException e) {
            log.info("IOException:{}", e.getMessage());
        }
    }

    /**
     * 下载资源
     *
     * @param inputStream 文件输入流
     * @param destFile    目标文件
     */
    public static void download(InputStream inputStream, File destFile) {
        try {
            if (inputStream == null) {
                log.info("download failed, inputStream is null");
                return;
            }
            byte[] bytes = readInputStream(inputStream);
            download(bytes, destFile);
        } catch (IOException e) {
            log.info("IOException:{}", e.getMessage());
        } finally {
            closeStream(inputStream);
        }
    }

    /**
     * 下载资源
     *
     * @param inputStream  文件输入流
     * @param destFilePath 目标文件路径
     */
    public static void download(InputStream inputStream, String destFilePath) {
        File file = new File(destFilePath);
        download(inputStream, file);
    }

    public static void download(String url, String destFilePath) {
        if (url.toUpperCase(Locale.ENGLISH).contains(CommonConstant.HTTPS)) {
            downloadFromHttps(url, destFilePath);
        } else {
            downloadFromHttp(url, destFilePath);
        }
    }

    private static void downloadFromHttp(String urlStr, String destFilePath) {
        HttpURLConnection conn = HttpClientUtil.getHttpUrlConnection(urlStr);
        if (conn == null) {
            log.info("downloadFromHttp: {} is failed, the HttpURLConnection is null", urlStr);
            return;
        }

        // 得到输入流
        try (InputStream inputStream = conn.getInputStream()) {
            download(inputStream, destFilePath);
        } catch (IOException e) {
            log.error("downloadFromHttp: {} is failed, {}", urlStr, e.getMessage());
        }
    }

    private static void downloadFromHttps(String url, String destFilePath) {
        InputStream inputStream = null;
        try {
            HttpsURLConnection connection = HttpClientUtil.getHttpsUrlConnection(url);
            if (connection != null) {
                inputStream = connection.getInputStream();
                download(inputStream, destFilePath);
            }
        } catch (IOException e) {
            log.error("download From Https: {} is failed", url);
        } finally {
            closeStream(inputStream);
        }
    }

    /**
     * 将字符串写入本地文件
     *
     * @param path         相对于Resource路径
     * @param writeContent 写入到本地文件的字符串
     */
    public static void writeToFile(String path, String writeContent) {
        if (StringUtil.isEmpty(path)) {
            log.info("file path not null");
            return;
        }

        String fileName;
        boolean isAbsolutePath = FileUtil.isAbsolutePath(path);
        log.info("{} isAbsolutePath {}", path, isAbsolutePath);
        if (isAbsolutePath) {
            fileName = path;
        } else {
            // 就是相对路径
            String resourcesPath = FileUtil.getResourcesPath();
            if (StringUtil.isEmpty(resourcesPath)) {
                throw new IllegalArgumentException("resourcesPath not null");
            }
            fileName = resourcesPath + File.separator + path;
        }

        writeToFile(new File(fileName), writeContent);
    }

    /**
     * 将字符串写入本地文件
     *
     * @param destFile     目标文件
     * @param writeContent 写入到本地文件的字符串
     */
    public static void writeToFile(File destFile, String writeContent) {
        if (destFile == null) {
            log.info("file path not null");
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(destFile);
             OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(streamWriter)) {
            bw.write(new String(writeContent.getBytes(StandardCharsets.UTF_8)));
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
