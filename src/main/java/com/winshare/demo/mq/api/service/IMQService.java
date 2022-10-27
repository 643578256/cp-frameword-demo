package com.winshare.demo.mq.api.service;

import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.producer.IMQProducer;

public interface IMQService {

    public IMQConsumer createConsumer();

    public IMQProducer createProducer();

}
