package com.winshare.demo.rest;

import com.winshare.demo.mq.api.producer.IMQProducer;
import com.winshare.demo.mq.api.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class FaMessage {

    @Autowired
    private IMQProducer producer;

    @GetMapping("/cp/fa")
    public void faMessage(){
        MessageVo vo = producer.sendSingleMessage("pay_info_direct", "pay.person.direct", new Person("cp", 22));
        System.out.println(vo);
    }

    @GetMapping("/cp/fa1")
    public void faMessage1(){
        MessageVo vo = producer.sendMessage("pay_info_topic", "pay.person.test",null, new Person("pay.person.test", 22));
        MessageVo vo1 = producer.sendMessage("pay_info_topic", "pay.person.two", null,new Person("pay.person.two", 22));
        System.out.println(vo);
    }
}
