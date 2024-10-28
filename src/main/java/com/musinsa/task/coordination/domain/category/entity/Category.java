package com.musinsa.task.coordination.domain.category.entity;

import com.musinsa.task.coordination.domain.BaseEntity;
import com.musinsa.task.coordination.domain.category.exception.InvalidLowestProductException;
import com.musinsa.task.coordination.domain.product.entity.Product;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lowest_product_id")
    private Product lowestProduct;

    public void checkLowestProduct(Product product) {
        if (!product.getCategory().getId().equals(this.id) || !ProductStatus.ACTIVATED.equals(product.getStatus())) {
            throw new InvalidLowestProductException();
        }
        if (this.lowestProduct == null || ProductStatus.REMOVED.equals(lowestProduct.getStatus())) {
            this.lowestProduct = product;
        } else if (this.lowestProduct.getPrice() >= product.getPrice()) {
            this.lowestProduct = product;
        }
    }
}
