package com.odanylchuk.cosmocatsmarketplace.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class CartDto {

    Long id;

    String userId;

    Set<CartItemDto> items;

    BigDecimal totalPrice;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
