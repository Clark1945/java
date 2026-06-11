package org.example;

import org.example.dependency_injection.Braver;
import org.example.dependency_injection.LightSaber;
import org.example.dependency_injection.Sword;
import org.example.dynamic_proxy.*;
import org.example.java_aop.CustomerBo;
import org.example.java_aop.NormalCharacter;
import org.example.java_aop.Smith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    private static AccountDAO accountDAO;
    public static void main(String[] args) {
//        moreGreenGrass();
//        JavaOptionalTest javaOptionalTest = new JavaOptionalTest();
//        Assert.isTrue(javaOptionalTest.isOpPresent());
//        Assert.isTrue(!javaOptionalTest.isOpNullPresent());
//        javaOptionalTest.isOpPresentThenPrint();
//        javaOptionalTest.isOpPresentThenNotPrint();
//        Assert.hasText("Clark",javaOptionalTest.OpReturnNotExist());
//        Assert.hasText("Not Exist",javaOptionalTest.OpNullReturnNotExist());
//        Assert.hasText("Not Exist", javaOptionalTest.OpNullReturnNotExist2());
//        javaOptionalTest.OpNotNullPrintMessage();
//        javaOptionalTest.OptionalMapFilterTesting();
//        javaOptionalTest.OptionalMapFilterRangeTesting();
//        javaOptionalTest.Ch502();
//        ServerSocketExample();

        GraphBFS graph = new GraphBFS();

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(2, 6);

        System.out.println("BFS traversal starting from node 0:");
        graph.bfs(0);
    }

    private static void ServerSocketExample() {
        final int port = 7;
        ServerSocket serverSkt;
        Socket skt;
        BufferedReader sktReader;
        String message;
        PrintStream sktStream;

        try {
            serverSkt = new ServerSocket(port);

            while(true) {
                System.out.printf("連接埠 %d 接受連線中......%n", port);
                skt = serverSkt.accept();
                System.out.printf("與 %s 建立連線%n",
                        skt.getInetAddress().toString());

                sktReader = new BufferedReader(new
                        InputStreamReader(skt.getInputStream()));

                while((message = sktReader.readLine()) != null) {
                    if(message.equals("/bye")) {
                        System.out.println("Bye!");
                        skt.close();
                        break;
                    }

                    System.out.printf("Client: %s%n", message);
                    sktStream = new PrintStream(skt.getOutputStream());
                    sktStream.printf("echo: %s%n", message);
                }
            }
        } catch(IOException e) {
            System.out.println(e.toString());
        }
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
        Class smithClass = Class.forName("org.example.java_aop.Smith");
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

class GraphBFS {

    // 使用鄰接表表示圖
    private Map<Integer, List<Integer>> adjacencyList;

    public GraphBFS() {
        adjacencyList = new HashMap<>();
    }

    // 添加邊
    public void addEdge(int source, int destination) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        adjacencyList.get(source).add(destination);
        // 如果是無向圖，加上這行
        // adjacencyList.get(destination).add(source);
    }

    // BFS 遍歷
    public void bfs(int startNode) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        // 從起始節點開始
        queue.offer(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");

            // 訪問所有相鄰節點
            List<Integer> neighbors = adjacencyList.getOrDefault(current, new ArrayList<>());
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }
}