package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResRefreshTokenPostDTOApiV1 {

    private String username;
    private String refreshToken;
    private LocalDateTime expiration;

    public static ResRefreshTokenPostDTOApiV1 of(String username, String refreshToken, LocalDateTime expiration) {
        return ResRefreshTokenPostDTOApiV1.builder()
                .username(username)
                .refreshToken(refreshToken)
                .expiration(expiration)
                .build();
    }
}
