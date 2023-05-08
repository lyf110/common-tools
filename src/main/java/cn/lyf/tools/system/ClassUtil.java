package cn.lyf.tools.system;


import cn.lyf.tools.str.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyf
 * @description 类型工具类
 * @since 2023/5/8 8:40:13
 */
public final class ClassUtil {
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE_MAP;
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE_MAP;

    private ClassUtil() {
    }

    static {
        WRAPPER_TO_PRIMITIVE_TYPE_MAP = new HashMap<>();
        PRIMITIVE_TO_WRAPPER_TYPE_MAP = new HashMap<>();

        add(byte.class, Byte.class);
        add(char.class, Character.class);
        add(short.class, Short.class);
        add(int.class, Integer.class);
        add(long.class, Long.class);
        add(float.class, Float.class);
        add(double.class, Double.class);
        add(boolean.class, Boolean.class);
        add(void.class, Void.class);
    }

    private static void add(Class<?> primClass,
                            Class<?> wrapClass) {
        PRIMITIVE_TO_WRAPPER_TYPE_MAP.put(primClass, wrapClass);
        WRAPPER_TO_PRIMITIVE_TYPE_MAP.put(wrapClass, primClass);
    }

    /**
     * 如果是基础数据类型，那么就返回其对应的包装类型，否则返回直接原始类型
     *
     * @param clazz 需要判断的类型
     * @return 如果是基础数据类型，那么就返回其对应的包装类型，否则直接原始类型
     */
    public static Class<?> getWrapperClass(Class<?> clazz) {
        Class<?> wrapClass = PRIMITIVE_TO_WRAPPER_TYPE_MAP.get(clazz);
        return wrapClass == null ? clazz : wrapClass;
    }

    /**
     * 通过包装类型获取其基础类型，如果不是包装类型的话，那么直接返回原始类型即可
     *
     * @param clazz 需要判断的类
     * @return 包装类型对应的基础类型
     */
    public static Class<?> getPrimitiveClass(Class<?> clazz) {
        Class<?> primitiveClass = WRAPPER_TO_PRIMITIVE_TYPE_MAP.get(clazz);
        return primitiveClass == null ? clazz : primitiveClass;
    }

    /**
     * 判断一个类是否为基础数据类型
     *
     * @param clazz 需要判断的类
     * @return true: 是基础数据类型，false: 不是基础数据类型
     * @see Class#isPrimitive()
     */
    public static boolean isPrimitiveClass(Class<?> clazz) {
        return PRIMITIVE_TO_WRAPPER_TYPE_MAP.containsKey(clazz);
    }

    /**
     * 判断一个类是否为包装数据类型
     *
     * @param clazz 需要判断的类
     * @return true: 是包装数据类型，false: 不是包装数据类型
     */
    public static boolean isWrapperClass(Class<?> clazz) {
        return WRAPPER_TO_PRIMITIVE_TYPE_MAP.containsKey(clazz);
    }


    /**
     * 此方法主要是对java.lang.Class#isAssignableFrom(java.lang.Class)方法的简单封装
     * ex: A.isAssignableFrom(B)
     * 是确定一个类(B)是不是继承来自另一个父类(A)
     * 或者是一个接口(A)是不是实现了另外一个接口(B)或两个类相同
     *
     * @param parentClass 父类
     * @param sonClass    子类
     * @return true：sonClass是parentClass的子类，false：sonClass不是parentClass的子类
     * @see Class#isAssignableFrom(java.lang.Class)
     */
    public static boolean isCastTo(Class<?> sonClass, Class<?> parentClass) {
        if (parentClass == null || sonClass == null) {
            return false;
        }
        return parentClass.isAssignableFrom(sonClass);
    }

    /**
     * 此方法主要是对java.lang.Class#isAssignableFrom(java.lang.Class)方法的简单封装
     * ex: A.isAssignableFrom(B)
     * 是确定一个类(B)是不是继承来自另一个父类(A)
     * 或者是一个接口(A)是不是实现了另外一个接口(B)或两个类相同
     *
     * @param parentClass 父类
     * @param sonClass    子类
     * @return true: sonClass不是parentClass的子类，false：sonClass是parentClass的子类
     * @see Class#isAssignableFrom(java.lang.Class)
     */
    public static boolean isNotCastTo(Class<?> sonClass, Class<?> parentClass) {
        return !isCastTo(sonClass, parentClass);
    }

    /**
     * 判断source对象是否为clazz类型的实例或者子实例
     *
     * @param source 目标对象
     * @param clazz  对象类型
     * @return true: 是clazz类型的实例或者是子实例, false: 取反
     * @see Class#isAssignableFrom(java.lang.Class)
     */
    public static boolean isInstanceOf(Object source, Class<?> clazz) {
        if (source == null) {
            return false;
        }

        if (clazz == null) {
            throw new IllegalArgumentException("clazz not null");
        }

        return clazz.isInstance(source);
    }

    /**
     * 判断source对象是否不为clazz类型的实例或者子实例
     *
     * @param source 目标对象
     * @param clazz  对象类型
     * @return true: 不是clazz类型的实例或者是子实例, false: 取反
     */
    public static boolean isNotInstanceOf(Object source, Class<?> clazz) {
        return !isInstanceOf(source, clazz);
    }

    /**
     * 根据类型获取对应的对象
     *
     * @param obj   对象
     * @param clazz 对象的具体类型的class对象
     * @param <T>   对象额度具体类型
     * @return T 类型的实例对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjectByType(Object obj, Class<T> clazz) {
        // 对象为空则直接返回空
        if (obj == null) {
            return null;
        }

        // 传入的clazz对象为空的话，则直接抛出异常
        if (clazz == null) {
            throw new IllegalArgumentException("clazz not null");
        }

        // 类型检查
        if (ClassUtil.isInstanceOf(obj, clazz)) {
            throw new IllegalArgumentException(StringUtil.format("{} can not cast to {}", obj.getClass(), clazz));
        }

        // 类型转换
        return (T) clazz;
    }

//    public static void main(String[] args) {
//        Class<?> wrapperClass = getWrapperClass(int.class);
//        System.out.println(wrapperClass);
//        System.out.println(getPrimitiveClass(wrapperClass));
//        A a = new A();
//        A b = new B();
//        A c = new C();
//        System.out.println(a.getClass().isPrimitive());
//        System.out.println(a.getClass().isInstance(b));
//        System.out.println(isInstanceOf(a, A.class));
//        System.out.println(isInstanceOf(b, A.class));
//        System.out.println(isInstanceOf(a, B.class));
//        System.out.println(getWrapperClass(void.class));
//        System.out.println(getPrimitiveClass(Void.class));
////
////        System.out.println(isCastTo(a.getClass(), A.class));
////        System.out.println(isCastTo(b.getClass(), A.class));
////
////        System.out.println(isCastTo(c.getClass(), A.class));
////
////        System.out.println(isCastTo(c.getClass(), b.getClass()));
//    }
//
//    static class A {
//    }
//
//    static class B extends A {
//    }
//
//    static class C extends A {
//    }
}
