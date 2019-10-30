package com.cxlsky.controller;

import com.cxlsky.mq.MessageProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/queue")
public class TestMqController {

    @Resource
    private MessageProcessor messageProcessor;

    @GetMapping("/one")
    public String one() {
        messageProcessor.output("测试队列");
        return "over";
    }
}