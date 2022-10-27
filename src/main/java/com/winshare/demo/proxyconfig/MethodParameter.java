package com.winshare.demo.proxyconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;



@Getter
@AllArgsConstructor
@ToString
public class MethodParameter {
    private final String parameterName;
    private final Class<?> parameterType;
    private final ParameterAnnotationType annotationType;
}

