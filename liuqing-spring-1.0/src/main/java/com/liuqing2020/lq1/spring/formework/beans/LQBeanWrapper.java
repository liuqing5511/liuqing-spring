package com.liuqing2020.lq1.spring.formework.beans;

/**
 * <p> </p>
 *
 * @className LQBeanWrapper
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 23:14
 * @Version 1.0.0
 * @see
 */
public class LQBeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;

    public LQBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }

    //获取实例
    public Object getWrappedInstance(){

        return this.wrappedInstance;
    }

    //获取创建实例
    public Class<?> getWrappedClass(){
        return this.wrappedInstance.getClass();
    }
}
