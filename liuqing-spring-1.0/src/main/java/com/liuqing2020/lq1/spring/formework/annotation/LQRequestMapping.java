package com.liuqing2020.lq1.spring.formework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LQRequestMapping {
    String value() default "";
}
