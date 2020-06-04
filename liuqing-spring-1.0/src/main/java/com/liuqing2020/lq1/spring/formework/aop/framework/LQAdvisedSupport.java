package com.liuqing2020.lq1.spring.formework.aop.framework;

import com.liuqing2020.lq1.spring.formework.LQUtils;
import com.liuqing2020.lq1.spring.formework.aop.aspect.LQAfterReturningAdviceInterceptor;
import com.liuqing2020.lq1.spring.formework.aop.aspect.LQAfterThrowingAdviceInterceptor;
import com.liuqing2020.lq1.spring.formework.aop.aspect.LQMethodBeforeAdviceInterceptor;
import com.liuqing2020.lq1.spring.formework.aop.config.LQAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> </p>
 *
 * @className LQAdvisedSupport
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 20:00
 * @Version 1.0.0
 * @see
 */
public class LQAdvisedSupport {
    private Class<?> targetClass;
    private Object target;
    private LQAopConfig lqAopConfig;
    private Pattern pointCutClassPattern;
    private transient Map<Method, List<Object>> methodCache;

    public LQAdvisedSupport(LQAopConfig lqAopConfig) {
        this.lqAopConfig = lqAopConfig;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parseAndBuildAopPattern();
    }
    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Object getTarget() {
        return this.target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws NoSuchMethodException {

        List<Object> cached = methodCache.get(method);
        if(cached == null){
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            //底层逻辑，对代理方法进行一个兼容处理
            this.methodCache.put(m,cached);
        }
        return cached;
    }

    //解析并构建正则匹配类
    private void parseAndBuildAopPattern() {
        //public .* com.liuqing2010.lq2.spring.dome.service..*Service..*(.*)
        String pointCut = lqAopConfig.getPointCut()
                .replaceAll("\\.","\\\\.")
                .replaceAll("\\\\.\\*",".*")
                .replaceAll("\\(","\\\\(")
                .replaceAll("\\)","\\\\)");
        //public .* com\.liuqing2020\.lq1\.spring\.dome\.service\..*Service\..*\(.*\)
        // String pointCutForClassRegex = pointCut.substring(0,pointCut.lastIndexOf("\\(")-4);
        String pointCutForClassRegex = pointCut.substring(0,pointCut.lastIndexOf("\\(") - 4);

        //class com.liuqing2020.lq1.spring.dome.service..*Service
        // pointCutClassPattern = Pattern.compile("class "+pointCutForClassRegex.substring(
        //         pointCutForClassRegex.lastIndexOf(" ")+1));
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(
                pointCutForClassRegex.lastIndexOf(" ") + 1));
        buildExecutionChain(pointCut);
    }

    /**
     * 构建执行器链
     */
    private void buildExecutionChain(String pointCut){
        try {
            methodCache = new HashMap<Method, List<Object>>();
            Pattern pattern = Pattern.compile(pointCut);

            Class<?> aspectClass = Class.forName(this.lqAopConfig.getAspectClass());
            Map<String,Method> aspectMethods = new HashMap<>();

            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(),method);
            }
            for (Method m : this.targetClass.getMethods()) {
                String methodToString = m.toString();
                if (methodToString.contains("throws")) {
                    methodToString = methodToString.substring(0, methodToString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodToString);
                if (matcher.matches()) {
                    //执行器链
                    List<Object> advices = new LinkedList<>();
                    //把每一个方法包装成MethodInterceptor
                    //before
                    if (!LQUtils.isEmpty(lqAopConfig.getAspectBefore())) {
                        //创建一个advices
                        advices.add(new LQMethodBeforeAdviceInterceptor(
                                aspectMethods.get(lqAopConfig.getAspectBefore()),aspectClass.newInstance()));
                    }
                    //after
                    if (!LQUtils.isEmpty(lqAopConfig.getAspectAfter())) {
                        //创建一个advices
                        advices.add(new LQAfterReturningAdviceInterceptor(
                                aspectMethods.get(lqAopConfig.getAspectAfter()),aspectClass.newInstance()));
                    }
                    //afterThrowing
                    if (!LQUtils.isEmpty(lqAopConfig.getAspectAfterThrow())) {
                        //创建一个advices
                        LQAfterThrowingAdviceInterceptor lqAfterThrowingAdviceInterceptor = new LQAfterThrowingAdviceInterceptor(
                                aspectMethods.get(lqAopConfig.getAspectAfterThrow()), aspectClass.newInstance());
                        lqAfterThrowingAdviceInterceptor.setThrowName(lqAopConfig.getAspectAfterThrowingName());
                        advices.add(lqAfterThrowingAdviceInterceptor);
                    }
                    methodCache.put(m,advices);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean pointCutMatch() {
        String s = this.targetClass.toString();
        boolean matches = pointCutClassPattern.matcher(s).matches();
        // return pointCutClassPattern.matcher(this.target.toString()).matches();
        return matches;
    }
}
