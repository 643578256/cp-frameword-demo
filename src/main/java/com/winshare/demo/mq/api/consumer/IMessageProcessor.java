package com.winshare.demo.mq.api.consumer;

public interface IMessageProcessor<T> {

    public boolean processor(T message);
}
