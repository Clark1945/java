package org.example.dynamicProxy;

public class AccountDAOImpl implements AccountDAO{
    public void accountByEmail(String email) {
        System.out.println("寄送");
    }
}
