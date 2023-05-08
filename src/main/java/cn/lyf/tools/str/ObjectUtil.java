package cn.lyf.tools.str;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Object工具类
 *
 * @author liu yang fang
 * @since 2023/04/24 10:28
 */
public final class ObjectUtil {
    /**
     * 私有构造器
     */
    private ObjectUtil() {
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return true: 为空, false: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        // Optional封装的对象
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }

        // 字符串或者字符对象
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        // 数组对象
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        // 集合对象
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }

        // Map对象
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        // else
        return false;
    }


    /**
     * 判断对象是否不为空
     *
     * @param obj 对象
     * @return true: 不为空, false: 为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
