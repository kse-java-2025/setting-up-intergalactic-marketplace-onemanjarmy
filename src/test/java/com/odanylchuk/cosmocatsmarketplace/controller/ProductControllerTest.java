package com.odanylchuk.cosmocatsmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductCreateDto;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductDto;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductUpdateDto;
import com.odanylchuk.cosmocatsmarketplace.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = Mockito.mock(ProductService.class);
        ProductController controller = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.odanylchuk.cosmocatsmarketplace.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void createProduct_valid_thenCreated() throws Exception {
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Space Mouse")
                .price(new BigDecimal("9.99"))
                .quantity(10)
                .category("Toys")
                .tags(Set.of("toy"))
                .build();

        ProductDto returned = ProductDto.builder()
                .slug("stellar-mouse")
                .name(createDto.getName())
                .price(createDto.getPrice())
                .quantity(createDto.getQuantity())
                .category(createDto.getCategory())
                .tags(createDto.getTags())
                .build();

        Mockito.when(productService.createProduct(any(ProductCreateDto.class))).thenReturn(returned);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("stellar-mouse"));
    }

    @Test
    void createProduct_invalid_thenBadRequest() throws Exception {
        // name too short and price null
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("ab")
                .price(null)
                .quantity(-1)
                .category("")
                .build();

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void updateProduct_invalid_thenBadRequest() throws Exception {
        ProductUpdateDto updateDto = ProductUpdateDto.builder()
                .name("x")
                .price(new BigDecimal("0.00"))
                .quantity(-5)
                .category("")
                .build();

        mockMvc.perform(put("/api/v1/products/some-slug")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void getProductBySlug_ok() throws Exception {
        ProductDto dto = ProductDto.builder()
                .slug("anti-gravity-yarn-ball")
                .name("Anti-Gravity Space Yarn Ball")
                .price(new BigDecimal("25.99"))
                .quantity(50)
                .category("Toys")
                .build();

        Mockito.when(productService.getProductBySlug(eq("anti-gravity-yarn-ball"), eq("IGC"))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/products/anti-gravity-yarn-ball"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("anti-gravity-yarn-ball"));
    }
}
