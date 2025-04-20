package com.monkey.storereservationservice.infrastructure.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqSlackStoreReservationPostDTOApiV1 {

    private SlackMessage slack;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SlackMessage {
        private String slackId;
        private String slackMessage;
    }
}