package com.winshare.demo.mq.api.suport;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MQConsumer {

    String topic();

    /**
     * 适用于rocketma
     * @return
     */
    String consumerGroup() default "";

    /**
     * 适用于rocketma
     * @return
     */
    String orderConsumer() default "";

    /**
     * 适用于rocketma
     * @return
     */
    String messageModel() default "";

    String routingKey() default "";

    String queueName() default "";
}
