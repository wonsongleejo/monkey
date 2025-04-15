package com.monkey.userservice.application.dto.request;

import com.monkey.userservice.domain.entity.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqUserPutDTOApiV1 {

    @NotNull(message = "유저 정보를 입력해주세요.")
    @Valid
    private User user;

    @Getter
    @Builder
    public static class User{

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "슬랙Id를 입력해주세요.")
        private String slackId;

        public void update(UserEntity userEntity) {
            userEntity.setPassword(this.password);
            userEntity.setSlackId(this.slackId);
        }
    }
}
