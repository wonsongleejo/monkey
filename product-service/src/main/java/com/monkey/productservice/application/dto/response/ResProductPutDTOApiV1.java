package com.monkey.productservice.application.dto.response;

import com.monkey.productservice.domain.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductPutDTOApiV1 {
    private Product product;

    public static ResProductPutDTOApiV1 of(ProductEntity productEntity) {
        return ResProductPutDTOApiV1.builder()
                .product(Product.from(productEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private String productName;
        private Integer price;
        private Integer quantity;
        private Integer purchaseLimitPerUser;

        public static Product from(ProductEntity entity) {
            return Product.builder()
                    .productName(entity.getProductName())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .purchaseLimitPerUser(entity.getPurchaseLimitPerUser())
                    .build();
        }
    }
}
