package org.example.design_pattern.creational.factory;

public class Tier extends GrandMasterArmorer {
    @Override
    public WitcherSuite createOwlArmor() {
        return new OwlArmor();
    }

    @Override
    public WitcherSuite createGraffinArmor() {
        return new GraffinArmor();
    }

    @Override
    public WitcherSuite createWolfArmor() {
        return new WolfArmor();
    }

    @Override
    public WitcherSuite createBearArmor() {
        return new BearArmor();
    }
}
