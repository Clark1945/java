package org.example.javaAOP;


import org.springframework.stereotype.Component;

@Component
public class Smith implements NormalCharacter {
    private String smithName = "Kimson";
    private int ID = 0631;
    private int power = 70;

    @Override
    public void talk(String name) {
        System.out.printf("Hello, %s%n", name);
    }

    private void SamuelJackson(String name) {
        System.out.printf("MDFK, %s%n", name);
    }
}
