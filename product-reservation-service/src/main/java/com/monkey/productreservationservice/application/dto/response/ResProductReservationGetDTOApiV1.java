package com.monkey.productreservationservice.application.dto.response;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
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
public class ResProductReservationGetDTOApiV1 {
    private List<ProductReservation> productReservationList;
    private PageInfo pageInfo;

    public static ResProductReservationGetDTOApiV1 of(Page<ProductReservationEntity> page) {
        return ResProductReservationGetDTOApiV1.builder()
                .productReservationList(ProductReservation.from(page.getContent()))
                .pageInfo(PageInfo.from(page))
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
