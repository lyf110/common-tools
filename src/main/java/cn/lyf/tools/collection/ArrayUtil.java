package cn.lyf.tools.collection;


import cn.lyf.tools.str.StringUtil;
import cn.lyf.tools.system.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;

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
    @SuppressWarnings("unchecked")
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
            arrayCopy(source, 0, newArray, 0, length);
            return newArray;
        }
    }

    /**
     * 对java.lang.System#arraycopy(java.lang.Object, int, java.lang.Object, int, int)
     * 的封装
     *
     * @param src     源数组
     * @param srcPos  从源数组的第几位开始复制
     * @param dest    目标数组
     * @param destPos 从目标数组的第几位开始存入源数组的元素
     * @param length  复制的元素长度
     * @see System#arraycopy(java.lang.Object, int, java.lang.Object, int, int)
     */
    @SuppressWarnings("all")
    public static void arrayCopy(Object src, int srcPos, Object dest, int destPos, int length) {
        // 校验参数
        checkArrayCopyParams(src, srcPos, dest, destPos, length);

        // 如果是基础数据类型或者是其对应的包装类型的话，那么走此逻辑处理
        if (isPrimitiveOrWrapperClassAndHandler(src, srcPos, dest, destPos, length)) {
            return;
        }

        if (ClassUtil.isNotCastTo(src.getClass(), dest.getClass())) {
            throw new IllegalArgumentException(StringUtil.format(
                    "{} not cast to {}", src.getClass(), dest.getClass()));
        }
        // 非基础数据类型，那么我们直接调用System.arraycopy方法进行数组的复制操作
        System.arraycopy(src, srcPos, dest, destPos, length);
    }


    private static void checkArrayCopyParams(Object src, int srcPos, Object dest, int destPos, int length) {
        if (src == null) {
            throw new IllegalArgumentException("src not null");
        }

        if (dest == null) {
            throw new IllegalArgumentException("dest not null");
        }

        if (isNotArray(src)) {
            throw new IllegalArgumentException(src.getClass() + " is not array");
        }

        if (isNotArray(dest)) {
            throw new IllegalArgumentException(dest.getClass() + " is not array");
        }

        // 获取原数组的长度
        int srcLength = Array.getLength(src);
        if (srcPos + length > srcLength) {
            throw new ArrayIndexOutOfBoundsException(String.format(
                    "src: %s[], srcLength: %s, srcPos: %s, copyLength: %s, the srcPos + copyLength is over srcLength",
                    src.getClass().getComponentType(), srcLength, srcPos, length));
        }

        // 获取目标数组的长度
        int destLength = Array.getLength(dest);
        if (destPos + length > destLength) {
            throw new ArrayIndexOutOfBoundsException(String.format(
                    "dest: %s[], destLength: %s, destPos: %s, copyLength: %s, the destPos + copyLength is over destLength",
                    dest.getClass().getComponentType(), destLength, destPos, length));
        }
    }

    private static boolean isPrimitiveOrWrapperClassAndHandler(Object src, int srcPos, Object dest, int destPos, int length) {
        // 获取数组源数组类型和目标数组类型
        Class<?> srcComponentType = getComponentType(src);
        Class<?> destComponentType = getComponentType(dest);
        boolean isSrcPrimitiveClass = ClassUtil.isPrimitiveClass(srcComponentType);
        boolean isDestPrimitiveClass = ClassUtil.isPrimitiveClass(destComponentType);
        boolean isSrcWrapperClass = ClassUtil.isWrapperClass(srcComponentType);
        boolean isDestWrapperClass = ClassUtil.isWrapperClass(destComponentType);
        // 是基础数据类型的话或者是其包装类型时
        if (isSrcPrimitiveClass || isSrcWrapperClass) {
            if ((isSrcPrimitiveClass && isDestPrimitiveClass) || (isSrcWrapperClass && isDestWrapperClass)) {
                return false;
            }

            // 只有当两种一个基础数据类型一个是包装数据类型时，我们才进行此处理
            // 这里需要将目标数组转成相应的源数组类型
            if (!ClassUtil.getPrimitiveClass(destComponentType).equals(ClassUtil.getPrimitiveClass(srcComponentType))) {
                throw new IllegalArgumentException(StringUtil.format(
                        "{} not cast to {}", srcComponentType, destComponentType));
            }

            // 此时只能手动赋值
            int destIndex = 0;
            for (int srcIndex = srcPos; srcIndex < srcPos + length; srcIndex++) {
                Array.set(dest, destPos + destIndex, Array.get(src, srcIndex));
                destIndex++;
            }
            return true;
        }
        return false;
    }
}

