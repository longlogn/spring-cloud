package com.cxlsky.constant;

public class OauthConstanse {

    /**
     * 登录方式
     */
    public static final String AUTH_TYPE_GRANT_TYPE = "grant_type";

    /**
     * 拦截路径
     */
    public static final String OAUTH_TOKEN_URL = "/oauth/token";

    /**
     * 手机登录
     */
    public static final String MOBILE_GRANT_TYPE = "mobile";
    public static final String PHONE_KEY = "telephone";
    public static final String SMS_KEY = "sms_code";

    /**
     * 账号密码登陆
     */
    public static final String PASSWORD_GRANT_TYPE = "password";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    /**
     * 微信登录
     */

    public static final String WECHAT_GRANT_TYPE = "wechat";

    public static final String WECHAT_CODE_KEY = "code";
    public static final String WECHAT_SOURCE = "source";

    public static final String APPID = "appid";
    public static final String OPENID = "openid";
    public static final String SECRET = "secret";
    public static final String JS_CODE = "js_code";
    public static final String WX_APP_SECRET = "e60264983411f04bb7ae831f3a1d8d89";
    public static final String WX_APP_ID = "wx6231e01938f46b99";
    public static final String WX_GRANT_TYPE = "authorization_code";
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
}
