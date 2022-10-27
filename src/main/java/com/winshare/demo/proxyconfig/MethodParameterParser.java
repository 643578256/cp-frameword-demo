package com.winshare.demo.proxyconfig;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 方法参数解析器
 */
public class MethodParameterParser {
    private final List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();

    public MethodParameterParser() {
        this.argumentResolvers.add(new RemoteRequestParamAnnotatedArgumentResolver());
        this.argumentResolvers.add(new RemoteRequestBodyAnnotatedArgumentResolver());
    }

    public void addMethodArgumentResolver(MethodArgumentResolver argumentResolver) {
        if (Objects.nonNull(argumentResolver)) {
            this.argumentResolvers.add(argumentResolver);
        }
    }

    public List<MethodArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }

    public MethodParameter parse(Parameter parameter) {
        for (MethodArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver.isSupport(parameter)) {
                return argumentResolver.resolve(parameter);
            }
        }
        return null;
    }
}
