package com.winshare.demo.mq.api.producer.rabbit;

import com.rabbitmq.client.Channel;

public class RabbitDirectMessage extends RabbitMessageService {

    public RabbitDirectMessage(Channel channel){
        super.exchange_type = "direct";
        super.channel = channel;
    }



}
