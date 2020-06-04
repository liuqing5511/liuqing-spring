package com.liuqing2020.lq1.spring.formework.aop;

/**
 * <p> 在spring中是代理顶层类属于 </p>
 *
 * @className LQAopProxy
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 19:55
 * @Version 1.0.0
 * @see
 */
public interface LQAopProxy {
    Object getProxy();
    Object getProxy(ClassLoader classLoader);
}
