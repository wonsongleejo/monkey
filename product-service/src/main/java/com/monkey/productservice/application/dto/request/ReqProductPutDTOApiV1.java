package com.monkey.productservice.application.dto.request;

import com.monkey.productservice.domain.entity.ProductEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqProductPutDTOApiV1 {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요.")
    private Product product;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Product {
        @NotBlank(message = "상품명을 입력해주세요.")
        @Size(max = 30, message = "상품명은 최대 30자까지 입력 가능합니다.")
        private String productName;

        @NotNull(message = "상품 가격을 입력해주세요.")
        @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
        private Integer price;

        @NotNull(message = "상품 수량을 입력해주세요.")
        @Min(value = 1, message = "상품 수량은 1개 이상이어야 합니다.")
        private Integer quantity;

        @NotNull(message = "1인당 구매 제한 수량을 입력해주세요.")
        @Min(value = 1, message = "1인당 구매 제한 수량은 1개 이상이어야 합니다.")
        private Integer purchaseLimitPerUser;

        // DTO 안에서 update 하는 방식 - 내부적으로 처리해서 깔끔
        public void update(ProductEntity productEntity) {
            productEntity.setProductName(productName);
            productEntity.setPrice(price);
            productEntity.setQuantity(quantity);
            productEntity.setPurchaseLimitPerUser(purchaseLimitPerUser);
        }
    }
}
