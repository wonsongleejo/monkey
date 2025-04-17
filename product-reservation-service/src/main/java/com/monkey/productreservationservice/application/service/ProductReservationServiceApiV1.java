package com.monkey.productreservationservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.UserFeignClientApiV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReservationServiceApiV1 {
    private final ProductReservationRepository productReservationRepository;
    private final StoreFeignClientApiV1 storeClient;
    private final UserFeignClientApiV1 userClient;
    private final ProductFeignClientApiV1 productClient;

    // 예약 등록
    public ResProductReservationPostDTOApiV1 postBy(UUID productId, ReqProductReservationPostDTOApiV1 reqDto, long userId) {
        // FeignClient 상품 정보 조회
        UUID storeId = productClient.getProductById(productId).getData().getStoreId();

        ProductReservationEntity productReservationEntity = reqDto.getProductReservation().toEntity(productId, userId, storeId, ProductReservationStatus.PENDING_PICKUP);
        ProductReservationEntity saved = productReservationRepository.save(productReservationEntity);

        return ResProductReservationPostDTOApiV1.of(saved);
    }

    // 예약 취소
    public ResProductReservationPostByIdCancelDTOApiV1 cancelBy(UUID productReservationId) {
        ProductReservationEntity productReservationEntity = getActiveProductReservationById(productReservationId);
        productReservationEntity.delete(123L);

        ProductReservationEntity saved = productReservationRepository.save(productReservationEntity);

        return ResProductReservationPostByIdCancelDTOApiV1.of(saved);
    }

    // 예약내역 전체 조회
    public ResProductReservationGetDTOApiV1 getBy() {
        List<ProductReservationEntity> productReservationList = productReservationRepository.findAllByIsDeletedFalse();
        return ResProductReservationGetDTOApiV1.of(productReservationList);
    }

    // 존재하는 예약 검증 메서드
    private ProductReservationEntity getActiveProductReservationById(UUID productReservationId) {
        return productReservationRepository.findByProductReservationIdAndIsDeletedFalse(productReservationId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }
}
