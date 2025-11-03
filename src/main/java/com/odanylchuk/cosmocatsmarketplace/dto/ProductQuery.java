package com.odanylchuk.cosmocatsmarketplace.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
public class ProductQuery {
    @Min(value = 1, message = "Page number must be at least 1")
    Integer page;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    Integer size;

    @Pattern(regexp = "^(name|-name|price|-price|quantity|-quantity)$",
            message = "Sort must be one of: name, -name, price, -price, quantity, -quantity")
    String sort;

    String category;

    @Min(value = 0, message = "Minimum price cannot be negative")
    BigDecimal minPrice;

    @Min(value = 0, message = "Maximum price cannot be negative")
    BigDecimal maxPrice;

    String search;

    @Pattern(regexp = "^([A-Z]{3})$", message = "Currency must be specified in international format, e.g. USD")
    String currency;
}
