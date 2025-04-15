package com.monkey.productreservationservice.application.dto.response;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResProductReservationPostByIdCancelDTOApiV1 {
    private ProductReservation productReservation;

    public static ResProductReservationPostByIdCancelDTOApiV1 of(ProductReservationEntity productReservationEntity) {
        return ResProductReservationPostByIdCancelDTOApiV1.builder()
                .productReservation(ProductReservation.from(productReservationEntity))
                .build();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReservation {
        private UUID productReservationId;
        private ProductReservationStatus status;

        public static ProductReservation from(ProductReservationEntity productReservationEntity) {
            return ProductReservation.builder()
                    .productReservationId(productReservationEntity.getProductReservationId())
                    .status(productReservationEntity.getStatus())
                    .build();
        }
    }
}
