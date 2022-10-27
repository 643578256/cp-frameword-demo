package com.winshare.demo.aop.spring;

import com.winshare.demo.es.ESRangeVo;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.ExpressionPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeAopConfig {

    @Bean
    public ESRangeVo ESRangeVo1(){
        return new ESRangeVo(null,null,null,null,null);
    }
    @Bean
    public ESRangeVo ESRangeVo2(){
        return new ESRangeVo(null,null,null,null,null);
    }

    @Bean
    public DefaultPointcutAdvisor testAnnotation(){
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(ClassAnnotationFlag.class);
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setPointcut(pointcut);
        pointcutAdvisor.setAdvice(new TestInterceptor());
        return pointcutAdvisor;
    }

    @Bean
    public DefaultPointcutAdvisor testExpression(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution (public * com.winshare.demo.rest..*.*(..))");
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setPointcut(pointcut);
        pointcutAdvisor.setAdvice(new TestInterceptor());
        return pointcutAdvisor;
    }

    //@Bean
    public DefaultPointcutAdvisor defaultTestExpression(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution (public * com.winshare.demo..*.*(..))");
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setPointcut(pointcut);
        pointcutAdvisor.setAdvice(new TestInterceptor());
        return pointcutAdvisor;
    }
}
