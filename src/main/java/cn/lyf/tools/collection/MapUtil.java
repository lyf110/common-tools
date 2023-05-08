package cn.lyf.tools.collection;

import java.util.Map;

/**
 * @author liu yang fang
 * @description Map工具类
 * @since 2023-04-24 10:23
 **/
public final class MapUtil {
    /**
     * map是否为空
     *
     * @param map map
     * @return 是否空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * map是否为非空
     *
     * @param map map
     * @return 是否非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 获取map的打印文字
     *
     * @param map map
     */
    public static String getPrintMap(Map<?, ?> map) {
        if (isEmpty(map)) {
            return "map is empty";
        }

        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> sb.append(key).append("=").append(value).append(System.lineSeparator()));
        return sb.toString();
    }


    /**
     * 打印map
     *
     * @param map map
     */
    public static void printMap(Map<?, ?> map) {
        String printMap = getPrintMap(map);
        System.out.println(printMap);
    }
}
