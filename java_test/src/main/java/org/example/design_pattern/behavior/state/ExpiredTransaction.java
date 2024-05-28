package org.example.design_pattern.behavior.state;

public class ExpiredTransaction implements Transaction {
    @Override
    public void getStatus() {
        System.out.println("Transaction is expired");
    }

    @Override
    public String getStatusMessage() {
        return "過期交易";
    }

}
