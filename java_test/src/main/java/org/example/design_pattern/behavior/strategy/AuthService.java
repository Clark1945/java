package org.example.design_pattern.behavior.strategy;

public class AuthService implements TransactionService{
    @Override
    public void verify() {
        System.out.println("授權驗證");
    }

    @Override
    public void transaction() {
        System.out.println("進行交易");
    }

    @Override
    public void createResponse() {
        System.out.println("建立回傳物件");
    }
}

// 相似的功能放置在同一個類別 策略模式
// 利用介面建立模板方法