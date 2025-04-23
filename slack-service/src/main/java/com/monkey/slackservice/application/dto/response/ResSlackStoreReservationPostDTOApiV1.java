package com.monkey.slackservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ResSlackStoreReservationPostDTOApiV1 {
    private UUID slackMessageId;
    private String slackMessage;
    private String slackMessageType;
    private StoreReservation storeReservation;

    @Getter
    @Builder
    public static class StoreReservation {
        private UUID storeReservationId;
        private String status;
        private TimeSlot timeSlot;
        private User user;

        @Getter
        @Builder
        public static class TimeSlot {
            private Store store;
            private String date;
            private String entryTime;
            private String exitTime;

            @Getter
            @Builder
            public static class Store {
                private UUID storeId;
            }
        }

        @Getter
        @Builder
        public static class User {
            private Long userId;
            private String userName;
        }
    }

    public static ResSlackStoreReservationPostDTOApiV1 of(
            String message, String type, UUID messageId,
            StoreReservation reservation
    ) {
        return ResSlackStoreReservationPostDTOApiV1.builder()
                .slackMessage(message)
                .slackMessageType(type)
                .slackMessageId(messageId)
                .storeReservation(reservation)
                .build();
    }
}