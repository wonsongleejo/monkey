package com.monkey.productreservationservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetByIdDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.service.ProductReservationServiceApiV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/product-reservations")
public class ProductReservationControllerApiV1 {
    private final ProductReservationServiceApiV1 productReservationService;

    // 예약 등록
    @PostMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductReservationPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqProductReservationPostDTOApiV1 reqDto,
            @PathVariable UUID productId,
            @RequestHeader("X-User-Id") long userId
            ) {
        ResProductReservationPostDTOApiV1 resDto = productReservationService.postBy(reqDto, productId, userId);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약 취소
    @PostMapping("/{productReservationId}/cancel")
    public ResponseEntity<ResDTO<ResProductReservationPostByIdCancelDTOApiV1>> cancelBy(
            @PathVariable UUID productReservationId,
            @RequestHeader("X-User-Id") long userId
    ) {
        ResProductReservationPostByIdCancelDTOApiV1 resDto = productReservationService.cancelBy(productReservationId, userId);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getBy(Pageable pageable) {
        ResProductReservationGetDTOApiV1 resDto = productReservationService.getBy(pageable);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 개인 예약내역 조회
    @GetMapping("/my")
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getMyProductReservations(
            @RequestHeader("X-User-Id") long userId,
            Pageable pageable
    ) {
        ResProductReservationGetDTOApiV1 resDto = productReservationService.getByUserId(userId, pageable);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 단건 조회
    @GetMapping("/{productReservationId}")
    public ResponseEntity<ResDTO<ResProductReservationGetByIdDTOApiV1>> getById(@PathVariable UUID productReservationId) {
        ResProductReservationGetByIdDTOApiV1 resDto = productReservationService.getById(productReservationId);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }
}
