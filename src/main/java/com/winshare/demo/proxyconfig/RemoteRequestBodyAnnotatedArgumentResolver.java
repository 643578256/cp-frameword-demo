package com.winshare.demo.proxyconfig;


import java.lang.reflect.Parameter;

/**
 * @RemoteRequestBody注解标注参数解析器
 */
public class RemoteRequestBodyAnnotatedArgumentResolver extends AbstractMethodArgumentResolver {
    @Override
    public boolean isSupport(Parameter parameter) {
        return !isSupportType(parameter.getParameterizedType()) && parameter.isAnnotationPresent(RemoteRequestBody.class);
    }

    @Override
    public MethodParameter resolve(Parameter parameter) {
        return new MethodParameter(parameter.getName(), (Class<?>) parameter.getParameterizedType(), ParameterAnnotationType.REMOTE_REQUEST_BODY);
    }
}
