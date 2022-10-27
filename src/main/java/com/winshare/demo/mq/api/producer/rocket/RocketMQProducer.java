package com.winshare.demo.mq.api.producer.rocket;

import com.winshare.demo.mq.api.producer.AbstractMQProducer;
import com.winshare.demo.mq.api.producer.SendCallBack;
import com.winshare.demo.mq.api.suport.rocket.RocketFactory;
import com.winshare.demo.mq.api.util.SerializableMessageUtil;
import com.winshare.demo.mq.api.vo.MessageVo;
import com.winshare.demo.mq.pro.MQConfigerPropertes;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.DisposableBean;


/**
 * spring 中直接注入 IMQProducer 类即可
 * @see com.winshare.demo.mq.api.producer.IMQProducer
 *
 */
public class RocketMQProducer extends AbstractMQProducer implements DisposableBean {

    private static DefaultMQProducer producer;

    private MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();

    public RocketMQProducer(MQConfigerPropertes configerPropertes) {
        super(configerPropertes);
    }
    @Override
    public boolean connectionMQ() {
        try{
            producer = RocketFactory.getProducer(super.configerPropertes);
            producer.setProducerGroup(super.configerPropertes.getProducerGroup());
            producer.setNamesrvAddr(super.configerPropertes.getNameServer());
            producer.start();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public MessageVo sendSingleMessage(String topic, String tag, Object message) {
        byte[] bytes = SerializableMessageUtil.enCode(message);
        Message msg = new Message(topic,tag,bytes);
        SendResult send = null;
        try {
             send = producer.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败topic="+topic+"  tag="+tag);
        }
        return MessageVo.returnVo(send.getMsgId(),message);
    }

    /**
     *
     * @param topic
     * @param tag
     * @param message
     * @param delayTime 只支持固定的几个 延迟时间段
     * @return
     */
    @Override
    public MessageVo sendSingleMessage(String topic, String tag, Object message, int delayTime) {
        byte[] bytes = SerializableMessageUtil.enCode(message);
        Message msg = new Message(topic,tag,bytes);
        SendResult send = null;
        try {
            msg.setDelayTimeLevel(delayTime);
            send = producer.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败topic="+topic+"  tag="+tag);
        }
        return MessageVo.returnVo(send.getMsgId(),message);
    }

    @Override
    public MessageVo sendAsyncSingleMessage(String topic, String tag, Object message, int delayTime, SendCallBack sendCallBack) {
        byte[] bytes = SerializableMessageUtil.enCode(message);
        Message msg = new Message(topic,tag,bytes);
        SendResult send = null;
        try {
            if(delayTime  > 0){
                msg.setDelayTimeLevel(delayTime);
            }
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    sendCallBack.onSuccess(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    sendCallBack.onException(new Exception(e),null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败topic="+topic+"  tag="+tag);
        }
        return null;
    }

    @Override
    public MessageVo sendMessage(String topic, String tag, String queueName, Object message) {
        byte[] bytes = SerializableMessageUtil.enCode(message);
        Message msg = new Message(topic,tag,bytes);
        SendResult send = null;
        try {
            send = producer.send(msg, messageQueueSelector,queueName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败topic="+topic+"  tag="+tag);
        }
        return MessageVo.returnVo(send.getMsgId(),message);
    }

    @Override
    public MessageVo sendMessage(String topic, String tag, String queueName, Object message, int delayTime) {
        byte[] bytes = SerializableMessageUtil.enCode(message);
        Message msg = new Message(topic,tag,bytes);
        SendResult send = null;
        try {
            if(delayTime > 0){
                msg.setDelayTimeLevel(delayTime);
            }
            send = producer.send(msg, messageQueueSelector,queueName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败topic="+topic+"  tag="+tag);
        }
        return MessageVo.returnVo(send.getMsgId(),message);
    }

    /**
     *
     * @param topic
     * @param tag
     * @param queueName  用来选择的queueName
     * @see SelectMessageQueueByHash  里的 Object，一般使用 消息id作为取 hash
     * @param message
     * @param sendCallBack
     * @return
     */
    @Override
    public MessageVo sendAsyncMessage(String topic, String tag, String queueName, Object message, SendCallBack sendCallBack) {
        byte[] bytes = SerializableMessageUtil.enCode(message);
        Message msg = new Message(topic,tag,bytes);

        try {
            producer.send(msg, messageQueueSelector, queueName, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    sendCallBack.onSuccess(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    sendCallBack.onException(new Exception(e),null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败topic="+topic+"  tag="+tag);
        }
        return null;
    }

    @Override
    @Deprecated
    public MessageVo sendAsyncMessage(String topic, String tag, String queueName, Object message, int delayTime, SendCallBack sendCallBack) {
        return null;
    }

    @Override
    @Deprecated
    public MessageVo sendBroadcastMessage(String topic, String tag, Object message, int delayTime) {
        return null;
    }

    @Override
    @Deprecated
    public MessageVo sendAsyncBroadcastMessage(String topic, String tag, Object message, int delayTime, SendCallBack sendCallBack) {
        return null;
    }

    @Override
    public void destroy() throws Exception {
        producer.shutdown();
    }
}
