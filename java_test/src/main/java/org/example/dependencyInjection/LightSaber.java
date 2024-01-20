package org.example.dependencyInjection;

public class LightSaber implements Sword {
    @Override
    public void Slash(){
        System.out.print("斬擊\n");
    }
    @Override
    public void Special_Skill() {
        System.out.print("使用聖劍技能：光よ\n");
    }
}
