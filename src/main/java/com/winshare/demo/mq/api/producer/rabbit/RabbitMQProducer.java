package com.winshare.demo.mq.api.producer.rabbit;

import com.rabbitmq.client.*;
import com.winshare.demo.mq.api.producer.AbstractMQProducer;
import com.winshare.demo.mq.api.producer.SendCallBack;
import com.winshare.demo.mq.api.suport.rabbit.RabbitFactory;
import com.winshare.demo.mq.api.vo.MessageDto;
import com.winshare.demo.mq.api.vo.MessageVo;
import com.winshare.demo.mq.pro.MQConfigerPropertes;


import java.io.IOException;
import java.util.concurrent.*;

public class RabbitMQProducer extends AbstractMQProducer implements ShutdownListener {

    private volatile Connection connection;
    private volatile Channel channel;
    private ConnectionFactory factory;
    private ExecutorService executors;

    public RabbitMQProducer(MQConfigerPropertes configerPropertes) {
        super(configerPropertes);
        executors = new ThreadPoolExecutor(3, 10, 30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }


    private MessageDto sendDirectMessage(String topic, String routingKey, Object meesage,int delayTime){
        MessageDto dto = new MessageDto(topic,routingKey,null,delayTime,meesage);
        checkChannel();
        RabbitDirectMessage send = new RabbitDirectMessage(channel);
        send.sendMessage(dto);
        return dto;
    }
    private MessageDto sendTopicMessage(String topic, String routingKey,String queueName, Object meesage,int delayTime){
        MessageDto dto = new MessageDto(topic,routingKey,queueName,delayTime,meesage);
        checkChannel();
        RabbitTopicMessage send = new RabbitTopicMessage(channel);
        send.sendMessage(dto);
        return dto;
    }
    private void checkChannel(){
        if(connection == null || !connection.isOpen()){
            connectionMQ();
        }
        if(channel == null || !channel.isOpen()){
            try {
                channel = connection.createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean connectionMQ() {
        try {
            if (factory == null) {
                factory = RabbitFactory.getConnectFacatory(configerPropertes);
            }
            connection = RabbitFactory.getConnect(factory,configerPropertes);
            channel = connection.createChannel();

        } catch (Exception ex) {
            log.error("链接rabbitMQ异常");
        }
        return true;
    }

    public void shutdownCompleted(ShutdownSignalException e) {
        try{
            log.error("rabbitmq 链接发生异常，异常信息:",e);
            if(connection != null && connection.isOpen()){
                connection.close();
            }
            if(channel != null && channel.isOpen()){
                channel.close();
            }
        }catch (Exception ex){
            log.error("rabbitmq 关闭发生异常，异常信息:",ex);
        }
    }


    @Override
    public MessageVo sendSingleMessage(String topic, String queueName, Object message) {
        MessageDto dto = sendDirectMessage(topic, queueName, message, 0);
        return MessageVo.returnVo(dto.getMsgId(),message);
    }

    @Override
    public MessageVo sendSingleMessage(String topic, String queueName, Object message, int delayTime) {
        MessageDto dto = sendDirectMessage(topic, queueName, message, delayTime);
        return MessageVo.returnVo(dto.getMsgId(),message);
    }

    @Override
    public MessageVo sendAsyncSingleMessage(String topic, String queueName, Object message, int delayTime, SendCallBack sendCallBack) {
        Callable<MessageDto> callable = ()->{
            try{
                MessageDto dto = sendDirectMessage(topic, queueName, message, 0);
                if(sendCallBack != null){
                    sendCallBack.onSuccess(message);
                }
                return dto;
            }catch (Exception ex){
                if(sendCallBack != null){
                    sendCallBack.onException(ex,message);
                }
                log.error("发送消息异常：topic:{},queueName:{},message:{}",topic,queueName,message);
            }
            return null;
        };
        Future<MessageDto> submit = executors.submit(callable);
        return MessageVo.returnVo(null,message);
    }

    @Override
    public MessageVo sendMessage(String topic, String routingKey,String queueName, Object message) {
        MessageDto dto = sendTopicMessage(topic, routingKey,queueName, message, 0);
        return MessageVo.returnVo(dto.getMsgId(),message);
    }

    @Override
    public MessageVo sendMessage(String topic, String routingKey,String queueName, Object message, int delayTime) {
        MessageDto dto = sendTopicMessage(topic, routingKey,queueName, message, delayTime);
        return MessageVo.returnVo(dto.getMsgId(),message);
    }

    @Override
    public MessageVo sendAsyncMessage(String topic, String routingKey,String queueName, Object message, SendCallBack sendCallBack) {
        Callable<MessageDto> callable = ()->{
            try{
                MessageDto dto = sendTopicMessage(topic, routingKey,queueName, message, 0);
                if(sendCallBack != null){
                    sendCallBack.onSuccess(message);
                }
                return dto;
            }catch (Exception ex){
                if(sendCallBack != null){
                    sendCallBack.onException(ex,message);
                }
                log.error("发送消息异常：topic:{},queueName:{},message:{}",topic,routingKey,message);
            }
            return null;
        };
        Future<MessageDto> submit = executors.submit(callable);
        return MessageVo.returnVo(null,message);
    }

    @Override
    public MessageVo sendAsyncMessage(String topic, String routingKey,String queueName, Object message, int delayTime, SendCallBack sendCallBack) {
        Callable<MessageDto> callable = ()->{
            try{
                MessageDto dto = sendTopicMessage(topic, routingKey,queueName,  message, delayTime);
                if(sendCallBack != null){
                    sendCallBack.onSuccess(message);
                }
                return dto;
            }catch (Exception ex){
                if(sendCallBack != null){
                    sendCallBack.onException(ex,message);
                }
                log.error("发送消息异常：topic:{},queueName:{},message:{}",topic,routingKey,message);
            }
            return null;
        };
        Future<MessageDto> submit = executors.submit(callable);
        return MessageVo.returnVo(null,message);
    }


    @Override
    public MessageVo sendBroadcastMessage(String topic, String routingKey, Object message, int delayTime) {
        return null;
    }

    @Override
    public MessageVo sendAsyncBroadcastMessage(String topic, String routingKey, Object message, int delayTime, SendCallBack sendCallBack) {
        return null;
    }
}
