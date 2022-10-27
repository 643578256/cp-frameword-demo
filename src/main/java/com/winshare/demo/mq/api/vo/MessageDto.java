package com.winshare.demo.mq.api.vo;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageDto<T> {
    private String msgId;
    private String topic;
    private Long delayTime = 0L;
    private String routingKey;
    private String queueName;
    private T data;

    public MessageDto(String topic, String routingKey,String queueName,long delayTime, T data) {
        this.msgId = UUID.randomUUID().toString();
        this.topic = topic;
        this.queueName =queueName;
        this.delayTime = delayTime;
        this.routingKey = routingKey;
        this.data = data;
    }
}
