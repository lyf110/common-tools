package cn.lyf.tools.core.constant;

/**
 * @author lyf
 * @description 字符的编码枚举类
 * @since 2023/5/4 15:48:08
 */
public enum CharsetEnum {
    US_ASCII("US-ASCII"),
    ISO_8859_1("ISO-8859-1"),
    UTF_16BE("UTF-16BE"),
    UTF_16LE("UTF-16LE"),
    UTF_8("UTF-8"),
    UTF_16("UTF-16"),
    GBK("GBK");

    private final String charsetName;

    public String getCharsetName() {
        return this.charsetName;
    }

    CharsetEnum(String charsetName) {
        this.charsetName = charsetName;
    }
}
