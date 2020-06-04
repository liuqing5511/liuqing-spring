package com.liuqing2020.lq1.spring.formework.aop;

import com.liuqing2020.lq1.spring.formework.aop.framework.LQAdvisedSupport;
import com.liuqing2020.lq1.spring.formework.aop.intercept.LQMethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * <p> </p>
 *
 * @className LQJdkDynamicAopProxy
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 19:57
 * @Version 1.0.0
 * @see
 */
public class LQJdkDynamicAopProxy implements LQAopProxy, InvocationHandler {

    private LQAdvisedSupport advised;

    public LQJdkDynamicAopProxy(LQAdvisedSupport advised) throws Exception {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,
                this.advised.getTargetClass());

        LQMethodInvocation invocation = new LQMethodInvocation(proxy, this.advised.getTarget(),
                method, args, this.advised.getTargetClass(), chain);
        return invocation.proceed();
    }
}
