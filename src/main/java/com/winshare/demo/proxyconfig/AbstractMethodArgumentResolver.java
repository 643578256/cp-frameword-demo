package com.winshare.demo.proxyconfig;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

public abstract class AbstractMethodArgumentResolver implements MethodArgumentResolver {

    protected boolean isSupportType(Type type) {
        return String.class.equals(type) || Date.class.equals(type) || Byte.class.equals(type) || Short.class.equals(type)
                || Character.class.equals(type) || Integer.class.equals(type) || Long.class.equals(type)
                || Double.class.equals(type) || Float.class.equals(type) || BigDecimal.class.equals(type) ||
                ((Class<?>) type).isPrimitive();

    }
}
