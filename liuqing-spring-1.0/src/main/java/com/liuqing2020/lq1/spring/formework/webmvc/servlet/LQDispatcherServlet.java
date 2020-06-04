package com.liuqing2020.lq1.spring.formework.webmvc.servlet;

import com.liuqing2020.lq1.spring.formework.annotation.LQController;
import com.liuqing2020.lq1.spring.formework.annotation.LQRequestMapping;
import com.liuqing2020.lq1.spring.formework.context.LQApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> </p>
 *
 * @className LQDispatcherServlet
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 11:49
 * @Version 1.0.0
 * @see
 */
@Slf4j
public class LQDispatcherServlet extends HttpServlet {
    private  static final  String  CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
    private LQApplicationContext context;

    private List<LQHandlerMapping> handlerMappings = new ArrayList<>();
    private Map<LQHandlerMapping,LQHandlerAdapter> handlerAdapters = new HashMap<>();

    private List<LQViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
            // new LQModelAndView(500);
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));

        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        //通过从request中拿到url匹配一个HandlerMapping
        LQHandlerMapping handler = getHandler(req);

        if(null == handler){
            processDispatchResult(req,resp,new LQModelAndView("404"));
            return ;
        }
        //准备好调用前的参数
        LQHandlerAdapter ha = getHandlerAdapter(handler);

        //处理请求头

        //调用方法,返回ModelAndView存储了要传页面的值和页面模板名称
        LQModelAndView mv = ha.handle(req, resp, handler);

        //视图处理
        processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, LQModelAndView mv) throws Exception {
        //由contextType决定把ModelAndView转换为下列哪种类型
        // html,outputStream、josn、freemark、veolcity
        if(null == mv ){
            // new LQModelAndView("404");
            return;
        }
        //渲染
        if(this.viewResolvers.isEmpty()){
            return;
        }
        for (LQViewResolver viewResolver : this.viewResolvers) {
            LQView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(),req,resp);
            return;
        }


    }

    private LQHandlerAdapter getHandlerAdapter(LQHandlerMapping handler) {
        if(null == this.handlerAdapters || this.handlerAdapters.isEmpty()){
            return null;
        }
        LQHandlerAdapter ha = this.handlerAdapters.get(handler);
        if(ha.supports(handler)){
            return  ha;
        }
        return null;
    }

    private LQHandlerMapping getHandler(HttpServletRequest req) throws Exception{
        if(this.handlerMappings.isEmpty()){ return null; }

        String url = req.getRequestURI();
        log.info("当前请求url=【{}】",url);

        String contextPath = req.getContextPath();
        log.info("当前请求contextPath=【{}】",contextPath);

        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (LQHandlerMapping handler : this.handlerMappings) {
            try{
                Matcher matcher = handler.getPattern().matcher(url);
                //如果没有匹配上继续下一个匹配
                if(!matcher.matches()){ continue; }
                log.info("匹配到url=【{}】class=【{}】method=【{}】",handler.getPattern().toString(),
                        handler.getController(),
                        handler.getMethod());
                return handler;
            }catch(Exception e){
                throw e;
            }
        }
        return null;
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化ApplicationContext
        context = new LQApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));

        //初始化SpringMVC 的九大组件
        initStrategies(context);
    }
    protected void initStrategies(LQApplicationContext context) {
        /*文件上传解析如果请求类型是multipart将通过MultipartResolver*/
        initMultipartResolver(context);
        /*本地化解析*/
        initLocaleResolver(context);
        /*主题解析-换肤*/
        initThemeResolver(context);
        /*通过HandlerMapping将请求映射到处理器*/
        initHandlerMappings(context);//TODO
        /*通过HandlerAdapters进行多类型的参数动态匹配*/
        initHandlerAdapters(context);//TODO
        /*异常*/
        initHandlerExceptionResolvers(context);
        /*请求解析视图*/
        initRequestToViewNameTranslator(context);
        /*解析逻辑视图的具体实现*/
        initViewResolvers(context);//TODO
        /*参数缓存器*/
        initFlashMapManager(context);
    }

    private void initFlashMapManager(LQApplicationContext context) {
    }

    private void initViewResolvers(LQApplicationContext context) {
        //拿到一个模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        for (File template : templateRootDir.listFiles()) {
            //这里主要是为了兼容多模板，所有模仿spring用LIst保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所以还是弄了个List
            this.viewResolvers.add(new LQViewResolver(templateRoot));
        }


    }

    private void initRequestToViewNameTranslator(LQApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(LQApplicationContext context) {
    }

    private void initHandlerAdapters(LQApplicationContext context) {
        //把一个request请求变成一个handler,参数都是字符串的，
        // 都需要自动匹配到handler中的形参。
        //  要拿到handlerMapping才能做事情
        //有几个handlerMapping就有几个HandlerAdapter
        for (LQHandlerMapping handlerMapping:this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping,new LQHandlerAdapter());
        }
    }

    private void initHandlerMappings(LQApplicationContext context) {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        try {
            for (String beanName:beanDefinitionNames) {
                Object beanClass = context.getBean(beanName);
                Class<?> aClass = beanClass.getClass();
                if(!aClass.isAnnotationPresent(LQController.class)){
                        continue;
                }
                //类上的URL
                String baseUrl = "";
                if (aClass.isAnnotationPresent(LQRequestMapping.class)) {
                    LQRequestMapping classRequestMapping = aClass.getAnnotation(LQRequestMapping.class);
                    baseUrl = classRequestMapping.value();
                }

                //获取Method的url配置
                Method[] methods = aClass.getMethods();
                for (Method m : methods) {
                    //没有加RequestMapping注解的直接忽略
                    if (!m.isAnnotationPresent(LQRequestMapping.class)) {
                        continue;
                    }
                    LQRequestMapping mRequestMapping = m.getAnnotation(LQRequestMapping.class);

                    //优化(如果多个/相连  全部替换成一个/)
                    String regex = (baseUrl + "/" + mRequestMapping.value().replaceAll("\\*",".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new LQHandlerMapping(pattern,beanClass,m));
                    log.info("初始化requestURL :" + regex + " , " + m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(LQApplicationContext context) {
    }

    private void initLocaleResolver(LQApplicationContext context) {
    }

    private void initMultipartResolver(LQApplicationContext context) {
    }
}
