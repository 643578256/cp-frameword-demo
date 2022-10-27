package com.winshare.demo.inject;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;


@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CpAutowired {

    boolean required() default true;
}
