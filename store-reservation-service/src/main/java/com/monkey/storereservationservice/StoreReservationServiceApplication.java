package com.monkey.storereservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.monkey")
@EnableFeignClients
public class StoreReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreReservationServiceApplication.class, args);
    }

}
