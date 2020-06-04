package com.liuqing2020.lq1.spring.formework.context;

import com.liuqing2020.lq1.spring.formework.annotation.LQAutowired;
import com.liuqing2020.lq1.spring.formework.annotation.LQController;
import com.liuqing2020.lq1.spring.formework.annotation.LQService;
import com.liuqing2020.lq1.spring.formework.aop.LQAopProxy;
import com.liuqing2020.lq1.spring.formework.aop.LQCglibAopProxy;
import com.liuqing2020.lq1.spring.formework.aop.LQJdkDynamicAopProxy;
import com.liuqing2020.lq1.spring.formework.aop.config.LQAopConfig;
import com.liuqing2020.lq1.spring.formework.aop.framework.LQAdvisedSupport;
import com.liuqing2020.lq1.spring.formework.beans.LQBeanFactory;
import com.liuqing2020.lq1.spring.formework.beans.LQBeanWrapper;
import com.liuqing2020.lq1.spring.formework.beans.config.LQBeanDefinition;
import com.liuqing2020.lq1.spring.formework.beans.config.LQBeanPostProcessor;
import com.liuqing2020.lq1.spring.formework.beans.support.LQBeanDefinitionReader;
import com.liuqing2020.lq1.spring.formework.beans.support.LQDefaultListableBeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 按照IOC、DI、AOP、MVC流程书写</p>
 *
 * @className LQApplicationContext
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 18:51
 * @Version 1.0.0
 * @see
 */
@Slf4j
public class LQApplicationContext extends LQDefaultListableBeanFactory implements LQBeanFactory {


    //单例IOC容器缓存
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    //通用IOC容器
    private Map<String, LQBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    private String[] configLocations;
    private LQBeanDefinitionReader reader;

