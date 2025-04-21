package com.monkey.productservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "store-service", path = "/v1/stores")
@Profile("!test")
public interface StoreFeignClientApiV1 {
    @GetMapping("/{storeId}")
    ResDTO<ResStoreClientGetByIdDTOApiV1> getStoreById(@PathVariable("storeId") UUID storeId);
}
