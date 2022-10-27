package com.winshare.demo.mq.api.util;

import com.winshare.demo.mq.api.constant.MQTypeEnum;
import com.winshare.demo.mq.api.service.IMQService;
import com.winshare.demo.mq.api.service.rabbit.RabbitMQService;
import com.winshare.demo.mq.api.service.rocket.RocketMQService;
import com.winshare.demo.mq.api.suport.rocket.RocketFactory;
import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

public class MQServiceFactory {

    public static IMQService createComsumer(MQConfigerPropertes propertes){
        if(MQTypeEnum.RABBIT_MQ.getCode().equals(propertes.getMqType())){
            RabbitMQService rabbitMQService = new RabbitMQService();
            rabbitMQService.initMQConfig(propertes);
            return rabbitMQService;
        }else if(MQTypeEnum.ROCKET_MQ.getCode().equals(propertes.getMqType())){
            RocketMQService rocketMQService = new RocketMQService();
            rocketMQService.initMQConfig(propertes);
            return rocketMQService;
        }
        return null;
    }
}
