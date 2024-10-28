package com.musinsa.task.coordination.config;

import com.musinsa.task.coordination.domain.brand.entity.Brand;
import com.musinsa.task.coordination.domain.brand.enums.BrandStatus;
import com.musinsa.task.coordination.domain.brand.exception.MissingCategoryProductException;
import com.musinsa.task.coordination.domain.brand.repository.BrandRepository;
import com.musinsa.task.coordination.domain.brand.service.BrandStatusManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrandStatusInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final BrandStatusManager brandStatusManager;
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            try {
                brandStatusManager.changeStatus(brand, BrandStatus.ACTIVATED);
            } catch (MissingCategoryProductException e) {
                log.error(e.getMessage());
            }
        }
    }
}