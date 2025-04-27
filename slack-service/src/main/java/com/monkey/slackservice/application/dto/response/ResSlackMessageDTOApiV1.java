package com.monkey.slackservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ResSlackMessageDTOApiV1 {
    private UUID slackMessageId;
    private String slackMessage;
}