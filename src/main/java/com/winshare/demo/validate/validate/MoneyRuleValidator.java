package com.winshare.demo.validate.validate;


import com.winshare.demo.validate.annotation.MoneyRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MoneyRuleValidator implements ConstraintValidator<MoneyRule, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("工资格式：" + value);
        if (value.startsWith("￥")) {
            return true;
        }
        return false;
    }
}
