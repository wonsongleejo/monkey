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

    public static ResProductGetByIdDTOApiV1 of(ProductEntity productEntity) {
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
        private UUID storeId;
        private String productName;
        private Integer price;
        private Integer quantity;

        public static Product from(ProductEntity productEntity) {
            return Product.builder()
                    .productId(productEntity.getProductId())
                    .storeId(productEntity.getStoreId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .quantity(productEntity.getQuantity())
                    .build();
        }
    }
}
