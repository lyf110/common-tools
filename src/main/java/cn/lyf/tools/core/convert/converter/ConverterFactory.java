package cn.lyf.tools.core.convert.converter;

/**
 * @author lyf
 * @description 类型转换工厂的父接口
 * @since 2023/5/8 17:12:18
 */
@FunctionalInterface
public interface ConverterFactory<S, R> {

    /**
     * 获取类型转换器，此类型转换器支持S类型转成T类型，其中T类型是R类型的实例或者子实例
     *
     * @param targetType 目标类型
     * @param <T>        目标类型
     * @return 类型转换器，支持S类型转成T类型
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
