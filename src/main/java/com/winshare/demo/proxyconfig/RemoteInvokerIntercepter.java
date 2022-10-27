package com.winshare.demo.proxyconfig;

public interface RemoteInvokerIntercepter {
    //排序
    int sort = 0;

    /**
     * 升序排序
     * @return
     */
    default int getSort(){
        return sort;
    }

    //拦截调用方法
    void intercepter(HttpRequestDomain request);
}
