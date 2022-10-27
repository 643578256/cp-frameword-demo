package com.winshare.demo.mq.api.producer;

import com.winshare.demo.mq.api.vo.MessageVo;

public interface IMQProducer {

    /**
     *
     * @param queueName 队列名，没有roukeing queueName就是一个一对一的路由key
     * @param message
     * @return
     */
    MessageVo sendSingleMessage(String topic,String queueName,Object message);

    MessageVo sendSingleMessage(String topic,String queueName,Object message,int delayTime);

    MessageVo sendAsyncSingleMessage(String topic,String queueName,Object message,int delayTime,SendCallBack sendCallBack);

    MessageVo sendMessage(String topic,String routingKey,String queueName,Object message);

    MessageVo sendMessage(String topic,String routingKey,String queueName,Object message,int delayTime);

    MessageVo sendAsyncMessage(String topic,String routingKey,String queueName,Object message,SendCallBack sendCallBack);

    MessageVo sendAsyncMessage(String topic,String routingKey,String queueName,Object message,int delayTime,SendCallBack sendCallBack);

    MessageVo sendBroadcastMessage(String topic,String routingKey,Object message,int delayTime);

    MessageVo sendAsyncBroadcastMessage(String topic,String routingKey,Object message,int delayTime,SendCallBack sendCallBack);

}
