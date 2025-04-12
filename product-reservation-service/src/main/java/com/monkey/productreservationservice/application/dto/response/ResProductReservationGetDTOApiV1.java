package com.monkey.productreservationservice.application.dto.response;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.enums.ProductReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductReservationGetDTOApiV1 {
    private List<ProductReservation> productReservationList;

    public static ResProductReservationGetDTOApiV1 of(List<ProductReservationEntity> productReservationEntityList) {
        return ResProductReservationGetDTOApiV1.builder()
                .productReservationList(ProductReservation.from(productReservationEntityList))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReservation {
        private UUID productReservationId;
        private UUID productId;
        private long userId;
        private UUID storeId;
        private Integer quantity;
        private ProductReservationStatus status;

        public static ProductReservation from(ProductReservationEntity productReservationEntity) {
            return ProductReservation.builder()
                    .productReservationId(productReservationEntity.getProductReservationId())
                    .productId(productReservationEntity.getProductId())
                    .userId(productReservationEntity.getUserId())
                    .storeId(productReservationEntity.getStoreId())
                    .quantity(productReservationEntity.getQuantity())
                    .status(productReservationEntity.getStatus())
                    .build();
        }

        public static List<ProductReservation> from(List<ProductReservationEntity> productReservationEntityList) {
            return productReservationEntityList.stream().map(ProductReservation::from).collect(Collectors.toList());
        }
    }
}
