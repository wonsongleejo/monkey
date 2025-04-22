package com.monkey.productreservationservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResStoreReservationClientGetDTOApiV1 {
    private List<StoreReservation> storeReservationList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreReservation {
        private User user;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class User {
            private long userId;
        }
    }
}
