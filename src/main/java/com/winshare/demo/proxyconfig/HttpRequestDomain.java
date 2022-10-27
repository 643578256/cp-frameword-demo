package com.winshare.demo.proxyconfig;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestDomain {

    private final Map<String, String> heards = new HashMap();

    private String REQUEST_URL;

    private Object param;

    public HttpRequestDomain(String REQUEST_URL, Object param) {
        this.REQUEST_URL = REQUEST_URL;
        this.param = param;
    }

    public HttpRequestDomain addHeard(String name, String value) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("heard的name=" + name + "或value" + value + "不能为空");
        }
        heards.put(name, value);
        return this;
    }

    public Map heards() {
        return heards;
    }

    public String getCurrentUrl() {
        final String currentUrl = REQUEST_URL;
        return currentUrl;
    }

    public Object getParam() {
        Object currentParam = null;
        try {
            currentParam = param.getClass().newInstance();
            BeanUtils.copyProperties(param,currentParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentParam;
    }
}
