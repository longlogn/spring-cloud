package com.cxlsky.filters;

import com.cxlsky.constant.OauthConstanse;
import com.cxlsky.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CXL
 */
public abstract class AbstractAuthenticationFilter extends GenericFilterBean {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private RequestMatcher requestMatcher;

    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected volatile AuthenticationTokenService authenticationTokenService;

    public AbstractAuthenticationFilter(RequestMatcher requestMatcher) {
        if (requestMatcher == null) {
            this.requestMatcher = new OrRequestMatcher(new AntPathRequestMatcher(OauthConstanse.OAUTH_TOKEN_URL, "GET"),
                    new AntPathRequestMatcher(OauthConstanse.OAUTH_TOKEN_URL, "POST"));
        } else {
            this.requestMatcher = requestMatcher;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (requestMatcher.matches(request)) {
            try {
                Authentication authentication = preHandle(request, response, filterChain);
                postHandle(request, response, authentication);
            } catch (Exception e) {
                afterThrowing(request, response, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 前置处理
     *
     * @param request
     * @param response
     * @param chain
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public abstract Authentication preHandle(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    /**
     * 后置处理
     *
     * @param request
     * @param response
     * @param preHandleAuthentication
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Authentication preHandleAuthentication) throws IOException, ServletException {
        if (preHandleAuthentication != null) {
            OAuth2AccessToken accessToken = createAccessToken(request, response, preHandleAuthentication);
            authenticationTokenService.writeJsonResponse(accessToken, response);
        }
    }

    /**
     * 异常处理
     *
     * @param request
     * @param response
     * @param throwable
     * @throws IOException
     * @throws ServletException
     */
    private void afterThrowing(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws IOException, ServletException {
        try {
            throwable.printStackTrace();
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("state", false);
            stringObjectMap.put("msg", throwable.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            authenticationTokenService.writeJsonResponse(stringObjectMap, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private OAuth2AccessToken createAccessToken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AccessToken accessToken;
        try {
            accessToken = authenticationTokenService.createAccessToken(request, response, authentication);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return accessToken;
    }
}
