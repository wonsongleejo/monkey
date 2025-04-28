package com.monkey.storereservationservice.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqStoreReservationPostDTOApiV1 {

    @NotNull(message = "예약 정보를 입력해주세요.")
    @Valid
    private StoreReservation storeReservation;

    @Getter
    @Builder
    public static class StoreReservation {
        @NotNull(message = "예약 시간대 입력은 필수입니다.")
        private UUID timeSlotId;

        @NotNull(message = "예약 인원 입력은 필수입니다.")
        @jakarta.validation.constraints.Min(value = 1, message = "예약은 최소 1명부터 가능합니다.")
        @jakarta.validation.constraints.Max(value = 4, message = "예약은 최대 4명까지 가능합니다.")
        private Integer personCount;
    }
}