package com.monkey.userservice.infrastructure.jwt;

import com.monkey.userservice.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    //데이터를 받는 필드
    private final User user;

    public static CustomUserDetails of(UserEntity userEntity) {
        return CustomUserDetails.builder()
                .user(User.fromEntity(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User {
        private Long userId;
        private String username;
        private String password;
        private String slackId;
        private UserEntity.Role role;

        public static User fromEntity(UserEntity userEntity) {
            return User.builder()
                    .userId(userEntity.getUserId())
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .slackId(userEntity.getSlackId())
                    .role(userEntity.getRole())
                    .build();
        }
    }

    // authority 값 반환
    // USER 권한을 GrantedAuthority 컬렉션으로 변환하여 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return user.getRole().getValue();
            }
        });

        return collection;
    }

    //USER ID 반환
    public Long getUserId() {
        return user.getUserId();
    }

    //PW 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //ID 반환
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
