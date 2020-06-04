package com.liuqing2020.lq1.spring.formework.aop.intercept;

/**
 * <p> </p>
 *
 * @className LQMethodInterceptor
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 20:23
 * @Version 1.0.0
 * @see
 */
    public interface LQMethodInterceptor {
    Object invoke(LQMethodInvocation invocation) throws Throwable;
}
