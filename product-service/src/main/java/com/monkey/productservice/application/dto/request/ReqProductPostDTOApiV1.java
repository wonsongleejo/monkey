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

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqProductPostDTOApiV1 {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요.")
    private Product product;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Product {
        @NotNull
        private UUID storeId;

        @NotBlank(message = "상품명을 입력해주세요.")
        @Size(max = 100)
        private String productName;

        @NotNull(message = "상품 가격을 입력해주세요.")
        @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
        private Integer price;

        @NotNull(message = "상품 수량을 입력해주세요.")
        @Min(value = 0, message = "상품 수량은 0 이상이어야 합니다.")
        private Integer quantity;

        // DTO → Entity 변환 메서드
        // 엔티티에 생성자 만들어서 이용하면 안됨 -> ProductEntity.builder() 사용하는 방식으로 수정
        public ProductEntity toEntity() {
            return ProductEntity.builder()
                    .storeId(storeId)
                    .productName(productName)
                    .price(price)
                    .quantity(quantity)
                    .build();
        }
    }
}
