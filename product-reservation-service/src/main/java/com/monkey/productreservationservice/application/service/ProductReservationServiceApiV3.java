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
import com.monkey.productreservationservice.application.event.ProductReservationEventProduceV1;
import com.monkey.productreservationservice.application.event.dto.ProductStockIncreasePayloadV1;
import com.monkey.productreservationservice.application.validator.v1.ProductReservationReadValidator;
import com.monkey.productreservationservice.application.validator.v2.ProductReservationValidatorV2;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.kafka.ProductReservationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReservationServiceApiV3 {
    private final ProductReservationRepository productReservationRepository;
    private final ProductReservationValidatorV2 reservationValidator;
    private final ProductReservationReadValidator readValidator;
    private final ProductFeignClientApiV1 productFeignClientApiV1;
    private final ProductReservationEventProduceV1 productReservationEventProduce;
    private final ProductReservationProducer productReservationProducer;

    // 예약 등록
    @Transactional
    @CheckUserRole(AccessLevel.USER)
    public ResProductReservationPostDTOApiV1 postBy(ReqProductReservationPostDTOApiV1 reqDto, UUID productId, Long userId) {
        // 예약 요청 검증
        var product = reservationValidator.validateReservationRequest(productId, userId, reqDto.getQuantity());
        ProductReservationEntity productReservation = saveNewReservation(product, reqDto, userId);

        // Kafka 메시지 전송
        String message = String.format(
                "productReservationId=%s,userId=%d,productId=%s,quantity=%d,status=%s",
                productReservation.getProductReservationId(),
                userId,
                productId,
                reqDto.getQuantity(),
                productReservation.getStatus()
        );
        productReservationProducer.sendReservationCreated(message);

        try {
            requestStockDecrease(productId, userId, reqDto.getQuantity());
        } catch (Exception e) {
            requestReservationFail(productReservation, userId, reqDto.getQuantity());
            throw new CustomException(ResponseCode.PRODUCT_OUT_OF_STOCK);
        }
        return ResProductReservationPostDTOApiV1.of(productReservation);
    }

    // 예약 취소
    @CheckUserRole(AccessLevel.USER)
    public ResProductReservationPostByIdCancelDTOApiV1 cancelBy(UUID productReservationId, long userId) {
        ProductReservationEntity productReservation = getActiveProductReservationById(productReservationId);
        UUID productId = productReservation.getProductId();
        int quantity = productReservation.getQuantity();

        productReservation.cancel(userId);

        // 상품 재고 복원
        productReservationEventProduce.increaseStock(
                ProductStockIncreasePayloadV1.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .build()
        );

        return ResProductReservationPostByIdCancelDTOApiV1.of(productReservationRepository.save(productReservation));
    }

    // 예약 취소 실패
    @Transactional
    public void cancelFailed(UUID productId, Long userId) {
        ProductReservationEntity productReservation = productReservationRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(()-> new CustomException(ResponseCode.NOT_FOUND));

        // 상태 값, soft delete 되돌리기
        productReservation.cancelFailed();
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
    @Cacheable(value = "product-reservation", key = "#userId")
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
                .status(ProductReservationStatus.WAITING_FOR_CONFIRM)
                .build();

        productReservationEntity = productReservationRepository.save(productReservationEntity);
        log.info("[상품 예약 생성] userId={}, productId={}, quantity={}", userId, product.getProductId(), reqDto.getQuantity());
        return productReservationEntity;
    }

    // 상품 재고 감소 요청
    private void requestStockDecrease(UUID productId, long userId, int quantity) {
        try {
            // DB 재고 차감
            productFeignClientApiV1.decreaseStock(productId, userId, quantity);
            log.info("[상품 재고 차감 요청] productId={}, userId={}, quantity={}", productId, userId, quantity);

        } catch (Exception e) {
            log.error("[DB 재고 차감 실패] productId={}, userId={}, quantity={}", productId, userId, quantity);
            throw new CustomException(ResponseCode.PRODUCT_FEIGN_CLIENT_ERROR);
        }
    }

    // 상품 예약 실패
    private void requestReservationFail(ProductReservationEntity productReservation, long userId, int quantity) {
        productReservation.fail(userId);
        productReservationRepository.save(productReservation);
        log.error("[재고 부족으로 인해 예약 실패] reservationId={}", productReservation.getProductReservationId());
    }
}