package com.monkey.storereservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationCancelDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/store-reservations")
@RequiredArgsConstructor
public class StoreReservationControllerApiV1 {

    @PostMapping
    public ResDTO<ResStoreReservationPostDTOApiV1> createReservation(@Valid @RequestBody ReqStoreReservationPostDTOApiV1 request) {
        ResStoreReservationPostDTOApiV1 response = ResStoreReservationPostDTOApiV1.builder()
                .storeReservationId(UUID.randomUUID()) // 임시 데이터
                .status(StoreReservationStatus.SCHEDULED) // 임시 데이터
                .build();
        return ResDTO.success(response);
    }

    @PostMapping("/{reservationId}/cancel")
    public ResDTO<ResStoreReservationCancelDTOApiV1> cancelReservation(@PathVariable UUID reservationId) {
        ResStoreReservationCancelDTOApiV1 response = ResStoreReservationCancelDTOApiV1.builder()
                .storeReservationId(reservationId)
                .storeId(UUID.randomUUID()) // 임시 데이터
                .timeSlotId(UUID.randomUUID()) // 임시 데이터
                .person(1) // 임시 데이터
                .status(StoreReservationStatus.CANCELED) // 임시 데이터
                .build();
        return ResDTO.success(response);
    }
}