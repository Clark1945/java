package org.example;

import org.example.aop.LogProxy;
import org.example.aop.Merchant;
import org.example.aop.NPC;
import org.example.aop.Smith;
import org.example.dependencyInjection.Braver;
import org.example.dependencyInjection.LightSaber;
import org.example.dependencyInjection.Sword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {


////        Dependency Injection
//        System.out.print("覺醒吧勇者\n");
//        Sword railGun = new LightSaber(); //定義聖劍
////        Sword MSowrd = new GreatSword();
//        Braver ALIS = new Braver(railGun); //讓勇者使用聖劍
//        ALIS.Brave_Skill();
//        ALIS.Sword_Skill();
//        ALIS.Attack();
//
////        AOP實作
////      NPC vivaldi = (NPC) new LogProxy().getLogProxy(new Merchant());
//        NPC Velen = (NPC) new LogProxy().getLogProxy(new Smith());
//        Velen.talk("Geralt");
//        Velen.run();
//

        ApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[] { "applicationContext.xml" });
//
        IHello hello = (IHello) appContext.getBean("hello");
        hello.hello("John");
//
        CustomerBo customer = (CustomerBo) appContext.getBean("customerBo");
        customer.addCustomer();
//        customer.extra();

    }
}