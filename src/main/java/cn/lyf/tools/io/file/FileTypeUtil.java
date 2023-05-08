package cn.lyf.tools.io.file;


import cn.lyf.tools.collection.CollectionUtil;
import cn.lyf.tools.io.IoUtil;
import cn.lyf.tools.str.StringUtil;
import net.sf.jmimemagic.*;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyf
 * @description 文件类型工具类
 * @since 2023/5/2 15:49:59
 */
public final class FileTypeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileTypeUtil.class);
    private static final Tika TIKA = new Tika();
    private static final Map<String, List<String>> MIME_TYPE_MAP;

    static {
        MIME_TYPE_MAP = new HashMap<>();
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(IoUtil.getInputStream("mime/tika-mimetypes.xml"));
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements("mime-type");

            for (Element element : elements) {
                String type = element.attributeValue("type");
                List<Element> globList = element.elements("glob");
                if (CollectionUtil.isNotEmpty(globList)) {
                    List<String> list = new ArrayList<>();
                    for (Element globEle : globList) {
                        String pattern = globEle.attributeValue("pattern");
                        list.add(pattern);
                    }
                    MIME_TYPE_MAP.put(type, list);
                }
            }
        } catch (DocumentException e) {
            LOGGER.error("DocumentException: ", e);
        }
    }

    /**
     * 获取文件真正的类型
     *
     * @param path path
     * @return 文件真正的类型
     */
    public static String getRealFileType(String path) {
        return getFileRealTypeByMagic(path);
    }

    /**
     * 获取文件的mimeType
     *
     * @param file file
     * @return 文件的mimeType
     */
    public static String getMimeType(File file) {
        if (file == null) {
            return null;
        }
        String mimeType = null;
        try {
            mimeType = TIKA.detect(file);
        } catch (IOException e) {
            LOGGER.info("{} getMimeType failed", file.getName());
        }
        return mimeType;
    }

    /**
     * 获取文件真正的类型
     *
     * @param inputStream inputStream
     * @return 文件真正的类型
     */
    public static String getRealFileType(InputStream inputStream) throws IOException {
        return getFileRealTypeByMagic(inputStream);
    }

    /**
     * 获取文件真正的类型
     *
     * @param file file
     * @return 文件真正的类型
     */
    public static String getRealFileType(File file) {
        return getFileRealTypeByMagic(file);
    }

    /**
     * 获取文件真正的类型
     *
     * @param bytes bytes
     * @return 文件真正的类型
     */
    public static String getRealFileType(byte[] bytes) {
        return getFileRealTypeByMagic(bytes);
    }

    /**
     * 通过jmimemagic 三方件类库获取文件真实的扩展名
     *
     * @param path path
     * @return 文件真实的扩展名（可能为null，该类库有些类型不能识别）
     */
    private static String getFileRealTypeByMagic(String path) {
        File file = FileUtils.getFile(path);
        return getFileRealTypeByMagic(file);
    }

    /**
     * 通过jmimemagic 三方件类库获取文件真实的扩展名
     *
     * @param file file
     * @return 文件真实的扩展名（可能为null，该类库有些类型不能识别）
     */
    private static String getFileRealTypeByMagic(File file) {
        try {
            if (file == null || !file.exists()) {
                LOGGER.info("file is null");
                return null;
            }

            MagicMatch magicMatch = Magic.getMagicMatch(file, true, false);
            if (magicMatch != null) {
                String mimeType = magicMatch.getMimeType();
                String extension = magicMatch.getExtension();
                LOGGER.info("file mimeType is {} extension is {}",
                        StringUtil.isEmpty(mimeType) ? "null" : mimeType,
                        StringUtil.isEmpty(extension) ? "null" : extension);
                if (StringUtil.isEmpty(extension) || extension.contains("?")) {
                    extension = getFileRealTypeByTika(file);
                }
                return extension;
            }
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException | IOException e) {
            LOGGER.error("exception: ", e);
        }
        LOGGER.info("MagicMatch is null");
        return null;
    }

    /**
     * 通过jmimemagic 三方件类库获取文件真实的扩展名
     *
     * @param inputStream inputStream
     * @return 文件真实的扩展名（可能为null，该类库有些类型不能识别）
     */
    private static String getFileRealTypeByMagic(InputStream inputStream) throws IOException {
        byte[] bytes = IoUtil.readInputStream(inputStream);
        return getFileRealTypeByMagic(bytes);
    }

    /**
     * 通过jmimemagic 三方件类库获取文件真实的扩展名
     *
     * @param bytes bytes
     * @return 文件真实的扩展名（可能为null，该类库有些类型不能识别）
     */
    private static String getFileRealTypeByMagic(byte[] bytes) {
        try {
            MagicMatch magicMatch = Magic.getMagicMatch(bytes, false);
            if (magicMatch != null) {
                String mimeType = magicMatch.getMimeType();
                String extension = magicMatch.getExtension();
                LOGGER.info("file mimeType is {} extension is {}",
                        StringUtil.isEmpty(mimeType) ? "null" : mimeType,
                        StringUtil.isEmpty(extension) ? "null" : extension);
                String extensionByTika = getFileRealTypeByTika(bytes);
                if (StringUtil.isEmpty(extension) || extension.contains("?") || !extension.equalsIgnoreCase(extensionByTika)) {
                    extension = extensionByTika;
                }
                return extension;
            }
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            LOGGER.error("exception: ", e);
        }
        LOGGER.info("MagicMatch is null");
        return null;
    }

    private static String getFileRealTypeByTika(byte[] bytes) {
        String mimeType = TIKA.detect(bytes);
        return handlerMimeType(mimeType);
    }

    private static String handlerMimeType(String mimeType) {
        LOGGER.info("mimeType:{}", mimeType);
        List<String> list = MIME_TYPE_MAP.get(mimeType);

        if (CollectionUtil.isEmpty(list)) {
            LOGGER.info("mimeTypeList: null");
            return null;
        }

        LOGGER.info("mimeTypeList: {}", list);
        return list.get(0).replace("*.", "");
    }

    /**
     * 获取文件的真实mimeType从而获取文件的真实扩展名
     *
     * @param path 文件路径
     * @return 文件的真实扩展名
     */
    private static String getFileRealTypeByTika(String path) throws IOException {
        File file = FileUtils.getFile(path);
        return getFileRealTypeByTika(file);
    }

    /**
     * 获取文件的真实mimeType从而获取文件的真实扩展名
     *
     * @param file 文件
     * @return 文件的真实扩展名
     */
    private static String getFileRealTypeByTika(File file) throws IOException {
        String mimeType = TIKA.detect(file);
        return handlerMimeType(mimeType);
    }
}
