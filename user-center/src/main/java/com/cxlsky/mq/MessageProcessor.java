package com.cxlsky.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableBinding(StreamMessageChanel.class)
public class MessageProcessor {

    @Resource
    private StreamMessageChanel streamMessageChanel;

    @StreamListener(StreamMessageChanel.MY_INPUT)
//    @SendTo(StreamMessageChanel.MY_OUTPUT)
//    public String input(Message<String> message) {
    public void input(Message<String> message) {
        try {
            System.out.println("接收到消息！！！！！！！");
            System.out.println(message.getPayload());
        } catch (Exception e) {
            System.out.println("error");
        }
//        return "开始大循环。。。";
    }

    public boolean output(String data) {
        System.out.println("发送消息-------------------");
        return streamMessageChanel.sendMessage().send(MessageBuilder.withPayload(data).build());
    }

}