package com.odanylchuk.cosmocatsmarketplace.mapper;

import com.odanylchuk.cosmocatsmarketplace.domain.entity.Category;
import com.odanylchuk.cosmocatsmarketplace.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    void toDto_and_toEntity() {
        Category cat = Category.builder().id(5L).name("Food").description("Cat food").build();
        CategoryDto dto = mapper.toDto(cat);
        assertEquals(cat.getName(), dto.getName());
        assertEquals(cat.getDescription(), dto.getDescription());

        CategoryDto in = CategoryDto.builder().name("Accessories").description("Cool stuff").build();
        Category entity = mapper.toEntity(in);
        assertEquals(in.getName(), entity.getName());
        assertEquals(in.getDescription(), entity.getDescription());
    }
}

