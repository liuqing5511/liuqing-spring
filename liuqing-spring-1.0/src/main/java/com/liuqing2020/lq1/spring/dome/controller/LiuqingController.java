package com.liuqing2020.lq1.spring.dome.controller;


import com.liuqing2020.lq1.spring.dome.service.IModifyService;
import com.liuqing2020.lq1.spring.dome.service.IQueryService;
import com.liuqing2020.lq1.spring.formework.annotation.LQAutowired;
import com.liuqing2020.lq1.spring.formework.annotation.LQController;
import com.liuqing2020.lq1.spring.formework.annotation.LQRequestMapping;
import com.liuqing2020.lq1.spring.formework.annotation.LQRequestParam;
import com.liuqing2020.lq1.spring.formework.webmvc.servlet.LQModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@LQController
@LQRequestMapping("/web")
public class LiuqingController {
    @LQAutowired
    private IQueryService queryService;
    @LQAutowired
    private IModifyService modifyService;

    @LQRequestMapping("/test.json")
    public LQModelAndView test() {
        System.out.println("测试");
        // return out(response,"这是一个测试");
        Map<String, Object> model = new HashMap<>();
        model.put("liuqing", "我是liuqing");
        model.put("data", new Date());
        model.put("token", UUID.randomUUID().toString());
        LQModelAndView lqModelAndView = new LQModelAndView("first", model);
        return lqModelAndView;
    }

    @LQRequestMapping("/query.json")
    public LQModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @LQRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @LQRequestMapping("/add*.json")
    public LQModelAndView add(HttpServletRequest request, HttpServletResponse response,
                              @LQRequestParam("name") String name, @LQRequestParam("addr") String addr) {
        String result = null;
        try {
            result = modifyService.add(name, addr);
            return out(response, result);
        } catch (Exception e) {
            // e.printStackTrace();
            Map<String, Object> model = new HashMap<>();
            model.put("detail", e.getCause().getMessage());
            // model.put("stackTrace",e.getStackTrace());
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", ""));
            return new LQModelAndView("500", model);
        }

    }

    @LQRequestMapping("/remove.json")
    public LQModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @LQRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @LQRequestMapping("/edit.json")
    public LQModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                               @LQRequestParam("id") Integer id,
                               @LQRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private LQModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
