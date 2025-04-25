package com.monkey.productreservationservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "product-service", path = "/v1/products")
@Profile("!test")
public interface ProductFeignClientApiV1 {

    // 상품 조회
    @GetMapping("/{productId}")
    ResDTO<ResProductClientGetByIdDTOApiV1> getProductById(@PathVariable UUID productId);

    // 상품 재고 차감
    @PutMapping("/{productId}/stock/decrease")
    void decreaseStock(@PathVariable UUID productId, @RequestHeader("X-User-Id") long userId, @RequestParam int quantity);
}
