package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ResStoreReservationPutByIdStatusDTOApiV1 {

    private StoreReservation storeReservation;

    public static ResStoreReservationPutByIdStatusDTOApiV1 from(StoreReservationEntity storeReservationentity) {
        return ResStoreReservationPutByIdStatusDTOApiV1.builder()
                .storeReservation(StoreReservation.from(storeReservationentity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreReservation {

        private UUID storeReservationId;
        private StoreReservationStatus status;

        public static StoreReservation from(StoreReservationEntity storeReservationentity) {
            return StoreReservation.builder()
                    .storeReservationId(storeReservationentity.getStoreReservationId())
                    .status(storeReservationentity.getStatus())
                    .build();
        }
    }
}
