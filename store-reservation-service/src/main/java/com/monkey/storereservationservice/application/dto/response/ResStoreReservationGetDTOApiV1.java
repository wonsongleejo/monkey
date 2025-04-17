package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResStoreReservationGetDTOApiV1 {

    private List<StoreReservation> storeReservationList;

    public static ResStoreReservationGetDTOApiV1 from(List<StoreReservationEntity> storeReservationEntityList) {
        List<StoreReservation> storeReservationList = storeReservationEntityList.stream()
                .map(StoreReservation::from)
                .collect(Collectors.toList());

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(storeReservationList)
                .build();
    }

    @Getter
    @Builder
    public static class StoreReservation {
        private UUID storeReservationId;
        private StoreReservationStatus status;
        private TimeSlot timeSlot;
        private User user;

        public static StoreReservation from(StoreReservationEntity storeReservationEntity) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationEntity.getStoreReservationId())
                    .status(storeReservationEntity.getStatus())
                    .timeSlot(null)
                    .user(null)
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