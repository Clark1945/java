package org.example.dependencyInjection;

public class Braver {
    private Sword sword;
    public Braver(Sword s){
        this.sword=s;
        System.out.print("勇者裝備了 劍\n");
    }
    public void Brave_Skill(){
        System.out.print("使用技能：不屈\n");
    }
    public void Attack(){
        this.sword.Slash();
    }
    public void Sword_Skill(){
        this.sword.Special_Skill();
    }
}
