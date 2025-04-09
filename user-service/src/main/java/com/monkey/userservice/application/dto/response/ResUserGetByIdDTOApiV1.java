package com.monkey.userservice.application.dto.response;

import com.monkey.userservice.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResUserGetByIdDTOApiV1 {

    private User user;

    //userEntity -> dto
    public static ResUserGetByIdDTOApiV1 of(UserEntity userEntity) {
        return ResUserGetByIdDTOApiV1.builder()
                .user(User.from(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User {

        private Long userId;
        private String username;
        private String slackId;
        private UserEntity.Role role;

        //User -> Dto
        public static User from(UserEntity userEntity) {
            return User.builder()
                    .userId(userEntity.getUserId())
                    .username(userEntity.getUsername())
                    .slackId(userEntity.getSlackId())
                    .role(userEntity.getRole())
                    .build();
        }
    }
}
