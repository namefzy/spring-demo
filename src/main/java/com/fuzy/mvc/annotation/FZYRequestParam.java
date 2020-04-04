package com.fuzy.mvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName FZYRequestParam
 * @Description TODO
 * @Author 11564
 * @Date 2020/4/4 13:23
 * @Version 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FZYRequestParam {
    String value();
}
