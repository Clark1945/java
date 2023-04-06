package org.example.aop;

public class Smith implements NPC{
    public void talk(String name){
        System.out.println("What do you want?");
    }
    public void run(){
        System.out.println("Monster!~");
    }
}
