package com.monkey.storereservationservice.application.dto.response;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResStoreReservationCancelDTOApiV1 {

    private UUID storeReservationId;
    private UUID storeId;
    private UUID timeSlotId;
    private Integer person;
    private StoreReservationStatus status;
}