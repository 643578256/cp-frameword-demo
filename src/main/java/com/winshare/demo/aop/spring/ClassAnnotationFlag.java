package com.winshare.demo.aop.spring;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ClassAnnotationFlag {
}
