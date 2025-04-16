package com.monkey.productservice.infrastructure.feignclient;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productservice.infrastructure.feignclient.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "v1/users")
public interface UserFeignClient {
    @GetMapping
    ResDTO<UserDTO> getUserById(@PathVariable("userId") Long userId);
}
