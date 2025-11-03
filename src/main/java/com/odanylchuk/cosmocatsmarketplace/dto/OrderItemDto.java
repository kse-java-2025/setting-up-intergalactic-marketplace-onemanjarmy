package com.odanylchuk.cosmocatsmarketplace.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class OrderItemDto {

    Long id;

    ProductDto product;

    Integer quantity;

    BigDecimal priceAtPurchase;
}
