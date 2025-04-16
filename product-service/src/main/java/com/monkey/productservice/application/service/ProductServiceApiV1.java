package com.monkey.productservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetByIdDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPutDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import com.monkey.productservice.infrastructure.feignclient.StoreFeignClient;
import com.monkey.productservice.infrastructure.feignclient.UserFeignClient;
import com.monkey.productservice.infrastructure.feignclient.dto.StoreDTO;
import com.monkey.productservice.infrastructure.feignclient.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceApiV1 {
    private final ProductRepository productRepository;
    private final StoreFeignClient storeFeignClient;
    private final UserFeignClient userFeignClient;

    // 상품 등록
    public ResProductPostDTOApiV1 postBy(ReqProductPostDTOApiV1 reqDto) {
        ProductEntity productEntity = reqDto.getProduct().toEntity();
        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return ResProductPostDTOApiV1.of(savedProductEntity);
    }

    // 상품 수정
    public ResProductPutDTOApiV1 putBy(UUID productId, ReqProductPutDTOApiV1 reqDto) {
        ProductEntity productEntity = getActiveProductById(productId);

        reqDto.getProduct().update(productEntity);
        ProductEntity updatedProductEntity = productRepository.save(productEntity);

        return ResProductPutDTOApiV1.of(updatedProductEntity);
    }

    // 상품 전체 조회
    public ResProductGetDTOApiV1 getBy() {
        List<ProductEntity> productList = productRepository.findAllByIsDeletedFalse();
        return ResProductGetDTOApiV1.of(productList);
    }

    // 상품 단건 조회
    public ResProductGetByIdDTOApiV1 getById(UUID productId) {
        ProductEntity productEntity = getActiveProductById(productId);

        StoreDTO store = storeFeignClient.getStoreById(productEntity.getStoreId()).getData();
        UserDTO user = userFeignClient.getUserById(productEntity.getCreatedBy()).getData();

        return ResProductGetByIdDTOApiV1.of(productEntity, store, user);
    }

    // 상품 삭제
    public void deleteById(UUID productId) {
        ProductEntity productEntity = getActiveProductById(productId);
        productEntity.delete(123L); // 추후에 인증 유저 ID로 교체해야됨
        productRepository.save(productEntity);
    }

    // 존재하는 상품 검증 메서드
    private ProductEntity getActiveProductById(UUID productId) {
        return productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }
}
