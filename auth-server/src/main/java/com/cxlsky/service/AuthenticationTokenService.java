package com.cxlsky.service;

import com.cxlsky.constant.OauthConstanse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author CXL
 */
@Service
public class AuthenticationTokenService implements InitializingBean {

    @Autowired
    private volatile ObjectMapper objectMapper;
    @Autowired
    private volatile ClientDetailsService clientDetailsService;
    @Autowired
    private volatile AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authorizationServerTokenServices, "AuthorizationServerTokenServices 不能为空");
        Assert.notNull(clientDetailsService, "ClientDetailsService 不能为空");
        Assert.notNull(objectMapper, "ObjectMapper 不能为空");
    }


    public OAuth2AccessToken createAccessToken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 获得头部登录信息令牌
        String[] tokens = basicClientValidator(request);
        // client是否是非法Client
        ClientDetails clientDetails = clientValidator(tokens, request);

        // 创建Access Token
        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(), clientDetails.getClientId(),
                clientDetails.getScope(), request.getParameter(OauthConstanse.AUTH_TYPE_GRANT_TYPE));
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        return authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
    }

    /**
     * 说明：检测Basic登录时client是否是非法Client
     */
    private ClientDetails clientValidator(String[] tokens, HttpServletRequest request) {
        if (tokens.length != 2) {
            throw new AuthenticationServiceException("错误的客户端/密码");
        }
        String clientId = tokens[0];
        String clientSecret = tokens[1];
        // 使用 clientDetailsService 查询Client
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        // 比对Client
        if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new BadCredentialsException("错误的客户端/密码");
        }
        return clientDetails;
    }

    /**
     * 说明：检测Basic登录的头部信息
     */
    private String[] basicClientValidator(HttpServletRequest request) throws IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
        }
        // 提取Header中的客户端（client）账号和密码
        String[] tokens = extractAndDecodeHeader(header);
        if (tokens.length != 2) {
            throw new AuthenticationServiceException("错误的客户端/密码");
        }
        return tokens;
    }

    /**
     * Decodes the header into a username and password.
     *
     * @throws BadCredentialsException if the Basic header is not present or is not valid  Base64
     */
    private String[] extractAndDecodeHeader(String header) throws IOException {
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("解码基本令牌错误");
        }
        String token = new String(decoded, "UTF-8");
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("无效的基本令牌");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * 说明：写出数据
     */
    public void writeJsonResponse(Object object, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(object));
        printWriter.flush();
    }
}
