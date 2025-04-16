package com.monkey.productservice.application.dto.response;

import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.infrastructure.feignclient.dto.StoreDTO;
import com.monkey.productservice.infrastructure.feignclient.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductGetByIdDTOApiV1 {
    private Product product;

    public static ResProductGetByIdDTOApiV1 of(ProductEntity productEntity, StoreDTO storeDto, UserDTO userDto) {
        return ResProductGetByIdDTOApiV1.builder()
                .product(Product.from(productEntity, storeDto, userDto))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product{
        private UUID productId;
        private String productName;
        private Integer price;
        private Integer quantity;
        private Integer purchaseLimitPerUser;
        private Store store;
        private User user;

        public static Product from(ProductEntity productEntity, StoreDTO storeDto, UserDTO userDto) {
            return Product.builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .quantity(productEntity.getQuantity())
                    .purchaseLimitPerUser(productEntity.getPurchaseLimitPerUser())
                    .store(Store.from(storeDto))
                    .user(User.from(userDto))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Store {
            private UUID storeId;
            private String storeName;

            public static Store from(StoreDTO storeDto) {
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
            private Long userId;

            public static User from(UserDTO userDto) {
                return User.builder()
                        .userId(userDto.getUserId())
                        .build();
            }
        }
    }
}
