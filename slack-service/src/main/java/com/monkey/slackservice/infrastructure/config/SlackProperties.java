package com.monkey.slackservice.infrastructure.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    private final String botToken;

    public SlackProperties(String botToken) {
        this.botToken = botToken;
    }

    public String getBotToken() {
        return botToken;
    }
}