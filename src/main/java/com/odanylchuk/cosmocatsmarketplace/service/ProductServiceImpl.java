package com.odanylchuk.cosmocatsmarketplace.service;

import com.odanylchuk.cosmocatsmarketplace.domain.entity.Category;
import com.odanylchuk.cosmocatsmarketplace.domain.entity.Product;
import com.odanylchuk.cosmocatsmarketplace.dto.*;
import com.odanylchuk.cosmocatsmarketplace.exception.ResourceNotFoundException;
import com.odanylchuk.cosmocatsmarketplace.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    private final Map<String, Category> categoryStore = new ConcurrentHashMap<>();

    private final Map<String, Product> productStore = new ConcurrentHashMap<>();

    @jakarta.annotation.PostConstruct
    private void initializeMockData() {
        Category catToys = Category.builder().id(1L).name("Toys").description("Toys for cats").build();
        Category catFood = Category.builder().id(2L).name("Food").description("Food for cats").build();
        categoryStore.put("Toys", catToys);
        categoryStore.put("Food", catFood);

        Product p1 = Product.builder()
                .id(1L)
                .slug("anti-gravity-yarn-ball")
                .name("Anti-Gravity Space Yarn Ball")
                .description("A cosmic yarn ball that defies gravity")
                .price(new BigDecimal("25.99"))
                .quantity(50)
                .category(catToys)
                .tags(new HashSet<>(Arrays.asList("space", "toy", "anti-gravity")))
                .currency("IGC")
                .build();
        productStore.put(p1.getSlug(), p1);
    }

    @Override
    public PaginatedProductListDto getAllProducts(ProductQuery query) {

        int page = Optional.ofNullable(query.getPage()).orElse(1);
        int size = Optional.ofNullable(query.getSize()).orElse(10);
        String currency = Optional.ofNullable(query.getCurrency()).orElse("IGC");
        String sort = query.getSort();

        Stream<Product> productStream = getFilteredProductStream(query);

        List<Product> sortedProducts = productStream.sorted(getComparator(sort)).toList();

        int totalItems = sortedProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int start = (page - 1) * size;
        int end = Math.min(start + size, totalItems);

        List<Product> paginatedProducts = sortedProducts.subList(start, end);

        List<ProductDto> paginatedProductDtos = paginatedProducts.stream()
                .map(product -> toDtoWithCurrency(product, currency))
                .collect(Collectors.toList());

        return PaginatedProductListDto.builder()
                .items(paginatedProductDtos)
                .totalItems((long) totalItems)
                .totalPages(totalPages)
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    private Stream<Product> getFilteredProductStream(ProductQuery query) {
        Stream<Product> productStream = productStore.values().stream();

        if (query.getCategory() != null && !query.getCategory().isBlank()) {
            productStream = productStream.filter(p ->
                    p.getCategory().getName().equalsIgnoreCase(query.getCategory()));
        }
        if (query.getMinPrice() != null) {
            productStream = productStream.filter(p ->
                    p.getPrice().compareTo(query.getMinPrice()) >= 0);
        }
        if (query.getMaxPrice() != null) {
            productStream = productStream.filter(p ->
                    p.getPrice().compareTo(query.getMaxPrice()) <= 0);
        }
        if (query.getSearch() != null && !query.getSearch().isBlank()) {
            String lowerSearch = query.getSearch().toLowerCase();
            productStream = productStream.filter(p ->
                    p.getName().toLowerCase().contains(lowerSearch) ||
                            p.getDescription() != null &&
                                    p.getDescription().toLowerCase().contains(lowerSearch)
            );
        }
        return productStream;
    }

    private Comparator<Product> getComparator(String sort) {

        if (sort == null || sort.isBlank()) return Comparator.comparing(Product::getName);

        return switch (sort) {
            case "-name" -> Comparator.comparing(Product::getName).reversed();
            case "price" -> Comparator.comparing(Product::getPrice);
            case "-price" -> Comparator.comparing(Product::getPrice).reversed();
            case "quantity" -> Comparator.comparing(Product::getQuantity);
            case "-quantity" -> Comparator.comparing(Product::getQuantity).reversed();
            default -> Comparator.comparing(Product::getName);
        };
    }

    @Override
    public ProductDto getProductBySlug(String slug, String currency) {
        Product product = findBySlugOrThrow(slug);
        return toDtoWithCurrency(product, currency);
    }

    @Override
    public ProductDto createProduct(ProductCreateDto createDto) {
        Product product = productMapper.toEntity(createDto);

        String slug = generateSlug(createDto.getName());
        product = product.toBuilder()
                .slug(slug)
                .category(findCategoryByName(createDto.getCategory()))
                .build();

        productStore.put(slug, product);

        return toDtoWithCurrency(product, "IGC");
    }

    @Override
    public ProductDto updateProduct(String slug, ProductUpdateDto updateDto, String currency) {
        Product existingProduct = findBySlugOrThrow(slug);

        Product updatedProduct = productMapper.applyUpdateFromDto(updateDto, existingProduct);

        updatedProduct = updatedProduct.toBuilder()
                .category(findCategoryByName(updateDto.getCategory()))
                .build();

        productStore.put(slug, updatedProduct);

        return toDtoWithCurrency(updatedProduct, currency);
    }

    @Override
    public void deleteProduct(String slug) {
        if (!productStore.containsKey(slug)) {
            throw new ResourceNotFoundException("Product", "slug", slug);
        }
        productStore.remove(slug);
    }

    // --- Helper Methods ---

    private Product findBySlugOrThrow(String slug) {
        Product product = productStore.get(slug);
        if (product == null) {
            throw new ResourceNotFoundException("Product", "slug", slug);
        }
        return product;
    }

    /**
     * In a real app, this would throw an error if the category doesn't exist
     */
    private Category findCategoryByName(String name) {
        return categoryStore.getOrDefault(name,
                Category.builder().name(name).build());
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private ProductDto toDtoWithCurrency(Product product, String currency) {
        ProductDto dto = productMapper.toDto(product);

        return dto.toBuilder().currency(currency).build();
    }
}