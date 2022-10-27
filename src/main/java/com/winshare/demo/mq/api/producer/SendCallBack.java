package com.winshare.demo.mq.api.producer;

public interface SendCallBack {
    public <T> T onSuccess(Object mmessage);

    public void onException(Exception e,Object message);
}
