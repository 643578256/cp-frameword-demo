package com.winshare.demo.es;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsIndexDocument {
    /**
     * es 7.0 之前 有 index 和 type之分。
     * 7.0之后es默认一个index就只能有一个名为 “_doc” 的type
     * 所以 我们这里的默认只有indexName
     * @return
     */
    String indexName();

    /**
     * 7.0之后es默认一个index就只能有一个名为 “_doc” 的type
     * @return
     */
    String typeName() default "_doc";
}
