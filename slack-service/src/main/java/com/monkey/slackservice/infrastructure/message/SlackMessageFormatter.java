package com.monkey.slackservice.infrastructure.message;

import org.springframework.stereotype.Component;

@Component
public class SlackMessageFormatter {

    public String format(String status) {
        return switch (status) {
            case "SCHEDULED" -> "예약되었습니다.";
            case "CANCELED" -> "취소되었습니다.";
            case "NO_SHOW" -> "방문하지 않았습니다.";
            case "VISITED" -> "방문하셨습니다.";
            default -> "상태를 알 수 없습니다.";
        };
    }
}