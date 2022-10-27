package com.winshare.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//@RestController
public class RestTest {
    @Autowired
    private RedisTemplate template;

    @RequestMapping(value = "/redis/test")
    public List<Object> test(){

        Object a = template.opsForValue().get("a");
        Object b = template.opsForValue().get("b");
        Object c = template.opsForValue().get("c");

        template.opsForValue().set("a","aaaaa");
        template.opsForValue().set("b","bbbb");
        template.opsForValue().set("c","cccc");
        List<Object> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        return list;
    }
}
