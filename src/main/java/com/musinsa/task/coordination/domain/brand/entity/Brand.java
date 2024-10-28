package com.musinsa.task.coordination.domain.brand.entity;

import com.musinsa.task.coordination.domain.BaseEntity;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.BrandStateNotAllowedException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "brand")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BrandStatus status;

    // 해당 브랜드의 카테고리별 최저가 상품의 총합
    @Column(name = "total_lowest_price")
    private Long totalLowestPrice;

    public void setName(@NotBlank @Size(min = 1, max = 20) String name) {
        this.name = name;
    }

    public void setTotalLowestPrice(@Positive Long totalLowestPrice) {
        this.totalLowestPrice = totalLowestPrice;
    }

    public void changeStatus(BrandStatus status) {
        if (this.status == status) {
            return;
        }

        if (!this.status.isMoveable(status)) {
            throw new BrandStateNotAllowedException(this.status.name(), status.name());
        }

        this.status = status;
    }
}
