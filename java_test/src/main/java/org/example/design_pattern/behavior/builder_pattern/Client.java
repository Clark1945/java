package org.example.design_pattern.behavior.builder_pattern;

public class Client {
    /*
    這裡是簡單工廠模式的範例
     */
    public static void main(String[] args) {
        NPC normalNPC = NPC.builder()
                .name("Draven")
                .age(34)
                .gender("M")
                .build();

        NPC normalNPC2 = new NPC();
        normalNPC2.setAge(51);
        normalNPC2.setGender("F");
        normalNPC2.setName("Lerveark");
    }
}
