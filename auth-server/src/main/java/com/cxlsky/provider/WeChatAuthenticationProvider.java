package com.cxlsky.provider;

import com.cxlsky.token.WeChatAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author cxl
 */
@Component
public class WeChatAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    @Qualifier("UserDetailServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WeChatAuthenticationToken weChatAuthenticationToken = (WeChatAuthenticationToken) authentication;
        String loginCode = (String) weChatAuthenticationToken.getPrincipal();
        String source = weChatAuthenticationToken.getSource();
        if (StringUtils.isEmpty(loginCode)) {
            throw new BadCredentialsException("微信未授权，登录失败");
        }
        if (StringUtils.isEmpty(source)) {
            throw new BadCredentialsException("来源为空，登录失败");
        }
//        String openid = getOpenid(loginCode);
        String openid = "cxl";
        if (StringUtils.isEmpty(openid)) {
            throw new BadCredentialsException("获取微信信息失败");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(openid);
        WeChatAuthenticationToken authenticationToken = new WeChatAuthenticationToken(userDetails, source, userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeChatAuthenticationToken.class.isAssignableFrom(authentication);
    }

//    private String getOpenid(String loginCode) {
//        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
//        requestParams.add(APPID, WX_APP_ID);
//        requestParams.add(SECRET, WX_APP_SECRET);
//        requestParams.add(AUTH_TYPE_GRANT_TYPE, WX_GRANT_TYPE);
//        requestParams.add(JS_CODE, loginCode);
//        String res = HttpUtil.httpGet(WX_LOGIN_URL, requestParams, String.class);
//        JSONObject jsonObject = JSONObject.parseObject(res);
//        return jsonObject.getString(OPENID);
//
//    }

}
