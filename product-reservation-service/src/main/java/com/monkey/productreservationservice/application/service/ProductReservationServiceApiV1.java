package com.monkey.productreservationservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetByIdDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.validator.ProductReservationReadValidator;
import com.monkey.productreservationservice.application.validator.ProductReservationValidator;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReservationServiceApiV1 {
    private final ProductReservationRepository productReservationRepository;
    private final ProductReservationValidator reservationValidator;
    private final ProductReservationReadValidator readValidator;

    // 예약 등록
    public ResProductReservationPostDTOApiV1 postBy(ReqProductReservationPostDTOApiV1 reqDto, UUID productId, long userId) {
        var product = reservationValidator.validateProduct(productId); // 유효한 상품 확인

        reservationValidator.validateStock(reqDto.getQuantity(), product.getQuantity()); // 재고 확인
        reservationValidator.validatePurchaseLimit(reqDto.getQuantity(), product.getPurchaseLimitPerUser()); // 구매수량 제한
        reservationValidator.validateStoreMember(userId); // 스토어 예약여부 확인
        reservationValidator.validateNotDuplicate(userId, productId); // 중복 예약 확인

        ProductReservationEntity productReservationEntity = ProductReservationEntity.builder()
                .productId(productId)
                .userId(userId)
                .storeId(product.getStoreId())
                .quantity(reqDto.getQuantity())
                .status(ProductReservationStatus.PENDING_PICKUP) // 예약 생성 시 상태: 픽업 대기중으로 고정
                .build();

        return ResProductReservationPostDTOApiV1.of(productReservationRepository.save(productReservationEntity));
    }

    // 예약 취소
    public ResProductReservationPostByIdCancelDTOApiV1 cancelBy(UUID productReservationId, long userId) {
        ProductReservationEntity productReservationEntity = getActiveProductReservationById(productReservationId);
        productReservationEntity.cancel(userId);

        return ResProductReservationPostByIdCancelDTOApiV1.of(productReservationRepository.save(productReservationEntity));
    }

    // 예약내역 전체 조회
    public ResProductReservationGetDTOApiV1 getBy() {
        List<ProductReservationEntity> productReservationList = productReservationRepository.findAllByIsDeletedFalse();
        return ResProductReservationGetDTOApiV1.of(productReservationList);
    }

    // 예약내역 단건 조회
    public ResProductReservationGetByIdDTOApiV1 getById(UUID productReservationId) {
        ProductReservationEntity productReservation = getActiveProductReservationById(productReservationId);

        ResProductClientGetByIdDTOApiV1 resProduct = readValidator.validateProduct(productReservation.getProductId());
        ResStoreClientGetByIdDTOApiV1 resStore = readValidator.validateStore(productReservation.getStoreId());
        ResUserClientGetByIdDTOApiV1 resUser = readValidator.validateUser(productReservation.getCreatedBy());

        return ResProductReservationGetByIdDTOApiV1.of(productReservation, resProduct, resStore, resUser);
    }

    // 존재하는 예약 검증 메서드
    private ProductReservationEntity getActiveProductReservationById(UUID productReservationId) {
        return productReservationRepository.findByProductReservationIdAndIsDeletedFalse(productReservationId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }

}
