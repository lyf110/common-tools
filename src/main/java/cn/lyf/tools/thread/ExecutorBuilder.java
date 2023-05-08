package cn.lyf.tools.thread;

import cn.lyf.tools.core.builder.Builder;

import java.util.concurrent.*;

/**
 * @author lyf
 * @description 线程池构建器
 * @since 2023/5/4 9:39:19
 */
public final class ExecutorBuilder implements Builder<ThreadPoolExecutor> {
    private static final long serialVersionUID = 1L;

    /**
     * 默认的等待队列容量
     */
    public static final int DEFAULT_QUEUE_CAPACITY = 1024;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 最大线程数=核心线程数+救急线程数
     */
    private Integer maximumPoolSize;

    /**
     * 救急线程的空闲时间值
     */
    private Long keepAliveTime;

    /**
     * 救急线程的空闲时间单位
     */
    private TimeUnit unit;

    /**
     * 任务队列，当任务数达到核心线程数，此时继续提交任务，此时的任务会进入阻塞队列中
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 线程工厂，可以在此步限定线程池中的线程属性，比如线程命名，比如设置守护线程
     */
    private ThreadFactory threadFactory;

    /**
     * 当任务队列也满了之后，此时还继续提交任务，就会创建救急线程，当救急线程数也满了之后，就会触发拒绝策略
     */
    private RejectedExecutionHandler handler;

    /**
     * 线程执行超时后是否回收线程
     */
    private Boolean allowCoreThreadTimeOut;

    private ExecutorBuilder() {
    }

    public ExecutorBuilder corePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public ExecutorBuilder maximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public ExecutorBuilder keepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public ExecutorBuilder unit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    public ExecutorBuilder workQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    public ExecutorBuilder threadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    public ExecutorBuilder handler(RejectedExecutionHandler handler) {
        this.handler = handler;
        return this;
    }

    public ExecutorBuilder allowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        return this;
    }

    /**
     * 创建构建器对象
     *
     * @return ExecutorBuilder
     */
    public static ExecutorBuilder create() {
        return new ExecutorBuilder();
    }

    @Override
    public ThreadPoolExecutor build() {
        if (corePoolSize == null) {
            corePoolSize = 1;
        }

        if (maximumPoolSize == null) {
            maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        }

        if (keepAliveTime == null) {
            keepAliveTime = 0L;
        }

        if (unit == null) {
            unit = TimeUnit.SECONDS;
        }

        if (workQueue == null) {
            workQueue = new LinkedBlockingQueue<>(1000);
        }

        if (threadFactory == null) {
            threadFactory = Executors.defaultThreadFactory();
        }

        if (handler == null) {
            handler = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        return new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, this.keepAliveTime, this.unit, this.workQueue, this.threadFactory, this.handler);
    }
}
