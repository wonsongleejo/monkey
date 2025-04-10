package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationClientGetDTOApiV1 {

    private String code;
    private String message;
    private ModelData data;

    @Getter
    @Builder
    public static class ModelData {
        private List<StoreReservation> storeReservationList;

        @Getter
        @Builder
        public static class StoreReservation {
            private Long userId;
            private UUID storeReservationId;
            private String visitStatus;
            private TimeSlot timeSlot;

            @Getter
            @Builder
            public static class TimeSlot {
                private Store store;
                private LocalDate date;
                private LocalTime entryTime;
                private LocalTime exitTime;

                @Getter
                @Builder
                public static class Store {
                    private UUID storeId;
                }
            }
        }
    }
}
