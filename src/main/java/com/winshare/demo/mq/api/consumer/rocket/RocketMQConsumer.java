package com.winshare.demo.mq.api.consumer.rocket;

import com.winshare.demo.mq.api.consumer.AbstractMQConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.suport.rocket.RocketFactory;
import com.winshare.demo.mq.api.util.SerializableMessageUtil;
import com.winshare.demo.mq.api.vo.MessageVo;
import com.winshare.demo.mq.pro.MQConfigerPropertes;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RocketMQConsumer extends AbstractMQConsumer implements IRocketMQConsumer {


    private static final Map<IMessageProcessor,Class> mapClass = new HashMap<>();

    public RocketMQConsumer(MQConfigerPropertes configerPropertes) {
        super(configerPropertes);
    }

    @Override
    protected void connection() {
        throw new RuntimeException("不支持");
    }


    @Override
    public MessageVo receiveMessage(String topic, String tag, IMessageProcessor processor) {
        receiveMessage(topic,tag,"consumer_group_"+topic,MessageModel.CLUSTERING,processor);
        return null;
    }

    @Override
    public MessageVo receiveMessage(String topic, String tag, String consumerGroup, IMessageProcessor processor) {

        receiveMessage(topic,tag,consumerGroup,MessageModel.CLUSTERING,processor);
        return null;
    }

    @Override
    public MessageVo receiveMessage(String topic, String tag, String consumerGroup, MessageModel messageModel, IMessageProcessor processor) {
       /* Type genericSuperclass = processor.getClass().getGenericSuperclass();
        Class<? extends Type> aClass = genericSuperclass.getClass();
        mapClass.put(processor,aClass);*/
        if(StringUtils.isEmpty(consumerGroup)){
            consumerGroup = "consumer_group_"+topic;
        }
        DefaultMQPushConsumer consumer = RocketFactory.getConsumer(super.configerPropertes);
        try {
            consumer.setConsumerGroup(consumerGroup);
            consumer.subscribe(topic,tag);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.setMessageModel(messageModel);
        consumer.setMessageListener(new MessageListenerConcurrently(){
            @SneakyThrows
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt :  msgs) { //size == 0
                    byte[] body = messageExt.getBody();
                    Object o = SerializableMessageUtil.deCode(body);
                    boolean processor1 = processor.processor(o);
                    if(processor1){
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return null;
            }
        });
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("启动消费失败topic=%s,tag=%s",topic,tag));
        }
        return null;
    }

    @Override
    public MessageVo receiveMessageForOrder(String topic, String tag, String consumerGroup, IMessageProcessor processor) {
        receiveMessage(topic,tag,consumerGroup,MessageModel.CLUSTERING,processor);
        return null;
    }

}
