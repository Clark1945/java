package org.example.design_pattern.behavior.prototype;

import java.util.Arrays;
import java.util.List;

class Smith {
    private List<String> attList = Arrays.asList("Fire", "Ice", "Lightning");
    private int attackRange = 10;

    Sword craftSword() {
        int attack = (int) (Math.random() * attackRange);
        String attribute = attList.get((int) (Math.random() * attList.size()));
        return new Sword(attribute, attack);
    }

    public Sword cloneSword(Sword sword) throws CloneNotSupportedException {
        return (Sword) sword.clone();
    }
}
