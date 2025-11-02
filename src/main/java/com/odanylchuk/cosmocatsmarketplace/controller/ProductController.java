package com.odanylchuk.cosmocatsmarketplace.controller;

import com.odanylchuk.cosmocatsmarketplace.dto.*;
import com.odanylchuk.cosmocatsmarketplace.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Retrieve a paginated, filterable, and sortable list of products")
    public ResponseEntity<PaginatedProductListDto> getAllProducts(ProductQuery query) {
        PaginatedProductListDto products = productService.getAllProducts(query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Retrieve a product by its unique slug")
    public ResponseEntity<ProductDto> getProductBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "IGC") String currency
    ) {
        ProductDto product = productService.getProductBySlug(slug, currency);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "Create a new product for the marketplace")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductCreateDto createDto) {
        ProductDto createdProduct = productService.createProduct(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{slug}")
    @Operation(summary = "Update an existing product by slug")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable String slug,
            @Valid @RequestBody ProductUpdateDto updateDto,
            @RequestParam(defaultValue = "IGC") String currency
    ) {
        ProductDto updatedProduct = productService.updateProduct(slug, updateDto, currency);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{slug}")
    @Operation(summary = "Delete a product from the marketplace by its unique slug")
    public ResponseEntity<Void> deleteProduct(@PathVariable String slug) {
        productService.deleteProduct(slug);
        return ResponseEntity.noContent().build();
    }
}