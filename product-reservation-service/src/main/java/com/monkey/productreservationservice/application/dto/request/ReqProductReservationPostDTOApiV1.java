package com.monkey.productreservationservice.application.dto.request;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.enums.ProductReservationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqProductReservationPostDTOApiV1 {
    @Valid
    @NotNull(message = "예약 정보를 입력해주세요.")
    private ProductReservation productReservation;

    @Getter
    @Setter
    public static class ProductReservation {
        @NotNull(message = "구매할 수량을 입력하세요.")
        @Min(value = 1, message = "구매 수량은 1개 이상이어야 합니다.")
        private Integer quantity; // 최대 수량 제한은 서비스에서 처리

        // DTO → Entity 변환 메서드
        public ProductReservationEntity toEntity(UUID productId, long userId, UUID storeId, ProductReservationStatus status) {
            return ProductReservationEntity.builder()
                    .productId(productId)
                    .userId(userId)
                    .storeId(storeId)
                    .status(ProductReservationStatus.PENDING_PICKUP)
                    .quantity(quantity)
                    .build();
        }
    }
}