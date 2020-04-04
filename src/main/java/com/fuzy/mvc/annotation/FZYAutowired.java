package com.fuzy.mvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName FZYAutowired
 * @Description TODO
 * @Author 11564
 * @Date 2020/4/4 14:22
 * @Version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FZYAutowired {
    String value() default "";
}
