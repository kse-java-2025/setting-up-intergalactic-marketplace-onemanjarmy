package com.odanylchuk.cosmocatsmarketplace.mapper;

import com.odanylchuk.cosmocatsmarketplace.domain.entity.Product;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductCreateDto;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductDto;
import com.odanylchuk.cosmocatsmarketplace.dto.ProductUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "category", source = "category.name")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "currency", constant = "IGC")
    Product toEntity(ProductCreateDto dto);


    // --- Fields from oldProduct (unchanged) ---
    @Mapping(target = "id", source = "oldProduct.id")
    @Mapping(target = "slug", source = "oldProduct.slug")
    @Mapping(target = "currency", source = "oldProduct.currency")

    // --- Fields from dto (the new values) ---
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "price", source = "dto.price")
    @Mapping(target = "quantity", source = "dto.quantity")
    @Mapping(target = "tags", source = "dto.tags")

    // --- Field to be handled in the service ---
    @Mapping(target = "category", ignore = true)
    Product applyUpdateFromDto(ProductUpdateDto dto, Product oldProduct);
}
