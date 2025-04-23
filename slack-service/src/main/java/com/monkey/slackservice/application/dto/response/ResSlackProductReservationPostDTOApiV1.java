package com.monkey.slackservice.application.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResSlackProductReservationPostDTOApiV1 {

    private UUID slackMessageId;
    private String slackMessage;
    private String slackMessageType;
    private ProductReservation productReservation;

    public static ResSlackProductReservationPostDTOApiV1 of(
            String message,
            String messageType,
            UUID slackMessageId,
            ProductReservation reservation
    ) {
        return ResSlackProductReservationPostDTOApiV1.builder()
                .slackMessage(message)
                .slackMessageType(messageType)
                .slackMessageId(slackMessageId)
                .productReservation(reservation)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReservation {
        private UUID productReservationId;
        private int quantity;
        private String status;

        private Product product;
        private Store store;
        private User user;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Product {
            private UUID productId;
            private String productName;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Store {
            private UUID storeId;
            private String storeName;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class User {
            private long userId;
            private String userName;
        }
    }
}