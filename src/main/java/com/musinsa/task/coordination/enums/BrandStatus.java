package com.musinsa.task.coordination.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BrandStatus {
    STANDBY("생성됌"),
    ACTIVATE("사용중"),
    STOP("사용정지"),
    REMOVED("제거됌");

    private final String description;

    public boolean isMoveable(BrandStatus status) {
        if (this == STANDBY) {
            return status == ACTIVATE;
        } else if (this == ACTIVATE) {
            return status == STOP || status == REMOVED;
        } else if (this == STOP) {
            return status == ACTIVATE || status == REMOVED;
        }
        return false;
    }
}
