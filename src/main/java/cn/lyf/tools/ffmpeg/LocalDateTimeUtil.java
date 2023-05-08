package cn.lyf.tools.ffmpeg;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * @program: huawei-study
 * @classname: com.huawei.util.LocalDateTimeUtil
 * @description: jdk1.8 新的日期工作类
 * @author: liu yang fang
 * @date: 2021-08-24 17:43
 * @version: v1.0
 **/
public final class LocalDateTimeUtil {
    private static final String TWELVE_TIME_PATTERN_ONE = "hh:mm:ss,SSS a";
    private static final String TWELVE_TIME_PATTERN_TWO = "hh:mm:ss.SSS a";
    private static final String TWENTY_FOUR_TIME_PATTERN_ONE = "HH:mm:ss,SSS";
    private static final String TWENTY_FOUR_TIME_PATTERN_TWO = "HH:mm:ss.SSS";
    private static final String DATE_PATTERN_ONE = "yyyy-MM-dd";
    private static final String DATE_PATTERN_TWO = "yyyy/MM/dd";

    public static final DateTimeFormatter TWELVE_TIME_FORMATTER_ONE;
    private static final DateTimeFormatter TWELVE_TIME_FORMATTER_TWO;

    public static final DateTimeFormatter TWENTY_FOUR_TIME_FORMATTER_ONE;
    private static final DateTimeFormatter TWENTY_FOUR_TIME_FORMATTER_TWO;
    static {
        TWELVE_TIME_FORMATTER_ONE = new DateTimeFormatterBuilder()
                .appendPattern(TWELVE_TIME_PATTERN_ONE)
                .parseDefaulting(ChronoField.YEAR, Year.now().getValue())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, YearMonth.now().getMonthValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, MonthDay.now().getDayOfMonth())
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter(Locale.ENGLISH);

        TWELVE_TIME_FORMATTER_TWO = new DateTimeFormatterBuilder()
                .appendPattern(TWELVE_TIME_PATTERN_TWO)
                .parseDefaulting(ChronoField.YEAR, Year.now().getValue())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, YearMonth.now().getMonthValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, MonthDay.now().getDayOfMonth())
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter(Locale.ENGLISH);

        TWENTY_FOUR_TIME_FORMATTER_ONE = new DateTimeFormatterBuilder()
                .appendPattern(TWENTY_FOUR_TIME_PATTERN_ONE)
                .parseDefaulting(ChronoField.YEAR, Year.now().getValue())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, YearMonth.now().getMonthValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, MonthDay.now().getDayOfMonth())
                .toFormatter(Locale.ENGLISH);

        TWENTY_FOUR_TIME_FORMATTER_TWO = new DateTimeFormatterBuilder()
                .appendPattern(TWENTY_FOUR_TIME_PATTERN_TWO)
                .parseDefaulting(ChronoField.YEAR, Year.now().getValue())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, YearMonth.now().getMonthValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, MonthDay.now().getDayOfMonth())
                .toFormatter(Locale.ENGLISH);
    }

    /**
     * 将String类型转为LocalDateTime
     *
     * @param text 输入内容格式为：HH:mm:ss,SSS 02:00:01,234
     * @return LocalDateTime
     */
    public static LocalDateTime parseByTwentyFourPatternOne(String text) {
        return LocalDateTime.parse(text, TWENTY_FOUR_TIME_FORMATTER_ONE);
    }

    /**
     * 将String类型转为LocalDateTime
     *
     * @param text 输入内容格式为：HH:mm:ss,SSS 02:00:01,234
     * @return LocalDateTime
     */
    public static LocalDateTime parseByTwentyFourPatternTwo(String text) {
        return LocalDateTime.parse(text, TWENTY_FOUR_TIME_FORMATTER_TWO);
    }

    public static String getFfmpegSpiltTime(long millisecond) {
        long milliToNanoseconds = 1000 * 1000L;
        LocalTime dateTime = LocalTime.ofNanoOfDay(millisecond * milliToNanoseconds);
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * 将String类型转为LocalDateTime
     * LocalDateTime 对12小时制的时间进行格式化时，存在问题，会造成异常
     *
     * @param text 输入内容格式为：HH:mm:ss,SSS 02:00:01,234
     * @return LocalDateTime
     */
    @Deprecated
    public static LocalDateTime parseByTwelvePatternOne(String text) {
        return LocalDateTime.parse(text, TWELVE_TIME_FORMATTER_ONE);
    }

    /**
     * 将String类型转为LocalDateTime
     *  LocalDateTime 对12小时制的时间进行格式化时，存在问题，会造成异常
     * @param text 输入内容格式为：HH:mm:ss,SSS 02:00:01,234
     * @return LocalDateTime
     */
    @Deprecated
    public static LocalDateTime parseByTwelvePatternTwo(String text) {
        return LocalDateTime.parse(text, TWELVE_TIME_FORMATTER_TWO);
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = parseByTwentyFourPatternOne("02:00:13,250");
        // LocalDateTime localDateTime = parseByTwelvePatternOne("02:00:13,250");
        String format = localDateTime.minusHours(2).format(TWENTY_FOUR_TIME_FORMATTER_ONE);

        System.out.println(format);
    }
}
