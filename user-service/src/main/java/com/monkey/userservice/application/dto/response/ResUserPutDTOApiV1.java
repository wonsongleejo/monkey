package com.monkey.userservice.application.dto.response;

import com.monkey.userservice.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResUserPutDTOApiV1 {

    private User user;

    public static ResUserPutDTOApiV1 of(UserEntity userEntity) {
        return ResUserPutDTOApiV1.builder()
                .user(User.from(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User {

        private String password;
        private String slackId;

        public static User from(UserEntity userEntity) {
            return User.builder()
                    .password(userEntity.getPassword())
                    .slackId(userEntity.getSlackId())
                    .build();
        }
    }
}
