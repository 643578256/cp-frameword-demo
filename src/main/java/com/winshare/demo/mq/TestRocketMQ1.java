package com.winshare.demo.mq;

import com.winshare.demo.mq.api.producer.IMQProducer;
import com.winshare.demo.mq.api.producer.SendCallBack;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRocketMQ1 {

    @Autowired
    private IMQProducer template;

    @GetMapping("/send/mq/msg")
    public String sendTopicMessge(@RequestParam("topic") String topic, @RequestParam("tag") String tag, @RequestParam ("body") String body){

        template.sendAsyncMessage(topic,tag, "1",body, new SendCallBack() {
            @Override
            public <T> T onSuccess(Object message) {
                    System.out.println("============onSuccess===============");
                return (T)message;
            }
            @Override
            public void onException(Exception e, Object message) {
                System.out.println("============onException===============");
            }
        });
        return topic;
    }
}
