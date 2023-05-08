package cn.lyf.tools.collection;

import cn.lyf.tools.str.ObjectUtil;
import cn.lyf.tools.str.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 集合的工具类
 *
 * @author liu yang fang
 * @since 2023-04-24 10:23
 **/
@Slf4j
public final class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static <T> void printCollection(Collection<T> collections) {
        if (CollectionUtil.isEmpty(collections)) {
            log.info("collections is empty");
            return;
        }

        System.out.println(getPrintCollectionString(collections));
    }

    /**
     * 返回集合打印字符串
     *
     * @param collections 集合
     * @param <T>         泛型
     * @return 集合每个元素换行显示
     */
    public static <T> String getPrintCollectionString(Collection<T> collections) {
        if (CollectionUtil.isEmpty(collections)) {
            log.info("collections is empty");
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (T collection : collections) {
            if (count == 0) {
                sb.append(collection);
            } else {
                sb.append(System.lineSeparator()).append(collection);
            }
            count++;
        }
        return sb.toString();
    }

    /**
     * 集合元素，全部转成大写形式
     *
     * @param list 集合
     * @return 内容全为大写的集合
     */
    public static List<String> getUpperCaseList(List<String> list) {
        List<String> upperCaseList = new ArrayList<>();
        if (CollectionUtil.isEmpty(list)) {
            log.info("collections is empty");
            return upperCaseList;
        }
        list.forEach(str -> {
            if (StringUtil.isNotEmpty(str)) {
                upperCaseList.add(StringUtil.toUpperCase(str));
            }
        });
        return upperCaseList;
    }

    /**
     * 合并两个集合
     *
     * @param list1 集合1
     * @param list2 集合2
     * @param <T>   泛型
     * @return 合并后的集合
     */
    public static <T> List<T> mergeCollectionAndDistinct(List<T> list1, List<T> list2) {
        List<T> allList = new ArrayList<>();

        if (isNotEmpty(list1)) {
            allList.addAll(list1);
        }

        if (isNotEmpty(list2)) {
            allList.addAll(list2);
        }

        return allList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 将数组转成集合
     *
     * @param tArr 数组对象
     * @param <T>  数组的类型
     * @return tList
     */
    public static <T> List<T> arrayToList(T[] tArr) {
        if (ObjectUtil.isEmpty(tArr)) {
            return Collections.emptyList();
        }

        return new ArrayList<>(Arrays.asList(tArr));
    }
}
