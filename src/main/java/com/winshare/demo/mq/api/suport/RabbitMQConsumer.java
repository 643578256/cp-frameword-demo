package com.winshare.demo.mq.api.suport;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.vo.MessageVo;
import com.winshare.demo.mq.pro.MQConfigerPropertes;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumer  {
    public IMQConsumer initMQ(MQConfigerPropertes propertes) {
        ConnectionFactory factory = new ConnectionFactory();
        //propertes.getUrl().split("\\,")
        factory.setHost(propertes.getUrl());
        //factory.setPort(propertes.);
        factory.setVirtualHost("/");
        factory.setUsername(propertes.getUserName());
        factory.setPassword(propertes.getPassword());

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //channel.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

}
