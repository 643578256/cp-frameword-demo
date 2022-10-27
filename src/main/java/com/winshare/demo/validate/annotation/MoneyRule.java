package com.winshare.demo.validate.annotation;


import com.winshare.demo.validate.validate.MoneyRuleValidator;

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
@Constraint(validatedBy = {MoneyRuleValidator.class})
public @interface MoneyRule {

    String message() default "钱格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
