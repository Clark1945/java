package org.example.design_pattern.behavior.factory;

public class Factory {

    public static void main(String[] args) {
        Armorer armorer = new Armorer();
        Armor worstArmor = armorer.craftNormalArmor(ArmorQuality.GREEN);
        worstArmor.printStatus();
        Armor bestArmor = armorer.craftNormalArmor(ArmorQuality.BROWN);
        bestArmor.printStatus();
        // 簡單工廠模式

        WitcherSuite firstWitcherSuite = armorer.craftWitcherSuite(Faction.OWL);
        firstWitcherSuite.getArmorClass();
        WitcherSuite secondWitcherSuite = armorer.craftWitcherSuite(Faction.GRAFFIN);
        secondWitcherSuite.getArmorClass();
        // 抽象工廠模式
    }
}
