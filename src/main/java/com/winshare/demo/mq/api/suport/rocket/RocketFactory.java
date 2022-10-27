package com.winshare.demo.mq.api.suport.rocket;

import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;


public class RocketFactory {

    public synchronized static DefaultMQPushConsumer getConsumer(MQConfigerPropertes configerPropertes){
        DefaultMQPushConsumer factory = new DefaultMQPushConsumer();
        factory.setNamesrvAddr(configerPropertes.getNameServer());
        return factory;
    }

    public synchronized static DefaultMQProducer getProducer(MQConfigerPropertes configerPropertes){
        DefaultMQProducer factory = new TransactionMQProducer();
        factory.setProducerGroup(configerPropertes.getProducerGroup());
        factory.setNamesrvAddr(configerPropertes.getNameServer());
        return factory;
    }
}
