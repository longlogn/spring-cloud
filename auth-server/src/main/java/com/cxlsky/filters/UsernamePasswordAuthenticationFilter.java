package com.cxlsky.filters;

import com.cxlsky.constant.OauthConstanse;
import com.cxlsky.token.UsernamePasswordAuthToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CXL
 */
@Component
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationFilter {

    public UsernamePasswordAuthenticationFilter() {
        super(null);
    }

    public UsernamePasswordAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication preHandle(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 当请求grant_type里面有【password】才进行处理,否则执行下下一个过滤器
        if (request.getParameter(OauthConstanse.AUTH_TYPE_GRANT_TYPE) == null
                || !request.getParameter(OauthConstanse.AUTH_TYPE_GRANT_TYPE).equalsIgnoreCase(OauthConstanse.PASSWORD_GRANT_TYPE)) {
            chain.doFilter(request, response);
            return null;
        }
        String username = obtainParameter(request, OauthConstanse.USERNAME_KEY);
        String password = obtainParameter(request, OauthConstanse.PASSWORD_KEY);
        UsernamePasswordAuthToken usernamePasswordAuthToken = new UsernamePasswordAuthToken(username, password);
        setDetails(request, usernamePasswordAuthToken);
        // 验证手机验证码
        return authenticationManager.authenticate(usernamePasswordAuthToken);
    }


    private String obtainParameter(HttpServletRequest request, String parameter) {
        String parameterResult = request.getParameter(parameter);
        if (StringUtils.isEmpty(parameterResult)) {
            return "";
        }
        return parameterResult;
    }

}
