package cn.lyf.tools.core.convert.support;

import cn.lyf.tools.core.convert.converter.Converter;
import cn.lyf.tools.core.convert.converter.ConverterFactory;
import cn.lyf.tools.str.StringUtil;
import org.springframework.util.NumberUtils;

import java.math.BigInteger;

/**
 * @author lyf
 * @description String类型转成数字类型
 * @since 2023/5/8 17:25:35
 */
public final class StringToNumberConverterFactory implements ConverterFactory<String, Number> {
    public static void main(String[] args) {
        ConverterFactory<String, Number> converterFactory = new StringToNumberConverterFactory();
        Converter<String, BigInteger> converter = converterFactory.getConverter(BigInteger.class);
        BigInteger convert = converter.convert("0x123213213213213123123213123");
        System.out.println(convert);
    }

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {
        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (StringUtil.isEmpty(source)) {
                return null;
            }
            return NumberUtils.parseNumber(source, this.targetType);
        }
    }
}
