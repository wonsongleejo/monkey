package com.monkey.productreservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.enums.ProductReservationStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{productId}/cancel")
    public ResponseEntity<ResDTO<ResProductReservationPostByIdCancelDTOApiV1>> cancelBy(
            @PathVariable UUID productId
    ) {
        // 임시 데이터
        ProductReservationEntity productReservationEntity = ProductReservationEntity.builder()
                .productReservationId(UUID.randomUUID())
                .productId(productId)
                .userId(123L)
                .storeId(UUID.randomUUID())
                .quantity(1)
                .status(ProductReservationStatus.CANCELED)
                .build();

        ResProductReservationPostByIdCancelDTOApiV1 resDto = ResProductReservationPostByIdCancelDTOApiV1.of(productReservationEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }



}
