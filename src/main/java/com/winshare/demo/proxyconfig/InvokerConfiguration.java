package com.winshare.demo.proxyconfig;

import java.util.Objects;

public class InvokerConfiguration {
    private String defaultRealm;
    private final MethodParameterParser methodParameterParser;

    public InvokerConfiguration() {
        this.methodParameterParser = new MethodParameterParser();
    }

    public String getDefaultRealm() {
        return defaultRealm;
    }

    public void setDefaultRealm(String defaultRealm) {
        this.defaultRealm = defaultRealm;
    }

    public void addMethodArgumentResolver(MethodArgumentResolver argumentResolver) {
        if (Objects.nonNull(argumentResolver)) {
            this.methodParameterParser.addMethodArgumentResolver(argumentResolver);
        }
    }

    public MethodParameterParser getMethodParameterParser() {
        return methodParameterParser;
    }
}
