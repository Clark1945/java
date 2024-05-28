package org.example.design_pattern.behavior.state;

public class NormalTransaction implements Transaction {
    @Override
    public void getStatus() {
        System.out.println("這筆訂單可以正常交易");
    }

    @Override
    public String getStatusMessage() {
        return "NORMAL";
    }
}
