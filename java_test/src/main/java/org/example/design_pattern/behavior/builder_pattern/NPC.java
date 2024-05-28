package org.example.design_pattern.behavior.builder_pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class NPC {
    private String name;
    private int age;
    private String gender;


}

