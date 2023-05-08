package cn.lyf.tools.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author lyf
 * @description:
 * @version: v1.0
 * @since 2022-05-04 15:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssObject implements Serializable {
    private static final long serialVersionUID = -6999136637416403874L;
    private String objectName;

    /**
     * 文件在minio服务器中的路径
     */
    private String url;
    private String bucketName;
    private InputStream objectContent;
    private String md5;
}
