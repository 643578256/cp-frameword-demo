package com.winshare.demo.mq.api.producer.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.winshare.demo.mq.api.util.SerializableMessageUtil;
import com.winshare.demo.mq.api.vo.MessageDto;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RabbitMessageService {
    protected final boolean durable = true;
    protected final int prefetchCount = 1;
    protected final boolean autoAck = false;
    protected final boolean autoDelete = false;

    protected String exchange_fix = "exchange_";

    protected String exchange_type;
    protected Channel channel;


    public void sendMessage(MessageDto message){
        try {
            String exchangeName = exchange_fix+message.getTopic();
            /*if(message.getDelayTime() > 0){
                exchangeName = "delay."+exchangeName;
            }*/
            buidExchange(exchangeName,message.getRoutingKey(),message.getDelayTime());
            if(!StringUtils.isEmpty(message.getQueueName())){
                declareQueue(message.getQueueName());
                bindQueue(message.getQueueName(),exchangeName,message.getRoutingKey());
            }
            AMQP.BasicProperties props;
            byte[] bytes = SerializableMessageUtil.enCode(message.getData());
            if (message.getDelayTime() > 0L) {
                props = (new AMQP.BasicProperties.Builder()).contentEncoding("UTF-8").messageId(message.getMsgId()).deliveryMode(2).expiration(String.valueOf(message.getDelayTime() * 1000L)).build();
            } else {
                props = (new AMQP.BasicProperties.Builder()).contentEncoding("UTF-8").messageId(message.getMsgId()).deliveryMode(2).build();
            }
            this.channel.basicPublish(exchangeName, message.getRoutingKey(), props, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void buidExchange(String exchangeName,String routingKey,long delayTime) throws IOException {
        if(delayTime > 0 ){
            Map<String,Object> args = new HashMap();
            args.put("x-dead-letter-exchange", exchangeName);
            args.put("x-dead-letter-routing-key", routingKey);
            channel.exchangeDeclare(exchangeName,exchange_type,durable,autoDelete,args);
        }else {
            channel.exchangeDeclare(exchangeName,exchange_type,durable);
        }
    }

    public void declareQueue(String queueName) throws IOException {
        channel.queueDeclare(queueName,durable,false,autoDelete,null);
    }

    public void bindQueue(String queueName,String exchangeName,String routingKey) throws IOException {
        channel.queueBind(queueName,exchangeName,routingKey);
    }



}
