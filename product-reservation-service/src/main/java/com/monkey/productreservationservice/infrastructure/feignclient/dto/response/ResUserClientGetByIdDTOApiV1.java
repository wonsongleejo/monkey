package com.monkey.productreservationservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResUserClientGetByIdDTOApiV1 {
    private User user;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {
        private Long userId;
        private String username;
    }
}
