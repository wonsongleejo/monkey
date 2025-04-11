package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationGetDTOApiV1 {

    private List<StoreReservation> storeReservationList;

    public static ResStoreReservationGetDTOApiV1 of(
            List<ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation> storeReservationList
    ){
        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(StoreReservation.from(storeReservationList))
                .build();
    }

    @Getter
    @Builder
    public static class StoreReservation {
        private Long userId;
        private UUID storeReservationId;
        private String visitStatus;
        private TimeSlot timeSlot;

        public static StoreReservation from(
                ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation storeReservation
        ) {
            return StoreReservation.builder()
                    .userId(storeReservation.getUserId())
                    .storeReservationId(storeReservation.getStoreReservationId())
                    .visitStatus(storeReservation.getVisitStatus())
                    .timeSlot(TimeSlot.from(storeReservation.getTimeSlot()))
                    .build();
        }

        public static List<StoreReservation> from(
                List<ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation> storeReservationList
        ){
            return storeReservationList.stream()
                    .map(StoreReservation::from)
                    .toList();
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

            public static TimeSlot from(
                    ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.TimeSlot timeSlot
            ){
                Store store = Store.builder()
                        .storeId(UUID.randomUUID())
                        .build();

                return TimeSlot.builder()
                        .store(store)
                        .date(timeSlot.getDate())
                        .entryTime(timeSlot.getEntryTime())
                        .exitTime(timeSlot.getExitTime())
                        .build();
            }
        }
    }
}
