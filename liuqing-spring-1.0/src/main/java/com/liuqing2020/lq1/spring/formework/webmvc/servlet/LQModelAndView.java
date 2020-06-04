package com.liuqing2020.lq1.spring.formework.webmvc.servlet;

import java.util.Map;
import java.util.PrimitiveIterator;

/**
 * <p> </p>
 *
 * @className LQModelAndView
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 13:15
 * @Version 1.0.0
 * @see
 */
public class LQModelAndView {
    private String viewName;
    private Map<String,?> model;

    public LQModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public LQModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
