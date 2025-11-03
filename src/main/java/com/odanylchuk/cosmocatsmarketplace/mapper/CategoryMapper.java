package com.odanylchuk.cosmocatsmarketplace.mapper;

import com.odanylchuk.cosmocatsmarketplace.domain.entity.Category;
import com.odanylchuk.cosmocatsmarketplace.dto.CategoryDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto dto);
}
