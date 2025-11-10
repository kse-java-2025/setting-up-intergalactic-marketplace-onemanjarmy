package com.odanylchuk.cosmocatsmarketplace.service;

import com.odanylchuk.cosmocatsmarketplace.domain.entity.Category;
import com.odanylchuk.cosmocatsmarketplace.domain.entity.Product;
import com.odanylchuk.cosmocatsmarketplace.dto.*;
import com.odanylchuk.cosmocatsmarketplace.exception.ResourceMovedPermanentlyException;
import com.odanylchuk.cosmocatsmarketplace.exception.ResourceNotFoundException;
import com.odanylchuk.cosmocatsmarketplace.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productMapper);
        // initialize mock data via PostConstruct
        // call initializeMockData using reflection
        // (I made it private so outside test environment it looks like a normal class,
        // if you know a way to do it prettier – let me know ;) )
        try {
            var method = ProductServiceImpl.class.getDeclaredMethod("initializeMockData");
            method.setAccessible(true);
            method.invoke(productService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllProducts_defaultPaginationAndCurrency() {
        ProductQuery query = ProductQuery.builder().build();
        PaginatedProductListDto result = productService.getAllProducts(query);
        assertNotNull(result);
        assertTrue(result.getTotalItems() >= 1);
        assertEquals(1, result.getCurrentPage());
        assertEquals(10, result.getPageSize());
        assertTrue(result.getItems().stream().allMatch(p -> "IGC".equals(p.getCurrency())));
    }

    @Test
    void testGetAllProducts_filterByCategory_and_search() {
        ProductQuery query = ProductQuery.builder().category("Toys").search("yarn").build();
        PaginatedProductListDto result = productService.getAllProducts(query);
        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getItems().size());
        assertTrue(result.getItems().getFirst().getName().toLowerCase().contains("yarn"));
    }

    @Test
    void testGetProductBySlug_found() {
        ProductDto dto = productService.getProductBySlug("anti-gravity-yarn-ball", "USD");
        assertNotNull(dto);
        assertEquals("anti-gravity-yarn-ball", dto.getSlug());
        assertEquals("USD", dto.getCurrency());
    }

    @Test
    void testGetProductBySlug_moved() {
        // create product then update name to change slug and create history
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Galactic Toy")
                .price(new BigDecimal("10.00"))
                .quantity(5)
                .category("Toys")
                .build();
        ProductDto created = productService.createProduct(createDto);
        ProductUpdateDto updateDto = ProductUpdateDto.builder()
                .name("Galactic Toy Updated")
                .price(new BigDecimal("12.00"))
                .quantity(5)
                .category("Toys")
                .build();
        ProductDto updated = productService.updateProduct(created.getSlug(), updateDto, "IGC");

        String oldSlug = created.getSlug();
        assertNotEquals(oldSlug, updated.getSlug());

        // retrieving by old slug should throw ResourceMovedPermanentlyException
        assertThrows(ResourceMovedPermanentlyException.class, () -> productService.getProductBySlug(oldSlug, "IGC"));
    }

    @Test
    void testGetProductBySlug_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductBySlug("non-existent-slug", "IGC"));
    }

    @Test
    void testCreateProduct_and_delete() {
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Space Fish")
                .price(new BigDecimal("5.50"))
                .quantity(2)
                .category("Food")
                .build();
        ProductDto created = productService.createProduct(createDto);
        assertNotNull(created.getSlug());
        assertEquals("IGC", created.getCurrency());

        productService.deleteProduct(created.getSlug());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductBySlug(created.getSlug(), "IGC"));
    }

    @Test
    void testUpdateProduct_nameChange_and_slugHistory() {
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Nebula Collar")
                .price(new BigDecimal("15.00"))
                .quantity(3)
                .category("Toys")
                .build();
        ProductDto created = productService.createProduct(createDto);

        ProductUpdateDto updateDto = ProductUpdateDto.builder()
                .name("Nebula Collar V2")
                .price(new BigDecimal("17.00"))
                .quantity(4)
                .category("Toys")
                .build();

        ProductDto updated = productService.updateProduct(created.getSlug(), updateDto, "EUR");
        assertEquals("EUR", updated.getCurrency());
        assertNotEquals(created.getSlug(), updated.getSlug());

        // old slug should result in moved
        assertThrows(ResourceMovedPermanentlyException.class, () -> productService.getProductBySlug(created.getSlug(), "EUR"));
    }

    @Test
    void testDeleteProduct_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct("does-not-exist"));
    }
}

