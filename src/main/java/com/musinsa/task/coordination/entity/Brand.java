package com.musinsa.task.coordination.entity;

import com.musinsa.task.coordination.enums.BrandStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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

    @Length(min = 1, max = 20)
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "status", nullable = false)
    private BrandStatus status;

    @NotBlank
    @Min(1)
    @Max(20)
    public void setName(String name) {
        this.name = name;
    }
}
