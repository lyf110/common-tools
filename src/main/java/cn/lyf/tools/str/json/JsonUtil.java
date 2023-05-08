package cn.lyf.tools.str.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * @author lyf
 * @description Json格式化工具类
 * @since 2023/5/4 10:38:04
 */
public final class JsonUtil {
    private JsonUtil() {
    }


    /**
     * 将对象按以格式化json的方式写出
     * 使用的json为Jackson
     *
     * @param obj obj
     */
    public static String formatJsonByJackson(Object obj) throws JsonProcessingException {
        return JacksonUtil.formatJson(obj);
    }

    /**
     * 将对象按以格式化json的方式写出
     * 使用的json为Jackson
     *
     * @param obj          obj
     * @param objectMapper objectMapper
     */
    public static String formatJsonByJackson(Object obj, ObjectMapper objectMapper) throws JsonProcessingException {
        return JacksonUtil.formatJson(obj, objectMapper);
    }

    /**
     * 将对象按以格式化json的方式写出
     * 使用的json为Jackson
     *
     * @param obj          obj
     * @param objectMapper objectMapper
     * @param printer      格式化器
     */
    public static String formatJsonByJackson(Object obj, ObjectMapper objectMapper, DefaultPrettyPrinter printer) throws JsonProcessingException {
        return JacksonUtil.formatJson(obj, objectMapper, printer);
    }


    /**
     * 输出格式化的json字符串
     * 使用的json为Jackson
     *
     * @param obj 任意对象
     * @return 格式化的json字符串
     * @throws JsonProcessingException jsonProcessingException
     */
    public static String formatJsonByJacksonWithDefaultPrettyPrinter(Object obj) throws JsonProcessingException {
        return JacksonUtil.formatJsonWithDefaultPrettyPrinter(obj);
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
    public static String formatJsonByFastjson(Object obj) {
        return FastjsonUtil.formatJson(obj);
    }

    /**
     * 输出格式化的json字符串
     * 使用的json为Gson
     *
     * @param obj obj
     * @return 格式化的json字符串
     */
    public static String formatJsonByGson(Object obj) {
        return GsonUtil.formatJson(obj);
    }

    /**
     * 输出格式化的json字符串
     * 使用的json为Gson
     *
     * @param obj  obj
     * @param gson gson对象
     * @return 格式化的json字符串
     */
    public static String formatJsonByGson(Object obj, Gson gson) {
        return GsonUtil.formatJson(obj, gson);
    }
}
