package com.winshare.demo.proxyspr;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MyImportBeanDefinitionRegistrar.class)
public @interface ss {

    String basePackages() default "";
}
