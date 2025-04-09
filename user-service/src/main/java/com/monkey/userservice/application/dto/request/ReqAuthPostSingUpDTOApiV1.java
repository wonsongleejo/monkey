package com.monkey.userservice.application.dto.request;

import com.monkey.userservice.domain.entity.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqAuthPostSingUpDTOApiV1 {

    @Valid
    @NotNull(message = "회원 정보를 입력해주세요.")
    private User user;

    @Getter
    @Builder
    public static class User {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "슬랙Id를 입력해주세요.")
        private String slackId;

        @NotNull
        @DefaultValue("USER")
        private UserEntity.Role role;
    }
}