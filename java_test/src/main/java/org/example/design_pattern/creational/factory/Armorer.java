package org.example.design_pattern.creational.factory;

public class Armorer {


    public Armor craftNormalArmor(ArmorQuality armorQuality) {
        switch (armorQuality){
            case BLUE:
                return new Armor(10,6);
            case GREEN:
                return new Armor(3,5);
            case PURPLE:
                return new Armor(23,19);
            case BROWN:
                return new Armor(57,43);
        }
        return null;
    }

    public WitcherSuite craftWitcherSuite(Faction faction) {
        switch (faction) {
            case OWL:
                return new OwlArmor();
            case GRAFFIN:
                return new GraffinArmor();
        }
        return null;
    }
}
