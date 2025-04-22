package com.monkey.storereservationservice.application.dto.request;

import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqStoreReservationPutByIdStatusDTOApiV1 {

    @NotNull(message = "예약 상태 정보는 필수입니다.")
    @Valid
    private StoreReservation storeReservation;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreReservation {
        @NotNull(message = "예약 상태는 필수입니다.")
        private StoreReservationStatus status;
    }
}