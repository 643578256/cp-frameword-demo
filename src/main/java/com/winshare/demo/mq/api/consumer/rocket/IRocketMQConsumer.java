package com.winshare.demo.mq.api.consumer.rocket;

import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.vo.MessageVo;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public interface IRocketMQConsumer extends IMQConsumer  {
    MessageVo receiveMessage(String topic, String tag, IMessageProcessor processor);

    MessageVo receiveMessage(String topic, String tag, String consumerGroup, IMessageProcessor processor);

    MessageVo receiveMessage(String topic, String tag, String consumerGroup, MessageModel messageModel, IMessageProcessor processor);

    MessageVo receiveMessageForOrder(String topic, String tag, String consumerGroup, IMessageProcessor processor);
}
