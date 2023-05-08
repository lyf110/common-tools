package cn.lyf.tools.system;


import cn.lyf.tools.str.ObjectUtil;
import cn.lyf.tools.str.StringUtil;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * @author lyf
 * @description System.out.println 封装的工具类
 * @since 2023/5/6 13:42:58
 */
public final class ConsoleUtil {
    private static final String TEMPLATE_VAR = "{}";
    private static final String TEMPLATE_PATTERN = "\\{}";
    private static final Pattern PATTERN = Pattern.compile(TEMPLATE_PATTERN);
    private static final String NULL_VALUE = "null";

    private ConsoleUtil() {

    }

    /**
     * 打印日志，输出到标准流
     */
    public static void log() {
        println(out);
    }

    /**
     * 打印日志，输出到错误流
     */
    public static void error() {
        println(err);
    }


    /**
     * 打印日志，输出到标准流
     *
     * @param format    字符串模板
     * @param throwable 异常对象
     */
    public static void log(String format, Throwable throwable) {
        log(out, format, throwable);
    }

    /**
     * 打印日志，输出到错误流
     *
     * @param format    字符串模板
     * @param throwable 异常对象
     */
    public static void error(String format, Throwable throwable) {
        log(err, format, throwable);
    }

    /**
     * 打印日志
     *
     * @param printStream 打印与输出流
     * @param format      字符串模板
     * @param throwable   异常对象
     */
    public static void log(PrintStream printStream, String format, Throwable throwable) {
        if (StringUtil.isEmpty(format)) {
            log(printStream, throwable);
            return;
        }

        if (format.contains(TEMPLATE_VAR)) {
            // 那么就解析{}
            handlerLogMessage(printStream, format, throwable);
        } else {
            log(printStream, throwable, format);
        }
    }

    /**
     * 打印日志，输出到标准流
     *
     * @param obj 对象
     */
    public static void log(Object obj) {
        log(out, obj);
    }

    /**
     * 打印日志，输出到错误流
     *
     * @param obj 对象
     */
    public static void error(Object obj) {
        log(err, obj);
    }

    /**
     * 打印日志，输出到标准流
     *
     * @param printStream 打印与输出流
     * @param obj         对象
     */
    public static void log(PrintStream printStream, Object obj) {
        if (obj instanceof Throwable) {
            Throwable throwable = (Throwable) obj;
            log(printStream, throwable, throwable.getMessage());
        } else {
            log(printStream, TEMPLATE_VAR, obj);
        }
    }

    /**
     * 打印日志
     *
     * @param throwable 异常对象
     * @param template  字符串模板
     * @param values    参数集
     */
    public static void log(Throwable throwable, String template, Object... values) {
        log(out, throwable, template, values);
    }

    /**
     * 打印日志，输出到错误流
     *
     * @param throwable 异常对象
     * @param template  字符串模板
     * @param values    参数集
     */
    public static void error(Throwable throwable, String template, Object... values) {
        log(err, throwable, template, values);
    }

    /**
     * 打印日志
     *
     * @param throwable 异常对象
     * @param template  字符串模板
     * @param values    参数集
     */
    public static void log(PrintStream printStream, Throwable throwable, String template, Object... values) {
        log(printStream, template, values);
        if (throwable != null) {
            throwable.printStackTrace(printStream);
            printStream.flush();
        }
    }

    /**
     * 打印日志，输出到标准流
     *
     * @param template 字符串模板
     * @param values   参数集
     */
    public static void log(String template, Object... values) {
        log(out, template, values);
    }

    /**
     * 打印日志，输出到错误流
     *
     * @param template 字符串模板
     * @param values   参数集
     */
    public static void error(String template, Object... values) {
        log(err, template, values);
    }

    /**
     * 打印日志支持各种PrintStream流
     *
     * @param template 字符串模板
     * @param values   参数集
     */
    public static void log(PrintStream printStream, String template, Object... values) {
        handlerLogMessage(printStream, template, values);
    }

    private static void handlerLogMessage(PrintStream printStream, String template, Object... values) {
        boolean isValuesEmpty = ObjectUtil.isEmpty(values);
        if (StringUtil.isEmpty(template)) {
            println(printStream);
            return;
        }

        if (template.contains(TEMPLATE_VAR)) {
            if (isValuesEmpty) {
                println(printStream, template);
            } else {
                Matcher matcher = PATTERN.matcher(template);
                int count = 0;
                StringBuffer stringBuffer = new StringBuffer();
                while (matcher.find()) {
                    if (count >= values.length) {
                        break;
                    }
                    Object value = values[count];
                    matcher.appendReplacement(stringBuffer, value == null ? NULL_VALUE : value.toString());
                    count++;
                }
                matcher.appendTail(stringBuffer);
                println(printStream, stringBuffer.toString());
            }
        } else {
            println(printStream, template);
        }
    }

    /**
     * 打印字符串, 不换行
     *
     * @param str 字符串
     */
    public static void print(String str) {
        print(out, str);
    }

    /**
     * 打印字符串, 不换行
     *
     * @param str         字符串
     * @param printStream 打印与输出流
     */
    public static void print(PrintStream printStream, String str) {
        if (printStream == null) {
            throw new IllegalArgumentException("PrintStream not null");
        }

        printStream.print(str);
    }

    /**
     * 打印字符串，安装一定格式
     *
     * @param format 模板字符串 ex: 年: %d, 2022
     * @param values %d对应的参数
     */
    public static void printf(String format, Object... values) {
        printf(out, format, values);
    }

    /**
     * 打印字符串，按照一定格式
     *
     * @param printStream 打印与输出流
     * @param format      模板字符串 ex: 年: %d, 2022
     * @param values      %d对应的参数
     */
    public static void printf(PrintStream printStream, String format, Object... values) {
        if (printStream == null) {
            throw new IllegalArgumentException("PrintStream not null");
        }

        printStream.printf(format, values);
    }


    /**
     * 打印字符串，带换行
     */
    public static void println() {
        println(out);
    }

    /**
     * 打印字符串，带换行
     *
     * @param str str
     */
    public static void println(String str) {
        println(out, str);
    }

    /**
     * 打印字符串，带换行
     *
     * @param str         str
     * @param printStream printStream
     */
    public static void println(PrintStream printStream, String str) {
        if (printStream == null) {
            throw new IllegalArgumentException("PrintStream not null");
        }

        printStream.println(str);
    }

    /**
     * 打印换行
     *
     * @param printStream printStream
     */
    public static void println(PrintStream printStream) {
        if (printStream == null) {
            throw new IllegalArgumentException("PrintStream not null");
        }
        printStream.println();
    }

    /**
     * 增强版的打印，专门用于打印集合、数组、map
     *
     * @param obj obj
     */
    public static void printPlus(Object obj) {
        log(TEMPLATE_VAR, getPrintStr(obj));
    }

    /**
     * 增强版的打印，专门用于打印集合、数组、map
     *
     * @param obj obj
     */
    public static String getPrintStr(Object obj) {
        if (ObjectUtil.isEmpty(obj)) {
            log("传入的参数obj为空");
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            for (Object colObj : collection) {
                sb.append(colObj).append(System.lineSeparator());
            }
            return sb.toString();
        }

        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sb.append("key: ").append(entry.getKey()).append(", value: ").append(entry.getValue()).append(System.lineSeparator());
            }
            return sb.toString();
        }

        if (obj.getClass().isArray()) {
            Object[] objectArray = (Object[]) obj;
            for (Object objEle : objectArray) {
                sb.append(objEle).append(System.lineSeparator());
            }

            return sb.toString();
        }

        return obj.toString();
    }
}
