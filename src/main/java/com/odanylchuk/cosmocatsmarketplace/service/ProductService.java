package com.odanylchuk.cosmocatsmarketplace.service;

import com.odanylchuk.cosmocatsmarketplace.dto.*;

public interface ProductService {

    PaginatedProductListDto getAllProducts(ProductQuery query);

    ProductDto getProductBySlug(String slug, String currency);

    ProductDto createProduct(ProductCreateDto createDto);

    ProductDto updateProduct(String slug, ProductUpdateDto updateDto, String currency);

    void deleteProduct(String slug);
}