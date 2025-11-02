package com.odanylchuk.cosmocatsmarketplace.dto;

import com.odanylchuk.cosmocatsmarketplace.validation.CosmicWordCheck;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ProductUpdateDto {

    @NotNull(message = "Product name is required")
    @Size(min = 3, max = 150, message = "Product name must be between 3 and 150 characters")
    @CosmicWordCheck
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

    @NotNull(message = "Category is required")
    String category;

    Set<String> tags;
}
