package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthPostRefreshDTOApiV1 {
    private String accessJwt;
    private String refreshJwt;

    public static ResAuthPostRefreshDTOApiV1 of(String accessJwt, String refreshJwt) {
        return ResAuthPostRefreshDTOApiV1.builder()
                .accessJwt(accessJwt)
                .refreshJwt(refreshJwt)
                .build();
    }
}
