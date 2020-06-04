package com.liuqing2020.lq1.spring.formework.aop.aspect;

import java.lang.reflect.Method;

/**
 * <p> </p>
 *
 * @className LQJoinPoint
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/28 0:11
 * @Version 1.0.0
 * @see
 */
public interface LQJoinPoint {
    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
