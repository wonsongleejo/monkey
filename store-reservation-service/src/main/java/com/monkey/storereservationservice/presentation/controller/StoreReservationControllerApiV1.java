package com.monkey.storereservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostByIdCancelDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationSearchDTOApiV1;
import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/store-reservations")
@RequiredArgsConstructor
public class StoreReservationControllerApiV1 {

    @PostMapping
    public ResponseEntity<ResDTO<ResStoreReservationPostDTOApiV1>> postBy(@Valid @RequestBody ReqStoreReservationPostDTOApiV1 request) {
        ResStoreReservationPostDTOApiV1 resDto = ResStoreReservationPostDTOApiV1.of();
        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ResDTO<ResStoreReservationPostByIdCancelDTOApiV1>> cancelBy(@PathVariable UUID reservationId) {
        ResStoreReservationPostByIdCancelDTOApiV1 resDto = ResStoreReservationPostByIdCancelDTOApiV1.of();
        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ResDTO<ResStoreReservationSearchDTOApiV1>> getBy() {

        List<ResStoreReservationGetDTOApiV1> content = List.of(
                ResStoreReservationGetDTOApiV1.builder()
                        .userId(1L)
                        .storeReservationId(UUID.randomUUID())
                        .status(StoreReservationStatus.SCHEDULED)
                        .timeSlot(
                                ResStoreReservationGetDTOApiV1.TimeSlot.builder()
                                        .store(
                                                ResStoreReservationGetDTOApiV1.Store.builder()
                                                        .storeId(UUID.randomUUID())
                                                        .build()
                                        )
                                        .date("2025-04-09")
                                        .startTime("10:00:00")
                                        .endTime("11:00:00")
                                        .build()
                        )
                        .build()
        );

        ResStoreReservationSearchDTOApiV1.PageInfo pageInfo = ResStoreReservationSearchDTOApiV1.PageInfo.builder()
                .size(10)
                .number(1)
                .totalElements(content.size())
                .totalPages(1)
                .build();

        ResStoreReservationSearchDTOApiV1 response = ResStoreReservationSearchDTOApiV1.builder()
                .storeReservationPage(
                        ResStoreReservationSearchDTOApiV1.StoreReservationPage.builder()
                                .content(content)
                                .page(pageInfo)
                                .build()
                )
                .build();

        return new ResponseEntity<>(
                ResDTO.success(response),
                HttpStatus.OK
        );
    }
}