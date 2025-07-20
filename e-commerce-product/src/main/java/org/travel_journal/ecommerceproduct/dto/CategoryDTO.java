package org.travel_journal.ecommerceproduct.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private String name;
    private String description;
    private String iconUrl;
    private Boolean enabled = true;
}
