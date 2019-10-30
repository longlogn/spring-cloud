package com.cxlsky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author CXL
 */
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class AliyunOssProperties {
    /**
     * aliyun oss accessKeyId
     */
    private String id;
    /**
     * 请求加密串
     */
    private String secret;
    /**
     * bucket name
     */
    private String bucket;
    /**
     * schema
     */
    private String endpoint;
    /**
     * 前缀
     */
    private String suffix;
}
