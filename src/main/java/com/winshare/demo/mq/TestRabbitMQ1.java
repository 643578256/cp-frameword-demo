package com.winshare.demo.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestRabbitMQ1 {
    // exchange名字
    public static String EXCHANGE_NAME = "topicExchange_cp";

    @SneakyThrows
    public static void main(String args) {



        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("132.232.20.136");
        factory.setPassword("123456");
        factory.setUsername("cp");
        factory.setPort(5672);
        Connection connection = null;
        Channel channel = null;
        try {
            // 1.创建连接和通道
            connection = factory.newConnection();
            channel = connection.createChannel();

            // 2.为通道声明topic类型的exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            //String queue = channel.queueDeclare().getQueue();

            // 3.发送消息到指定的exchange,队列指定为空,由exchange根据情况判断需要发送到哪些队列
            String routingKey = "cp.wo.de";
            //channel.queueBind(queue, EXCHANGE_NAME, "cp.wode");
//            String routingKey = "log4j.error";
//            String routingKey = "logback.error";
//            String routingKey = "log4j.warn";
            String msg = " hello rabbitmq, I am " + routingKey;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
            System.out.println("product send a msg: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 4.关闭连接

            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}