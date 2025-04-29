package com.monkey.productreservationservice.infrastructure.redis;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.domain.service.ProductStockServiceV1;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductStockRedisServiceImplV1 implements ProductStockServiceV1 {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductFeignClientApiV1 productFeignClientApiV1;

    private static final String PRODUCT_STOCK_KEY_PREFIX = "product_stock:";
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);


    // Redis에서 상품 재고를 조회하고, 없을 경우 DB에서 조회하여 Redis에 저장
    @Override
    public Integer getProductStock(UUID productId) {
        String key = getStockKey(productId);
        Integer cachedStock = (Integer) redisTemplate.opsForValue().get(key);

        if (cachedStock == null) {
            return refreshStockFromDB(productId);
        }

        return cachedStock;
    }

    // DB에서 상품 정보를 조회하여 Redis에 재고 정보 저장
    @Override
    public Integer refreshStockFromDB(UUID productId) {
        String key = getStockKey(productId);

        try {
            var productResponse = productFeignClientApiV1.getProductById(productId);
            var product = productResponse.getData().getProduct();

            if (product != null) {
                int quantity = product.getQuantity();
                redisTemplate.opsForValue().set(key, quantity, DEFAULT_TTL);
                log.info("[Redis 재고 세팅] productId={}, quantity={}", productId, quantity);
                return quantity;
            } else {
                log.error("[Redis 재고 세팅 실패] productId={}", productId);
                throw new CustomException(ResponseCode.PRODUCT_NOT_FOUND);
            }
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw e;
            }
            throw new CustomException(ResponseCode.PRODUCT_FEIGN_CLIENT_ERROR);
        }
    }

    // Redis에 상품 재고가 있는지 확인하고, 없으면 DB에서 가져와 설정
    @Override
    public void ensureStockInRedis(UUID productId) {
        getProductStock(productId);
    }

    // Redis에서 상품 재고를 감소시킴
    @Override
    public void decreaseStock(UUID productId, int quantity) {
        String key = getStockKey(productId);
        Integer cachedStock = getProductStock(productId);

        if (cachedStock < quantity) {
            throw new CustomException(ResponseCode.PRODUCT_OUT_OF_STOCK);
        }

        redisTemplate.opsForValue().increment(key, -quantity);
        log.info("[Redis 재고 감소] productId={}, quantity={}, newStock={}",
                productId, quantity, (Integer) redisTemplate.opsForValue().get(key));
    }

    // Redis에서 상품 재고를 증가시킴
    @Override
    public void increaseStock(UUID productId, int quantity) {
        String key = getStockKey(productId);

        redisTemplate.opsForValue().increment(key, quantity);
        log.info("[Redis 재고 증가] productId={}, quantity={}, newStock={}",
                productId, quantity, (Integer) redisTemplate.opsForValue().get(key));
    }

    // 상품 재고 키 생성
    private String getStockKey(UUID productId) {
        return PRODUCT_STOCK_KEY_PREFIX + productId;
    }
}