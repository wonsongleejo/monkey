package com.monkey.userservice.infrastructure.client;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.application.dto.response.ResStoreReservationClientGetDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store-reservation-service")
public interface StoreReservationFeignClientApiV1 {

    @GetMapping("/v1/users/{userId}/store-reservations")
    ResDTO<ResStoreReservationClientGetDTOApiV1> getStoreReservations(@PathVariable("userId") Long userId);
}
