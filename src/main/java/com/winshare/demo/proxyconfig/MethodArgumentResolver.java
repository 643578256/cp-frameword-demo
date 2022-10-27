package com.winshare.demo.proxyconfig;

import java.lang.reflect.Parameter;

/**
 * 方法参数Resolver
 */
public interface MethodArgumentResolver {
    boolean isSupport(Parameter parameter);

    MethodParameter resolve(Parameter parameter);

}
