package com.winshare.demo.mq.api.suport;

import com.winshare.demo.mq.api.consumer.IMQConsumer;
import com.winshare.demo.mq.api.producer.IMQProducer;
import com.winshare.demo.mq.api.service.IMQService;
import com.winshare.demo.mq.api.util.MQServiceFactory;
import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = MQConfigerPropertes.class)
public class MQServiceConfig {

    @Autowired
    private MQConfigerPropertes propertes;


    @Bean(name = "iMQService")
    public IMQService createService(){
       return MQServiceFactory.createComsumer(propertes);
    }

    @Bean(name = "iMQConsumer")
    public IMQConsumer createConsumer(IMQService service){
        return service.createConsumer();
    }

    @Bean(name = "iMQProducer")
    public IMQProducer createProducer(IMQService service){
        return service.createProducer();
    }


}
