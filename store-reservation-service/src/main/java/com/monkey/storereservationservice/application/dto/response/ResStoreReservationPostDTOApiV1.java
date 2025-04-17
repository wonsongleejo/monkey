package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationPostDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationPostDTOApiV1 from(StoreReservationEntity storeReservationEntity) {
        return ResStoreReservationPostDTOApiV1.builder()
                .storeReservation(StoreReservation.from(storeReservationEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreReservation {

        private UUID storeReservationId;
        private StoreReservationStatus status;

        public static StoreReservation from(StoreReservationEntity storeReservationEntity) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationEntity.getStoreReservationId())
                    .status(storeReservationEntity.getStatus())
                    .build();
        }
    }
}