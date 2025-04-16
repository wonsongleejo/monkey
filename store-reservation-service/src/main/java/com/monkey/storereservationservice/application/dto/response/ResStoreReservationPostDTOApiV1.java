package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationPostDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationPostDTOApiV1 of() {
        return ResStoreReservationPostDTOApiV1.builder()
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