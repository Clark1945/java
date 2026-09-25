package org.travel_journal.ecommerceproduct.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductDTO {
    private LocalDate establishDate;
    private LocalDate releaseDate;
    private String description;
    private Long categoryId;
    private boolean isRefundable;
    private String mainPhoto;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;

    // 這邊不開放使用者輸入 viewCount、saleAmount、status 等內部欄位
    // 可加上 Validation 註解如 @NotBlank、@Min 等
}
