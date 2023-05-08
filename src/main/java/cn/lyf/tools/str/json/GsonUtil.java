package cn.lyf.tools.str.json;


import com.google.gson.*;
import com.google.gson.internal.$Gson$Types;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lyf
 * @description 谷歌的Gson json工具类
 * @since 2023/5/4 12:32:10
 */
@Slf4j
public final class GsonUtil {
    /**
     * byte buffer 默认缓冲区大小
     */
    private static final int BYTE_BUFFER_SIZE = 8 * 1024;

    /**
     * 私有构造器
     */
    private GsonUtil() {
    }

    /**
     * 获取支持类信息编解码的gson
     *
     * @return Gson
     */
    public static Gson getSupportClassCodecGson() {
        return GsonHolder.CLASS_CODEC_GSON;
    }

    /**
     * 获取支持格式化json字符串的gson
     *
     * @return Gson
     */
    public static Gson getSupportPrettyGson() {
        return GsonHolder.PRETTY_PRINT_GSON;
    }

    /**
     * 获取支持类信息编解码和支持格式化json字符串的gson
     *
     * @return Gson
     */
    public static Gson getSupportPrettyAndClassCodecGson() {
        return GsonHolder.PRETTY_PRINT_CLASS_CODEC_GSON;
    }

    private static class GsonHolder {
        /**
         * 获取支持格式化json字符串的gson
         */
        private static final Gson CLASS_CODEC_GSON = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassCodec())
                .create();

        /**
         * 获取支持格式化json字符串的gson
         */
        private static final Gson PRETTY_PRINT_GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        /**
         * 获取支持类信息编解码和支持格式化json字符串的gson
         */
        private static final Gson PRETTY_PRINT_CLASS_CODEC_GSON = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassCodec())
                .setPrettyPrinting()
                .create();
    }

    public static String toJsonStr(Object obj) {
        return getSupportClassCodecGson().toJson(obj);
    }

    /**
     * 输出格式化的json字符串
     * 使用的json为Gson
     *
     * @param obj obj
     * @return 格式化的json字符串
     */
    public static String formatJson(Object obj) {
        return formatJson(obj, getSupportPrettyGson());
    }

    /**
     * 输出格式化的json字符串
     * 使用的json为Gson
     *
     * @param obj  obj
     * @param gson gson对象
     * @return 格式化的json字符串
     */
    public static String formatJson(Object obj, Gson gson) {
        if (gson == null) {
            gson = getSupportPrettyGson();
        }
        return gson.toJson(obj);
    }

    /**
     * 将文件输入流转成List<T>
     *
     * @param inputStream 文件输入流
     * @param <T>         List泛型
     * @return List<T>
     */
    public static <T> List<T> inputStreamToList(InputStream inputStream, Class<T> clazz) {
        byte[] bytes = inputStreamToByte(inputStream);
        if (bytes == null) {
            return Collections.emptyList();
        }

        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        return jsonStringToList(jsonStr, clazz);
    }

    /**
     * 将json字符串转成List<T>
     *
     * @param jsonStr json字符串
     * @param <T>     List泛型
     * @return List<T>
     */
    public static <T> List<T> jsonStringToList(String jsonStr, Class<T> clazz) {
        try {
            Type listType = $Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);
            return GsonHolder.CLASS_CODEC_GSON.fromJson(jsonStr, listType);
        } catch (JsonSyntaxException e) {
            log.error("json string to List error", e);
            return Collections.emptyList();
        }
    }

    /**
     * 将文件输入流转成byte 数组（不支持大文件转换）
     *
     * @param inputStream 文件输入流
     * @return json字符串
     */
    public static byte[] inputStreamToByte(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] byteBuf = new byte[BYTE_BUFFER_SIZE];
            int length;
            while ((length = bis.read(byteBuf)) != -1) {
                baos.write(byteBuf, 0, length);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            log.error("inputStream to Json String error", e);
            return null;
        }
    }

    /**
     * 支持序列化Class<?>
     */
    public static class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }
    }
}
