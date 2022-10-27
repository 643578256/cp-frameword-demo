package com.winshare.demo.mq.api.producer.rabbit;

public class RabbitMQBaseService {
    protected boolean durable = true;
    protected int prefetchCount = 1;
    protected boolean autoAck = false;
}
