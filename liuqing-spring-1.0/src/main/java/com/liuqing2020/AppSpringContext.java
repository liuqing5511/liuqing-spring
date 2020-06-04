package com.liuqing2020;

import com.liuqing2020.lq1.spring.formework.context.LQApplicationContext;

/**
 * Hello world!
 *
 */
public class AppSpringContext
{
    public static void main( String[] args ) throws Exception {
        LQApplicationContext lqApplicationContext = new LQApplicationContext("classpath:application.properties");
        System.out.println();
        Object liuqingController = lqApplicationContext.getBean("liuqingController");
        System.out.println(liuqingController);
        // Object beans = lqApplicationContext.getBean(QueryService.class);
        // System.out.println(beans);
    }
}
