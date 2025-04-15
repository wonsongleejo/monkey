package com.monkey.productreservationservice.application.dto.response;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
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

    public static ResProductReservationGetByIdDTOApiV1 of(ProductReservationEntity productReservationEntity) {
        return ResProductReservationGetByIdDTOApiV1.builder()
                .productReservation(ProductReservation.from(productReservationEntity))
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
        private User user; // FeignClient
        private Store store; // FeignClient

        public static ProductReservation from(
                ProductReservationEntity productReservationEntity) {
            // 추후에 ProductDTO, UserDTO, StoreDTO 받아와서 from()에 넣어야 함
            return ProductReservation.builder()
                    .productReservationId(productReservationEntity.getProductReservationId())
                    .quantity(productReservationEntity.getQuantity())
                    .status(productReservationEntity.getStatus())
                    .createdAt(productReservationEntity.getCreatedAt())
                    .product(Product.from())
                    .user(User.from())
                    .store(Store.from())
                    .build();
        }

        // Product
        @Getter
        @Builder
        public static class Product {
            private UUID productId;
            private String productName;

            public static Product from() { // Product 값 받아와서 채우기
                return Product.builder()
                        .productId(UUID.randomUUID())
                        .productName("")
                        .build();
            }
        }

        @Getter
        @Builder
        public static class User {
            private long userId;
            private String userName;

            public static User from() { // User 값 받아와서 채우기
                return User.builder()
                        .userId(123L)
                        .userName("")
                        .build();
            }
        }

        @Getter
        @Builder
        public static class Store {
            private UUID storeId;
            private String storeName;

            public static Store from() { // Store 값 받아와서 채우기
                return Store.builder()
                        .storeId(UUID.randomUUID())
                        .storeName("")
                        .build();
            }
        }
    }
}
