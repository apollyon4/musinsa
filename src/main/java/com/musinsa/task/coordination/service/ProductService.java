package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.ProductCreateDto;
import com.musinsa.task.coordination.dto.req.ProductUpdateDto;
import com.musinsa.task.coordination.dto.res.CategoryPriceRangeResponseDto;
import com.musinsa.task.coordination.dto.res.LowestPriceStyleResponseDto;
import com.musinsa.task.coordination.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.entity.Category;
import com.musinsa.task.coordination.entity.Product;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.enums.ProductStatus;
import com.musinsa.task.coordination.error.exception.*;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.repository.CategoryRepository;
import com.musinsa.task.coordination.repository.ProductRepository;
import com.musinsa.task.coordination.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductListResponseDto getLowestPerCategoryProducts() {
        List<Product> products = productRepositoryCustom.selectLowestPerCategoryProducts();
        return new ProductListResponseDto(products);
    }

    @Transactional
    public LowestPriceStyleResponseDto getLowestBrandProducts() {
        Brand lowestBrand = brandRepository.findFirstByStatusOrderByTotalLowestPriceAsc(BrandStatus.ACTIVATED);

        List<Product> products = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(lowestBrand);
        ProductListResponseDto productListResponseDto = new ProductListResponseDto(products);
        productListResponseDto.setBrandName(lowestBrand.getName());
        return new LowestPriceStyleResponseDto(productListResponseDto);
    }

    public CategoryPriceRangeResponseDto getLowestHighestByCategoryProducts(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));
        Product lowestProduct = productRepository.findFirstByCategoryAndStatusOrderByPriceAsc(category, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new CategoryHasNoProductsException(categoryName));
        Product highestProduct = productRepository.findFirstByCategoryAndStatusOrderByPriceDesc(category, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new CategoryHasNoProductsException(categoryName));

        CategoryPriceRangeResponseDto responseDto = new CategoryPriceRangeResponseDto();
        responseDto.setCategoryName(category.getName());
        responseDto.setLowestProduct(ProductResponseDto.builder()
                .brandName(lowestProduct.getBrand().getName())
                .price(lowestProduct.getPrice())
                .build());
        responseDto.setHighestProduct(ProductResponseDto.builder()
                .brandName(highestProduct.getBrand().getName())
                .price(highestProduct.getPrice())
                .build());
        return responseDto;
    }

    public ProductResponseDto addProduct(ProductCreateDto productCreateDto) {
        Product product = productRepository.save(Product.builder()
                .price(productCreateDto.getPrice())
                .category(categoryRepository.getReferenceById(productCreateDto.getCategoryId()))
                .brand(brandRepository.getReferenceById(productCreateDto.getBrandId()))
                .status(ProductStatus.STANDBY)
                .build());
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        Product product = productRepository.findById(productUpdateDto.getId())
                .orElseThrow(() -> new ProductNotFoundException(productUpdateDto.getId()));

        if (productUpdateDto.getPrice() != null) {
            product.setPrice(productUpdateDto.getPrice());
        }
        if (productUpdateDto.getStatus() != null) {
            this.changeStatus(product, productUpdateDto.getStatus());
        }
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        this.changeStatus(product, ProductStatus.REMOVED);
        productRepository.save(product);
    }

    public ProductResponseDto changeProductStatus(Long productId, ProductStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        this.changeStatus(product, status);
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponseDto.builder()
                        .id(product.getId())
                        .price(product.getPrice())
                        .brandName(product.getBrand().getName())
                        .categoryName(product.getCategory().getName())
                        .status(product.getStatus())
                        .build())
                .toList();
    }

    private void changeStatus(Product product, ProductStatus status) {
        if (ProductStatus.ACTIVATED.equals(status)) {
            if (!BrandStatus.ACTIVATED.equals(product.getBrand().getStatus())) {
                throw new BrandNotActiveException(product.getBrand().getId());
            }
        } else if (ProductStatus.REMOVED.equals(status)) {
            long count = productRepository.countByBrandAndCategory(product.getBrand(), product.getCategory());
            if (count <= 1) {
                throw new LastProductInBrandCategoryException(product.getBrand().getId(), product.getCategory().getId());
            }
        }
        product.changeStatus(status);

        // 카테고리별 최저가 브랜드 상품 로직 재검사
        productRepository.findFirstByCategoryAndStatusOrderByPriceAsc(product.getCategory(), ProductStatus.ACTIVATED)
                .ifPresent(lowestProduct -> product.getCategory().checkLowestProduct(lowestProduct));
        // 브랜드 최저가 상품 로직 재검사
        List<Product> products = productRepositoryCustom.selectLowestProductsByBrandGroupByCategory(product.getBrand());
        product.getBrand().setTotalLowestPrice(products.stream()
                .map(Product::getPrice)
                .reduce(0L, Long::sum));
    }
}
