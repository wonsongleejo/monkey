package com.monkey.storereservationservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResStoreTimeSlotDTOApiV1 {
    private UUID timeSlotId;
    private StoreInfo store;
    private LocalDate date;
    private LocalTime entryTime;
    private LocalTime exitTime;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreInfo {
        private UUID storeId;
        private String storeName;
    }
}