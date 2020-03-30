package com.fuzy.mvc.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fuzy
 * @version 1.0
 * @Description
 * @date 2020/3/30 18:11
 */
public class DemoDispatcherServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        //加载配置文件
        String packageName = loadConfig();
        //扫描包
        doScanner(packageName);
        //实例化bean，添加到IoC容器
        doInstance();
        //DI依赖注入
        doAutowired();
        //初始化HandlerMapping
        initHandlerMapping();
        //初始化HandlerAdapter
        initHanlderAdapter();
    }

    private void initHanlderAdapter() {

    }

    private void initHandlerMapping() {

    }

    private void doAutowired() {

    }

    private void doInstance() {

    }

    private void doScanner(String packageName) {

    }

    private String loadConfig() {
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * 具体的请求分发啊
     * @author fuzy
     * @date  2020/3/30 18:17
     */
    private void doDispatch() {

    }


}
