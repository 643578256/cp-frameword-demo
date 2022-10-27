package com.winshare.demo.rest.service;

import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.suport.MQConsumer;
import com.winshare.demo.rest.Person;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
//@MQConsumer(topic = "pay_info_topic",routingKey = "pay.person.#",queueName = "pay.person.one")
public class RbbMessageProccesorOne implements IMessageProcessor<Person> {
    @Override
    public boolean processor(Person message) {
        System.out.println(message.toString());
        System.out.println(new Date().getTime()+"------------------");
        return true;
    }
}
