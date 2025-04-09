package com.monkey.productservice.application.dto.response;

import com.monkey.productservice.domain.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResProductPostDTOApiV1 {
    private Product product;

    public static ResProductPostDTOApiV1 of(ProductEntity productEntity) {
        return ResProductPostDTOApiV1.builder()
                .product(Product.from(productEntity))
                .build();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private UUID productId;

        public static Product from(ProductEntity productEntity) {
            return Product.builder()
                    .productId(productEntity.getProductId())
                    .build();
        }
    }
}
