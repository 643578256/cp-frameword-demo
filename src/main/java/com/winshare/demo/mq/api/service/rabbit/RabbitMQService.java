package com.winshare.demo.mq.api.service.rabbit;

import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.consumer.rabbit.IRabbitConsumer;
import com.winshare.demo.mq.api.consumer.rabbit.RabbitMQConsumer;
import com.winshare.demo.mq.api.producer.IMQProducer;
import com.winshare.demo.mq.api.producer.rabbit.RabbitMQProducer;
import com.winshare.demo.mq.api.service.IMQService;
import com.winshare.demo.mq.api.service.MQBaseService;

public class RabbitMQService extends MQBaseService implements IMQService {


    @Override
    public IRabbitConsumer createConsumer() {
        RabbitMQConsumer rabbitMQConsumer = new RabbitMQConsumer(propertes);
        rabbitMQConsumer.createConsumer();
        return rabbitMQConsumer;
    }

    @Override
    public IMQProducer createProducer() {
        RabbitMQProducer rabbitMQProducer = new RabbitMQProducer(propertes);
        rabbitMQProducer.createProducer();
        return rabbitMQProducer;
    }
}
