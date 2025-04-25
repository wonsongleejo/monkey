package com.monkey.storereservationservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPutByIdStatusDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPutByIdStatusDTOApiV1;
import com.monkey.storereservationservice.application.service.StoreReservationServiceApiV1;
import com.monkey.storereservationservice.infrastructure.security.UserContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/store-reservations")
@RequiredArgsConstructor
public class StoreReservationControllerApiV1 {

    private final StoreReservationServiceApiV1 storeReservationServiceApiV1;
    private final UserContext userContext;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ResDTO<ResStoreReservationPostDTOApiV1>> postBy(@Valid @RequestBody ReqStoreReservationPostDTOApiV1 request) {
        ResStoreReservationPostDTOApiV1 resDto = storeReservationServiceApiV1.create(request, userContext);
        return ResponseEntity.ok(ResDTO.success(resDto));
    }

    // 예약 전체 목록 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResStoreReservationGetDTOApiV1>> getAll(
            @RequestParam(required = false) UUID storeId
    ) {
        ResStoreReservationGetDTOApiV1 resDto = storeReservationServiceApiV1.getAll(userContext, storeId);
        return ResponseEntity.ok(ResDTO.success(resDto));
    }

    // 예약 단건 상세 조회
    @GetMapping("/{storeReservationId}")
    public ResponseEntity<ResDTO<ResStoreReservationGetByIdDTOApiV1>> getById(@PathVariable UUID storeReservationId) {
        ResStoreReservationGetByIdDTOApiV1 resDto = storeReservationServiceApiV1.getById(userContext, storeReservationId);
        return ResponseEntity.ok(ResDTO.success(resDto));
    }

    // 예약 상태 변경
    @PutMapping("/{storeReservationId}/status")
    public ResponseEntity<ResDTO<ResStoreReservationPutByIdStatusDTOApiV1>> putByStatus(
            @PathVariable UUID storeReservationId,
            @Valid @RequestBody ReqStoreReservationPutByIdStatusDTOApiV1 request
    ) {
        ResStoreReservationPutByIdStatusDTOApiV1 resDto = storeReservationServiceApiV1.changeStatus(
                userContext,
                storeReservationId,
                request.getStoreReservation().getStatus()
        );
        return ResponseEntity.ok(ResDTO.success(resDto));
    }

    // feignClient: 상품 예약 시 스토어 예약내역 전체 조회 -> 스토어 예약한 회원인지 확인
    @GetMapping("/feign")
    public ResponseEntity<ResDTO<ResStoreReservationGetDTOApiV1>> getAllByUserIdAndStoreId(
            @RequestParam UUID storeId,
            @RequestParam Long userId
    ) {
        ResStoreReservationGetDTOApiV1 resDto = storeReservationServiceApiV1.getAllByUserIdAndStoreId(userId, storeId);
        return ResponseEntity.ok(ResDTO.success(resDto));
    }
}