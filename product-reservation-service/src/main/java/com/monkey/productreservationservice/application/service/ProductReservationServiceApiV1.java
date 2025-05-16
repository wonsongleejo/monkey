package com.monkey.productreservationservice.application.service;

import com.monkey.common_module.aop.AccessLevel;
import com.monkey.common_module.aop.CheckUserRole;
import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.common_module.lock.DistributedLock;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetByIdDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.validator.v1.ProductReservationReadValidator;
import com.monkey.productreservationservice.application.validator.v1.ProductReservationValidator;
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
    @DistributedLock(key = "'product:' + #productId", waitTime = 10, leaseTime = 2)
    public ResProductReservationPostDTOApiV1 postBy(ReqProductReservationPostDTOApiV1 reqDto, UUID productId, long userId) {
        // 예약 요청 검증
        var product = reservationValidator.validateReservationRequest(productId, userId, reqDto.getQuantity());

        ProductReservationEntity productReservation = saveNewReservation(product, reqDto, userId);

        try {
            requestStockDecrease(productId, userId, reqDto.getQuantity());
        } catch (Exception e) {
            requestReservationFail(productReservation, userId);
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


    /// ========================================== 서비스 내부 검증 로직 ==========================================

    // 존재하는 예약 검증 메서드
    private ProductReservationEntity getActiveProductReservationById(UUID productReservationId) {
        return productReservationRepository.findByProductReservationIdAndIsDeletedFalse(productReservationId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }

    // 상품 예약 엔티티 생성, 저장
    private ProductReservationEntity saveNewReservation(ResProductClientGetByIdDTOApiV1.Product product, ReqProductReservationPostDTOApiV1 reqDto, long userId) {
        ProductReservationEntity productReservationEntity = ProductReservationEntity.builder()
                .productId(product.getProductId())
                .userId(userId)
                .storeId(product.getStore().getStoreId())
                .quantity(reqDto.getQuantity())
                .status(ProductReservationStatus.PENDING_PICKUP)
                .build();

        productReservationEntity = productReservationRepository.save(productReservationEntity);
        log.info("[상품 예약 생성] userId={}, productId={}, quantity={}", userId, product.getProductId(), reqDto.getQuantity());
        return productReservationEntity;
    }

    // 상품 재고 감소 요청
    private void requestStockDecrease(UUID productId, long userId, int quantity) {
        productFeignClientApiV1.decreaseStock(productId, userId, quantity);
        log.info("[상품 재고 차감 요청] productId={}, userId={}, quantity={}", productId, userId, quantity);
    }

    // 상품 예약 실패
    private void requestReservationFail(ProductReservationEntity productReservation, long userId) {
        productReservation.fail(userId);
        productReservationRepository.save(productReservation);
        log.error("[재고 부족으로 인해 예약 실패] reservationId={}", productReservation.getProductReservationId());
    }
}
