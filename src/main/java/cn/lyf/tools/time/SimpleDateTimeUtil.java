package cn.lyf.tools.time;


import cn.lyf.tools.ffmpeg.VideoUtil;
import it.sauronsoftware.jave.EncoderException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 16:10:07
 */
public final class SimpleDateTimeUtil {
    private SimpleDateTimeUtil() {
    }

    /**
     * ex: 2022-09-08
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * ex: 2022-09-08 13:12:23
     * 24小时制
     */
    public static final String PATTERN_DATE_TIME_24 = "yyyy-MM-dd HH:mm:ss";

    /**
     * ex: 2022-09-08 01:12:23
     * 12小时制
     */
    public static final String PATTERN_DATE_TIME_12 = "yyyy-MM-dd hh:mm:ss";


    /**
     * 年月日
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_DATE));

    /**
     * 年月日时分秒 24小时制
     */
    private static final ThreadLocal<SimpleDateFormat> dateTime24FormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_DATE_TIME_24));

    /**
     * 年月日时分秒 12小时制
     */
    private static final ThreadLocal<SimpleDateFormat> dateTime12FormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_DATE_TIME_12));

    /**
     * 将日期转成字符串
     *
     * @param date 日期
     * @return 字符串形式，ex：2022-12-21
     */
    public static String formatWithDate(Date date) {
        return formatWithDate(date, dateFormatThreadLocal);
    }

    /**
     * 将日期转成字符串
     *
     * @param date        日期
     * @param threadLocal 存储SimpleDateFormat变量的ThreadLocal对象
     * @return 时间字符串
     */
    public static String formatWithDate(Date date, ThreadLocal<SimpleDateFormat> threadLocal) {
        if (threadLocal == null) {
            threadLocal = dateFormatThreadLocal;
        }
        return threadLocal.get().format(date);
    }

    /**
     * 将日期转成字符串
     *
     * @param date 日期
     * @return 字符串形式，ex：2022-12-21 12:12:12(24小时制)
     */
    public static String formatWithDateTime24(Date date) {
        return formatWithDate(date, dateTime24FormatThreadLocal);
    }

    /**
     * 将日期转成字符串
     *
     * @param date 日期
     * @return 字符串形式，ex：2022-12-21 12:12:12(12小时制)
     */
    public static String formatWithDateTime12(Date date) {
        return formatWithDate(date, dateTime12FormatThreadLocal);
    }

    /**
     * 将时间字符串转成时间对象
     *
     * @param timeStr 时间字符串 ex：2022-12-12
     * @return date
     * @throws ParseException ParseException
     */
    public static Date parseDate(String timeStr) throws ParseException {
        return parseDate(timeStr, dateFormatThreadLocal);
    }

    /**
     * 将时间字符串转成时间对象
     *
     * @param timeStr     时间字符串 ex：2022-12-12
     * @param threadLocal 存储SimpleDateFormat变量的ThreadLocal对象
     * @return date
     * @throws ParseException ParseException
     */
    public static Date parseDate(String timeStr, ThreadLocal<SimpleDateFormat> threadLocal) throws ParseException {
        if (threadLocal == null) {
            threadLocal = dateFormatThreadLocal;
        }

        return threadLocal.get().parse(timeStr);
    }

    /**
     * 将时间字符串转成时间对象
     *
     * @param timeStr 时间字符串 ex：2022-12-12 14:21:21(24小时制)
     * @return date
     * @throws ParseException ParseException
     */
    public static Date parseDateWithDateTime24(String timeStr) throws ParseException {
        return parseDate(timeStr, dateTime24FormatThreadLocal);
    }

    /**
     * 将时间字符串转成时间对象
     *
     * @param timeStr 时间字符串 ex：2022-12-12 11:21:21(12小时制)
     * @return date
     * @throws ParseException ParseException
     */
    public static Date parseDateWithDateTime12(String timeStr) throws ParseException {
        return parseDate(timeStr, dateTime12FormatThreadLocal);
    }

    public static final long SECONDS_TO_MILLIS = 1000L;
    public static final long MINUTE_TO_MILLIS = 60 * SECONDS_TO_MILLIS;
    public static final long HOUR_TO_MILLIS = 60 * MINUTE_TO_MILLIS;

    /**
     * 将以秒为单位的视频时间转成HH:mm:ss的形式
     *
     * @param milliseconds 视频时间
     * @return HH:mm:ss的时间格式时间
     */
    public static String formatForVideoTime(long milliseconds) {
        if (milliseconds == 0) {
            return "00:00:00.000";
        }

        // 计算出小时
        long videoHour = milliseconds / HOUR_TO_MILLIS;
        // 计算出分钟
        long videoMinute = (milliseconds - videoHour * HOUR_TO_MILLIS) / MINUTE_TO_MILLIS;
        // 计算出秒
        long videoSeconds = (milliseconds - videoHour * HOUR_TO_MILLIS - videoMinute * MINUTE_TO_MILLIS) / SECONDS_TO_MILLIS;
        // 计算出毫秒值
        long millis = milliseconds - videoHour * HOUR_TO_MILLIS - videoMinute * MINUTE_TO_MILLIS - videoSeconds * SECONDS_TO_MILLIS;

        String videoHourStr = getTimeStr(videoHour);
        String videoMinuteStr = getTimeStr(videoMinute);
        String millisStr = getMillisStr(millis);

        return String.format("%s:%s:%s.%s", videoHourStr, videoMinuteStr, videoSeconds, millisStr);
    }

    private static String getMillisStr(long millis) {
        String millisStr;
        if (millis == 0) {
            millisStr = "000";
        } else if (millis < 10) {
            millisStr = millis + "00";
        } else if (millis < 100) {
            millisStr = millis + "0";
        } else {
            millisStr = "" + millis;
        }
        return millisStr;
    }

    private static String getTimeStr(long time) {
        String timeStr;
        if (time == 0) {
            timeStr = "00";
        } else if (time < 10) {
            timeStr = "0" + time;
        } else {
            timeStr = "" + time;
        }
        return timeStr;
    }

    public static void main(String[] args) throws EncoderException {
        String video1 = "C:\\DISH\\test\\target\\video1.mp4";
        String video2 = "C:\\Users\\11029\\Pictures\\iCloud Photos\\Photos\\卿卿日常-10.mp4";
//        String duration1 = VideoUtil.getDuration(video1);
//        String duration2 = VideoUtil.getDuration(video2);
//        System.out.println(duration1);
//        System.out.println(duration2);
        int i = 8 * 60 * 60 * 1000;
        System.out.println(i);
        //long duration1 = VideoUtil.getVideoMetadata(video1).getMultimediaInfo().getDuration();
//        System.out.println(duration1);
//        System.out.println(formatForVideoTime(duration1));
    }
}
