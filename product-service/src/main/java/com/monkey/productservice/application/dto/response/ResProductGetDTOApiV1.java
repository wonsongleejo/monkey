package com.monkey.productservice.application.dto.response;

import com.monkey.productservice.domain.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductGetDTOApiV1 {
    private List<Product> productList;
    private PageInfo pageInfo;

    public static ResProductGetDTOApiV1 of(Page<ProductEntity> productPage) {
        return ResProductGetDTOApiV1.builder()
                .productList(Product.from(productPage.getContent()))
                .pageInfo(PageInfo.from(productPage))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private int page;
        private int size;
        private int totalPages;
        private long totalElements;

        public static PageInfo from(Page<?> page) {
            return PageInfo.builder()
                    .page(page.getNumber())
                    .size(page.getSize())
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private UUID productId;
        private UUID storeId;
        private String productName;
        private Integer price;
        private Integer quantity;
        private Integer purchaseLimitPerUser;

        public static Product from(ProductEntity productEntity) {
            return Product.builder()
                    .productId(productEntity.getProductId())
                    .storeId(productEntity.getStoreId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .quantity(productEntity.getQuantity())
                    .purchaseLimitPerUser(productEntity.getPurchaseLimitPerUser())
                    .build();
        }

        public static List<Product> from(List<ProductEntity> productEntityList) {
            return productEntityList.stream().map(Product::from).collect(Collectors.toList());
        }
    }
}
