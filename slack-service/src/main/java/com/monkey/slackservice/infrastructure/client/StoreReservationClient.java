package com.monkey.slackservice.infrastructure.client;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.slackservice.infrastructure.dto.response.ResStoreReservationGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "store-reservation-service", path = "/v1/store-reservations")
public interface StoreReservationClient {
    @GetMapping("/{storeReservationId}")
    ResDTO<ResStoreReservationGetByIdDTOApiV1> getById(
            @PathVariable("storeReservationId") UUID storeReservationId
    );
}