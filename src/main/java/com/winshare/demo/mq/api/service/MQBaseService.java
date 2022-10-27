package com.winshare.demo.mq.api.service;

import com.winshare.demo.mq.pro.MQConfigerPropertes;

public abstract class MQBaseService implements IMQService {

    protected MQConfigerPropertes propertes;

    public void initMQConfig(MQConfigerPropertes propertes) {
        this.propertes = propertes;
    }
}
