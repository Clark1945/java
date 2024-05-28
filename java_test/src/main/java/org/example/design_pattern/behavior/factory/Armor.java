package org.example.design_pattern.behavior.factory;

public class Armor {
    private int defense;
    private int elementConsist;

    public Armor(int defense, int elementConsist) {
        this.defense = defense;
        this.elementConsist = elementConsist;
    }
    public void printStatus() {
        System.out.println("物理防禦力: " + defense + " 元素抵抗: " + elementConsist);
    }
}
