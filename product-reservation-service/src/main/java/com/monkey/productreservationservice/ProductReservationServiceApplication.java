package com.monkey.productreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.monkey")
@EnableFeignClients
@EnableCaching
public class ProductReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductReservationServiceApplication.class, args);
    }

}
