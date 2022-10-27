package com.winshare.demo.validate.annotation;


import com.winshare.demo.validate.validate.AddressValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AddressValidator.class})
public @interface AddrassLocal {

    String message() default "地址格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}