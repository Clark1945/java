package org.example;

import org.example.Optional.JavaOptionalTest;
import org.example.dependencyInjection.Braver;
import org.example.dependencyInjection.LightSaber;
import org.example.dependencyInjection.Sword;
import org.example.dynamicProxy.*;
import org.example.javaAOP.CustomerBo;
import org.example.javaAOP.NormalCharacter;
import org.example.javaAOP.Smith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static AccountDAO accountDAO;
    public static void main(String[] args) {
//        moreGreenGrass();

        JavaOptionalTest javaOptionalTest = new JavaOptionalTest();
        Assert.isTrue(javaOptionalTest.isOpPresent());
        Assert.isTrue(!javaOptionalTest.isOpNullPresent());
        javaOptionalTest.isOpPresentThenPrint();
        javaOptionalTest.isOpPresentThenNotPrint();
        Assert.hasText("Clark",javaOptionalTest.OpReturnNotExist());
        Assert.hasText("Not Exist",javaOptionalTest.OpNullReturnNotExist());
        Assert.hasText("Not Exist", javaOptionalTest.OpNullReturnNotExist2());
        javaOptionalTest.OpNotNullPrintMessage();
        javaOptionalTest.OptionalMapFilterTesting();
        javaOptionalTest.OptionalMapFilterRangeTesting();
        javaOptionalTest.Ch502();
    }

    private static void doDynamicProxy() {
        accountDAO = new AccountDAOImpl();
        accountDAO = accountDAO();
        ((Nullable)accountDAO).enable();
        accountDAO.accountByEmail(null);
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

    private static void doWrapper() {
        NullableAccountDAOProxy accountDAO = new NullableAccountDAOProxy();
        accountDAO.NullableProxy(new AccountDAOImpl());
        System.out.println("Isenable ? " + accountDAO.isEnabled());
        accountDAO.enable();
        System.out.println("Isenable ? " + accountDAO.isEnabled());
        accountDAO.disable();
        System.out.println("Isenable ? " + accountDAO.isEnabled());
        ((Nullable) accountDAO).enable();
        accountDAO.accountByEmail(null);
        ((Nullable) accountDAO).disable();
        accountDAO.accountByEmail(null);
    }

    public static AccountDAO accountDAO() {
        List<Class<?>> interfaces = new ArrayList<>(
                Arrays.asList(accountDAO.getClass().getInterfaces())
        );
        interfaces.add(Nullable.class);
        return (AccountDAO) Proxy.newProxyInstance(
                accountDAO.getClass().getClassLoader(), // AccountDAOImpl ClassLoader
                interfaces.toArray(new Class[interfaces.size()]), // AccountDAO, Nullable
                new NewNullableProxy(accountDAO)
        );
    }

    // show a array to display every farmer see whose field was greener. Usually the greenest one would not aware greenest himself. But if greenest field exist more than 1.

    public static void moreGreenGrass() {
        String[] lines = {"10","1 3 2 4 5 3 0 111 113 -1"}; // input
        String[] arrayList = lines[1].split(" ");
        int maxValue = 0;
        int maxIndex =0;
        int secondMaxValue = 0;
        int val = 0;
        for (int i = 0, l = arrayList.length; i < l; i++) {
            val = Integer.parseInt(arrayList[i]);
            if (val == maxValue) { // if the second greenest appear. output shall be the same.
                secondMaxValue=val;
            }
            if (val > maxValue) { // val to the maxVal, former maxVal to the secondMaxVal
                secondMaxValue = maxValue;
                maxValue = val;
                maxIndex = i;
            }
            if (val > secondMaxValue && val < maxValue) { // if val is smaller than maxValue but it's bigger than secondVal, shall update secondVal only.
                secondMaxValue = val;
            }

        }
        for (int i = 0;i<arrayList.length;i++) {
            int printValue;
            if (i == maxIndex) {
                printValue = secondMaxValue;
            } else {
                printValue = maxValue;
            }
            String output = String.format("%s", printValue);
            System.out.println(output);
        }
    }
}