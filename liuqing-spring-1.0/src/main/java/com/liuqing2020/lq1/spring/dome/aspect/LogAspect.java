package com.liuqing2020.lq1.spring.dome.aspect;

import com.liuqing2020.lq1.spring.formework.aop.aspect.LQJoinPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * <p> </p>
 *
 * @className LogAspect
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 19:50
 * @Version 1.0.0
 * @see
 */
@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(LQJoinPoint joinPoint){
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(),System.currentTimeMillis());
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    //在调用一个方法之后，执行after方法
    public void after(LQJoinPoint joinPoint){
        log.info("Invoker After Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println(joinPoint.getMethod().toString()+",执行时间 :" + (endTime - startTime));
    }

    public void afterThrowing(LQJoinPoint joinPoint, Throwable ex){
        log.info("出现异常" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nThrows:" + ex.getMessage());
    }

}
