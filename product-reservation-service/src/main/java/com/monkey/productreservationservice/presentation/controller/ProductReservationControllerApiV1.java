package com.monkey.productreservationservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetByIdDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationGetDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostByIdCancelDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.service.ProductReservationServiceApiV1;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
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
    private final ProductReservationServiceApiV1 productReservationService;

    // 예약 등록
    @PostMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductReservationPostDTOApiV1>> postBy(
            @PathVariable UUID productId,
            @RequestBody @Valid ReqProductReservationPostDTOApiV1 reqDto
            ) {
        long userId = 123L; // 추후에 인증된 정보에서 받아오기
        ResProductReservationPostDTOApiV1 resDto = productReservationService.postBy(productId, reqDto, userId);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약 취소
    @PostMapping("/{productReservationId}/cancel")
    public ResponseEntity<ResDTO<ResProductReservationPostByIdCancelDTOApiV1>> cancelBy(
            @PathVariable UUID productReservationId
    ) {
        ResProductReservationPostByIdCancelDTOApiV1 resDto = productReservationService.cancelBy(productReservationId)
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getBy() {
        ResProductReservationGetDTOApiV1 resDto = productReservationService.getBy();
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 예약내역 상세 조회
    @GetMapping("/{productReservationId}")
    public ResponseEntity<ResDTO<ResProductReservationGetByIdDTOApiV1>> getById(@PathVariable UUID productReservationId) {
        ProductReservationEntity productReservation = getActiveProductReservationById(productReservationId);
        ResProductReservationGetByIdDTOApiV1 resDto = ResProductReservationGetByIdDTOApiV1.of(productReservation);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }



}
