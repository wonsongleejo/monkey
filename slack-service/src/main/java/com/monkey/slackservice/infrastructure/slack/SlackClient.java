package com.monkey.slackservice.infrastructure.slack;

import com.monkey.slackservice.infrastructure.config.SlackProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SlackClient {

    private final WebClient slackWebClient;

    public SlackClient(SlackProperties slackProperties) {
        this.slackWebClient = WebClient.builder()
                .baseUrl("https://slack.com/api")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + slackProperties.getBotToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void sendSlackMessage(String slackUserId, String message) {
        slackWebClient.post()
                .uri("/chat.postMessage")
                .bodyValue(Map.of(
                        "channel", slackUserId,
                        "text", message
                ))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
}