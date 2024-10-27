package com.musinsa.task.coordination.entity;

import com.musinsa.task.coordination.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

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
            throw new IllegalArgumentException("요청한 상태로 변경 불가능합니다.");
        }
        this.status = status;
    }
}
