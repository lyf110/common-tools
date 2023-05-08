package cn.lyf.tools.crypto;

import cn.lyf.tools.str.StringUtil;

/**
 * @author lyf
 * @description: 密码工具类
 * @version: v1.0
 * @since 2022-03-30 19:46
 */
public final class PasswordUtil {
    /**
     * 加解密的次数
     */
    private static final int ENCRYPT_DECRYPT_TIMES = 20;

    private PasswordUtil() {
    }

    /**
     * 对密码进行加密
     *
     * @param password password
     * @return 加密后的密码
     */
    public static String encrypt(String password) throws Exception {
        if (StringUtil.isEmpty(password)) {
            throw new IllegalArgumentException("password not be null");
        }
        String passwordPart1 = BCrypt.hashpw(password, BCrypt.gensalt()).substring(0, ENCRYPT_DECRYPT_TIMES);
        String passwordPart2 = DesUtil.encrypt(password, ENCRYPT_DECRYPT_TIMES);

        return passwordPart1 + passwordPart2;
    }

    /**
     * 对密码进行解密
     *
     * @param password password
     * @return 解密后的密码
     */
    public static String decrypt(String password) throws Exception {
        if (StringUtil.isEmpty(password)) {
            throw new IllegalArgumentException("password not be null");
        }

        return DesUtil.decrypt(password.substring(ENCRYPT_DECRYPT_TIMES), ENCRYPT_DECRYPT_TIMES);
    }
}
