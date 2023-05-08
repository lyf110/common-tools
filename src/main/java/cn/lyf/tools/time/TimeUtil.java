package cn.lyf.tools.time;

import java.util.concurrent.TimeUnit;

/**
 * @author lyf
 * @description 支持超高并发获取当前系统时间的毫秒值
 * @since 2023/5/4 14:34:34
 */
public class TimeUtil {
    private static volatile long currentTimeMillis;

    private TimeUtil() {
    }

    static {
        currentTimeMillis = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            while (true) {
                currentTimeMillis = System.currentTimeMillis();
                sleepAndIgnoreException(1, TimeUnit.MILLISECONDS);
            }
        });

        thread.setDaemon(true);
        thread.setName("time-tick-thead");
        thread.start();
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @return currentTimeMillis
     */
    public static long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    /**
     * 获取当前时间的秒值
     *
     * @return currentTimeMillis
     */
    public static long getCurrentTimeSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis);
    }

    /**
     * 线程休眠seconds秒
     *
     * @param seconds 休眠时间
     */
    public static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠seconds秒
     *
     * @param seconds 休眠时间
     */
    public static void sleepAndIgnoreException(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ignore) {

        }
    }

    /**
     * 线程休眠
     *
     * @param time 休眠时间
     * @param unit 休眠时间单位
     */
    public static void sleep(long time, TimeUnit unit) {
        try {
            TimeUnit.SECONDS.sleep(unit.toSeconds(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠
     *
     * @param time 休眠时间
     * @param unit 休眠时间单位
     */
    public static void sleepAndIgnoreException(long time, TimeUnit unit) {
        try {
            TimeUnit.SECONDS.sleep(unit.toSeconds(time));
        } catch (InterruptedException ignore) {
        }
    }
}
