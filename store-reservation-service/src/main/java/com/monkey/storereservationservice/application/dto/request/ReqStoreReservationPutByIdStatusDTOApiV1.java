package com.monkey.storereservationservice.application.dto.request;

import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReqStoreReservationPutByIdStatusDTOApiV1 {
    @NotNull
    private StoreReservationStatus storeReservationStatus;
}