package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationPostByIdCancelDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationPostByIdCancelDTOApiV1 of() {
        return ResStoreReservationPostByIdCancelDTOApiV1.builder()
                .storeReservation(StoreReservation.from())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreReservation {

        private UUID storeReservationId;
        private StoreReservationStatus status;

        public static StoreReservation from() {
            return StoreReservation.builder()
                    .storeReservationId(UUID.randomUUID())
                    .status(StoreReservationStatus.SCHEDULED)
                    .build();
        }
    }
}