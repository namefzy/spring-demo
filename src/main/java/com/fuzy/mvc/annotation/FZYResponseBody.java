package com.fuzy.mvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName FZYResponseBody
 * @Description TODO
 * @Author 11564
 * @Date 2020/4/4 14:29
 * @Version 1.0
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FZYResponseBody {
    String value() default "";
}
