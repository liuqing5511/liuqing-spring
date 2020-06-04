package com.liuqing2020.lq1.spring.formework.aop.aspect;

import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInvocation;
import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInterceptor;

import java.lang.reflect.Method;

/**
 * <p> </p>
 *
 * @className LQAfterReturningAdviceInterceptor
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 23:17
 * @Version 1.0.0
 * @see
 */
public class LQAfterReturningAdviceInterceptor extends LQAbstractAspectAdvice implements LQAdvice,LQMethodInterceptor {
    private LQJoinPoint joinPoint;

    /**
     * @param aspectMethod
     * @param aspectTarget 代理目标类
     */
    public LQAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(LQMethodInvocation mi) throws Throwable {
        Object proceed = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(proceed,mi.getMethod(),mi.getArguments(),mi.getThis());
        return proceed;
    }
    private void afterReturning(Object proceed, Method method, Object[] arguments, Object aThis){
        try {
            super.invokeAdviceMethod(this.joinPoint, proceed, null);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
