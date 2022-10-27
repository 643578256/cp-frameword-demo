package com.winshare.demo.mq;

import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.suport.MQConsumer;
import org.springframework.stereotype.Component;

@MQConsumer(topic = "demo_topic2",routingKey = "demo_tag2")
@Component
public class TestRocketMQListener2 implements IMessageProcessor<String> {

    @Override
    public boolean processor(String message) {

        System.out.println("============processor22222==============="+message);
        return true;
    }
}
