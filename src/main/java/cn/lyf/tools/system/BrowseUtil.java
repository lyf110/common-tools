package cn.lyf.tools.system;

import cn.lyf.tools.core.constant.CommonConstant;
import cn.lyf.tools.io.file.FileUtil;
import cn.lyf.tools.str.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 打开浏览器或者文件夹的工具类
 *
 * @author liu yang fang
 * @since 2023/04/24 09:51
 */
@Slf4j
public final class BrowseUtil {

    /**
     * 私有构造器
     */
    private BrowseUtil() {
    }

    /**
     * java调用默认浏览器打开网址
     *
     * @param url 网址
     */
    public static void browse(String url) {
        if (StringUtil.isEmpty(url)) {
            log.error("the url is not empty");
            return;
        }
        if (FileUtil.isUrl(url)) {
            // 打开浏览器
            log.debug("打开浏览器, url: {}", url);
            try {
                Desktop desktop = Desktop.getDesktop();
                if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
                    URI uri = new URI(url);
                    desktop.browse(uri);
                }
            } catch (URISyntaxException | IOException e) {
                log.error("browse net fail", e);
            }
        } else {
            try {
                File file = new File(url);
                log.debug("打开本地文件或者本地文件夹, url: {}", file.getCanonicalPath());
                // 打开本地本地文件夹或者文件
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 打开exe文件
     *
     * @param appPath appPath
     */
    public static void openExeApp(String appPath) {
        if (isExeApplication(appPath)) {
            // 打开exe文件
            browse(appPath);
        }
    }

    /**
     * 是否为exe程序
     *
     * @param appPath exe程序地址
     * @return 是否为exe程序
     */
    public static boolean isExeApplication(String appPath) {
        return StringUtil.isNotEmpty(appPath) && StringUtil.toUpperCase(appPath).endsWith(CommonConstant.EXE_SUFFIX);
    }
}
