package org.example;


import org.springframework.stereotype.Component;
@Component
public class Hello implements IHello{
    @Override
    public void hello(String name) {
        System.out.printf("Hello, %s%n", name);
    }
}
