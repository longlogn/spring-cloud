package com.cxlsky.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author CXL
 */
@ConfigurationProperties(prefix = "spring.oauth2.wechat")
@Data
public class WechatLoginProperties {

    @NestedConfigurationProperty
    private WechatCommonProperties trucker;
}
