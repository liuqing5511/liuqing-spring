package com.liuqing2020.lq1.spring.formework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

/**
 * <p> </p>
 *
 * @className LQViewResolver
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 13:43
 * @Version 1.0.0
 * @see
 */
public class LQViewResolver {
    private final static String DEFAULT_TEMPLATE_SUFFX = ".html";

    private File templateRootDir;

    public LQViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public LQView resolveViewName(String viewName, Locale locale) throws Exception {
        if (null == viewName || "".equals(viewName)) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new LQView(templateFile);
    }
}
