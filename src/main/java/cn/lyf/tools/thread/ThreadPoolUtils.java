package cn.lyf.tools.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @program: huawei-study
 * @classname: com.huawei.ffmpeg.util.TreadPoolUtils
 * @description:
 * @author: liu yang fang
 * @date: 2021-08-29 21:50
 * @version: v1.0
 **/
public final class ThreadPoolUtils {
    /**
     * 普通的线程池
     */
    private static final ExecutorService THREAD_POOL;

    /**
     * 关于时间任务的线程池
     */
    private static final ScheduledExecutorService SCHEDULED_THREAD_POOL;

    static {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 5L;
        TimeUnit keepAliveTimeUnit = TimeUnit.MINUTES;
        int queSize = 1024;
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("simple-pool-%d")
                .setDaemon(true)
                .build();
        THREAD_POOL = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                keepAliveTimeUnit, new LinkedBlockingQueue<>(queSize), threadFactory, new ThreadPoolExecutor.AbortPolicy());

        SCHEDULED_THREAD_POOL = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder().setNameFormat("time-scheduled-pool-%d").setDaemon(true).build());
    }

    public static ExecutorService getThreadPool() {
        return THREAD_POOL;
    }

    public static ScheduledExecutorService getScheduledThreadPool() {
        return SCHEDULED_THREAD_POOL;
    }

    public static void main(String[] args) {

    }
}
