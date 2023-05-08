package cn.lyf.tools.str.json;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author lyf
 * @description 阿里的Fastjson工具类
 * @since 2023/5/4 12:47:07
 */
public final class FastjsonUtil {
    private FastjsonUtil() {
    }

    /**
     * 输出格式化的json字符串
     * 使用的json为FastJson
     * <p>
     * 加上SerializerFeature.DisableCircularReferenceDetect关闭循环引用
     * 解决：
     * [
     * {
     * "playVideo":"观看视频",
     * "aboutVersion":"版本详情"
     * },
     * {"$ref":"$[0]"},
     * {"$ref":"$[0]"},
     * {"$ref":"$[0]"}
     * ]
     *
     * @param obj obj
     * @return 格式化的json字符串
     */
    public static String formatJson(Object obj) {
        // 输出格式化后的字符串
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect);
    }
}
