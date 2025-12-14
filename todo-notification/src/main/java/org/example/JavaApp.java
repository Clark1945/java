package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.googletask.GoogleTasksService;
import org.example.linebot.LinePushService;
import org.example.writeimage.TodoListDrawer;

public class JavaApp {

    public static void main(String[] args) throws Exception {
        System.out.println("Start process!");

        // 在 Spring 應用程式啟動之前載入 .env 檔案
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue())); // 將 .env 變數設定為系統屬性

        GoogleTasksService googleTasksService = new GoogleTasksService();
        TodoListDrawer todoListDrawer = new TodoListDrawer(googleTasksService);

        LinePushService linePushService = new LinePushService(System.getProperty("LINE_CHANNEL_TOKEN"),System.getProperty("SUPERBASE_SECRET_KEY"));

        todoListDrawer.drawImage(); // 使用注入的 todoListDrawer 實例
        linePushService.pushImageMessage(System.getProperty("USER_ID"));

        System.out.println("Process finished!");
    }
}
