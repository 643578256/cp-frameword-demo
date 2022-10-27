package com.winshare.demo.proxyconfig;

import java.io.Serializable;


public class Result<T> implements Serializable {
    private static final long serialVersionUID = -7698281555825270908L;
    /**失败*/
    public static final Integer ERROR = 500;
    /**成功*/
    public static final Integer OK = 200;
    /**操作成功 返回*/
    public static final Result<String> SUCCESS = new Result<>(null, OK, "操作成功！");
    /**操作失败 返回*/
    public static final Result<String> FAILED = new Result<>(null, ERROR, "操作失败！");


    private T data;
    private Integer code;
    private String msg;

    public Result() {
    }

    public Result(T data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> succeed() {
        return succeedWith(null, OK, null);
    }

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, OK, msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, OK, msg);
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model, OK, "");
    }

    public static <T> Result<T> succeedWith(T datas, Integer code, String msg) {
        return new Result<>(datas, code, msg);
    }
    public static <T> Result<T> failed(String msg) {
        return failedWith(null, ERROR, msg);
    }

    public static <T> Result<T> ofFailed(Integer code, String msg) {
        return failedWith(null, code, msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, ERROR, msg);
    }

    public static <T> Result<T> failedWith(T datas, Integer code, String msg) {
        return new Result<>(datas, code, msg);
    }

    public static <T> Result<T> copyFrom(Result result){
        return new Result(result.getData(), result.getCode(), result.getMsg());
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
