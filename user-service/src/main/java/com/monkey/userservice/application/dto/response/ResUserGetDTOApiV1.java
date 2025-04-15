package com.monkey.userservice.application.dto.response;

import com.monkey.userservice.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResUserGetDTOApiV1 {

    private UserPage userPage;


    public static ResUserGetDTOApiV1 of(Page<UserEntity> userPage) {
        return ResUserGetDTOApiV1.builder()
                .userPage(new UserPage(userPage))
                .build();
    }

    @Getter
    @ToString
    public static class UserPage extends PagedModel<UserPage.User> {

        public UserPage(Page<UserEntity> userPage){
            super(
                    new PageImpl<>(
                            UserPage.User.from(userPage.getContent()),
                            userPage.getPageable(),
                            userPage.getTotalElements()
                    )
            );
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
}
