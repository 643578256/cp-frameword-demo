package com.winshare.demo.mq.api.vo;

import lombok.Data;

@Data
public class MessageVo<T> {
    private String msgId;
    private Boolean isSuccess;
    private T data;
    public static MessageVo returnVo(String msgId,Object data){
        MessageVo vo = new MessageVo();
        vo.setData(data);
        vo.setMsgId(msgId);
        vo.setIsSuccess(true);
        return vo;
    }
}
