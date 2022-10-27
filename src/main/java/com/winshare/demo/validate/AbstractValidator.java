package com.winshare.demo.validate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public abstract class AbstractValidator {
    private static Validator validator;

    public synchronized Validator getValidator() {
        if (validator == null) {
            validator = Validation.buildDefaultValidatorFactory().getValidator();
        }
        return validator;
    }

    public Set<ConstraintViolation<Object>> validate(Object obj, Class... classes) {
        Validator validator = getValidator();
        Set<ConstraintViolation<Object>> validate = validator.validate(obj, classes);
        return validate;
    }
}
