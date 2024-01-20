package org.example.aop;

public class Merchant implements NPC {
    public void talk(String name) {
        System.out.println("Would you like a few round of gwent? " + name);
    }
    public void run() {
        System.out.println("Arrrrrr!");
    }
}
