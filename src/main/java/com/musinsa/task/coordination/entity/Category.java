package com.musinsa.task.coordination.entity;

import com.musinsa.task.coordination.enums.ProductStatus;
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
        if (this.lowestProduct == null || ProductStatus.REMOVED.equals(lowestProduct.getStatus())) {
            this.lowestProduct = product;
        } else if (this.lowestProduct.getPrice() > product.getPrice()) {
            this.lowestProduct = product;
        }
    }
}
