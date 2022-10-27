package com.winshare.demo.mq.api.service.rocket;

import com.winshare.demo.mq.api.consumer.IMQConsumer;

import com.winshare.demo.mq.api.consumer.rocket.IRocketMQConsumer;
import com.winshare.demo.mq.api.consumer.rocket.RocketMQConsumer;
import com.winshare.demo.mq.api.producer.IMQProducer;
import com.winshare.demo.mq.api.producer.rocket.RocketMQProducer;
import com.winshare.demo.mq.api.service.IMQService;
import com.winshare.demo.mq.api.service.MQBaseService;

public class RocketMQService extends MQBaseService implements IMQService {

    @Override
    public IRocketMQConsumer createConsumer() {
        RocketMQConsumer rocketMQConsumer = new RocketMQConsumer(propertes);
        return rocketMQConsumer;
    }

    @Override
    public IMQProducer createProducer() {
        RocketMQProducer rabbitMQProducer = new RocketMQProducer(propertes);
        rabbitMQProducer.createProducer();
        return rabbitMQProducer;
    }
}
