package org.travel_journal.ecommerceproduct.utility;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.travel_journal.ecommerceproduct.dto.ProductDTO;
import org.travel_journal.ecommerceproduct.dto.ProductUpdateDTO;
import org.travel_journal.ecommerceproduct.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDTO dto);

    ProductDTO toDTO(Product entity);

    // 更新時只套用不為 null 的欄位
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductUpdateDTO dto, @MappingTarget Product entity);

}
