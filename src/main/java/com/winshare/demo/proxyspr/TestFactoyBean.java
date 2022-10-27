package com.winshare.demo.proxyspr;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestFactoyBean implements FactoryBean<Object> {

    private Class<?> interfaceClass;

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Request request = new Request.Builder().post( RequestBody.create(MediaType.parse( "application/json"), JSON.toJSONString(args[0]))).url("http://localhost:8080/v1").build();

                OkHttpClient build = new OkHttpClient().newBuilder().callTimeout(3000, TimeUnit.SECONDS).
                        build();

                Response execute = build.newCall(request).execute();
                String resp = null;
                if (Objects.nonNull(execute.body())) {
                    resp = execute.body().string();
                }
                return resp;
            }
        });
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }


}
