package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationPutByIdStatusDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationPutByIdStatusDTOApiV1 from(
            StoreReservationEntity storeReservationentity,
            Integer currentReservedPerson,
            Integer maxPerson
    ) {
        return ResStoreReservationPutByIdStatusDTOApiV1.builder()
                .storeReservation(StoreReservation.from(
                        storeReservationentity,
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
                StoreReservationEntity storeReservationentity,
                Integer currentReservedPerson,
                Integer maxPerson
        ) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationentity.getStoreReservationId())
                    .status(storeReservationentity.getStatus())
                    .currentReservedPerson(currentReservedPerson)
                    .maxPerson(maxPerson)
                    .build();
        }
    }
}
