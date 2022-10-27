package com.winshare.demo.validate;

import com.winshare.demo.validate.grop.DZGroup;
import com.winshare.demo.validate.grop.GZGroup;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Component
public class PersonValidator extends AbstractValidator {

    public void test(PersonReqDto personReqDto) {
        personReqDto.setLocal("lkasdjfllllllljlas");
        personReqDto.setCountValue("lkasdjfllllllljlas");

        Set<ConstraintViolation<Object>> validate = validate(personReqDto, GZGroup.class);
        Set<ConstraintViolation<Object>> validate1 = validate(personReqDto, DZGroup.class);
        System.out.println("llllllllll");
    }
}
