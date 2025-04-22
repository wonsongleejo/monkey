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
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        private StoreTimeSlot storeTimeSlot;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreTimeSlot {
        private UUID timeSlotId;
        private UUID storeId;
        private LocalDate slotDate;
        private LocalTime entryTime;
        private LocalTime exitTime;
        private Integer maxPerson;
    }
}