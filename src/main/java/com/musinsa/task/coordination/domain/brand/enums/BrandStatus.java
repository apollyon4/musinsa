package com.musinsa.task.coordination.domain.brand.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BrandStatus {
    STANDBY("생성됌"),
    ACTIVATED("사용중"),
    REMOVED("제거됌");

    private final String description;

    public boolean isMoveable(BrandStatus status) {
        if (this == STANDBY) {
            return status == ACTIVATED;
        } else if (this == ACTIVATED) {
            return status == REMOVED;
        }
        return false;
    }
}
