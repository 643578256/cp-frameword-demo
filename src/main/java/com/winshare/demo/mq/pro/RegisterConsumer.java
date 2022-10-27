package com.winshare.demo.mq.pro;


import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.consumer.rabbit.IRabbitConsumer;
import com.winshare.demo.mq.api.consumer.rocket.IRocketMQConsumer;
import com.winshare.demo.mq.api.suport.MQConsumer;
import com.winshare.demo.rest.FaMessage;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class RegisterConsumer implements ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {
    private ApplicationContext applicationContext;

    @Autowired
    private IMQConsumer consumer;

    @Autowired
    private MQConfigerPropertes configerPropertes;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("----------------------------onApplicationEvent");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @PostConstruct
    public void buidConsumer() {
        //rabbitMQ     rocketMQ
        System.out.println("--------PostConstruct--------------------buidConsumer");
        Map<String, IMessageProcessor> beansOfType = applicationContext.getBeansOfType(IMessageProcessor.class);
        if(MQConfigerPropertes.ROCKET_MQ.equals(configerPropertes.getMqType())){
            IRocketMQConsumer rocketConsumer =  (IRocketMQConsumer)consumer;
            if (beansOfType != null && beansOfType.size() > 0) {
                beansOfType.values().forEach(processor -> {
                    MQConsumer annotation = processor.getClass().getAnnotation(MQConsumer.class);
                    if(annotation != null){

                        String topic = annotation.topic();
                        String consumerGroup = annotation.consumerGroup();
                        String orderConsumer = annotation.orderConsumer();
                        String tag = annotation.routingKey();
                        String messageModel = annotation.messageModel();
                        if(StringUtils.isEmpty(tag)){
                           throw new RuntimeException("routingKey 标识不能为空 ");
                        }
                        if(!StringUtils.isEmpty(orderConsumer)){
                            rocketConsumer.receiveMessageForOrder(topic,tag,consumerGroup,processor);
                        }else {
                            if(StringUtils.isEmpty(messageModel)){
                                rocketConsumer.receiveMessage(topic,tag,consumerGroup,processor);
                            }else {
                                rocketConsumer.receiveMessage(topic,tag,consumerGroup, MessageModel.valueOf(messageModel),processor);
                            }

                        }
                    }
                });
            }
        }else if(MQConfigerPropertes.RABBIT_MQ.equals(configerPropertes.getMqType())){
            IRabbitConsumer rabbitConsumer =  (IRabbitConsumer)consumer;
            if (beansOfType != null && beansOfType.size() > 0) {
                beansOfType.values().forEach(processor -> {
                    MQConsumer annotation = processor.getClass().getAnnotation(MQConsumer.class);
                    if(annotation != null){
                        String queueName = annotation.queueName();
                        String topic = annotation.topic();
                        String routingKey = annotation.routingKey();
                        if(StringUtils.isEmpty(routingKey)){
                            rabbitConsumer.receiveMessage(topic,queueName,processor);
                        }else {
                            rabbitConsumer.receiveMessage(topic,routingKey,queueName,processor);
                        }

                    }
                });
            }
        }
    }
}
