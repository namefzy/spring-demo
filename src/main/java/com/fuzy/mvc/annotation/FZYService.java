package com.fuzy.mvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName FZYController 自定义service注解
 * @Description TODO
 * @Author 11564
 * @Date 2020/4/4 13:12
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FZYService {
    String value() default "";
}
