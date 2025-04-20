package com.monkey.userservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqAuthPostRefreshDTOApiV1 {

    @NotBlank(message = "리프레시 토큰이 없습니다.")
    private String refreshJwt;
}
