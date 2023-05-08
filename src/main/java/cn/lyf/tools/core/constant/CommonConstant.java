package cn.lyf.tools.core.constant;

import java.io.File;

/**
 * 常量类
 *
 * @author liu yang fang
 * @since 2023/04/24 10:19
 */
public final class CommonConstant {
    public static final String HTTPS = "HTTPS://";
    public static final String HTTP = "HTTP://";
    public static final String SPLIT_STR = "@";

    public static final String ENCODE_GBK = "GBK";


    public static final String OS_NAME = "os.name";

    public static final String OS_NAME_WIN = "win";

    public static final String OS_NAME_LINUX = "linux";

    public static final String OS_NAME_MAC = "mac";

    public static final String SYSTEM_NAME = System.getProperty(OS_NAME);

    public static final String FFMPEG_PATH = Thread.currentThread().getContextClassLoader().getResource("ffmpeg/ffmpeg.exe").getPath();



    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String SSL = "SSL";

    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_VALUE =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36";



    public static final String TARGET_CLASSES = File.separator + "target" + File.separator + "classes";
    public static final String SRC_MAIN_RESOURCES =
            File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    public static final String EXE_SUFFIX = ".EXE";
}
