package cn.lyf.tools.str;


import cn.hutool.core.util.StrUtil;
import cn.lyf.tools.system.ConsoleUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lyf
 * @description String的工具类
 * @since 2023/5/2 15:49:59
 */
@Slf4j
public final class StringUtil {
    private static final String TEMPLATE_VAR = "{}";
    private static final String TEMPLATE_PATTERN = "\\{}";
    private static final Pattern PATTERN = Pattern.compile(TEMPLATE_PATTERN);
    private static final String NULL_VALUE = "null";

    private StringUtil() {
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 字符串是否为空，true：为空，false：不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return 字符串是否不为空，true：不为空，false：为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 去除字符串的空格
     *
     * @param str str
     * @return 去除字符串首尾空格
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 将字符串转成小写形式
     *
     * @param str str
     * @return str的小写形式
     */
    public static String toLowerCase(String str) {
        return toLowerCase(str, Locale.ENGLISH);
    }


    /**
     * 将字符串转成小写形式
     *
     * @param str    str
     * @param locale 编码形式
     * @return str的小写形式
     */
    public static String toLowerCase(String str, Locale locale) {
        if (isEmpty(str)) {
            return str;
        }

        return str.toLowerCase(locale);
    }

    /**
     * 将字符串转成大写形式
     *
     * @param str str
     * @return str的大写形式
     */
    public static String toUpperCase(String str) {
        return toUpperCase(str, Locale.ENGLISH);
    }


    /**
     * 将字符串转成小写形式
     *
     * @param str    str
     * @param locale 编码形式
     * @return str的大写形式
     */
    public static String toUpperCase(String str, Locale locale) {
        if (isEmpty(str)) {
            return str;
        }

        return str.toUpperCase(locale);
    }

    /**
     * 看一个父字符串是否包含子字符串，支持大小写匹配
     *
     * @param parentStr 父字符串
     * @param childStr  子字符串
     * @return true: 包含， false：不包含
     */
    public static boolean isContains(String parentStr, String childStr) {
        // 空字符串直接返回false
        if (isEmpty(parentStr) || isEmpty(childStr)) {
            return false;
        }
        return toUpperCase(parentStr).contains(childStr.toUpperCase());
    }

    /**
     * 看一个父字符串是否包含子字符串，支持大小写匹配
     *
     * @param parentStr 父字符串
     * @param childStr  子字符串
     * @return true: 包含， false：不包含
     */
    public static boolean isNotContains(String parentStr, String childStr) {
        return !isContains(parentStr, childStr);
    }

    /**
     * 解析字符串中的{}
     *
     * @param template 模板字符串
     * @param values   {}对应的参数
     * @return {} 处理后的字符串
     */
    public static String format(String template, Object... values) {
        return StrUtil.format(template, values);
    }
}
