package org.example.design_pattern.creational.singleton;

public class RedDragon {
    private RedDragon() {
    }

    public static RedDragon getInstance() {
        return RedDragonHolder.instance;
    }

    /**
     * 靜態的內部類別
     */
    private static class RedDragonHolder {
        private static RedDragon instance = new RedDragon();
    }
}
