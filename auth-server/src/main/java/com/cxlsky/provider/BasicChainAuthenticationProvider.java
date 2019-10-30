package com.cxlsky.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

/**
 * 说明：一个基本的链式结构AuthenticationProvider
 *
 * @author CXL
 */
public class BasicChainAuthenticationProvider implements AuthenticationProvider {

    /**
     * 说明：保存要使用的 AuthenticationProvider（非线程安全的）
     * */
    private final List<AuthenticationProvider> authenticationProviders;

    public BasicChainAuthenticationProvider(List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

    /**
     * 说明：使用保存的 AuthenticationProvider 进行验证
     * */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticate = null;
        for (AuthenticationProvider provider : authenticationProviders) {
            if (provider.supports(authentication.getClass())) {
                authenticate = provider.authenticate(authentication);
                break;
            }
        }
        return authenticate;
    }

    /**
     * 说明：判断 AuthenticationProvider 是否支持改Token
     * */
    @Override
    public boolean supports(Class<?> authentication) {
        for (AuthenticationProvider provider : authenticationProviders) {
            if (provider.supports(authentication))
                return true;
        }
        return false;
    }

    /**
     * 说明：返回一个AuthenticationProvider
     * */
    public List<AuthenticationProvider> getAuthenticationProviders() {
        return authenticationProviders;
    }
}
