package org.example.design_pattern.creational.prototype;

import java.util.ArrayList;

public class Prototype {
    /*
    Prototype 原型
    可以複製一個Object的屬性。
    繼承java.lang的Clonable介面，使用java物件的Object.clone功能
     */

    public static void main(String[] args) throws CloneNotSupportedException {
        Smith smith = new Smith();

        ArrayList<Sword> swords = new ArrayList<Sword>();

        for (int i = 0; i < 5; i++) {
            Sword sword = smith.craftSword();
            sword.printStatus();
            swords.add(sword);

            if (sword.getAttack() > 8) {
                System.out.println("額外製作一把");
                Sword copySword = smith.cloneSword(sword);
                copySword.printStatus();
                swords.add(sword);
            }
        }
        System.out.println("今天總製作" + swords.size() + "把劍");
    }
}
