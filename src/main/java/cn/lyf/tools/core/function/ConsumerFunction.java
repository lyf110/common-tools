package cn.lyf.tools.core.function;

import java.io.IOException;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 16:02:16
 */
@FunctionalInterface
public interface ConsumerFunction {
    /**
     * 执行方法
     */
    void run() throws Exception;
}
