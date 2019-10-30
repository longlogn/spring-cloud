package com.cxlsky.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StreamMessageChanel {


    String MY_OUTPUT = "test_out_put";
    String MY_INPUT = "test_in_put";

    @Output(StreamMessageChanel.MY_OUTPUT)
    MessageChannel sendMessage();

    @Input(StreamMessageChanel.MY_INPUT)
    SubscribableChannel dealMessage();

}