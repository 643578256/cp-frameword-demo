package com.winshare.demo.mq.api.consumer.rabbit;

import com.rabbitmq.client.*;
import com.winshare.demo.mq.api.consumer.IMessageProcessor;
import com.winshare.demo.mq.api.util.SerializableMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ExRabbitConsumer extends DefaultConsumer {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private IMessageProcessor processor;

    private Channel channel;

    private AtomicInteger retry ;
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public ExRabbitConsumer(Channel channel,IMessageProcessor processor) {
        super(channel);
        this.channel = channel;
        this.processor = processor;
        retry = new AtomicInteger(10);
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body)
            throws IOException
    {
        super.handleDelivery(consumerTag,envelope,properties,body);
        boolean isSuccsss = false;
        try{
            Object o = SerializableMessageUtil.deCode(body);
            isSuccsss = processor.processor(o);
        }catch (Exception ex){
            log.error("接收MQ消息处理异常，consumerTag：{},DeliveryTag：{}",consumerTag,envelope.getDeliveryTag());
        }
        if(isSuccsss){
            channel.basicAck(envelope.getDeliveryTag(),false);
        }else {
            if(retry.getAndAdd(1)>5){
                channel.basicReject(envelope.getDeliveryTag(),false);
                retry.set(1);
            }else{
                channel.basicNack(envelope.getDeliveryTag(),false,true);
            }
        }
    }
}
