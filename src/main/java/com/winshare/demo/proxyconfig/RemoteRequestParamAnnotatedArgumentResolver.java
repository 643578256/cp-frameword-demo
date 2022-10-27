package com.winshare.demo.proxyconfig;


import java.lang.reflect.Parameter;

/**
 * @RemoteRequestParam注解标注解析器
 */
public class RemoteRequestParamAnnotatedArgumentResolver extends AbstractMethodArgumentResolver {
    @Override
    public boolean isSupport(Parameter parameter) {
        return isSupportType(parameter.getParameterizedType()) && parameter.isAnnotationPresent(RemoteRequestParam.class);
    }

    @Override
    public MethodParameter resolve(Parameter parameter) {
        String value = parameter.getAnnotation(RemoteRequestParam.class).value();
        return new MethodParameter(value, (Class<?>) parameter.getParameterizedType(), ParameterAnnotationType.REMOTE_REQUEST_PARAM);
    }
}
