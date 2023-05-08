package cn.lyf.tools.system;

import cn.lyf.tools.str.StringUtil;

import static cn.lyf.tools.core.constant.CommonConstant.*;

/**
 * 获取当前代码的运行系统
 *
 * @author liu yang fang
 * @since 2023-04-24 10:23
 **/
public final class SystemUtil {

    private SystemUtil() {
    }

    /**
     * 是否为windows系统
     *
     * @return windows系统
     */
    public static boolean isWindowSystem() {
        return StringUtil.isContains(SYSTEM_NAME, OS_NAME_WIN);
    }

    /**
     * 是否为linux系统
     *
     * @return linux系统
     */
    public static boolean isLinuxSystem() {
        return StringUtil.isContains(SYSTEM_NAME, OS_NAME_LINUX);
    }

    /**
     * 是否为MAC OS系统
     *
     * @return MAC OS系统
     */
    public static boolean isMacSystem() {
        return StringUtil.isContains(SYSTEM_NAME, OS_NAME_MAC);
    }

    /**
     * 不是windows、linux、mac os系统
     *
     * @return 其他操作系统
     */
    public static boolean isOtherSystem() {
        return !(isWindowSystem() || isLinuxSystem() || isMacSystem());
    }

    /**
     * 获取当前的系统名
     * System.getProperty("os.name");
     *
     * @return 当前的系统名称
     */
    public static String getOsName() {
        return SYSTEM_NAME;
    }
}


