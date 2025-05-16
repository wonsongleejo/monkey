package com.monkey.productservice.application.service;

import com.monkey.common_module.aop.AccessLevel;
import com.monkey.common_module.aop.CheckUserRole;
import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetByIdDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPutDTOApiV1;
import com.monkey.productservice.application.event.ProductEventProduceV1;
import com.monkey.productservice.application.event.dto.ProductStockIncreaseFailPayloadV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import com.monkey.productservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

// v1~v4 전체 통합한 서비스 코드
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceApiV4 {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductEventProduceV1 productEventProduce;
    private final StoreFeignClientApiV1 storeClient;

    // ====================== CRUD ======================
    // 상품 등록
    @CheckUserRole(AccessLevel.MANAGER)
    public ResProductPostDTOApiV1 postBy(ReqProductPostDTOApiV1 reqDto) {
        ProductEntity productEntity = reqDto.getProduct().toEntity();
        return ResProductPostDTOApiV1.of(productRepository.save(productEntity));
    }

    // 상품 수정
    @CheckUserRole(AccessLevel.MANAGER)
    public ResProductPutDTOApiV1 putBy(UUID productId, ReqProductPutDTOApiV1 reqDto) {
        ProductEntity productEntity = getActiveProductById(productId);
        reqDto.getProduct().update(productEntity);
        return ResProductPutDTOApiV1.of(productRepository.save(productEntity));
    }

    // 상품 전체 조회 (캐싱 적용)
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

    // 상품 단건 조회
    @CheckUserRole(AccessLevel.ALL)
    public ResProductGetByIdDTOApiV1 getById(UUID productId) {
        ProductEntity product = getActiveProductById(productId);

        try {
            var storeRes = storeClient.getStoreById(product.getStoreId());
            ResStoreClientGetByIdDTOApiV1.Store storeDto = storeRes.getData().getStore();
            return ResProductGetByIdDTOApiV1.of(product, storeDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResponseCode.STORE_FEIGN_CLIENT_ERROR);
        }
    }

    // 상품 삭제
    @CheckUserRole(AccessLevel.MANAGER)
    public void deleteById(UUID productId, Long userId) {
        ProductEntity productEntity = getActiveProductById(productId);
        productEntity.delete(userId);
        productRepository.save(productEntity);
    }

    // ====================== 예약 생성/취소 (Kafka 적용) ======================

    // 상품 재고 차감
    @Transactional
    public void decreaseStock(UUID productId, Long userId, int quantity) {
        ProductEntity productEntity = getActiveProductById(productId);

        if(productEntity.getQuantity() < quantity) {
            log.warn("[상품 재고 부족] 요청 수량={}, 현재 수량={}, productId={}", quantity, productEntity.getQuantity(), productId);
            throw new CustomException(ResponseCode.PRODUCT_OUT_OF_STOCK);
        }
        productEntity.decreaseStock(quantity);
        productRepository.save(productEntity);
    }

    // 상품 재고 증가
    @Transactional
    public void increaseStock(UUID productId, Long userId, int quantity) {
        try{
            ProductEntity productEntity = getActiveProductById(productId);
            productEntity.increaseStock(quantity);
            productRepository.save(productEntity);
        } catch (Exception e){
            log.error("[재고 증가 실패] {}", e.getMessage());
            productEventProduce.increaseStockFailed(
                    ProductStockIncreaseFailPayloadV1.builder()
                            .productId(productId)
                            .build()
            );
        }
    }

    // ====================== 존재하는 상품 검증 메서드 ====================== //
    private ProductEntity getActiveProductById(UUID productId) {
        return productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }
}
