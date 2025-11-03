package com.odanylchuk.cosmocatsmarketplace.domain.entity;

import lombok.Value;
import lombok.Builder;

@Value
@Builder(toBuilder = true)
public class Category {

    Long id;
    String name;
    String description;
}
