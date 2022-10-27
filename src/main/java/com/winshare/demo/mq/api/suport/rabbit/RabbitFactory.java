package com.winshare.demo.mq.api.suport.rabbit;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class RabbitFactory {

    public static ConnectionFactory getConnectFacatory(MQConfigerPropertes configerPropertes){
        ConnectionFactory  factory = new ConnectionFactory();
        if(StringUtils.isEmpty(configerPropertes.getUrl())){
            factory.setPort(configerPropertes.getPort());
            factory.setHost(configerPropertes.getHost());
        }
        factory.setUsername(configerPropertes.getUserName());
        factory.setPassword(configerPropertes.getPassword());
        factory.setVirtualHost("/");

        return factory;
    }

    public static Connection getConnect(ConnectionFactory factory,MQConfigerPropertes configerPropertes) throws IOException, TimeoutException {
        if (!StringUtils.isEmpty(configerPropertes.getUrl())) {
            String[] split = configerPropertes.getUrl().split("\\;");
            Assert.isNull(split, "rabbitmq 链接地址不正确");
            List<String> urls = Arrays.asList(split);
            List<Address> listAddress = new ArrayList<>();
            urls.stream().forEach(url -> {
                String[] u = url.split("\\:");
                Address address = new Address(u[0], Integer.parseInt(u[1]));
                listAddress.add(address);
            });
           return  factory.newConnection(listAddress);
        }
        return factory.newConnection();
    }
}
