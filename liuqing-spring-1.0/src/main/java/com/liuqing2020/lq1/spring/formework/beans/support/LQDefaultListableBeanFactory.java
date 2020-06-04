package com.liuqing2020.lq1.spring.formework.beans.support;

import com.liuqing2020.lq1.spring.formework.beans.config.LQBeanDefinition;
import com.liuqing2020.lq1.spring.formework.context.support.LQAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> </p>
 *
 * @className LQDefaultListableBeanFactory
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 19:01
 * @Version 1.0.0
 * @see
 */
public class LQDefaultListableBeanFactory extends LQAbstractApplicationContext {

    /** Map of bean definition objects, keyed by bean name */
    //注册BeanDefinition的信息也就是传说中的IOC 容器,伪容器
    protected final Map<String, LQBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


    @Override
    protected void refresh() throws Exception {
        super.refresh();
    }
}
