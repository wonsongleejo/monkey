package com.monkey.productreservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetByIdDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/product-reservations")
public class ProductReservationControllerApiV1 {

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

        ResProductReservationPostDTOApiV1 resDto = ResProductReservationPostDTOApiV1.of(productReservationEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약 취소
    @PostMapping("/{productReservationId}/cancel")
    public ResponseEntity<ResDTO<ResProductReservationPostByIdCancelDTOApiV1>> cancelBy(
            @PathVariable UUID productReservationId
    ) {
        // 임시 데이터
        ProductReservationEntity productReservationEntity = ProductReservationEntity.builder()
                .productReservationId(productReservationId)
                .productId(UUID.randomUUID())
                .userId(123L)
                .storeId(UUID.randomUUID())
                .quantity(1)
                .status(ProductReservationStatus.CANCELED)
                .build();

        ResProductReservationPostByIdCancelDTOApiV1 resDto = ResProductReservationPostByIdCancelDTOApiV1.of(productReservationEntity);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getBy() {
        List<ProductReservationEntity> productEntityList = new ArrayList<>();

        // 임시 조회용 데이터
        for (int i = 0; i < 5; i++) {
            productEntityList.add(ProductReservationEntity.builder()
                    .productReservationId(UUID.randomUUID())
                    .productId(UUID.randomUUID())
                    .userId(123)
                    .storeId(UUID.randomUUID())
                    .quantity(10 + i)
                    .status(ProductReservationStatus.PENDING_PICKUP)
                    .build()
            );
        }

        ResProductReservationGetDTOApiV1 resDto = ResProductReservationGetDTOApiV1.of(productEntityList);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 상세 조회
    @GetMapping("/{productReservationId}")
    public ResponseEntity<ResDTO<ResProductReservationGetByIdDTOApiV1>> getById(@PathVariable UUID productReservationId) {
        ProductReservationEntity productReservation = ProductReservationEntity.builder()
                .productReservationId(productReservationId)
                .productId(UUID.randomUUID())
                .userId(123L)
                .storeId(UUID.randomUUID())
                .quantity(1)
                .status(ProductReservationStatus.PENDING_PICKUP)
                .build();

        ResProductReservationGetByIdDTOApiV1 resDto = ResProductReservationGetByIdDTOApiV1.of(productReservation);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

}
