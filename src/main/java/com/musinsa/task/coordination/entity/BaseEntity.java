package com.musinsa.task.coordination.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private long createdAt;

    @Column(name = "modified_at")
    private long modifiedAt;

    @PreUpdate
    public void preUpdate() {
        modifiedAt = System.currentTimeMillis();
    }
}
