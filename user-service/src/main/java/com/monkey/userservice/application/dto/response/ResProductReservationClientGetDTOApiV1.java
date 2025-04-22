package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ResProductReservationClientGetDTOApiV1 {

    private String code;
    private String message;
    private ModelData data;

    @Getter
    @Builder
    public static class ModelData {

        private List<ProductReservation> productReservationList;

        @Getter
        @Builder
        public static class ProductReservation{
            private UUID productReservationId;
            private Product product;
            private Store store;

            @Getter
            @Builder
            public static class Product{
                private UUID productId;
                private String productName;
            }

            @Getter
            @Builder
            public static class Store{
                private UUID storeId;
                private String storeName;
                private int quantity;
                private String receivedStatus;
            }
        }
    }
}