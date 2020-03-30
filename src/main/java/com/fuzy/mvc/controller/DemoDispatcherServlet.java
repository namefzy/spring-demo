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
     * @param request
     * @param response
     */
    private void doDispatch() {

    }


}
