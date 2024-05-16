package org.example.design_pattern.behavior.prototype;

public class Prototype {
    /*
    Prototype 原型
    可以複製一個Object的屬性。
    繼承java.lang的Clonable介面，使用java物件的Object.clone功能
     */

    public static void main(String[] args) {
        Smith smith = new Smith();
        try {
            Sword sword = smith.craftSword();
            sword.setAttack("Flame");
            Sword copySword = smith.cloneSword(sword);
            System.out.println(sword.getAttack());
            System.out.println(copySword.getAttack());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
