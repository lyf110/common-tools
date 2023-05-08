package cn.lyf.tools.core.builder;

import java.io.Serializable;

/**
 * @param <T> 建造者对象类型
 * @author lyf
 * @description 建造者模式接口定义
 * @since 2023/5/4 10:00:10
 */
public interface Builder<T> extends Serializable {
    /**
     * 构建
     *
     * @return 被构建的对象
     */
    T build();
}
