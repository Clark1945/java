package org.example.design_pattern.behavior.builder_pattern;

class MerchantFactory {
    public Merchant createMerchant(MerchantType merchantType) {
        if (merchantType.equals(MerchantType.GroceryShopKeeper)) {
            return new GroceryShopKeeper();
        } else {
            return new JewlyMerchant();
        }
    }
}
