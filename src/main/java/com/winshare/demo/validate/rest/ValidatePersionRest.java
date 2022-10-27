package com.winshare.demo.validate.rest;

import com.winshare.demo.validate.PersonReqDto;
import com.winshare.demo.validate.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidatePersionRest {

    @Autowired
    private PersonValidator personValidator;

    @RequestMapping("/test/v")
    public void test() {
        personValidator.test(new PersonReqDto());
    }
}
