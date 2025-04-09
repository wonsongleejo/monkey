package com.monkey.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthPostSignInDTOApiV1 {

    private String accessJwt;
    private String refreshJwt;

    public static ResAuthPostSignInDTOApiV1 of(String accessJwt, String refreshJwt) {
        return ResAuthPostSignInDTOApiV1.builder()
                .accessJwt(accessJwt)
                .refreshJwt(refreshJwt)
                .build();
    }
}