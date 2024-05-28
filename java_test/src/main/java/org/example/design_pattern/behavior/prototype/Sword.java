package org.example.design_pattern.behavior.prototype;

class Sword implements Cloneable {

    private int attack;
    private String attribute;

    public Sword(String attribute, int attack) {
        this.attribute = attribute;
        this.attack = attack;
    }
    public int getAttack() {
        return attack;
    }

    public void printStatus() {
        System.out.println("Attack is " + attack + " ,attribute is " + attribute);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
