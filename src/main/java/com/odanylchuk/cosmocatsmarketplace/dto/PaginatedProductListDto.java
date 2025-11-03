package com.odanylchuk.cosmocatsmarketplace.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class PaginatedProductListDto {

    List<ProductDto> items;

    Long totalItems;

    Integer totalPages;

    Integer currentPage;

    Integer pageSize;
}
