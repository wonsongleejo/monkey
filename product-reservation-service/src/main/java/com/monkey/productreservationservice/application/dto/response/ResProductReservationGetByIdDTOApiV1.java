package com.monkey.productreservationservice.application.dto.response;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductReservationGetByIdDTOApiV1 {
    private ProductReservation productReservation;

    public static ResProductReservationGetByIdDTOApiV1 of(ProductReservationEntity productReservationEntity,
                                                          ResProductClientGetByIdDTOApiV1 productDto,
                                                          ResStoreClientGetByIdDTOApiV1 storeDto,
                                                          ResUserClientGetByIdDTOApiV1 userDto
                                                          ) {
        return ResProductReservationGetByIdDTOApiV1.builder()
                .productReservation(ProductReservation.from(productReservationEntity, productDto, storeDto, userDto))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReservation {
        private UUID productReservationId;
        private Integer quantity;
        private ProductReservationStatus status;
        private LocalDateTime createdAt;
        private Product product; // FeignClient
        private Store store; // FeignClient
        private User user; // FeignClient

        public static ProductReservation from(
                ProductReservationEntity productReservationEntity,
                ResProductClientGetByIdDTOApiV1 productDto,
                ResStoreClientGetByIdDTOApiV1 storeDto,
                ResUserClientGetByIdDTOApiV1 userDto
                ) {
            return ProductReservation.builder()
                    .productReservationId(productReservationEntity.getProductReservationId())
                    .quantity(productReservationEntity.getQuantity())
                    .status(productReservationEntity.getStatus())
                    .createdAt(productReservationEntity.getCreatedAt())
                    .product(Product.from(productDto))
                    .store(Store.from(storeDto))
                    .user(User.from(userDto))
                    .build();
        }

        // Product
        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Product {
            private UUID productId;
            private String productName;

            public static Product from(ResProductClientGetByIdDTOApiV1 productDto) {
                return Product.builder()
                        .productId(productDto.getProductId())
                        .productName(productDto.getProductName())
                        .build();
            }
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Store {
            private UUID storeId;
            private String storeName;

            public static Store from(ResStoreClientGetByIdDTOApiV1 storeDto) {
                return Store.builder()
                        .storeId(storeDto.getStoreId())
                        .storeName(storeDto.getStoreName())
                        .build();
            }
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class User {
            private long userId;
            private String userName;

            public static User from(ResUserClientGetByIdDTOApiV1 userDto) {
                return User.builder()
                        .userId(userDto.getUserId())
                        .userName(userDto.getUsername())
                        .build();
            }
        }
    }
}
