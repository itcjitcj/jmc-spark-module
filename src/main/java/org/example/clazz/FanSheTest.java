package org.example.clazz;

import lombok.SneakyThrows;

public class FanSheTest {
    @SneakyThrows
    public static void main(String[] args) {
        test1();
    }

    //定义一个方法 用来测试反射
    public static void test1() throws ClassNotFoundException {
        //1.通过对象的getClass方法
        FanSheTest fanSheTest = new FanSheTest();
        Class<? extends FanSheTest> aClass = fanSheTest.getClass();
        System.out.println(aClass);
        //2.通过类名.class
        Class<FanSheTest> fanSheTestClass = FanSheTest.class;
        System.out.println(fanSheTestClass);
        //3.通过Class.forName
        Class<?> aClass1 = Class.forName("org.example.clazz.FanSheTest");
        System.out.println(aClass1);
    }

}
