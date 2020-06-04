package com.liuqing2020.lq1.spring.formework.aop.aspect;

import java.lang.reflect.Method;

/**
 * <p> </p>
 *
 * @className LQAbstractAspectAdvice
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 23:26
 * @Version 1.0.0
 * @see
 */
public abstract class LQAbstractAspectAdvice implements LQAdvice{

    private Method aspectMethod;
    private Object aspectTarget;

    /**
     * @param aspectMethod
     * @param aspectTarget 代理目标类
     */
    public LQAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    /**
     *
     * @param joinPoint  切入对象
     * @param returnValue 返回结果
     * @param t 异常
     * @return
     * @throws Throwable
     */
    protected Object invokeAdviceMethod(LQJoinPoint joinPoint, Object returnValue, Throwable t) throws Throwable {
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if (null == parameterTypes || parameterTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == LQJoinPoint.class) {
                    args[i] = joinPoint;
                }else if(parameterTypes[i] == Throwable.class){
                    args[i] = t;
                }else if(parameterTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget,args);
        }

    }
}
