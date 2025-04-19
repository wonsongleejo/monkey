package com.monkey.productreservationservice.application.dto.response;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResProductReservationPostDTOApiV1 {
    private ProductReservation productReservation;

    public static ResProductReservationPostDTOApiV1 of(ProductReservationEntity productReservationEntity) {
        return ResProductReservationPostDTOApiV1.builder()
                .productReservation(ProductReservation.from(productReservationEntity))
                .build();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReservation {
        private UUID productReservationId;
        private UUID productId;
        private Integer quantity;
//        private Long createdBy; // 회원기능 구현 시 userId 필드 지우고 이걸로 대체하기

        public static ProductReservation from(ProductReservationEntity productReservationEntity) {
            return ProductReservation.builder()
                    .productReservationId(productReservationEntity.getProductReservationId())
                    .productId(productReservationEntity.getProductId())
                    .quantity(productReservationEntity.getQuantity())
//                    .createdBy(productReservationEntity.getCreatedBy())
                    .build();
        }

    }
}

