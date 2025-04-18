package com.monkey.productreservationservice.infrastructure.config;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.persistence.ProductReservationJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Configuration
@Profile("test")
public class ProductReservationDataLoader {
    @Bean
    public CommandLineRunner loadProductReservationData(ProductReservationJpaRepository productReservationRepository) {
        return args -> {
            if (productReservationRepository.count() == 0) {
                ProductReservationEntity productReservationEntity = ProductReservationEntity.builder()
                        .productId(UUID.fromString("6b6fd8e9-1008-4023-ae4e-601b595a824e"))
                        .userId(123L)
                        .storeId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                        .quantity(1)
                        .status(ProductReservationStatus.PENDING_PICKUP)
                        .build();

                productReservationRepository.save(productReservationEntity);
            }
        };
    }
}
