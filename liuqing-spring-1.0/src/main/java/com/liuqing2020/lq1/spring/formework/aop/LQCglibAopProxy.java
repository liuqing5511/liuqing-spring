package com.liuqing2020.lq1.spring.formework.aop;

import com.liuqing2020.lq1.spring.formework.aop.framework.LQAdvisedSupport;

/**
 * <p> </p>
 *
 * @className CglibAopProxy
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 19:56
 * @Version 1.0.0
 * @see
 */
public class LQCglibAopProxy implements LQAopProxy {
    public LQCglibAopProxy(LQAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
