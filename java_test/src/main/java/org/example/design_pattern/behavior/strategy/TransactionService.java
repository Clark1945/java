package org.example.design_pattern.behavior.strategy;

public interface TransactionService {
    public void verify();

    public void transaction();

    public void createResponse();
}
