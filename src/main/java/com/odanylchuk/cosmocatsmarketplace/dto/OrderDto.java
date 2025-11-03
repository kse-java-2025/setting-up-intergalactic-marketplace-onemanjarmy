package com.odanylchuk.cosmocatsmarketplace.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class OrderDto {

    Long id;

    String orderNumber;

    String userId;

    Set<OrderItemDto> items;

    String status;

    BigDecimal totalPrice;

    String shippingAddress;

    LocalDateTime createdAt;

    LocalDateTime completedAt;
}
