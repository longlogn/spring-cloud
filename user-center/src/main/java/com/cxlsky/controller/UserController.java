package com.cxlsky.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CXL
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("test")
    public String test() {
        return "hello";
    }
}
