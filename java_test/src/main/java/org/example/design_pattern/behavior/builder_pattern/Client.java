package org.example.design_pattern.behavior.builder_pattern;

public class Client {
    /*
    這裡是簡單工廠模式的範例
     */
    public static void main(String[] args) {
        MerchantFactory heroFactory = new MerchantFactory();
        Merchant merchant = heroFactory.createMerchant(MerchantType.GroceryShopKeeper);
        merchant.bargin();
    }


}
