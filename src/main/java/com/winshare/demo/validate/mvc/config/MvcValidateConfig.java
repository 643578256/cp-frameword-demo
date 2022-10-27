package com.winshare.demo.validate.mvc.config;


import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

//@Configuration
public class MvcValidateConfig {

    //@Bean(name = "methodValidationPostProcessor")
    public MethodValidationPostProcessor validationPostProcessor(){
        return new MethodValidationPostProcessor();
    }
}
