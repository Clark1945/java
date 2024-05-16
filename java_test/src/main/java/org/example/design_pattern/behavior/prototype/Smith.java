package org.example.design_pattern.behavior.prototype;

class Smith {
    // Prototype registry
    public Sword craftSword() {
        return new Sword();
    }
    public Sword cloneSword(Sword sword) throws CloneNotSupportedException {
        return (Sword) sword.clone();
    }
}
