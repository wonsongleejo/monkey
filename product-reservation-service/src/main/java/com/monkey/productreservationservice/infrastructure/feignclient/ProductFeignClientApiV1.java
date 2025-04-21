package com.monkey.productreservationservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service", path = "/v1/products")
@Profile("!test")
public interface ProductFeignClientApiV1 {
    @GetMapping("/{productId}")
    ResDTO<ResProductClientGetByIdDTOApiV1> getProductById(@PathVariable UUID productId);
}
