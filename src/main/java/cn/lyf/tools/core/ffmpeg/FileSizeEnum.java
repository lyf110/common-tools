package cn.lyf.tools.core.ffmpeg;

import lombok.Getter;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 20:02:03
 */
public enum FileSizeEnum {
    GB_SIZE("gb"),
    MB_SIZE("mb"),
    KB_SIZE("kb");
    @Getter
    private final String unit;

    FileSizeEnum(String unit) {
        this.unit = unit;
    }
}
