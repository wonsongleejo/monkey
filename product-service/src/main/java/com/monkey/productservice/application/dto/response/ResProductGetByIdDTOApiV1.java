package com.monkey.productservice.application.dto.response;

import com.monkey.productservice.domain.entity.ProductEntity;
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

    public static ResProductGetByIdDTOApiV1 of(ProductEntity productEntity) { // ,StoreDTO 추가해서 from 안에 넣기 (FeignClient로 받아옴)
        return ResProductGetByIdDTOApiV1.builder()
                .product(Product.from(productEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product{
        private UUID productId;
        private Store store; // Store 자체를 리턴하도록 수정
        private String productName;
        private Integer price;
        private Integer quantity;
        private Integer purchaseLimitPerUser;

        public static Product from(ProductEntity productEntity) { // ,StoreDTO 추가해서 from 안에 넣기 (FeignClient로 받아옴)
            return Product.builder()
                    .productId(productEntity.getProductId())
                    .store(Store.from())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .quantity(productEntity.getQuantity())
                    .purchaseLimitPerUser(productEntity.getPurchaseLimitPerUser())
                    .build();
        }

        @Getter
        @Builder
        public static class Store {
            private UUID storeId;
            private String storeName;

            public static Store from() { // Store 값 나중에 받아오기
                return Store.builder()
                        .storeId(UUID.randomUUID())
                        .storeName(UUID.randomUUID().toString())
                        .build();
            }
        }
    }
}
