package com.monkey.userservice.infrastructure.client;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.infrastructure.client.dto.ResProductReservationClientGetDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "product-reservation-service")
public interface ProductReservationFeignClientApiV1 {

    @GetMapping("/v1/product-reservations/my")
    ResDTO<ResProductReservationClientGetDTOApiV1> getProductReservations(@RequestHeader("userId") Long userId);
}
