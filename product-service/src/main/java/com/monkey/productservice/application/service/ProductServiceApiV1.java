package com.monkey.productservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productservice.application.dto.response.ResProductGetByIdDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import com.monkey.productservice.infrastructure.feignclient.StoreFeignClient;
import com.monkey.productservice.infrastructure.feignclient.UserFeignClient;
import com.monkey.productservice.infrastructure.feignclient.dto.StoreDTO;
import com.monkey.productservice.infrastructure.feignclient.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceApiV1 {
    private final ProductRepository productRepository;
    private final StoreFeignClient storeFeignClient;
    private final UserFeignClient userFeignClient;

    // 상품 단건 조회
    public ResProductGetByIdDTOApiV1 getById(UUID productId) {
        ProductEntity productEntity = getActiveProductById(productId);

        StoreDTO store = storeFeignClient.getStoreById(productEntity.getStoreId()).getData();
        UserDTO user = userFeignClient.getUserById(productEntity.getCreatedBy()).getData();

        return ResProductGetByIdDTOApiV1.of(productEntity, store, user);
    }

    // 존재하는 상품 검증 메서드
    private ProductEntity getActiveProductById(UUID productId) {
        return productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }
}
