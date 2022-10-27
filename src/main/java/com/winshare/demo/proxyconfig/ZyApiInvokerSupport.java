package com.winshare.demo.proxyconfig;


public abstract class ZyApiInvokerSupport {
    protected ZyApiInvoker zyApiInvoker;


    public void setZyApiInvoker(ZyApiInvoker zyApiInvoker) {
        this.zyApiInvoker = zyApiInvoker;
    }
}
