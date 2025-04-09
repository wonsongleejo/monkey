package com.monkey.userservice.application.dto.response;

import com.monkey.userservice.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResUserGetDTOApiV1 {

    private List<User> userList;

    public static ResUserGetDTOApiV1 of(List<UserEntity> userEntityList) {
        return ResUserGetDTOApiV1.builder()
                .userList(User.from(userEntityList))
                .build();
    }

    @Getter
    @Builder
    public static class User {

        private Long userId;
        private String username;
        private String slackId;
        private UserEntity.Role role;

        public static User from(UserEntity userEntity) {
            return User.builder()
                    .userId(userEntity.getUserId())
                    .username(userEntity.getUsername())
                    .slackId(userEntity.getSlackId())
                    .role(userEntity.getRole())
                    .build();
        }

        public static List<User> from(List<UserEntity> userEntityList) {
            return userEntityList
                    .stream()
                    .map(userEntity -> User.from(userEntity))
                    .collect(Collectors.toList());
        }
    }
}
