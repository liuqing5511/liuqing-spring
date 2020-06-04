package com.liuqing2020.lq1.spring.formework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * <p> </p>
 *
 * @className LQHandlerMapping
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 12:54
 * @Version 1.0.0
 * @see
 */
public class LQHandlerMapping {
    protected Object controller;	//保存方法对应的实例
    protected Method method;		//保存映射的方法
    protected Pattern pattern; //url的正则匹配


    public LQHandlerMapping(Pattern pattern,Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }
    /*    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception{

        }*/
    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }


}
