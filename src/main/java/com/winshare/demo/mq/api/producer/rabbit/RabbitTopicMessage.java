package com.winshare.demo.mq.api.producer.rabbit;

import com.rabbitmq.client.Channel;

public class RabbitTopicMessage extends RabbitMessageService {

    public RabbitTopicMessage(Channel channel){
        super.channel = channel;
        super.exchange_type = "topic";
    }
}
