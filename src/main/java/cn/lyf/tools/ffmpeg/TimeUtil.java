package cn.lyf.tools.ffmpeg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: huawei-study
 * @classname: com.lyf.swing.util.TimeUtil
 * @description: 校验24小时时间
 * @author: liu yang fang
 * @date: 2021-10-11 15:48
 * @version: v1.0
 **/
public final class TimeUtil {
    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

    /**
     * 校验字符串是否为符合24小时制的字符
     *
     * @param hourTime 例如：00:00:00
     * @return 是否符合规则
     */
    public static boolean isValidTime(String hourTime) {
        Pattern pattern = Pattern.compile(TIME24HOURS_PATTERN);
        Matcher matcher = pattern.matcher(hourTime);
        return matcher.matches();
    }

    /**
     * 开始时间必须小于结束时间
     *
     * @param startTime 例如：00:00:00
     * @return 是否符合规则
     */
    public static boolean isValidTime(String startTime, String endTime) {
        if (!isValidTime(startTime) || !isValidTime(endTime)) {
            throw new IllegalArgumentException("the time format is invalid");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long time = end.getTime() - start.getTime();
            return time > 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
