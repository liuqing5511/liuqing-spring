package com.liuqing2020.lq1.spring.formework.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LQAutowired {
    String value() default "";
}
