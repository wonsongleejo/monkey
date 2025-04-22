package com.monkey.userservice.infrastructure.client;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.application.dto.response.ResStoreReservationClientGetDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-reservation-service")
public interface ProductReservationFeignClientApiV1 {

    @GetMapping("/v1/users/{userId}/store-reservations")
    ResDTO<List<ResStoreReservationClientGetDTOApiV1>> getProductReservations(@PathVariable("userId") Long userId);
}
