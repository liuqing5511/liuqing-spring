package com.liuqing2020.lq1.spring.formework.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LQRequestParam {
    String value() default "";
}
