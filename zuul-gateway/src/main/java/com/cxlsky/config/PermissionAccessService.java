package com.cxlsky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口权限控制
 */
@Service
@Slf4j
//@EnableConfigurationProperties(value = {ApiUrlProperties.class})
public class PermissionAccessService {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

//    @Autowired
//    private ApiUrlProperties apiUrlProperties;
//
//
//    @Autowired
//    private UserAuthService userAuthService;

    /**
     * 根据接口地址判断是否有权限
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            if (log.isDebugEnabled()) {
//                log.debug("======= user not  login , return 403 code =============");
//            }
//            return false;
//        }
//        if (!authentication.isAuthenticated()) {
//            if (log.isDebugEnabled()) {
//                log.debug("=======  authentication not authenticated , return 403 code =============");
//            }
//            return false;
//        }
//        if (apiUrlProperties.isAuth() && authentication.isAuthenticated() && authentication instanceof OAuth2Authentication) {
////            log.info("userAuthService is null?{},authentication is null ? {}", Objects.isNull(userAuthService), Objects.isNull(authentication));
////            List<String> authUrls = userAuthService.getUrlsByUser((IntegrationUser) authentication.getPrincipal());
//            Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
//            if(userAuthentication instanceof IntegrationAuthenticationToken){
//                List<RequestUrl> userAuthUrls = userAuthService.getUserAuthUrls((IntegrationAuthorizationUser) userAuthentication.getDetails());
//                if (log.isDebugEnabled()) {
//                    log.debug("user is :" + authentication.getPrincipal());
//                    log.debug("user authorities :{}", userAuthUrls);
//                    log.debug("request uri:{} request method :{}", request.getRequestURI(), request.getMethod());
//                }
//
//                for (RequestUrl requestUrl : userAuthUrls) {
//                    boolean match = antPathMatcher.match("/" + requestUrl.getApplicationName() + requestUrl.getUrl(), request.getRequestURI());
//                    if (match && request.getMethod().equalsIgnoreCase(requestUrl.getMethod())) {
//                        if (log.isDebugEnabled()) {
//                            log.debug("=======  request matched , request will be continue =============");
//                        }
//                        return true;
//                    }
//                }
//
//            }
//        }
//        if (log.isDebugEnabled()) {
//            log.debug("=======  request not matched , return 403 code =============");
//        }
        return false;
    }

}
