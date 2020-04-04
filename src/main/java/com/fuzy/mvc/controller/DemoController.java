package com.fuzy.mvc.controller;

import com.fuzy.mvc.annotation.*;
import com.fuzy.mvc.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName DemoController
 * @Description TODO
 * @Author 11564
 * @Date 2020/4/4 14:19
 * @Version 1.0
 */
@FZYController
@FZYRequestMapping("/demo")
public class DemoController {

    @FZYAutowired
    IDemoService demoService;

    @FZYRequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write(String.format("%s",demoService.getInfo()));
    }
}
