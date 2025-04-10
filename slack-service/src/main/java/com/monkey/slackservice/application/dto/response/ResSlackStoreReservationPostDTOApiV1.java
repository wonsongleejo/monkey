package com.monkey.slackservice.application.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResSlackStoreReservationPostDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResSlackStoreReservationPostDTOApiV1 of(UUID storeReservationId, String status) {
        return ResSlackStoreReservationPostDTOApiV1.builder()
                .storeReservation(StoreReservation.from(storeReservationId, status))
                .build();
    }

    @Getter
    @Builder
    public static class StoreReservation {
        private UUID storeReservationId;
        private String status;

        public static StoreReservation from(UUID storeReservationId, String status) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationId)
                    .status(status)
                    .build();
        }
    }
}