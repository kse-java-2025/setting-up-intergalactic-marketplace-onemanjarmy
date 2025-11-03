package com.odanylchuk.cosmocatsmarketplace.domain.entity;

import com.odanylchuk.cosmocatsmarketplace.validation.CosmicWordCheck;
import jakarta.validation.constraints.*;
import lombok.Value;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class Product {

    Long id;
    String slug;

    @Builder.Default
    List<String> slugHistory = new ArrayList<>();

    @NotNull(message = "Product name is required")
    @Size(min = 3, max = 150, message = "Product name must be between 3 and 150 characters")
    @CosmicWordCheck()
    String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "1000.00", message = "Price must not exceed 1000")
    BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    Integer quantity;

    Category category;

    @Builder.Default
    Set<String> tags = new HashSet<>();

    @Builder.Default
    String currency = "IGC";
}
