package com.monkey.productservice.application.service.v3;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productservice.application.event.ProductEventProduceV1;
import com.monkey.productservice.application.event.dto.ProductStockIncreaseFailPayloadV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImplApiV3 implements ProductServiceApiV3 {
    private final ProductRepository productRepository;
    private final ProductEventProduceV1 productEventProduce;

    // 존재하는 상품 검증 메서드
    private ProductEntity getActiveProductById(UUID productId) {
        return productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }

    // 상품 재고 증가
    @Transactional
    @Override
    public void increaseStock(UUID productId, int quantity) {
        try{
            ProductEntity productEntity = getActiveProductById(productId);
            if(quantity == 1) throw new Exception("실패");
            productEntity.increaseStock(quantity);
            productRepository.save(productEntity);
        } catch (Exception e){
            log.error(e.getMessage());
            productEventProduce.increaseStockFailed(
                    ProductStockIncreaseFailPayloadV1.builder()
                            .productId(productId)
                            .build()
            );
        }
    }
}
