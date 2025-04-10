package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ResProductReservationGetDTOApiV1 {

    private List<ProductReservation> productReservationList;

    public static ResProductReservationGetDTOApiV1 of(
            List<ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation> resClientDtoList
    ) {
        return ResProductReservationGetDTOApiV1.builder()
                .productReservationList(ProductReservation.from(resClientDtoList))
                .build();
    }

    @Getter
    @Builder
    public static class ProductReservation{
        private UUID productReservationId;
        private Product product;
        private Store store;

        public static ProductReservation from(
                ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation resClientDto
        ) {
            return ProductReservation.builder()
                    .productReservationId(resClientDto.getProductReservationId())
                    .product(Product.from(resClientDto.getProduct()))
                    .store(Store.from(resClientDto.getStore()))
                    .build();
        }

        public static List<ProductReservation> from(
                List<ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation> resClientDtoList
        ){
            return resClientDtoList.stream()
                    .map(ProductReservation::from)
                    .toList();
        }

        @Getter
        @Builder
        public static class Product{
            private UUID productId;
            private String productName;

            public static Product from(
                    ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.Product product
            ) {
                return Product.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .build();
            }
        }

        @Getter
        @Builder
        public static class Store{
            private UUID storeId;
            private String storeName;
            private int quantity;
            private String receivedStatus;

            public static Store from(
                    ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.Store store
            ) {
                return Store.builder()
                        .storeId(store.getStoreId())
                        .storeName(store.getStoreName())
                        .quantity(store.getQuantity())
                        .receivedStatus(store.getReceivedStatus())
                        .build();
            }
        }
    }
}
