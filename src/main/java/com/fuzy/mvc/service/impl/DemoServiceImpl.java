package com.fuzy.mvc.service.impl;

import com.fuzy.mvc.annotation.FZYService;
import com.fuzy.mvc.service.IDemoService;

/**
 * @ClassName DemoServiceImpl
 * @Description TODO
 * @Author 11564
 * @Date 2020/4/4 14:24
 * @Version 1.0
 */
@FZYService
public class DemoServiceImpl implements IDemoService {

    public String getInfo() {
        return "Hello World";
    }
}
