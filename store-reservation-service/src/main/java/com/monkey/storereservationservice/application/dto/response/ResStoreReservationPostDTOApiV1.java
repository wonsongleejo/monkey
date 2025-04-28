package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationPostDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationPostDTOApiV1 from(
            StoreReservationEntity storeReservationEntity,
            Integer currentReservedPerson,
            Integer maxPerson
    ) {
        return ResStoreReservationPostDTOApiV1.builder()
                .storeReservation(StoreReservation.from(
                        storeReservationEntity,
                        currentReservedPerson,
                        maxPerson
                ))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreReservation {

        private UUID storeReservationId;
        private StoreReservationStatus status;

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
                    .currentReservedPerson(currentReservedPerson)
                    .maxPerson(maxPerson)
                    .build();
        }
    }
}