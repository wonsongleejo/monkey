package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationGetByIdDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationGetByIdDTOApiV1 of() {
        return ResStoreReservationGetByIdDTOApiV1.builder()
                .storeReservation(StoreReservation.from())
                .build();
    }

    @Getter
    @Builder
    public static class StoreReservation {
        private UUID storeReservationId;
        private StoreReservationStatus status;
        private TimeSlot timeSlot;
        private User user;

        public static StoreReservation from() {
            return StoreReservation.builder()
                    .storeReservationId(UUID.randomUUID())
                    .status(StoreReservationStatus.SCHEDULED)
                    .timeSlot(TimeSlot.from())
                    .user(User.from())
                    .build();
        }

        @Getter
        @Builder
        public static class TimeSlot {
            private Store store;
            private LocalDate date;
            private LocalTime entryTime;
            private LocalTime exitTime;

            public static TimeSlot from() {
                return TimeSlot.builder()
                        .store(Store.from())
                        .date(LocalDate.parse("2025-04-19"))
                        .entryTime(LocalTime.parse("10:00"))
                        .exitTime(LocalTime.parse("11:00"))
                        .build();
            }

            @Getter
            @Builder
            public static class Store {
                private UUID storeId;

                public static Store from() {
                    return Store.builder()
                            .storeId(UUID.randomUUID())
                            .build();
                }
            }
        }

        @Getter
        @Builder
        public static class User {
            private Long userId;

            public static User from() {
                return User.builder()
                        .userId(1L)
                        .build();
            }
        }
    }
}