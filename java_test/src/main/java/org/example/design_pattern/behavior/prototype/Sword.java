package org.example.design_pattern.behavior.prototype;

class Sword implements Cloneable {
    //Prototype

    private String attack;
    public String getAttack() {
        return attack;
    }
    public void setAttack(String attack) {
        this.attack = attack;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
