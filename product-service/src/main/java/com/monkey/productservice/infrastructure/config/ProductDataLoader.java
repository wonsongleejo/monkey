package com.monkey.productservice.infrastructure.config;

import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.infrastructure.persistence.ProductJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Configuration
@Profile("test")
public class ProductDataLoader {

    @Bean
    public CommandLineRunner loadProductData(ProductJpaRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                ProductEntity productEntity1 = ProductEntity.builder()
//                        .productId(UUID.randomUUID())
                        .storeId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                        .productName("테스트 상품 1")
                        .price(10000)
                        .quantity(100)
                        .purchaseLimitPerUser(1)
                        .build();

                ProductEntity productEntity2 = ProductEntity.builder()
//                        .productId(UUID.randomUUID())
                        .storeId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                        .productName("테스트 상품 2")
                        .price(20000)
                        .quantity(200)
                        .purchaseLimitPerUser(2)
                        .build();

                productRepository.save(productEntity1);
                productRepository.save(productEntity2);
            }
        };
    }
}
