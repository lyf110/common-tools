package cn.lyf.tools.io.http;

import cn.lyf.tools.core.constant.CommonConstant;
import cn.lyf.tools.str.StringUtil;

/**
 * HTTP的简单封装的工具类
 *
 * @author liu yang fang
 * @since 2023/04/24 10:48
 */
public final class HttpUtil {
    /**
     * 私有构造器
     */
    private HttpUtil() {
    }

    /**
     * 是http请求
     *
     * @param url url
     * @return true: 是http请求, false: 不是Http请求
     */
    public static boolean isHttpUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return false;
        }

        // 不是https是http
        return StringUtil.toUpperCase(url).startsWith(CommonConstant.HTTP);
    }

    /**
     * 是https请求
     *
     * @param url url
     * @return true: 是https请求, false: 不是Https请求
     */
    public static boolean isHttpsUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return false;
        }

        return StringUtil.toUpperCase(url).startsWith(CommonConstant.HTTPS);
    }

    /**
     * 是否是http或者https请求
     *
     * @param url url
     * @return true: 是http或者https请求, false: 不是http或者https请求
     */
    public static boolean isHttpOrHttps(String url) {
        if (StringUtil.isEmpty(url)) {
            return false;
        }
        String upperUrl = StringUtil.toUpperCase(url);
        return upperUrl.startsWith(CommonConstant.HTTP) || upperUrl.startsWith(CommonConstant.HTTPS);
    }
}
