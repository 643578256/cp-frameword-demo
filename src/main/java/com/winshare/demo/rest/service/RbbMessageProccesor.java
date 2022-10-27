package com.winshare.demo.rest.service;

import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.suport.MQConsumer;
import com.winshare.demo.rest.Person;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
//@MQConsumer(topic = "pay_info_direct",queueName = "pay.person.direct")
public class RbbMessageProccesor implements IMessageProcessor<Person> {
    @Override
    public boolean processor(Person message) {
        System.out.println(message.toString());
        System.out.println(new Date().getTime()+"------------------");
        return true;
    }
}
