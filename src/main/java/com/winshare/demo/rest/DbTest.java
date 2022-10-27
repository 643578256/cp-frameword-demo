package com.winshare.demo.rest;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbTest {



    @PostMapping(value = "/v1/test/db")
    public void testaop(@RequestBody Person person){
        System.out.println(person);
        int a = 0;
    }

}
