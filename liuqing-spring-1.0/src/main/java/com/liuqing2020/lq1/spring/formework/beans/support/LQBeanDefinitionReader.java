package com.liuqing2020.lq1.spring.formework.beans.support;

import com.liuqing2020.lq1.spring.formework.LQUtils;
import com.liuqing2020.lq1.spring.formework.beans.config.LQBeanDefinition;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p> </p>
 *
 * @className LQBeanDefinitionReader
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/26 22:08
 * @Version 1.0.0
 * @see
 */
@Slf4j
public class LQBeanDefinitionReader {
    private List<String> registryBeanClasses = new ArrayList<>();

    //固定配置文件的key定义
    private final static String SCAN_PACKAGE = "scanPackage";

    private Properties config = new Properties();

    public LQBeanDefinitionReader(String... locations) {
        //通过URL定位找到对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));

    }

    public Properties getConfig() {
        return this.config;
    }

    private void doScanner(String scanPackage) {
        //转换文件路径，实际上就是把.替换为/就OK了
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if (null == url) {
            throw new RuntimeException("包" + scanPackage + "路径错误");
        }
        File classPath = new File(url.getFile());
        //遍历文件
        if (null != classPath && classPath.exists()) {
            for (File f : classPath.listFiles()) {
                if (f.isDirectory()) {
                    doScanner(scanPackage + "." + f.getName());
                } else {
                    if (!f.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = (scanPackage + "." + f.getName().replace(".class", ""));
                    registryBeanClasses.add(className);
                }

            }
        } else {
            throw new RuntimeException("文件资源加载错误:" + classPath);
        }


    }


    /**
     * 读取配置文件中扫描到的配置信息解析成LQBeanDefinition的集合，并与IOC操作
     *
     * @return
     */
    public List<LQBeanDefinition> loadBeanDefinitions() {
        List<LQBeanDefinition> beanDefinitions = new ArrayList<>();
        if (null == registryBeanClasses || registryBeanClasses.isEmpty()) {
            log.debug("当前没有需要扫描的类!!!!!!");
            return beanDefinitions;
        }
        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                //beanName有三种情况
                //1，默认类名字首字母小写
                //2，自定义名字
                //3，接口注入
                beanDefinitions.add(doCreateBeanDefinition(LQUtils.toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
//                beanDefinitions.add(doCreateBeanDefinition(beanClass.getName(),beanClass.getName()));
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i: interfaces) {
                    //如果是多个实现类，只能覆盖
                    beanDefinitions.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    private LQBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName) {
        LQBeanDefinition beanDefinition = new LQBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

}
