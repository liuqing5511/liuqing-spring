package com.liuqing2020.lq1.spring.formework.beans;

/**
 * <p> 单例工厂的顶层设计 </p>
 *
 * @className LQBeanFactory
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 18:47
 * @Version 1.0.0
 * @see
 */
public interface LQBeanFactory {

    /**
     * 根据beanName从IOC容器中获得一个Bean实例
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass)throws Exception;
}
