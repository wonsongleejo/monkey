package com.monkey.productreservationservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetByIdDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/product-reservations")
public class ProductReservationControllerApiV1 {
    private final ProductReservationRepository productReservationRepository; // 추후에 서비스로 이동

    // 예약 생성
    @PostMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductReservationPostDTOApiV1>> postBy(
            @PathVariable UUID productId,
            @RequestBody @Valid ReqProductReservationPostDTOApiV1 reqDto
            ) {
        // storeId, userId -> 임시로 값 받아옴
        UUID storeId = UUID.randomUUID();
        long userId = 123L;

        ProductReservationEntity productReservationEntity = reqDto.getProductReservation().toEntity(
                productId, userId, storeId, ProductReservationStatus.PENDING_PICKUP
        );

        ProductReservationEntity saved = productReservationRepository.save(productReservationEntity);

        ResProductReservationPostDTOApiV1 resDto = ResProductReservationPostDTOApiV1.of(saved);

        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약 취소
    @PostMapping("/{productReservationId}/cancel")
    public ResponseEntity<ResDTO<ResProductReservationPostByIdCancelDTOApiV1>> cancelBy(
            @PathVariable UUID productReservationId
    ) {
        ProductReservationEntity productReservationEntity = getActiveProductReservationById(productReservationId);

        productReservationEntity.delete(123L);

        ProductReservationEntity saved = productReservationRepository.save(productReservationEntity);

        ResProductReservationPostByIdCancelDTOApiV1 resDto = ResProductReservationPostByIdCancelDTOApiV1.of(saved);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getBy() {
        List<ProductReservationEntity> productReservationList = productReservationRepository.findAllByIsDeletedFalse();
        ResProductReservationGetDTOApiV1 resDto = ResProductReservationGetDTOApiV1.of(productReservationList);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 상세 조회
    @GetMapping("/{productReservationId}")
    public ResponseEntity<ResDTO<ResProductReservationGetByIdDTOApiV1>> getById(@PathVariable UUID productReservationId) {
        ProductReservationEntity productReservation = getActiveProductReservationById(productReservationId);
        ResProductReservationGetByIdDTOApiV1 resDto = ResProductReservationGetByIdDTOApiV1.of(productReservation);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 존재하는 예약 검증 메서드
    private ProductReservationEntity getActiveProductReservationById(UUID productReservationId) {
        return productReservationRepository.findByProductReservationIdAndIsDeletedFalse(productReservationId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }

}
