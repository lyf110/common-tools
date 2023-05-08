package cn.lyf.tools.collection;


import cn.lyf.tools.system.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author lyf
 * @description 数组的工具类
 * @since 2023/5/8 9:21:22
 */
@Slf4j
public final class ArrayUtil {
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private ArrayUtil() {
    }

    /**
     * 判断一个对象是否为数组
     *
     * @param obj 对象
     * @return true: 为数组, false: 不是数组
     */
    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }


    /**
     * 判断一个对象是否不是数组
     *
     * @param obj 对象
     * @return true: 不是数组, false: 是数组
     */
    public static boolean isNotArray(Object obj) {
        return !isArray(obj);
    }

    /**
     * 先判断一个对象是否为数组，如果是数组的话，那么就获取数组的类型
     *
     * @param source 源对象
     * @return 如果源对象是数组的话，那么就返回该数组的类型
     */
    public static Class<?> getComponentType(Object source) {
        return null == source ? null : source.getClass().isArray() ? source.getClass().getComponentType() : null;
    }

    public static void main(String[] args) {

        int[] objArray = {1, 2, 4};
        Object[] objects = toObjectArray(objArray);
        System.out.println(Arrays.toString(objects));
    }


    /**
     * 将对象转成Object数组
     *
     * @param source 任意对象
     * @return Object数组
     */
    public static Object[] toObjectArray(Object source) {
        if (source == null) {
            return EMPTY_OBJECT_ARRAY;
        }

        if (source instanceof Object[]) {
            return (Object[]) source;
        }

        if (isNotArray(source)) {
            throw new IllegalArgumentException("source is not an array: " + source);
        }

        // 获取数组的长度
        int length = Array.getLength(source);
        if (length == 0) {
            return EMPTY_OBJECT_ARRAY;
        } else {
            // 获取源数组的类型
            Class<?> arrayComponentType = Array.get(source, 0).getClass();
            Object[] newArray = (Object[]) Array.newInstance(arrayComponentType, length);

            for (int i = 0; i < length; ++i) {
                newArray[i] = Array.get(source, i);
            }

            return newArray;
        }
    }


    /**
     * 将对象转成Object数组
     *
     * @param source 任意对象
     * @param clazz  数组的泛型的class对象
     * @param <T>    数组的泛型
     * @return Object数组
     */
    @SuppressWarnings("all")
    public static <T> T[] toArray(Object source, Class<T> clazz) {
        // source为null的话，那么直接返回null
        if (source == null) {
            return null;
        }

        // 排除class是null的情况
        if (clazz == null) {
            throw new IllegalArgumentException("clazz not be null");
        }

        // 排除source不是数组
        if (ArrayUtil.isNotArray(source)) {
            throw new IllegalArgumentException(source.getClass() + " is not array");
        }

        // 获取数组的元素类型
        Class<?> sourceArrayComponentType = getComponentType(source);
        // 判断数组的元素类型是否为基础数据类型
        boolean isBaseType = ClassUtil.isPrimitiveClass(sourceArrayComponentType);
        if (isBaseType) {
            // 如果是基础数据类型的话，这里需要做一步转换，将基础数据类型转成对应的包装数据类型
            sourceArrayComponentType = ClassUtil.getWrapperClass(sourceArrayComponentType);
        }

        // 判断原始的数组的类型是否强制转换成clazz对应的对象
        if (ClassUtil.isNotCastTo(sourceArrayComponentType, clazz)) {
            throw new IllegalArgumentException(sourceArrayComponentType + ", can not cast to " + clazz);
        }

        // 现在可以确定源数组的泛型和我们传入的泛型一致了
        int length = Array.getLength(source);

        if (length == 0) {
            return (T[]) Array.newInstance(clazz, length);
        } else {
            // 创建一个泛型为clazz的数组
            T[] newArray = (T[]) Array.newInstance(clazz, length);

            if (isBaseType) {
                /*
                    如果数组的泛型是基础数据类型的话，那么就不能使用System.arraycopy进行复制了
                    使用System.arraycopy进行复制会抛出如下异常：
                        Exception in thread "main" java.lang.ArrayStoreException
                 */
                arrayCopyOnBaseClassType(source, length, newArray);
            } else {
                // 非基础数据类型则可以使用System.arraycopy
                System.arraycopy(source, 0, newArray, 0, length);
            }

            return newArray;
        }
    }

    /**
     * 处理基础数据类型的数据拷贝动作
     *
     * @param source   源数组
     * @param length   源数组长度
     * @param newArray 新数组
     * @param <T>      数组泛型
     */
    private static <T> void arrayCopyOnBaseClassType(Object source, int length, T[] newArray) {
        for (int i = 0; i < length; ++i) {
            Object obj = Array.get(source, i);
            if (obj == null) {
                newArray[i] = null;
                continue;
            }

            newArray[i] = (T) obj;
        }
    }
}
