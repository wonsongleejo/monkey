package com.monkey.userservice.infrastructure.client;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.infrastructure.client.dto.ResStoreReservationClientGetDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "store-reservation-service")
public interface StoreReservationFeignClientApiV1 {

    @GetMapping("/v1/store-reservations")
    ResDTO<ResStoreReservationClientGetDTOApiV1> getStoreReservations(@RequestParam("userId") Long userId);
}
