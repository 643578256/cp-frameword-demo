package com.winshare.demo.thread;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(Pserson.class)
@ConditionalOnProperty(
        value = {"feign.httpclient.enabled"},
        matchIfMissing = false
)
public class ConfigTest {

    @Bean
    public Pserson person(){

        return Pserson.builder().build();
    }
}
