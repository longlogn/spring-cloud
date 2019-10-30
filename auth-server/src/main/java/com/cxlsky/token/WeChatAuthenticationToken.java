package com.cxlsky.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class WeChatAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final String source;

    /**
     * 未认证的token
     *
     * @param principal 微信openid
     * @param source    来源：标记不同的微信小程序端客户端
     */
    public WeChatAuthenticationToken(String principal, String source) {
        super(null);
        this.principal = principal;
        this.source = source;
        setAuthenticated(false);
    }

    /**
     * 认证的token
     *
     * @param principal   用户信息
     * @param source      来源
     * @param authorities 权限
     */
    public WeChatAuthenticationToken(Object principal, String source, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.source = source;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getSource() {
        return this.source;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
