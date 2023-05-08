package cn.lyf.tools.str.json;


import cn.lyf.tools.str.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 12:44:09
 */
public final class JacksonUtil {
    private static final ObjectMapper OBJECT_MAPPER;
    private static final DefaultPrettyPrinter OBJECT_MAPPER_PRINTER;
    private JacksonUtil() {
    }

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 配置四个空格的缩进
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
        OBJECT_MAPPER_PRINTER = new DefaultPrettyPrinter();
        OBJECT_MAPPER_PRINTER.indentObjectsWith(indenter); // Indent JSON objects
        OBJECT_MAPPER_PRINTER.indentArraysWith(indenter);  // Indent JSON arrays

    }

    /**
     * 将对象按以格式化json的方式写出
     * 使用的json为Jackson
     *
     * @param obj obj
     */
    public static String formatJson(Object obj) throws JsonProcessingException {
        return formatJson(obj, OBJECT_MAPPER);
    }

    /**
     * 将对象按以格式化json的方式写出
     * 使用的json为Jackson
     *
     * @param obj          obj
     * @param objectMapper objectMapper
     */
    public static String formatJson(Object obj, ObjectMapper objectMapper) throws JsonProcessingException {
        return formatJson(obj, objectMapper, OBJECT_MAPPER_PRINTER);
    }

    /**
     * 将对象按以格式化json的方式写出
     * 使用的json为Jackson
     *
     * @param obj          obj
     * @param objectMapper objectMapper
     * @param printer      格式化器
     */
    public static String formatJson(Object obj, ObjectMapper objectMapper, DefaultPrettyPrinter printer) throws JsonProcessingException {
        if (ObjectUtil.isEmpty(obj)) {
            return null;
        }

        if (objectMapper == null) {
            objectMapper = OBJECT_MAPPER;
        }

        if (printer == null) {
            printer = OBJECT_MAPPER_PRINTER;
        }

        return objectMapper.writer(printer).writeValueAsString(obj);
    }


    /**
     * 输出格式化的json字符串
     * 使用的json为Jackson
     *
     * @param obj 任意对象
     * @return 格式化的json字符串
     * @throws JsonProcessingException jsonProcessingException
     */
    public static String formatJsonWithDefaultPrettyPrinter(Object obj) throws JsonProcessingException {
        if (ObjectUtil.isEmpty(obj)) {
            return null;
        }
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
