package com.odanylchuk.cosmocatsmarketplace.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class CartItemDto {

    Long id;

    ProductDto product;

    Integer quantity;
}
