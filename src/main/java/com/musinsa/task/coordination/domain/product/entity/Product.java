package com.musinsa.task.coordination.domain.product.entity;

import com.musinsa.task.coordination.domain.BaseEntity;
import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.category.entity.Category;
import com.musinsa.task.coordination.domain.product.enums.ProductStatus;
import com.musinsa.task.coordination.domain.product.exception.ProductStateNotAllowedException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Objects;

@Getter
@Builder
@Entity
@Table(name = "PRODUCT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void setPrice(@Positive Long price) {
        this.price = price;
    }

    public void changeStatus(ProductStatus status) {
        if (!this.status.isMoveable(status)) {
            throw new ProductStateNotAllowedException(this.status.name(), status.name());
        }

        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.id, ((Product) o).id);
    }
}
