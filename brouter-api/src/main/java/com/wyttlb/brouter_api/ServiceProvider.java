package com.wyttlb.brouter_api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceProvider {
    /**
     * 类集合
     * @return
     */
    Class<?>[] value();

    /**
     * 优先级
     * @return
     */
    int priority() default 0;

    /**
     * 别名
     * @return
     */
    String alias() default "";
}
