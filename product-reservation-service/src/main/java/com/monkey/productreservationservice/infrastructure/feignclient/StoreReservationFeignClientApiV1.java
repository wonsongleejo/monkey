package com.monkey.productreservationservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreReservationClientGetDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "store-reservation-service", path = "/v1/store-reservations")
@Profile("!test")
public interface StoreReservationFeignClientApiV1 {
    @GetMapping
    ResDTO<ResStoreReservationClientGetDTOApiV1> getReservationsByUserIdAndStoreId(@RequestParam Long userId, @RequestParam UUID storeId);
}