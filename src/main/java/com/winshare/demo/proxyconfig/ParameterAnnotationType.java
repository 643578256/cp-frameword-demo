package com.winshare.demo.proxyconfig;

/**
 * 参数注解类型
 */
public enum ParameterAnnotationType {
    /**
     * 占位符
     */
    PLACE_HOLDER,
    /**
     * 远程调用请求参数
     */
    REMOTE_REQUEST_PARAM,
    /**
     * 远程调用请求体
     */
    REMOTE_REQUEST_BODY,
    /**
     * 非注解
     */
    NON_ANNOTATION;
}
