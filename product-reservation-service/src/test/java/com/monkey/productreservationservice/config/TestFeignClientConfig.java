package com.monkey.productreservationservice.config;

import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreReservationFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.UserFeignClientApiV1;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestFeignClientConfig {

    @Bean
    @Primary
    public ProductFeignClientApiV1 productFeignClientApiV1() {
        return Mockito.mock(ProductFeignClientApiV1.class);
    }

    @Bean
    @Primary
    public StoreFeignClientApiV1 storeFeignClientApiV1() {
        return Mockito.mock(StoreFeignClientApiV1.class);
    }

    @Bean
    @Primary
    public StoreReservationFeignClientApiV1 storeReservationFeignClientApiV1() {
        return Mockito.mock(StoreReservationFeignClientApiV1.class);
    }

    @Bean
    @Primary
    public UserFeignClientApiV1 userFeignClientApiV1() {
        return Mockito.mock(UserFeignClientApiV1.class);
    }
}
