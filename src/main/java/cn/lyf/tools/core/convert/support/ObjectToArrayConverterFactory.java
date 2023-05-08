package cn.lyf.tools.core.convert.support;

import cn.lyf.tools.collection.ArrayUtil;
import cn.lyf.tools.core.convert.converter.Converter;
import cn.lyf.tools.core.convert.converter.ConverterFactory;

import java.util.Arrays;

/**
 * @author lyf
 * @description
 * @since 2023/5/8 17:50:12
 */
public final class ObjectToArrayConverterFactory<R> implements ConverterFactory<Object, R> {

    public static void main(String[] args) {
        ConverterFactory<Object, Object[]> factory = new ObjectToArrayConverterFactory<>();
        Converter<Object, Object[]> converter = factory.getConverter(Object[].class);
        String[] str = {"1", "2", "3"};
        Object[] convert = converter.convert(str);
        System.out.println(Arrays.toString(convert));
    }


    /**
     * 获取类型转换器，此类型转换器支持S类型转成T类型，其中T类型是R类型的实例或者子实例
     *
     * @param targetType 目标类型
     * @return 类型转换器，支持S类型转成T类型
     */
    @Override
    public <T extends R> Converter<Object, T> getConverter(Class<T> targetType) {
        return new ObjectToArray<>(targetType);
    }

    private static final class ObjectToArray<T> implements Converter<Object, T> {
        private final Class<T> targetClass;

        public ObjectToArray(Class<T> targetClass) {
            this.targetClass = targetClass;
        }

        /**
         * 将source对象转成T类型对象
         *
         * @param source source类型
         * @return T 类型对象
         */
        @Override
        @SuppressWarnings("unchecked")
        public T convert(Object source) {
            return (T) ArrayUtil.toArray(source, targetClass.getComponentType());
        }
    }
}
