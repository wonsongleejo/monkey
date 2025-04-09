package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationGetDTOApiV1 {

    private Long userId;
    private UUID storeReservationId;
    private StoreReservationStatus status;
    private TimeSlot timeSlot;

    @Getter
    @Builder
    public static class TimeSlot {
        private Store store;
        private String date;
        private String startTime;
        private String endTime;
    }

    @Getter
    @Builder
    public static class Store {
        private UUID storeId;
    }
}