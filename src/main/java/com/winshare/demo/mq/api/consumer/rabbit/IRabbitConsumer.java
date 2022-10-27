package com.winshare.demo.mq.api.consumer.rabbit;

import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.vo.MessageVo;

public interface IRabbitConsumer extends IMQConsumer  {
    MessageVo receiveMessage(String topic,String queueName, IMessageProcessor processor);

    MessageVo receiveMessage(String topic,String routingKey,  String queueName, IMessageProcessor processor);
}
