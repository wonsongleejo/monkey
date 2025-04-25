package com.monkey.productreservationservice.application.service;

import com.monkey.common_module.aop.AccessLevel;
import com.monkey.common_module.aop.CheckUserRole;
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
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReservationServiceApiV1 {
    private final ProductReservationRepository productReservationRepository;
    private final ProductReservationValidator reservationValidator;
    private final ProductReservationReadValidator readValidator;
    private final ProductFeignClientApiV1 productFeignClientApiV1;

    // 예약 등록
    @CheckUserRole(AccessLevel.USER)
    public ResProductReservationPostDTOApiV1 postBy(ReqProductReservationPostDTOApiV1 reqDto, UUID productId, long userId) {
        var product = reservationValidator.validateProduct(productId); // 유효한 상품 확인

        reservationValidator.validateStock(reqDto.getQuantity(), product.getQuantity()); // 재고 확인
        reservationValidator.validatePurchaseLimit(reqDto.getQuantity(), product.getPurchaseLimitPerUser()); // 구매수량 제한
        reservationValidator.validateStoreMember(userId, product.getStore().getStoreId()); // 스토어 예약여부 확인
        reservationValidator.validateNotDuplicate(userId, productId); // 중복 예약 확인

        ProductReservationEntity productReservation = ProductReservationEntity.builder()
                .productId(productId)
                .userId(userId)
                .storeId(product.getStore().getStoreId())
                .quantity(reqDto.getQuantity())
                .status(ProductReservationStatus.PENDING_PICKUP) // 예약 생성 시 상태: 픽업 대기중으로 고정
                .build();

        productReservation = productReservationRepository.save(productReservation);
        log.info("[예약 생성] userId={}, productId={}, quantity={}", userId, productId, reqDto.getQuantity());

        try {
            productFeignClientApiV1.decreaseStock(productId, userId, reqDto.getQuantity());
            log.info("[상품 재고 차감 요청] productId={}, userId={}, quantity={}", productId, userId, reqDto.getQuantity());
        } catch (Exception e) {
            log.warn("[재고 부족으로 인해 예약 실패] reservationId={}", productReservation.getProductReservationId());
            productReservation.fail(userId);
            productReservationRepository.save(productReservation);
            throw new CustomException(ResponseCode.PRODUCT_OUT_OF_STOCK);
        }
        return ResProductReservationPostDTOApiV1.of(productReservation);
    }

    // 예약 취소
    @CheckUserRole(AccessLevel.USER)
    public ResProductReservationPostByIdCancelDTOApiV1 cancelBy(UUID productReservationId, long userId) {
        ProductReservationEntity productReservation = getActiveProductReservationById(productReservationId);
        productReservation.cancel(userId);

        // 상품 재고 복원
        try {
            productFeignClientApiV1.increaseStock(productReservation.getProductId(), userId, productReservation.getQuantity());
        } catch (Exception e) {
            log.warn("[예약 취소 성공, 재고 복원 실패] reservationId={}, productId={}", productReservation.getProductReservationId(), productReservation.getProductId());
        }
        return ResProductReservationPostByIdCancelDTOApiV1.of(productReservationRepository.save(productReservation));
    }

    // 예약내역 전체 조회
    @CheckUserRole(AccessLevel.ALL)
    public ResProductReservationGetDTOApiV1 getBy(Pageable pageable) {
        Page<ProductReservationEntity> productReservationPage = productReservationRepository.findAllByIsDeletedFalse(pageable);
        return ResProductReservationGetDTOApiV1.of(productReservationPage);
    }

    // 예약내역 단건 조회
    @CheckUserRole(AccessLevel.ALL)
    public ResProductReservationGetByIdDTOApiV1 getById(UUID productReservationId) {
        ProductReservationEntity productReservation = getActiveProductReservationById(productReservationId);

        ResProductClientGetByIdDTOApiV1.Product resProduct = readValidator.validateProduct(productReservation.getProductId());
        ResStoreClientGetByIdDTOApiV1.Store resStore = readValidator.validateStore(productReservation.getStoreId());
        ResUserClientGetByIdDTOApiV1.User resUser = readValidator.validateUser(productReservation.getUserId());

        return ResProductReservationGetByIdDTOApiV1.of(productReservation, resProduct, resStore, resUser);
    }

    // 개인 예약내역 조회
    @CheckUserRole(AccessLevel.USER)
    public ResProductReservationGetDTOApiV1 getByUserId(long userId, Pageable pageable) {
        Page<ProductReservationEntity> productReservationPage =
                productReservationRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
        return ResProductReservationGetDTOApiV1.of(productReservationPage);
    }

    // 존재하는 예약 검증 메서드
    private ProductReservationEntity getActiveProductReservationById(UUID productReservationId) {
        return productReservationRepository.findByProductReservationIdAndIsDeletedFalse(productReservationId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }

}
