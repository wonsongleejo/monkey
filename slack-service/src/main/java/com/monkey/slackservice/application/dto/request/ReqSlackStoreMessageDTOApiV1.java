package com.monkey.slackservice.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqSlackStoreMessageDTOApiV1 {

    @NotNull(message = "슬랙 정보를 입력해주세요.")
    @Valid
    private Slack slack;

    @Getter
    public static class Slack {

        @NotNull(message = "슬랙 ID 입력은 필수입니다.")
        private String slackId;

        @NotNull(message = "예약 ID 입력은 필수입니다.")
        private UUID storeReservationId;
    }
}