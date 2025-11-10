package com.odanylchuk.cosmocatsmarketplace.mapper;

import com.odanylchuk.cosmocatsmarketplace.domain.entity.Category;
import com.odanylchuk.cosmocatsmarketplace.domain.entity.Product;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductCreateDto;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductDto;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductUpdateDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void toDto_and_toEntity_roundTrip() {
        Product product = Product.builder()
                .id(10L)
                .slug("space-toy")
                .name("Space Toy")
                .description("Fun toy")
                .price(new BigDecimal("12.34"))
                .quantity(5)
                .category(Category.builder().id(1L).name("Toys").build())
                .tags(Set.of("toy", "space"))
                .currency("IGC")
                .build();

        ProductDto dto = mapper.toDto(product);
        assertEquals(product.getName(), dto.getName());
        assertEquals(product.getCategory().getName(), dto.getCategory());

        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Space Ship")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .category("Toys")
                .build();

        Product fromCreate = mapper.toEntity(createDto);
        assertEquals(createDto.getName(), fromCreate.getName());
        assertEquals("IGC", fromCreate.getCurrency());
    }

    @Test
    void applyUpdateFromDto_updatesFields() {
        Product old = Product.builder()
                .id(1L)
                .slug("old-slug")
                .name("Old Name")
                .price(new BigDecimal("10.00"))
                .quantity(2)
                .category(Category.builder().id(1L).name("Toys").build())
                .build();

        ProductUpdateDto updateDto = ProductUpdateDto.builder()
                .name("New Name")
                .price(new BigDecimal("15.50"))
                .quantity(3)
                .category("Toys")
                .build();

        Product updated = mapper.applyUpdateFromDto(updateDto, old);
        assertEquals(old.getId(), updated.getId());
        assertEquals(old.getSlug(), updated.getSlug());
        assertEquals(updateDto.getName(), updated.getName());
        assertEquals(updateDto.getPrice(), updated.getPrice());
        assertEquals(updateDto.getQuantity(), updated.getQuantity());
    }
}

