package com.winshare.demo.sm;

import com.winshare.demo.proxyspr.TestA1;
import com.winshare.demo.proxyspr.TestA2;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpringEvent implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, SpringApplicationRunListener {

    @Autowired
    private TestA1 testA1;

    @Autowired
    private TestA2 testA2;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

       /* Class aClass = isProxy(testA1)? AopProxyUtils.getSingletonTarget(testA1).getClass():testA1.getClass();
        Method[] allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(testA1.getClass());
        int a = 0;*/
    }

    public  boolean isProxy(@Nullable Object object) {
        return  (Proxy.isProxyClass(object.getClass()) || object.getClass().getName().contains("$"));
    }


    @Override
    public void started(ConfigurableApplicationContext context) {
        int a = 0;
    }

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        list.add((byte)0);
        list.add((short)1);
        list.add(2);
        list.add(3L);
        list.add(4.0F);
        list.add(5.0D);
        list.add(true);
        list.add('c');
        int a = 0;
    }
}
