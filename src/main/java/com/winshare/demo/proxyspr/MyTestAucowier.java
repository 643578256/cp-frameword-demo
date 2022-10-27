package com.winshare.demo.proxyspr;

import com.winshare.demo.rest.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*@Component*/
public class MyTestAucowier {

    @Autowired
    private TestA1 testA1;

    @Autowired
    private TestA2 testA2;

   @PostConstruct
    public void aa(){
        testA2.insert(new Person("1",2));
        int a = 0;
    }
}
