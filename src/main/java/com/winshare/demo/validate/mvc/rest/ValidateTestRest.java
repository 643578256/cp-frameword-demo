package com.winshare.demo.validate.mvc.rest;

import com.winshare.demo.validate.PersonReqDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ValidateTestRest {


    @RequestMapping("/test/mvc/v")
    public void test(@RequestBody @Valid PersonReqDto personReqDto) {
        System.out.println("----"+personReqDto.toString());
    }
}
