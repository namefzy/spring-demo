package com.fuzy.mvc.annotation;

import java.lang.annotation.*;

/**
 * Target：描述了注解修饰的对象范围
 *      METHOD：用于描述方法
 *      PACKAGE：用于描述包
 *      PARAMETER：用于描述方法变量
 *      TYPE：用于描述类、接口或enum类型
 * Retention: 表示注解保留时间长短
 *      SOURCE：在源文件中有效，编译过程中会被忽略
 *      CLASS：随源文件一起编译在class文件中，运行时忽略
 *      RUNTIME：在运行时有效
 * @ClassName FZYController 自定义控制器注解
 * @Description
 * @Author 11564
 * @Date 2020/4/4 13:12
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FZYController{
    String value() default "";
}
