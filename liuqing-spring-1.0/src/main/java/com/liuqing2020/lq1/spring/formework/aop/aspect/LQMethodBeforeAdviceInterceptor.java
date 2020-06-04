package com.liuqing2020.lq1.spring.formework.aop.aspect;

import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInvocation;
import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInterceptor;

import java.lang.reflect.Method;

/**
 * <p> </p>
 *
 * @className LQMethodBeforeAdviceInterceptor
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 23:16
 * @Version 1.0.0
 * @see
 */
public class LQMethodBeforeAdviceInterceptor extends LQAbstractAspectAdvice implements LQAdvice,LQMethodInterceptor {

    private LQJoinPoint joinPoint;
    /**
     * @param aspectMethod
     * @param aspectTarget 代理目标类
     */
    public LQMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    protected  void before(Method method,Object[] args,Object target)throws Throwable{
        //传递给织入的参数
        // method.invoke(target);
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
    @Override
    public Object invoke(LQMethodInvocation mi) throws Throwable {
        //从被织入的代码中才能，JoinPoint
        this.joinPoint=mi;
        before(mi.getMethod(),mi.getArguments(),mi.getThis());
        return mi.proceed();
    }
}
