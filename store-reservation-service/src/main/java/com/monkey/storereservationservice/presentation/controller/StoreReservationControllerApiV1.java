package com.monkey.storereservationservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostByIdCancelDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.service.StoreReservationServiceApiV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/store-reservations")
@RequiredArgsConstructor
public class StoreReservationControllerApiV1 {

    private final StoreReservationServiceApiV1 storeReservationServiceApiV1;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ResDTO<ResStoreReservationPostDTOApiV1>> postBy(@Valid @RequestBody ReqStoreReservationPostDTOApiV1 request) {

        ResStoreReservationPostDTOApiV1 resDto = storeReservationServiceApiV1.create(request);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    // 예약 취소
    @PostMapping("/{storeReservationId}/cancel")
    public ResponseEntity<ResDTO<ResStoreReservationPostByIdCancelDTOApiV1>> cancelBy(@PathVariable UUID storeReservationId) {

        ResStoreReservationPostByIdCancelDTOApiV1 resDto = storeReservationServiceApiV1.cancel(storeReservationId);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    // 예약 전체 목록 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResStoreReservationGetDTOApiV1>> getAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) UUID storeId
    ) {
        ResStoreReservationGetDTOApiV1 resDto = storeReservationServiceApiV1.getAll(userId, storeId);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    // 예약 단건 상세 조회
    @GetMapping("/{storeReservationId}")
    public ResponseEntity<ResDTO<ResStoreReservationGetByIdDTOApiV1>> getById(
            @PathVariable UUID storeReservationId
    ) {
        ResStoreReservationGetByIdDTOApiV1 resDto = storeReservationServiceApiV1.getById(storeReservationId);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }
}