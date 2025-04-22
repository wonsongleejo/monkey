package com.monkey.productreservationservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", path = "v1/users")
@Profile("!test")
public interface UserFeignClientApiV1 {
    @GetMapping("/details")
    ResDTO<ResUserClientGetByIdDTOApiV1> getUserById(@RequestHeader("X-User-Id") Long userId);
}
