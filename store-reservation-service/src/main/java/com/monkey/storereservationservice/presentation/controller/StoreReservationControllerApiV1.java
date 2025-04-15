package com.monkey.storereservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostByIdCancelDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
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
    public ResponseEntity<ResDTO<ResStoreReservationPostDTOApiV1>> postBy(@Valid @RequestBody ReqStoreReservationPostDTOApiV1 request) {

        ResStoreReservationPostDTOApiV1 resDto = ResStoreReservationPostDTOApiV1.of();

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    @PostMapping("/{storeReservationId}/cancel")
    public ResponseEntity<ResDTO<ResStoreReservationPostByIdCancelDTOApiV1>> cancelBy(@PathVariable UUID storeReservationId) {

        ResStoreReservationPostByIdCancelDTOApiV1 resDto = ResStoreReservationPostByIdCancelDTOApiV1.of();

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ResDTO<ResStoreReservationGetDTOApiV1>> getBy(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) UUID storeId
    ) {
        ResStoreReservationGetDTOApiV1 resDto = ResStoreReservationGetDTOApiV1.of();

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    @GetMapping("/{storeReservationId}")
    public ResponseEntity<ResDTO<ResStoreReservationGetByIdDTOApiV1>> getDetailBy(
            @PathVariable UUID storeReservationId
    ) {
        ResStoreReservationGetByIdDTOApiV1 resDto = ResStoreReservationGetByIdDTOApiV1.of();

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }
}