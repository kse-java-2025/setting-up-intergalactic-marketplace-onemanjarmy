package com.odanylchuk.cosmocatsmarketplace.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class CategoryDto {

    Long id;

    String name;

    String description;
}
