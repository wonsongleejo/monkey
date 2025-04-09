package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResStoreReservationPostDTOApiV1 {

    private UUID storeReservationId;
    private StoreReservationStatus status;
}
