package org.example;

import org.example.aop.LogProxy;
import org.example.aop.Merchant;
import org.example.aop.NPC;
import org.example.dependencyInjection.Braver;
import org.example.dependencyInjection.LightSaber;
import org.example.dependencyInjection.Sword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class smithClass = Class.forName("org.example.Smith");
        Smith smith = (Smith) smithClass.newInstance(); //取得類別
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
//        doAnotherThing();
    }

    private static void doAnotherThing() {
        //         AOP 手動注入
        NPC npc = (NPC) new LogProxy().getLogProxy(new Smith());
        npc.talk("Geralt");
        npc.run();

//        Dependency Injection
        Sword railGun = new LightSaber(); //定義聖劍
        Braver ALIS = new Braver(railGun); //讓勇者使用聖劍
        ALIS.Brave_Skill();
        ALIS.Sword_Skill();
        ALIS.Attack();

//        手動取得Beans
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext.xml"});
        NormalCharacter hello = (NormalCharacter) appContext.getBean("hello");
        hello.talk("John");

        CustomerBo customer = (CustomerBo) appContext.getBean("customerBo");
        customer.addCustomer();

    }
}