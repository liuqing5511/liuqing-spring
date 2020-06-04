package com.liuqing2020.lq1.spring.formework.beans.config;

import lombok.Data;

/**
 * <p>用来存储配置文件中的信息
 * 相当于保存在内存中的配置
 * </p>
 *
 * @className LQBeanDefinition
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 19:05
 * @Version 1.0.0
 * @see
 */

@Data
public class LQBeanDefinition {
    //bean类名称
    private String beanClassName;
    //是否延迟加载
    private  Boolean lazyInit = false;
    //工厂Bean名称
    private  String factoryBeanName;
    private Boolean isSingleton = true;
}
