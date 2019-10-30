package com.cxlsky.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CXL
 */
@RestController
@RequestMapping("/gateway")
public class IndexController {

    @Value("${spring.cloud.config.profile}")
    private String profile;

    @GetMapping("/index")
    public String index() {
        return "Hello Gateway! Current Env is : " + profile;
    }
}
