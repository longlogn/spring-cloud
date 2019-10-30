package com.cxlsky.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/weChat")
@RestController
public class WeChatController {

    @Value("${weflag.mini-app.app-id}")
    private String wechatAppId;
    @Value("${weflag.mini-app.app-secret}")
    private String wechatAppSecret;

    private static final String WECHAT_OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/info")
    public JSONObject getWeChatInfoByCode(String code) {
        return getWeChatSessionResponse(code);
    }

    private JSONObject getWeChatSessionResponse(String code) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("appid", wechatAppId);
        params.put("secret", wechatAppSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        log.info("get wx_openid code: {} ", code);
        String forObject = restTemplate.getForObject(WECHAT_OPENID_URL, String.class, params);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        String openId = jsonObject.getString("openid");
        if (StringUtils.isEmpty(openId)) {
            log.error("get openId failed, url: {}, request: {}, response: {}", WECHAT_OPENID_URL, params, forObject);
        }
        return jsonObject;
    }


}