    public LQApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }


    /**
     * 依赖注入，从这里开始，通过读取BeanDefinition中的信息
     * 然后，通过反射机制创建一个实例并返回
     * 会用一个BeanWrapper来进行一次包装
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) throws Exception {

        //在容器中获取BeanDefinition
        LQBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        Object instance = null;
        /*处理前后置 ,在spring中是工厂模式+策略模式*/
        LQBeanPostProcessor beanPostProcessor = new LQBeanPostProcessor();
        beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

        /*初始化和注入分开的原因是为了解决循环依赖注入*/
        //1，初始化
        instance = instantiateBean(beanName, beanDefinition);

        //3，把这个对象封装到BeanWrapper
        LQBeanWrapper lqBeanWrapper = new LQBeanWrapper(instance);


        //singletonObjects
        //factoryBeanInstanceCache

        //2，拿到lqBeanWrapper之后，把lqBeanWrapper保存到IOC容器中
        this.factoryBeanInstanceCache.put(beanName, lqBeanWrapper);

        beanPostProcessor.postProcessAfterInitialization(instance, beanName);

        //3，注入
        populateBean(beanName, beanDefinition, lqBeanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    /**
     * 注入
     *
     * @param beanName
     * @param lqBeanDefinition
     * @param lqBeanWrapper
     */
    private void populateBean(String beanName, LQBeanDefinition lqBeanDefinition, LQBeanWrapper lqBeanWrapper) {
        Object wrappedInstance = lqBeanWrapper.getWrappedInstance();

        //判断，只有加了注解的类才执行依赖注入
        Class<?> wrappedClass = lqBeanWrapper.getWrappedClass();
        if (!(wrappedClass.isAnnotationPresent(LQController.class)
                || wrappedClass.isAnnotationPresent(LQService.class))) {
            return;
        }

        //获得所有fields
        Field[] declaredFields = wrappedClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!field.isAnnotationPresent(LQAutowired.class)) {
                continue;
            }
            LQAutowired annotation = field.getAnnotation(LQAutowired.class);
            String autowiredBeanName = annotation.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
                // autowiredBeanName = field.getName();
            }
            //设置容许访问
            field.setAccessible(true);
            try {
                if (null == this.factoryBeanInstanceCache.get(autowiredBeanName)) {
                    log.info("factoryBeanInstanceCache【{}】中找不到", autowiredBeanName);
                    // getBean(autowiredBeanName);
                    continue;
                }
                if (null == wrappedInstance) {
                    continue;
                }
                field.set(wrappedInstance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 初始化
     *
     * @param beanName
     * @param lqBeanDefinition
     * @return
     */
    private Object instantiateBean(String beanName, LQBeanDefinition lqBeanDefinition) {
        //1，拿到要实例化的对象的类名
        String beanClassName = lqBeanDefinition.getBeanClassName();

        //2，进行反射实例化
        Object instance = null;
        try {
            String factoryBeanName = lqBeanDefinition.getFactoryBeanName();

            //默认就是单例
            if (singletonObjects.containsKey(beanClassName)) {
                instance = singletonObjects.get(beanClassName);
            } else {
                Class<?> aClass = Class.forName(beanClassName);
                instance = aClass.newInstance();

                LQAdvisedSupport config = instantionAopConfig(lqBeanDefinition);
                config.setTargetClass(aClass);
                config.setTarget(instance);

                //符合PointCut的规则的话，将创建代理对象
                if (config.pointCutMatch()) {
                    instance = createProxy(config).getProxy();
                }
                this.singletonObjects.put(beanClassName, instance);
                this.singletonObjects.put(factoryBeanName, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    //创建代理对象
    private LQAopProxy createProxy(LQAdvisedSupport config) throws Exception {
        Class<?> targetClass = config.getTargetClass();
        if (targetClass.getInterfaces().length > 0) {
            return new LQJdkDynamicAopProxy(config);
        }
        return new LQCglibAopProxy(config);
    }

    private LQAdvisedSupport instantionAopConfig(LQBeanDefinition lqBeanDefinition) {
        LQAopConfig lqAopConfig = new LQAopConfig();
        lqAopConfig.setPointCut(this.reader.getConfig().getProperty(lqAopConfig.POINT_CUT));
        lqAopConfig.setAspectClass(this.reader.getConfig().getProperty(lqAopConfig.ASPECT_CLASS));
        lqAopConfig.setAspectBefore(this.reader.getConfig().getProperty(lqAopConfig.ASPECT_BEFORE));
        lqAopConfig.setAspectAfter(this.reader.getConfig().getProperty(lqAopConfig.ASPECT_AFTER));
        lqAopConfig.setAspectAfterThrow(this.reader.getConfig().getProperty(lqAopConfig.ASPECT_AFTER_THROW));
        lqAopConfig.setAspectAfterThrowingName(this.reader.getConfig().getProperty(lqAopConfig.ASPECT_AFTER_THROWING_NAME));
        return new LQAdvisedSupport(lqAopConfig);
    }

    @Override
    protected void refresh() throws Exception {
        //1，定位配置文件()
        reader = new LQBeanDefinitionReader(this.configLocations);

        //2，加载配置文件，扫描相关类，封装成BeanDefinition
        List<LQBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 3，注册，把配置信息放入容器中（伪IOC容器中）
        doRegisterBeanDefinitions(beanDefinitions);
        log.info("=======注册完成=======");
        for (String s : this.beanDefinitionMap.keySet()) {
            log.info("【{}={}】", s, this.beanDefinitionMap.get(s).toString());
        }
        log.info("=======注册完成=======");
        //4，把不是延迟加载的类要提前初始化
        doAutowrited();
        log.info("=======依赖注入完成=======");
        for (String s : this.factoryBeanInstanceCache.keySet()) {
            log.info("【{}={}=】", s,
                    this.factoryBeanInstanceCache.get(s).getWrappedClass(),
                    this.factoryBeanInstanceCache.get(s).getWrappedInstance());
        }
        log.info("=======依赖注入完成=======");

        log.info("初始化容器完成");
    }

    //只处理非延迟加载的
    private void doAutowrited() {
        for (String beanName : super.beanDefinitionMap.keySet()) {
            if (!super.beanDefinitionMap.get(beanName).getLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("当前【{}】类需要延迟加载", beanName);
            }
        }
    }

    private void doRegisterBeanDefinitions(List<LQBeanDefinition> beanDefinitions) throws Exception {
        if (null == beanDefinitions || beanDefinitions.isEmpty()) {
            log.info("没有需要注册的类");
            return;
        }
        for (LQBeanDefinition bean : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(bean.getFactoryBeanName())) {
                throw new Exception("The “" + bean.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(bean.getFactoryBeanName(), bean);
        }
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

}
