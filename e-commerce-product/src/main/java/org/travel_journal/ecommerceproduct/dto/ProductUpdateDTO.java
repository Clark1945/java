package org.travel_journal.ecommerceproduct.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductUpdateDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Integer stockQuantity;
    private Boolean isRefundable;
    private String mainPhoto;
    private LocalDate releaseDate;
}
