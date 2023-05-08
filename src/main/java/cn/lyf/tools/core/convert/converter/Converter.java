package cn.lyf.tools.core.convert.converter;

import cn.hutool.core.lang.Assert;

/**
 * @author lyf
 * @description
 * @since 2023/5/8 17:14:09
 */
@FunctionalInterface
public interface Converter<S, T> {
    /**
     * 将source对象转成T类型对象
     *
     * @param source source类型
     * @return T 类型对象
     */
    T convert(S source);

    default <U> Converter<S, U> andThen(Converter<? super T, ? extends U> after) {
        Assert.notNull(after, "after converter must not be null");

        return source -> {
            T result = convert(source);
            return result == null ? null : after.convert(result);
        };
    }
}
