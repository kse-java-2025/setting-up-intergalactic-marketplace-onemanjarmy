package com.odanylchuk.cosmocatsmarketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ProductDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String slug;

    String name;
    String description;
    BigDecimal price;
    Integer quantity;
    String category;
    Set<String> tags;

    @Builder.Default
    String currency = "IGC";
}
