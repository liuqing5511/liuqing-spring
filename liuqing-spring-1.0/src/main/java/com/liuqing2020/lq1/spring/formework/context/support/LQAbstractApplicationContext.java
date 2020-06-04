package com.liuqing2020.lq1.spring.formework.context.support;

/**
 * <p> IOC容器实现的顶层设计 </p>
 *
 * @className LQAbstractApplicationContext
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 18:57
 * @Version 1.0.0
 * @see
 */
public abstract class LQAbstractApplicationContext {

    //只提供给子类重写
    protected  void refresh() throws Exception {}

}
