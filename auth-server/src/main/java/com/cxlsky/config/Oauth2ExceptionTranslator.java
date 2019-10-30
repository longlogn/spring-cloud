package com.cxlsky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author cxl
 */
@Component
@Slf4j
public class Oauth2ExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        if (e instanceof InvalidGrantException) {
            OAuth2Exception oAuth2Exception = new OAuth2Exception("账号或密码不正确");
            return ResponseEntity.badRequest().body(oAuth2Exception);
        }
        return super.translate(e);
    }

}
