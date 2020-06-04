package com.liuqing2020.lq1.spring.formework.context;

/**
 * <p>
 *          观察者模式
 *          通过解耦的方式获得IOC容器的顶层设计
 *          后面将通过监听器去扫描所有的类，只要实现此接口，将自动调用setApplicationContext
 *          从而将IOC容器注入到目标类中
 * </p>
 *
 * @className ApplicationContextAware
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 19:14
 * @Version 1.0.0
 * @see
 */
public interface LQApplicationContextAware {

    void setApplicationContext(LQApplicationContext applicationContext) throws Exception;
}
