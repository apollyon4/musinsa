package com.musinsa.task.coordination.config;

import com.musinsa.task.coordination.entity.Brand;
import com.musinsa.task.coordination.enums.BrandStatus;
import com.musinsa.task.coordination.repository.BrandRepository;
import com.musinsa.task.coordination.service.BrandService;
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
    private final BrandService brandService;
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("init");
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            try {
                brandService.changeStatus(brand, BrandStatus.ACTIVATED);
            } catch (IllegalArgumentException e) {
                log.error("브랜드 상태 변경 실패: {}", e.getMessage());
            }
        }
    }
}