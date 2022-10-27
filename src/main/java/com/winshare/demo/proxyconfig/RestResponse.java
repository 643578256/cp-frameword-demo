package com.winshare.demo.proxyconfig;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 响应包装实体
 */
@Getter
@Setter
public class RestResponse<T> implements Serializable {
    public static final String SUCCESS_CODE = "0";
    public static final String FAIL_CODE = "10000";
    public static final String SUCCESS_MESSAGE = "success";
    public static final RestResponse SUCCESS=new RestResponse(SUCCESS_CODE,SUCCESS_MESSAGE);


    private String resultCode;
    private String resultMsg;
    private final T data;

    public RestResponse(T data) {
        this.resultCode = SUCCESS_CODE;
        this.resultMsg = SUCCESS_MESSAGE;
        this.data = data;
    }

    public RestResponse(String responseMsg, String responseCode) {
        this.resultMsg = responseMsg;
        this.resultCode = responseCode;
        this.data = null;
    }

    public RestResponse(String responseMsg, String responseCode, T data) {
        this.resultMsg = responseMsg;
        this.resultCode = responseCode;
        this.data = data;
    }
    public boolean isSuccess(){
        return SUCCESS_CODE.equals(resultCode);
    }

}
