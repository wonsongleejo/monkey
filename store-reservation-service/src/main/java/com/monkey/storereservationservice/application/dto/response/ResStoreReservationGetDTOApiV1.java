package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResStoreReservationGetDTOApiV1 {

    private List<StoreReservation> storeReservationList;

    public static ResStoreReservationGetDTOApiV1 from(
            List<StoreReservationEntity> storeReservationEntityList,
            Map<UUID, Integer> reservedPersonMap,
            Map<UUID, Integer> maxPersonMap
    ) {
        List<StoreReservation> storeReservationList = storeReservationEntityList.stream()
                .map(entity -> StoreReservation.from(
                        entity,
                        reservedPersonMap.getOrDefault(entity.getTimeSlotId(), 0),
                        maxPersonMap.getOrDefault(entity.getTimeSlotId(), 0)
                ))
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
        private Integer currentReservedPerson;
        private Integer maxPerson;

        public static StoreReservation from(
                StoreReservationEntity storeReservationEntity,
                Integer currentReservedPerson,
                Integer maxPerson
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