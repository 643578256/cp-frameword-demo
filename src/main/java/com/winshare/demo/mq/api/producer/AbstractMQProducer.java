package com.winshare.demo.mq.api.producer;

import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AbstractMQProducer implements IMQProducer{

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected MQConfigerPropertes configerPropertes;
    public AbstractMQProducer(MQConfigerPropertes configerPropertes){
        this.configerPropertes = configerPropertes;
    }

    private volatile int retrey = 3;

    public void createProducer(){
           boolean hasConnection =  connectionMQ();
           if(hasConnection && retrey > 3){
               try {
                   TimeUnit.SECONDS.sleep(5);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               createProducer();
               retrey++;
           }
    }

    public abstract boolean connectionMQ();
}
