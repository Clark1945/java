package org.example;

import org.example.dependencyInjection.Braver;
import org.example.dependencyInjection.LightSaber;
import org.example.dependencyInjection.Sword;
import org.example.javaAOP.CustomerBo;
import org.example.javaAOP.NormalCharacter;
import org.example.javaAOP.Smith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        doJavaReflection();
        doAnotherThing();
    }

    // Java反射 練習
    public static void doJavaReflection() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("Java Reflection started");
        Class smithClass = Class.forName("org.example.javaAOP.Smith");
        Smith smith = (Smith) smithClass.newInstance(); // 以反射取得類別
        smith.talk("Geralt");

        Field[] fields = smithClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            System.out.println("field" + i + " : " + fields[i].getName()); // 印出類別下的所有屬性
        }

        Method[] methods = smithClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) { // 印出類別下的所有方法
            System.out.println("method" + i + " : " + methods[i].getName());
            System.out.println("應傳入參數型別為 = " + Arrays.toString(methods[i].getGenericParameterTypes()));
        }

        System.out.println("Java Reflection end");
    }

    private static void doAnotherThing() {
//        Dependency Injection
        Sword railGun = new LightSaber(); //定義聖劍
        Braver ALIS = new Braver(railGun); //讓勇者使用聖劍
        ALIS.Brave_Skill();
        ALIS.Sword_Skill();
        ALIS.Attack();

//        手動取得Beans
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext.xml"});
        NormalCharacter normalNPC = (NormalCharacter) appContext.getBean("hello");
        normalNPC.talk("John");

        CustomerBo customer = (CustomerBo) appContext.getBean("customerBo");
        customer.addCustomer();
    }
}