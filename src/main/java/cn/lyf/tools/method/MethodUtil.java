package cn.lyf.tools.method;


import cn.lyf.tools.core.function.ConsumerFunction;
import cn.lyf.tools.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lyf
 * @description 提供一些常用的方法增强方法
 * @since 2023/5/4 16:00:29
 */
@Slf4j
public final class MethodUtil {
    private MethodUtil() {
    }

    /**
     * 计算方法的运行时间
     *
     * @param consumer consumer
     */
    public static long costTime(ConsumerFunction consumer) throws Exception {
        if (consumer == null) {
            throw new IllegalArgumentException("传入的接口为null");
        }
        long startTime = TimeUtil.getCurrentTimeMillis();
        consumer.run();
        long endTime = TimeUtil.getCurrentTimeMillis();
        long costTime = endTime - startTime;
        log.info("cost time: {} ms", costTime);
        return costTime;
    }

    /**
     * 通过反射调用方法并计算方法运行时间
     *
     * @param obj    目标对象
     * @param method 目标方法
     * @param args   目标方法参数
     */
    public static long costTime(Object obj, Method method, Object... args) {
        if (method == null) {
            throw new IllegalArgumentException("method is not null");
        }
        long startTime = TimeUtil.getCurrentTimeMillis();
        try {
            method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        long endTime = TimeUtil.getCurrentTimeMillis();
        long costTime = endTime - startTime;
        log.info("cost time: {} ms", costTime);
        return costTime;
    }
}
