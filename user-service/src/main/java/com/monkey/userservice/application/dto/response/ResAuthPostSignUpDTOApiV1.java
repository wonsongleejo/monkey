package com.monkey.userservice.application.dto.response;

import com.monkey.userservice.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAuthPostSignUpDTOApiV1 {

    private User user;

    public static ResAuthPostSignUpDTOApiV1 of(UserEntity userEntity) {
        return ResAuthPostSignUpDTOApiV1.builder()
                .user(User.from(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User{
        private String username;

        public static User from(UserEntity userEntity) {
            return User.builder()
                    .username(userEntity.getUsername())
                    .build();
        }
    }
}
