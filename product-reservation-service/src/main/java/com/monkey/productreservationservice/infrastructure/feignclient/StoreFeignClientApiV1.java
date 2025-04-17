package com.monkey.productreservationservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "store-service", path = "v1/stores")
public interface StoreFeignClientApiV1 {
    @GetMapping
    ResDTO<ResStoreClientGetByIdDTOApiV1> getStoreById(@PathVariable UUID storeId);
}
