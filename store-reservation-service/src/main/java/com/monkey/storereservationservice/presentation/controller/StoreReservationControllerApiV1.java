package com.monkey.storereservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
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

    @PostMapping
    public ResponseEntity<ResDTO<ResStoreReservationPostDTOApiV1.StoreReservation>> postBy(@Valid @RequestBody ReqStoreReservationPostDTOApiV1 request) {
        ResStoreReservationPostDTOApiV1.StoreReservation resDto = ResStoreReservationPostDTOApiV1.StoreReservation.of(
                UUID.randomUUID(), // 임시 데이터
                StoreReservationStatus.SCHEDULED
        );
        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ResDTO<ResStoreReservationPostDTOApiV1.StoreReservation>> cancelBy(@PathVariable UUID reservationId) {
        ResStoreReservationPostDTOApiV1.StoreReservation resDto = ResStoreReservationPostDTOApiV1.StoreReservation.of(
                reservationId,
                StoreReservationStatus.CANCELED
        );
        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }
}