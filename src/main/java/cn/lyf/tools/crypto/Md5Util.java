package cn.lyf.tools.crypto;

import cn.lyf.tools.core.entity.OssObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author lyf
 * @description: 计算文件的Md5
 * @version: v1.0
 * @since 2022-04-29 20:53
 */
@Slf4j
public final class Md5Util {
    private static final int BUFFER_SIZE = 8 * 1024;

    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    private Md5Util() {
    }


    /**
     * 计算字节数组的md5
     *
     * @param bytes bytes
     * @return 文件流的md5
     */
    public static String fileMd5(byte[] bytes) {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            return encodeHex(md5MessageDigest.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("no md5 found");
        }
    }

    /**
     * 计算文件的输入流
     *
     * @param inputStream inputStream
     * @return 文件流的md5
     */
    public static String fileMd5(InputStream inputStream) {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            try (BufferedInputStream bis = new BufferedInputStream(inputStream);
                 DigestInputStream digestInputStream = new DigestInputStream(bis, md5MessageDigest)) {

                final byte[] buffer = new byte[BUFFER_SIZE];

                while (digestInputStream.read(buffer) > 0) {
                    // 获取最终的MessageDigest
                    md5MessageDigest = digestInputStream.getMessageDigest();
                }

                return encodeHex(md5MessageDigest.digest());
            } catch (IOException ioException) {
                log.error("", ioException);
                throw new IllegalArgumentException(ioException.getMessage());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("no md5 found");
        }
    }

    /**
     * 获取字符串的MD5值
     *
     * @param input 输入的字符串
     * @return md5
     */
    public static String getMd5(String input) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数，可以换成SHA1）
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = input.getBytes(StandardCharsets.UTF_8);
            md5MessageDigest.update(inputByteArray);

            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = md5MessageDigest.digest();
            // 将字符数组转成字符串返回
            return encodeHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("md5 not found");
        }
    }


    /**
     * 将已加密的md5字节数组转成md5字符串, 生成的MD5为大写字符串
     *
     * @param binaryData binaryData
     * @return md5值
     */
    private static String encodeMD5(byte[] binaryData) {
        int len = binaryData.length;
        char[] buf = new char[len * 2];
        for (int i = 0; i < len; i++) {
            buf[i * 2] = HEX_DIGITS[(binaryData[i] >>> 4) & 0x0f];
            buf[i * 2 + 1] = HEX_DIGITS[binaryData[i] & 0x0f];
        }
        return new String(buf);
    }

    /**
     * 转成的md5值为全小写
     *
     * @param bytes bytes
     * @return 全小写的md5值
     */
    private static String encodeHex(byte[] bytes) {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return new String(chars);
    }


    /**
     * 一边计算md5，一边复制流，不支持大文件, 当文件太大了的时候会因为创建了大量的对象而导致OOM
     *
     * @return OssObject
     */
    public static OssObject copyInputStreamAndCalculateMd5V1(InputStream inputStream) {
        try {
            // md5计算器
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");

            // 缓冲区
            byte[] buffer = new byte[1024];
            int readNum;

            // 构建一个List<InputStream> 用于存储分段流
            List<InputStream> inputStreamList = new ArrayList<>();
            // 通过ByteArrayOutputStream 来实现边计算md5，边复制流
            // 计数器
            try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
                while ((readNum = bis.read(buffer)) != -1) {
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        baos.write(buffer, 0, readNum);
                        // 计算md5
                        md5MessageDigest.update(buffer, 0, readNum);
                        // 存储流

                        try (InputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                            inputStreamList.add(bais);
                        } catch (IOException e) {
                            log.error("", e);
                        }
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
            } catch (IOException e) {
                log.error("", e);
            }

            // 汇总流
            Enumeration<InputStream> enumeration = Collections.enumeration(inputStreamList);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
            // 获取md5
            byte[] digest = md5MessageDigest.digest();
            String md5 = encodeHex(digest);

            OssObject ossObject = new OssObject();
            ossObject.setObjectContent(sequenceInputStream);
            ossObject.setMd5(md5);

            return ossObject;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("no md5 found");
        }
    }


    /**
     * 一边计算md5，一边复制流，不支持大文件, 当文件太大了 加载了太多的字节数组导致OOM
     *
     * @return OssObject
     */
    public static OssObject copyInputStreamAndCalculateMd5V2(InputStream inputStream) {
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            // 获取md5
            String fileMd5 = fileMd5(bytes);

            OssObject ossObject = new OssObject();
            ossObject.setObjectContent(new ByteArrayInputStream(bytes));
            ossObject.setMd5(fileMd5);
            return ossObject;
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid inputStream: " + e.getMessage());
        }
    }
}
