package com.monkey.productreservationservice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.monkey.productreservationservice.application.validator.ProductReservationReadValidator;
import com.monkey.productreservationservice.application.validator.ProductReservationValidator;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.kafka.producer.ProductReservationProducer;
import com.monkey.productreservationservice.infrastructure.kafka.dto.ProductReservationCreatedPayloadV1;
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
public class ProductReservationServiceApiV4 {
    private final ProductReservationRepository productReservationRepository;
    private final ProductReservationValidator reservationValidator;
    private final ProductReservationReadValidator readValidator;
    private final ProductReservationProducer productReservationProducer;
    private final ProductReservationEventProduceV1 productReservationEventProduce;

    private final ObjectMapper objectMapper;

    // 예약 등록
    @CheckUserRole(AccessLevel.USER)
    public ResProductReservationPostDTOApiV1 postBy(ReqProductReservationPostDTOApiV1 reqDto, UUID productId, long userId) {
        // 예약 요청 검증
        var product = reservationValidator.validateReservationRequest(productId, userId, reqDto.getQuantity());
        ProductReservationEntity productReservation = saveNewReservation(product, reqDto, userId);

        // Kafka 메시지 전송
        try {
            String message = objectMapper.writeValueAsString(
                    ProductReservationCreatedPayloadV1.builder()
                            .productReservationId(productReservation.getProductReservationId())
                            .userId(userId)
                            .productId(productId)
                            .quantity(reqDto.getQuantity())
                            .build()
            );
            productReservationProducer.sendReservationCreated(message);
        } catch (JsonProcessingException e) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
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

        // Kafka 이벤트 발행 (상품 서비스에 재고 증가 요청)
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
}
