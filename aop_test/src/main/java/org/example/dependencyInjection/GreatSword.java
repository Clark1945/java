package org.example.dependencyInjection;

public class GreatSword implements Sword {
    @Override
    public void Slash(){
        System.out.print("大劍揮砍\n");
    }
    @Override
    public void Special_Skill(){
        System.out.print("重劈\n");
    }
}
