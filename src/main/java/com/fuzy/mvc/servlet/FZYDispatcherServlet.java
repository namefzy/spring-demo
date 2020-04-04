package com.fuzy.mvc.servlet;

import com.fuzy.mvc.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuzy
 * @version 1.0
 * @Description
 * @date 2020/3/30 18:11
 */
public class FZYDispatcherServlet extends HttpServlet {

    private List<String> classNames = new ArrayList<String>();
    private ConcurrentHashMap<String,Object> ioc = new ConcurrentHashMap();
    private ConcurrentHashMap<String,Method> handlerMapping = new ConcurrentHashMap<String, Method>();


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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doDispatch(request,response);
    }

    /**
     * 具体的请求分发啊
     * @author fuzy
     * @date  2020/3/30 18:17
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response){
        try{

            String url = request.getRequestURI();
            if(!this.handlerMapping.containsKey(url)){
                response.getWriter().write("404 Not Found!!");
                return;
            }
            Method method = this.handlerMapping.get(url);
            Map<String,String[]> parameterMap = request.getParameterMap();
            String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
            method.invoke(ioc.get(beanName),new Object[]{request,response});
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
    *@Description 初始化handlerMapping,将映射关系存入
    *@Param
    *@Return
    *@Author fuzy
    *@Date 2020/4/4
    */
    private void initHandlerMapping() {
        if(ioc.isEmpty()){return;}
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //类上的@FZYRequestMapping注解
            String path = "";
            if(entry.getValue().getClass().isAnnotationPresent(FZYRequestMapping.class)){
                FZYRequestMapping annotation = entry.getValue().getClass().getAnnotation(FZYRequestMapping.class);
                //获取FZYRequestMapping上的路径
                path = annotation.value();
            }
            //方法上的@FZYRequestMapping注解
            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(FZYRequestMapping.class)){
                    FZYRequestMapping annotation1 = method.getAnnotation(FZYRequestMapping.class);
                    String path1 = annotation1.value();
                    handlerMapping.put(path+path1,method);
                }
            }
        }
    }

    /**
    *@Description 依赖注入，eg:给控制器注入service
    *@Param
    *@Return
    *@Author fuzy
    *@Date 2020/4/4
    */
    private void doAutowired() {
        if(ioc.isEmpty()){return;}
        for (Map.Entry<String,Object> entry : ioc.entrySet()) {
            /**
             * getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
             * getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段
             */
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if(!field.isAnnotationPresent(FZYAutowired.class)){
                    continue;
                }
                //FZYAutowired注解上的 beanName可以自定定义
                FZYAutowired autowired = field.getAnnotation(FZYAutowired.class);
                String beanName = autowired.value().trim();
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                    System.out.println("注解获取beanName:"+beanName);
                }
                //反射暴力访问
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
    *@Description 根据扫描到的className来反射生成实例对象
    *@Param
    *@Return
    *@Author fuzy
    *@Date 2020/4/4
    */
    private void doInstance() {
        if(classNames.isEmpty()){return;}
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(FZYController.class)) {
                    //key提取出来了，把value也搞出来
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                }else if(clazz.isAnnotationPresent(FZYService.class)){
                    //1、在多个包下出现相同的类名，只能自己起一个全局唯一的名字
                    //自定义命名
                    String beanName = clazz.getAnnotation(FZYService.class).value();
                    if("".equals(beanName.trim())){
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }

                    //2、默认的类名首字母小写
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    //3、如果是接口
                    //判断有多少个实现类，如果只有一个，默认就选择这个实现类
                    //如果有多个，只能抛异常
                    for (Class<?> i : clazz.getInterfaces()) {
                        if(ioc.containsKey(i.getName())){
                            throw new Exception("The " + i.getName() + " is exists!!");
                        }
                        ioc.put(i.getName(),instance);
                    }

                }else{
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
    *@Description 扫描com.fuzy.mvc包下的所有文件
    *@Param
    *@Return
    *@Author fuzy
    *@Date 2020/4/4
    */
    private void doScanner(String packageName) {
        //packageName=com.fuzy.mvc
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScanner(packageName+"."+file.getName());
            }else{
                //可能包含其他文件，所以这里只对.class文件进行处理
                if(file.getName().endsWith(".class")){
                    classNames.add(packageName+"."+file.getName().replace(".class",""));
                }
            }
        }

    }
    /**
    *@Description 加载配置文件
    *@Param
    *@Return
    *@Author fuzy
    *@Date 2020/4/4
    */
    private String loadConfig() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            return properties.getProperty("scanner");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *@Description 将类名转换为首字母小写
     *@Param
     *@Return
     *@Author fuzy
     *@Date 2020/4/4
     */
    private String toLowerFirstCase(String className) {
        char[] chars = className.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }
}
