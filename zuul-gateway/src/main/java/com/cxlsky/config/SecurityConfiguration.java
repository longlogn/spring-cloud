package com.cxlsky.config;

import com.cxlsky.properties.ApiUrlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableWebSecurity
@EnableConfigurationProperties(ApiUrlProperties.class)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    ApiUrlProperties apiUrlProperties;

    @Autowired
    ConsulDiscoveryClient consulDiscoveryClient;

    /**
     * 服务如果不在consul 的注册列表的话就会报403【待优化】
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        List<RequestMatcher> antPathMatchers = new ArrayList<>();
        for (String url : apiUrlProperties.getPermitAll()) {
            antPathMatchers.add(new AntPathRequestMatcher(url));
        }
        Map<String, List<String>> application = apiUrlProperties.getApplication();
        for (String app : application.keySet()) {
            List<ServiceInstance> instances = consulDiscoveryClient.getInstances(app);
            if (CollectionUtils.isEmpty(instances)) {
                log.warn("Service 【{}】 is unavaliable, please start application first", app);
            }
            List<String> urls = application.get(app);
            for (String url : urls) {
                antPathMatchers.add(new AntPathRequestMatcher("/" + app + url));
            }
        }
        antPathMatchers.add(new AntPathRequestMatcher("/**", RequestMethod.OPTIONS.name()));
        web.ignoring().requestMatchers(new OrRequestMatcher(antPathMatchers));
    }
}
