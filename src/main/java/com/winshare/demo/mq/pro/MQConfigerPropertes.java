package com.winshare.demo.mq.pro;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mq.broker")
@Data
public class MQConfigerPropertes {
    public final static String  RABBIT_MQ = "rabbit";
    public final static String  ROCKET_MQ ="rocket";
    private String url;
    private Integer port;
    private String host;
    private String userName;
    private String password;
    private String mqType;
    private String producerGroup;
    private String nameServer;
}
