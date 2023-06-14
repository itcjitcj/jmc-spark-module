package org.example.clazz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@MyAnnotation("hello")
public class ZhuJieTest {
    public static void main(String[] args) {
        //获取类上的注解
        MyAnnotation annotation = ZhuJieTest.class.getAnnotation(MyAnnotation.class);
        System.out.println(annotation.value());
    }
}

//添加一个注解
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    String value() default "";
}