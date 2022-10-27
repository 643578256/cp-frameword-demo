package com.winshare.demo;

import com.winshare.demo.netty.service.NettyService;
import com.winshare.demo.rest.ATest;
import com.winshare.demo.rest.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;
import java.util.*;

@SpringBootApplication
public class DemoApplication {


    static ThreadLocal aa = new InheritableThreadLocal();

    public static void main(String[] args) {
       // BeanInfo beanInfo = new SimpleBeanInfo();
        SpringApplication.run(DemoApplication.class, args);
       /* Jedis jedis = new Jedis("132.232.20.136",7001);
        jedis.set("b","bbbb");*/

        /*AnnotationConfigApplicationContext annotation = new AnnotationConfigApplicationContext(DemoApplication.class);
       // annotation.refresh();
        ATest bean = annotation.getBean(ATest.class);
        //.run(DemoApplication.class, args);
        System.out.println("-------------startServier-------------------");*/

        //NettyService.startServier();
    }

}


