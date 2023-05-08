package cn.lyf.tools.system;


import cn.hutool.core.lang.Console;
import cn.lyf.tools.core.constant.CharsetEnum;
import cn.lyf.tools.io.IoUtil;
import cn.lyf.tools.str.ObjectUtil;
import cn.lyf.tools.str.StringUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lyf
 * @description Base64的加解密工具类
 * @since 2023/5/6 12:45:05
 */
public final class Base64Util {
    private Base64Util() {
        Console.log();
    }

    /**
     * 将字符串用Base64编码编码成base64格式
     *
     * @param input 字符串
     * @return base64格式的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String encode(String input) throws UnsupportedEncodingException {
        return encode(input, CharsetEnum.UTF_8.getCharsetName());
    }

    /**
     * 将字符串用Base64编码编码成base64格式
     *
     * @param input       字符串
     * @param charsetName 字符串的字符编码
     * @return base64格式的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String encode(String input, String charsetName) throws UnsupportedEncodingException {
        return encode(input.getBytes(charsetName));
    }

    /**
     * 将字符串用Base64编码编码成base64格式
     *
     * @param byteArr 输入的字节数组
     * @return base64格式的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String encode(byte[] byteArr) throws UnsupportedEncodingException {
        if (ObjectUtil.isEmpty(byteArr)) {
            throw new UnsupportedEncodingException();
        }
        return Base64.getEncoder().encodeToString(byteArr);
    }

    /**
     * 将文件转成base64编码
     *
     * @param inputStream inputStream
     * @return 文件转成base64编码
     */
    public static String encode(InputStream inputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] bytes = IoUtil.toByteArray(bis);
            return encode(bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException("获取图片失败：" + e.getMessage());
        }
    }


    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param input   Base64编码的字符串
     * @param charset Base64编码的字符串的字符编码
     * @return Base64编码的字节数组解析后的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String decodeToString(String input, Charset charset) throws UnsupportedEncodingException {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return new String(decode(input, charset), charset);
    }

    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param input Base64编码的字符串
     * @return Base64编码的字节数组解析后的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String decodeToString(String input) throws UnsupportedEncodingException {
        return decodeToString(input, StandardCharsets.UTF_8);
    }

    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param bytes Base64编码的字节数组
     * @return Base64编码的字节数组解析后的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String decodeToString(byte[] bytes) throws UnsupportedEncodingException {
        return decodeToString(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param bytes   Base64编码的字节数组
     * @param charset 限定解析后的字符串的编码形式
     * @return Base64编码的字节数组解析后的字符串
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String decodeToString(byte[] bytes, Charset charset) throws UnsupportedEncodingException {
        // 设置默认的编码为UTF8
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return new String(decode(bytes), charset);
    }


    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param input Base64编码的字符串
     * @return Base64编码的字节数组解析后的字符串
     */
    public static byte[] decode(String input) {
        return decode(input, StandardCharsets.UTF_8);
    }

    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param input Base64编码的字符串
     * @return Base64编码的字节数组解析后的字符串
     */
    public static byte[] decode(String input, Charset charset) {
        if (StringUtil.isEmpty(input)) {
            throw new IllegalArgumentException("Base64解码的字节字符串不能为空");
        }

        if (input.contains("data:")) {
            int start = input.indexOf(",");
            input = input.substring(start + 1);
        }

        // 替换掉\r和\n
        input = input.replaceAll("\r\\|\n", "");
        input = StringUtil.trim(input);

        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        return decode(input.getBytes(charset));
    }

    /**
     * 将Base64编码的字节数组解析为正常的字符串
     *
     * @param base64Bytes Base64编码的字节数组
     * @return Base64编码的字节数组解析后的字符串
     */
    public static byte[] decode(byte[] base64Bytes) {
        if (ObjectUtil.isEmpty(base64Bytes)) {
            throw new IllegalArgumentException("Base64解码的字节数组不能为空");
        }

        return Base64.getDecoder().decode(base64Bytes);
    }
}
