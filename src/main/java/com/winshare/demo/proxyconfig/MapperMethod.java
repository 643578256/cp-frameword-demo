package com.winshare.demo.proxyconfig;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MapperMethod {
    //请求方式
    private final HttpRequestType httpRequestType;

    private final MethodParam methodParam;


    public MapperMethod(Method method, MethodParameterParser parameterParser) {
        this.httpRequestType = getHttpRequestType(method);
        this.methodParam = new MethodParam(method, parameterParser);
    }


    private HttpRequestType getHttpRequestType(Method method) {
        if (!method.isAnnotationPresent(RemoteUrl.class)) {
            throw new IllegalArgumentException(method + "缺少RemoteUrl注解标注");
        }
        return method.getAnnotation(RemoteUrl.class).requestType();
    }

    /**
     * 执行http请求
     *
     * @param zyApiInvoker
     * @param args
     * @return
     */
    public Object execute(ZyApiInvoker zyApiInvoker, String remoteRealm, Object[] args) {
        Object param = null;
        String url = UrlHelper.getUrl(remoteRealm, httpRequestType, methodParam, args);
        if (httpRequestType == HttpRequestType.POST || httpRequestType == HttpRequestType.PUT || httpRequestType == HttpRequestType.DELETE) {
            Integer index = methodParam.getRemoteRequestBodyMethodParameterArgIndex();
            if (Objects.nonNull(index)) {
                param = args[index];
            }
        }

        String respStr = zyApiInvoker.httpRequest(httpRequestType, url, param);
        Object result = null;
        if (!StringUtils.isEmpty(respStr)) {
            result = JSON.parseObject(respStr, methodParam.getReturnType());
        }
        return result;
    }


    static class UrlHelper {
        static String getUrl(String realm, HttpRequestType httpRequestType, MethodParam methodParam, Object[] args) {
            StringBuilder urlBuilder = new StringBuilder(realm);
            if (!realm.endsWith("/")) {
                urlBuilder.append("/");
            }
            if (args.length > 0) {
                Map<String, Object> placeHolderNameValue = new HashMap<>();
                Map<String, Object> paramNameValueMap = new HashMap<>();
                for (int i = 0; i < args.length; i++) {
                    MethodParameter methodParameter = methodParam.getMethodParameterMap().get(i);
                    if (ParameterAnnotationType.PLACE_HOLDER == methodParameter.getAnnotationType()) {
                        placeHolderNameValue.put(methodParameter.getParameterName(), args[i]);
                    }
                    if (HttpRequestType.GET == httpRequestType) {
                        if (ParameterAnnotationType.REMOTE_REQUEST_PARAM == methodParameter.getAnnotationType()) {
                            paramNameValueMap.put(methodParameter.getParameterName(), args[i]);

                        }
                        if (ParameterAnnotationType.NON_ANNOTATION == methodParameter.getAnnotationType()) {
                            Object obj = args[i];
                            if (obj != null) {
                                Class<?> clz = methodParameter.getParameterType();
                                while (!Object.class.equals(clz)) {
                                    Field[] declaredFields = clz.getDeclaredFields();
                                    for (Field declaredField : declaredFields) {
                                        declaredField.setAccessible(true);
                                        try {
                                            Object val = declaredField.get(obj);
                                            String fieldName = declaredField.getName();
                                            if (Objects.nonNull(val)) {
                                                if (isRawType(val)) {
                                                    paramNameValueMap.put(fieldName, val);
                                                } else {
                                                    paramNameValueMap.put(fieldName, JSON.toJSON(val));
                                                }
                                            }

                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    clz = clz.getSuperclass();
                                }
                            }
                        }
                    }
                }
                for (UrlSegment urlSegment : methodParam.getUrlSegments()) {
                    if (urlSegment.isNeedReplace()) {
                        if (placeHolderNameValue.containsKey(urlSegment.getSegmentName())) {
                            urlBuilder.append(placeHolderNameValue.get(urlSegment.getSegmentName()));
                        } else {
                            throw new RuntimeException(methodParam.getMethodInfo() + "{" + urlSegment.getSegmentName() + "}" + "并没有@PlceHolder标注的注解与其关联");
                        }
                    } else {
                        urlBuilder.append(urlSegment.getSegmentName());
                    }
                    urlBuilder.append("/");
                }
                if (paramNameValueMap.size() > 0) {
                    urlBuilder.append("?");
                    paramNameValueMap.forEach((name, value) -> urlBuilder.append(name).append("=").append(value).append("&"));
                    urlBuilder.deleteCharAt(urlBuilder.length() - 1);
                }

            }
            return urlBuilder.toString();
        }

        static boolean isRawType(Object obj) {
            return obj instanceof String || obj instanceof Character ||
                    obj instanceof Number || obj.getClass().isPrimitive();
        }
    }


}

