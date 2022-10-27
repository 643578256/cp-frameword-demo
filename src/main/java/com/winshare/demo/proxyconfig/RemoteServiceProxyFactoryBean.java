package com.winshare.demo.proxyconfig;

import org.springframework.beans.factory.FactoryBean;

/**
 * 代理类通过FactoryBean,getObject
 */
public class RemoteServiceProxyFactoryBean<T> extends ZyApiInvokerSupport implements FactoryBean<T> {

    private Class<T> proxyClz;

    public RemoteServiceProxyFactoryBean(Class<T> proxyClz) {
        this.proxyClz = proxyClz;
    }

    @Override
    public T getObject() throws Exception {
        return new RemoteServiceProxy<>(zyApiInvoker, proxyClz).newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return proxyClz;
    }

}
