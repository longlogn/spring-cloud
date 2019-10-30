package com.cxlsky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author CXL
 */
@Data
@ConfigurationProperties(prefix = "api.url")
@Configuration
public class ApiUrlProperties {

    private List<String> permitAll;
    private boolean auth = true;

    private Map<String, List<String>> application;
}
