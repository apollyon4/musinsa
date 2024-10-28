package com.musinsa.task.coordination.domain.brand.service;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.product.service.ProductStatusManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandStatusManager {

    private final ProductStatusManager productStatusManager;

    public void changeStatus(Brand brand, BrandStatus status) {
        brand.changeStatus(status);
        if (BrandStatus.ACTIVATED.equals(status)) {
            productStatusManager.updateConstraintAfterActivateBrand(brand);
        } else if (BrandStatus.REMOVED.equals(status)) {
            productStatusManager.updateConstraintAfterRemoveBrand(brand);
        }
    }
}

