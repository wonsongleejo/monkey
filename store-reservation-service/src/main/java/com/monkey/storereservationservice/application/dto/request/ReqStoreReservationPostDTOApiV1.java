package com.monkey.storereservationservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqStoreReservationPostDTOApiV1 {

    @NotNull(message = "예약 시간대 입력은 필수입니다.")
    private UUID timeSlotId;

    @NotNull(message = "예약 인원 입력은 필수입니다.")
    private Integer person;
}
