package com.winshare.demo.mq.api.consumer.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.winshare.demo.mq.api.consumer.AbstractMQConsumer;
import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.suport.rabbit.RabbitFactory;
import com.winshare.demo.mq.api.vo.MessageVo;
import com.winshare.demo.mq.pro.MQConfigerPropertes;


public class RabbitMQConsumer extends AbstractMQConsumer implements IRabbitConsumer {
    private ConnectionFactory connectFacatory;

    private Connection connection;

    private Channel channel;
    public RabbitMQConsumer(MQConfigerPropertes configerPropertes) {
        super(configerPropertes);
    }

    @Override
    protected void connection() {
        try{
            if(connectFacatory == null){
                connectFacatory = RabbitFactory.getConnectFacatory(configerPropertes);
            }
            connection = RabbitFactory.getConnect(connectFacatory,configerPropertes);
            channel = connection.createChannel();

        }catch (Exception ex){
            log.error("RabbitMQConsumer#链接rabbitMQ异常",ex);
        }
    }

    @Override
    public MessageVo receiveMessage(String topic,String queueName, IMessageProcessor processor) {
        RabbitReceiveOpration opration = new RabbitReceiveOpration(processor,channel);
        String s = opration.directMessageReceive(topic, queueName);
        return MessageVo.returnVo(s,null);
    }

    @Override
    public MessageVo receiveMessage(String topic, String routingKey, String queueName, IMessageProcessor processor) {
        RabbitReceiveOpration opration = new RabbitReceiveOpration(processor,channel);
        String s = opration.topicMessageReceive(topic, routingKey,queueName);
        return MessageVo.returnVo(s,null);
    }

}
