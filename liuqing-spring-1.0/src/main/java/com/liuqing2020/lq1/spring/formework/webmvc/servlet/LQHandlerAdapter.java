package com.liuqing2020.lq1.spring.formework.webmvc.servlet;

import com.liuqing2020.lq1.spring.formework.annotation.LQRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> </p>
 *
 * @className LQHandlerAdapter
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 13:10
 * @Version 1.0.0
 * @see
 */
public class LQHandlerAdapter {
    //吧方法的形参列表和request循序一一对应
    protected Map<String,Integer> paramIndexMapping;	//参数顺序

    public boolean supports(Object handler) {
        return (handler instanceof LQHandlerMapping);
    }

    public LQModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LQHandlerMapping h = (LQHandlerMapping) handler;
        //把方法的形参列表和request的参数列表所在顺序进行一一对应
        Map<String,Integer> paramIndexMapping = new HashMap<String, Integer>();

        //提取方法中加了注解的参数
        //把方法上的注解拿到，得到的是一个二维数组
        //因为一个参数可以有多个注解，而一个方法又有多个参数
        Annotation[] [] pa = h.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length ; i ++) {
            for(Annotation a : pa[i]){
                if(a instanceof LQRequestParam){
                    String paramName = ((LQRequestParam) a).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }
        //提取方法中的request和response参数
        Class<?> [] paramsTypes = h.getMethod().getParameterTypes();
        for (int i = 0; i < paramsTypes.length ; i ++) {
            Class<?> type = paramsTypes[i];
            if(type == HttpServletRequest.class ||
                    type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }
        //获得方法的形参列表
        Map<String,String[]> params = request.getParameterMap();
        //实参列表
        Object [] paramValues = new Object[paramsTypes.length];

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","")
                    .replaceAll("\\s",",");

            if(!paramIndexMapping.containsKey(param.getKey())){continue;}

            int index = paramIndexMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value,paramsTypes[index]);
        }

        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }

        Object result = h.getMethod().invoke(h.getController(),paramValues);
        if(result == null || result instanceof Void){ return null; }

        boolean isModelAndView = (h.getMethod().getReturnType() == LQModelAndView.class);
        if(isModelAndView){
            return (LQModelAndView) result;
        }
        return  null;
    }

    public static Object caseStringValue(String value, Class<?> paramsType) {
        if(String.class == paramsType){
            return value;
        }
       else if(Integer.class == paramsType){ //如果是int
            return Integer.valueOf(value);
        }
        else if(Double.class == paramsType){
            return Double.valueOf(value);
        }else {
            if(value != null){
                return value;
            }
            return null;
        }
        //如果还有double或者其他类型，继续加if

    }
}
