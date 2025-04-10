package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationGetDTOApiV1 {

    private List<StoreReservation> storeReservationList;

    public static ResStoreReservationGetDTOApiV1 of() {
        List<UUID> tmpList = List.of(UUID.randomUUID(), UUID.randomUUID());

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(StoreReservation.from(tmpList))
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

        public static List<StoreReservation> from(List<UUID> tmpList) {
//            List<UUID> tmpList = List.of(UUID.randomUUID(), UUID.randomUUID());

            return tmpList.stream()
                    .map(id -> StoreReservation.from())
                    .toList();
        }

        @Getter
        @Builder
        public static class TimeSlot {
            private Store store;
            private String date;
            private String startTime;
            private String endTime;

            public static TimeSlot from() {
                return TimeSlot.builder()
                        .store(Store.from())
                        .date("0")
                        .startTime("0")
                        .endTime("0")
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