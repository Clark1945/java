package org.travel_journal.ecommerceproduct.utility;

import org.mapstruct.Mapper;
import org.travel_journal.ecommerceproduct.entity.Category;
import org.travel_journal.ecommerceproduct.dto.CategoryDTO;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDTO dto);
    CategoryDTO toDTO(Category category);
}

