package com.winshare.demo.mq.api.consumer;

import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractMQConsumer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected MQConfigerPropertes configerPropertes;

    public AbstractMQConsumer(MQConfigerPropertes configerPropertes){
        this.configerPropertes = configerPropertes;
    }

    public void createConsumer() {
        connection();
    }

    protected abstract void connection();


}
