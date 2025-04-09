package com.monkey.userservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqAuthPostSignInDTOApiV1 {

    @NotNull(message = "유저 정보를 입력해주세요.")
    private User user;

    @Getter
    @Builder
    public static class User{
        @NotBlank(message = "아이디를 입력해주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }
}