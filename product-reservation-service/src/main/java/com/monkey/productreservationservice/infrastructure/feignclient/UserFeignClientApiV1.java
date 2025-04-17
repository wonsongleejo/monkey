package com.monkey.productreservationservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "v1/users")
public interface UserFeignClientApiV1 {
    @GetMapping
    ResDTO<ResUserClientGetByIdDTOApiV1> getUserById(@PathVariable("userId") Long userId);
}
