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
@NoArgsConstructor
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

        @NotBlank
        @Size(max = 100)
        private String productName;

        @NotNull
        @Min(0)
        private Integer price;

        @NotNull
        @Min(0)
        private Integer quantity;

        // DTO → Entity 변환 메서드
        public ProductEntity toEntity() {
            return new ProductEntity(this);
        }
    }
}
