package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

public class ResStoreReservationPostDTOApiV1 {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreReservation {

        private UUID storeReservationId;
        private StoreReservationStatus status;

        public static StoreReservation of(UUID storeReservationId, StoreReservationStatus status) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationId)
                    .status(status)
                    .build();
        }
    }
}