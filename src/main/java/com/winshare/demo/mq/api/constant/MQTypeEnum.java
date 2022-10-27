package com.winshare.demo.mq.api.constant;


public enum MQTypeEnum {
    RABBIT_MQ("rabbit","RabbitMQ"),
    ROCKET_MQ("rocket","RocketMQ");

    private String code;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private String desc;


    MQTypeEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
