package com.musinsa.task.coordination.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "create_at")
    private long createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private long modifiedAt;
}
