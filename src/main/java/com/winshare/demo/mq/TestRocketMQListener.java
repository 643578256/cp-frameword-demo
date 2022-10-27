package com.winshare.demo.mq;

import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.producer.IMQProducer;
import com.winshare.demo.mq.api.producer.SendCallBack;
import com.winshare.demo.mq.api.suport.MQConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@MQConsumer(topic = "demo_topic",routingKey = "demo_tag")
@Component
public class TestRocketMQListener implements IMessageProcessor<String> {

    @Override
    public boolean processor(String message) {

        System.out.println("============processor==============="+message);
        return true;
    }
}
