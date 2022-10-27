package com.winshare.demo.validate.mvc.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceRestHandler {


    @ExceptionHandler(Exception.class)
    public String resutlHandler(Throwable throwable){
        return throwable.getMessage();
    }
}
