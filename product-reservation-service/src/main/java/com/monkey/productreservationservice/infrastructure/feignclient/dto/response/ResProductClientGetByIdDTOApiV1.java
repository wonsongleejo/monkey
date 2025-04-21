package com.monkey.productreservationservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResProductClientGetByIdDTOApiV1 {
    private Product product;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer quantity;
        private Integer purchaseLimitPerUser;
        private Store store;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Store {
            private UUID storeId;
            private String storeName;
        }
    }
}

