package org.example.design_pattern.behavior.strategy;

public class RefundService implements TransactionService {

    @Override
    public void verify() {
        System.out.println("退貨驗證");
    }

    @Override
    public void transaction() {
        System.out.println("退貨");
    }

    @Override
    public void createResponse() {
        System.out.println("建立退貨回傳物件");
    }
}
