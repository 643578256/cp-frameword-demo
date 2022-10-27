package com.winshare.demo.mq.api.consumer.rabbit;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;

import java.io.IOException;

public class RabbitReceiveOpration {

    private IMessageProcessor processor;

    private String exchange_fix = "exchange_";

    private Channel channel;


    public RabbitReceiveOpration(IMessageProcessor processor,Channel channel){
        this.processor = processor;
        this.channel = channel;
    }

    /*public void newInstance(){
        if(opration == null){
            synchronized (RabbitReceiveOpration.class){
                if(opration == null){
                    opration = new RabbitReceiveOpration();
                }
            }
        }
    }*/

    public String directMessageReceive(String exchange, String queueName){
        String exchangeName = exchange_fix+exchange;
        try {
            channel.basicQos(1);
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,true,false,null);
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName,exchangeName,queueName);
            DefaultConsumer consumer = new ExRabbitConsumer(channel,processor);
            return channel.basicConsume(queueName,false,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String topicMessageReceive(String exchange,String routigKey, String queueName){
        String exchangeName = exchange_fix+exchange;
        try {
            channel.basicQos(1);
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,null);
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName,exchangeName,routigKey);
            DefaultConsumer consumer = new ExRabbitConsumer(channel,processor);
            return channel.basicConsume(queueName,false,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
