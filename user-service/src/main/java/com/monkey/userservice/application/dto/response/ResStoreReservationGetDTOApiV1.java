package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationGetDTOApiV1 {

    private StoreReservationPage storeReservationPage;

    public static ResStoreReservationGetDTOApiV1 of(
            Page<ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation> storeReservationList
    ){
        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationPage(new StoreReservationPage((storeReservationList)))
                .build();
    }

    @Getter
    @ToString
    public static class StoreReservationPage extends PagedModel<StoreReservationPage.StoreReservation> {

        public StoreReservationPage(
                Page<ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation> storeReservationPage
        ) {
            super(
                    new PageImpl<>(
                            StoreReservationPage.StoreReservation.from(storeReservationPage.getContent()),
                            storeReservationPage.getPageable(),
                            storeReservationPage.getTotalElements()
                    )
            );
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
                        .userId(storeReservation.getUser().getUserId())
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
}