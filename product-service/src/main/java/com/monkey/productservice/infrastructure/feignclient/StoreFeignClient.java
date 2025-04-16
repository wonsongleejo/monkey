package com.monkey.productservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productservice.infrastructure.feignclient.dto.StoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "store-service", path = "v1/stores")
public interface StoreFeignClient {
    @GetMapping
    ResDTO<StoreDTO> getStoreById(@PathVariable("storeId") UUID storeId);
}
