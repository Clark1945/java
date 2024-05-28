package org.example.design_pattern.behavior.singleton;

// 積極單例模式
public class GoldenDragon {

    //創建 GoldenDragon 的一個對象
    private static GoldenDragon instance = new GoldenDragon();

    //讓構造函數為 private，這樣該類就不會被實例化
    private GoldenDragon() {
    }

    //獲取唯一可用的對象
    public static GoldenDragon getInstance() {
        return instance;
    }
}
