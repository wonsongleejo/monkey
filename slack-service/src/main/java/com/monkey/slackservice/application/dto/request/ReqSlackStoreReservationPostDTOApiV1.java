package com.monkey.slackservice.application.dto.request;

import com.monkey.slackservice.domain.slack.vo.SlackMessageType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqSlackStoreReservationPostDTOApiV1 {

    @NotNull(message = "슬랙 메세지를 입력해주세요.")
    @Valid
    private SlackMessage slack;

    @Getter
    @Builder
    public static class SlackMessage {
        @NotNull(message = "Slack ID 입력은 필수입니다.")
        private String slackId;

        @NotNull(message = "Slack 메세지 입력은 필수입니다.")
        private String slackMessage;

        @NotNull(message = "Slack 상태 값은 필수입니다.")
        private SlackMessageType slackMessageType;
    }
}