package com.winshare.demo.rest;

import com.winshare.demo.aop.spring.ClassAnnotationFlag;
import com.winshare.demo.thread.Test;
import org.springframework.stereotype.Service;

@Service
@ClassAnnotationFlag
public class ATestImpl implements ATest {
    @Override
    public Object testAop() {
        return new Test();
    }
}
