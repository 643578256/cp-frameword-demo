package com.winshare.demo.validate.validate;


import com.winshare.demo.validate.annotation.AddrassLocal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<AddrassLocal, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("当前地址信息为：" + value);
        if ("四川成都".equals(value)) {
            return true;
        }
        return false;
    }
}
