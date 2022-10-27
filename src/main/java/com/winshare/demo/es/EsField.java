package com.winshare.demo.es;


import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsField {
    String indexFieldName();

    /**
     * 如果是 Text 类型可以指定子类 keyword 型映射 ，查询的时候可以使用 term方式精确匹配
     * 因为keyword 不会分词。使用的时候就是  xxx.keyword
     * @return
     */
    String indexFieldType() default "";

    String classFieldName() default "";
}
