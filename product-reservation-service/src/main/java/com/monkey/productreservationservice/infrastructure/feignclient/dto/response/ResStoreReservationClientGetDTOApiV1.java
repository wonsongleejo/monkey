package com.monkey.productreservationservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResStoreReservationClientGetDTOApiV1 {
    private List<StoreReservation> storeReservationList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreReservation {
        private UUID storeReservationId;
        private TimeSlot timeSlot;
        private User user;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class TimeSlot {
            private Store store;

            @Getter
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Store {
                private UUID storeId;
            }
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class User {
            private Long userId;
        }
    }
}
