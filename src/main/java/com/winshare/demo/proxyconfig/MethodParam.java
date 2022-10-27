package com.winshare.demo.proxyconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

/**
 * parameter信息
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class MethodParam {


    //url
    private final List<UrlSegment> urlSegments;


    //返回值外部类型
    private final Type returnType;

    //方法信息，方法名称参数
    private final String methodInfo;

    //参数
    private final Map<Integer, MethodParameter> methodParameterMap;


    public MethodParam(Method method, MethodParameterParser parameterParser) {
        this.urlSegments = creatUrlSegmentsFromMethod(method);
        this.returnType = method.getGenericReturnType();
        this.methodParameterMap = parseMethodParameter(method, parameterParser);
        this.methodInfo = method.toGenericString();

    }

    private Map<Integer, MethodParameter> parseMethodParameter(Method method, MethodParameterParser parameterParser) {
        Map<Integer, MethodParameter> parameterMap = null;
        Parameter[] parameters = method.getParameters();
        if (Objects.nonNull(parameters) && parameters.length > 0) {
            parameterMap = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                MethodParameter methodParameter = parameterParser.parse(parameters[i]);
                if (Objects.nonNull(methodParameter)) {
                    parameterMap.put(i, methodParameter);
                }
            }
        }

        return parameterMap;
    }

    /*private Class<?> getMethodReturnParameterType(Method method) {
        Class<?> parameterType = null;
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            parameterType = (Class<?>) actualTypeArguments[0];
        }
        return parameterType;
    }*/

    private List<UrlSegment> creatUrlSegmentsFromMethod(Method method) {
        String url = method.getAnnotation(RemoteUrl.class).url();
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        String[] urlSplits = url.split("/");
        List<UrlSegment> urlSegmentList = new ArrayList<>(urlSplits.length);
        for (String urlSplit : urlSplits) {
            UrlSegment urlSegment = new UrlSegment();
            if (urlSplit.startsWith("{") && urlSplit.endsWith("}")) {
                urlSegment.setNeedReplace(true);
                urlSplit = urlSplit.substring(1, urlSplit.length() - 1);
            }
            urlSegment.setSegmentName(urlSplit);
            urlSegmentList.add(urlSegment);
        }
        return urlSegmentList;
    }


    /**
     * 获取参数下标
     * @return
     */
    public Integer getRemoteRequestBodyMethodParameterArgIndex() {
        Integer index=null;
        for (Map.Entry<Integer, MethodParameter> entry : methodParameterMap.entrySet()) {
            if (entry.getValue().getAnnotationType() == ParameterAnnotationType.REMOTE_REQUEST_BODY) {
                index= entry.getKey();
            }
        }
        return index;
    }

}
