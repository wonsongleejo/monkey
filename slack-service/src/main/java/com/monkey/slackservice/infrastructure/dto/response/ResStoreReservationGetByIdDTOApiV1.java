package com.monkey.slackservice.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ResStoreReservationGetByIdDTOApiV1 {

    private StoreReservation storeReservation;

    @Getter
    @NoArgsConstructor
    public static class StoreReservation {
        private UUID storeReservationId;
        private String status;
        private TimeSlot timeSlot;
        private User user;

        @Getter
        @NoArgsConstructor
        public static class TimeSlot {
            private Store store;
            private String date;
            private String entryTime;
            private String exitTime;

            @Getter
            @NoArgsConstructor
            public static class Store {
                private UUID storeId;
                private String storeName;
            }
        }

        @Getter
        @NoArgsConstructor
        public static class User {
            private Long userId;
            private String userName;
            private String slackId;
        }
    }
}