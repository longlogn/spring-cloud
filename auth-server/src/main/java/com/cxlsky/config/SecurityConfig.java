package com.cxlsky.config;

import com.cxlsky.provider.BasicChainAuthenticationProvider;
import com.cxlsky.provider.UsernamePasswordAuthProvider;
import com.cxlsky.provider.WeChatAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * className SecurityConfig
 * description SecurityConfig
 *
 * @author cxl
 * @date 2018-9-6
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(-1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    @Qualifier("UserDetailServiceImpl")
    private UserDetailsService userDetailService;
    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
    @Autowired
    private WeChatAuthenticationProvider weChatAuthenticationProvider;

    /**
     * 认证管理器
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * BCrypt密码加密解密
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 放开option方式的请求，解决跨域问题
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers(HttpMethod.OPTIONS, "/**").and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BasicChainAuthenticationProvider chainAuthenticationProvider
                = new BasicChainAuthenticationProvider(Arrays.asList(usernamePasswordAuthProvider, weChatAuthenticationProvider));

        auth.userDetailsService(userDetailService)
                .and()
                .authenticationProvider(chainAuthenticationProvider);
    }

    /**
     * token使用redis方式存储
     *
     * @return
     */
    @Bean
    public TokenStore redisTokenStore () {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 客户端服务
     * @return
     */
    @Bean(name = "jdbcClientDetailsService")
    public ClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }
}
