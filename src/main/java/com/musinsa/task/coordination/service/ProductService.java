package com.musinsa.task.coordination.service;

import com.musinsa.task.coordination.dto.req.CreateProductDto;
import com.musinsa.task.coordination.dto.req.UpdateProductDto;
import com.musinsa.task.coordination.dto.res.CategoryPriceRangeResponseDto;
import com.musinsa.task.coordination.dto.res.LowestPriceStyleResponseDto;
import com.musinsa.task.coordination.dto.res.ProductListResponseDto;
import com.musinsa.task.coordination.dto.res.ProductResponseDto;
import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.entity.Category;
import com.musinsa.task.coordination.entity.Product;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.enums.ProductStatus;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));
        Product lowestProduct = productRepository.findFirstByCategoryAndStatusOrderByPriceAsc(category, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리에 상품이 존재하지 않습니다."));
        Product highestProduct = productRepository.findFirstByCategoryAndStatusOrderByPriceDesc(category, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리에 상품이 존재하지 않습니다."));

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

    public ProductResponseDto addProduct(CreateProductDto createProductDto) {
        Product product = productRepository.save(Product.builder()
                .price(createProductDto.getPrice())
                .category(categoryRepository.getReferenceById(createProductDto.getCategoryId()))
                .brand(brandRepository.getReferenceById(createProductDto.getBrandId()))
                .status(ProductStatus.STANDBY)
                .build());
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public ProductResponseDto updateProduct(UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(updateProductDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        if (updateProductDto.getPrice() != null) {
            product.setPrice(updateProductDto.getPrice());
        }
        if (updateProductDto.getStatus() != null) {
            this.changeStatus(product, updateProductDto.getStatus());
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
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        this.changeStatus(product, ProductStatus.REMOVED);
        productRepository.save(product);
    }

    public ProductResponseDto changeProductStatus(Long productId, ProductStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
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
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
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
                throw new IllegalArgumentException("브랜드가 활성화 상태여야 합니다.");
            }
        } else if (ProductStatus.REMOVED.equals(status)) {
            long count = productRepository.countByBrandAndCategory(product.getBrand(), product.getCategory());
            if (count <= 1) {
                throw new IllegalArgumentException("브랜드의 카테고리에 최소 하나의 상품이 활성화되어 있어야 합니다.");
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
