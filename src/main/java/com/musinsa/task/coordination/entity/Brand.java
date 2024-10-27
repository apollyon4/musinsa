package com.musinsa.task.coordination.entity;

import com.musinsa.task.coordination.enums.BrandStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BrandStatus status;

    @Column(name = "total_lowest_price")
    private Long totalLowestPrice;

    public void setName(@NotBlank @Size(min = 1, max = 20) String name) {
        this.name = name;
    }
}
