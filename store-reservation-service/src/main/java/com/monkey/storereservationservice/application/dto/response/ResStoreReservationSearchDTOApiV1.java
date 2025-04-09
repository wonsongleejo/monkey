package com.monkey.storereservationservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResStoreReservationSearchDTOApiV1 {

    private StoreReservationPage storeReservationPage;

    @Getter
    @Builder
    public static class StoreReservationPage {
        private List<ResStoreReservationGetDTOApiV1> content;
        private PageInfo page;
    }

    @Getter
    @Builder
    public static class PageInfo {
        private int size;
        private int number;
        private long totalElements;
        private int totalPages;
    }
}
