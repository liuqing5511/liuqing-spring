package com.liuqing2020.lq1.spring.formework.beans.config;

/**
 * <p> </p>
 *
 * @className LQBeanPostProcessor
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 11:33
 * @Version 1.0.0
 * @see
 */
public class LQBeanPostProcessor {
    //为在Bean 的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    //为在Bean 的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}
