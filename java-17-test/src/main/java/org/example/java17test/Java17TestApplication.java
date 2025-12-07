package org.example.java17test;

import io.github.cdimascio.dotenv.Dotenv; // 導入 Dotenv
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Java17TestApplication {

    public static void main(String[] args) {
        // 在 Spring 應用程式啟動之前載入 .env 檔案
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue())); // 將 .env 變數設定為系統屬性

        SpringApplication.run(Java17TestApplication.class, args);
    }

}
