package com.monkey.productservice.application.service.v2;

import com.monkey.common_module.aop.AccessLevel;
import com.monkey.common_module.aop.CheckUserRole;
import com.monkey.productservice.application.dto.response.ResProductGetDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceApiV2 {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @CheckUserRole(AccessLevel.ALL)
    public ResProductGetDTOApiV1 getBy(Pageable pageable) {
        String cacheKey = "products:page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

        // 캐시 조회
        ResProductGetDTOApiV1 cached = (ResProductGetDTOApiV1) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("캐시 조회 성공. key={}", cacheKey);
            return cached;
        }

        // 캐시 없으면 DB 조회
        Page<ProductEntity> productPage = productRepository.findAllByIsDeletedFalse(pageable);
        ResProductGetDTOApiV1 resDto = ResProductGetDTOApiV1.of(productPage);

        // Redis 저장
        redisTemplate.opsForValue().set(cacheKey, resDto, Duration.ofMinutes(3));
        log.info("캐시 없음. 새로 저장. key={}", cacheKey);

        return resDto;
    }
}
