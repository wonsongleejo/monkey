package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.vo.StoreReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationGetByIdDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationGetByIdDTOApiV1 from(
            StoreReservationEntity storeReservationEntity,
            Integer currentReservedPerson,
            Integer maxPerson
    ) {
        return ResStoreReservationGetByIdDTOApiV1.builder()
                .storeReservation(StoreReservation.from(
                        storeReservationEntity,
                        currentReservedPerson,
                        maxPerson
                ))
                .build();
    }

    @Getter
    @Builder
    public static class StoreReservation {
        private UUID storeReservationId;
        private StoreReservationStatus status;
        private TimeSlot timeSlot;
        private User user;
        private Integer currentReservedPerson;
        private Integer maxPerson;

        public static StoreReservation from(
                StoreReservationEntity storeReservationEntity,
                Integer currentReservedPerson
                , Integer maxPerson
        ) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationEntity.getStoreReservationId())
                    .status(storeReservationEntity.getStatus())
                    .timeSlot(null)
                    .user(null)
                    .currentReservedPerson(currentReservedPerson)
                    .maxPerson(maxPerson)
                    .build();
        }

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

        @Getter
        @Builder
        public static class User {
            private Long userId;
            private String userName;
        }
    }
}