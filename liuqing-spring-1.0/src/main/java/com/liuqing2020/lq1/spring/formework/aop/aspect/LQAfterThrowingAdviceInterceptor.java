package com.liuqing2020.lq1.spring.formework.aop.aspect;

import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInvocation;
import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInterceptor;

import java.lang.reflect.Method;

/**
 * <p> spring 是基于 AspectJ 来实现 </p>
 *
 * @className LQAfterThrowAdviceInterceptor
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 23:18
 * @Version 1.0.0
 * @see
 */
public class LQAfterThrowingAdviceInterceptor extends LQAbstractAspectAdvice implements LQAdvice,LQMethodInterceptor {

    private String throwName;

    /**
     * @param aspectMethod
     * @param aspectTarget 代理目标类
     */
    public LQAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(LQMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwName = throwName;
    }
}
