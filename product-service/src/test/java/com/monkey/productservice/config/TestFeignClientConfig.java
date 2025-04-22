package com.monkey.productservice.config;

import com.monkey.productservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productservice.infrastructure.feignclient.UserFeignClientApiV1;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestFeignClientConfig {
    @Bean
    @Primary
    public StoreFeignClientApiV1 storeFeignClientApiV1() {
        return Mockito.mock(StoreFeignClientApiV1.class);
    }

    @Bean
    @Primary
    public UserFeignClientApiV1 userFeignClientApiV1() {
        return Mockito.mock(UserFeignClientApiV1.class);
    }
}
