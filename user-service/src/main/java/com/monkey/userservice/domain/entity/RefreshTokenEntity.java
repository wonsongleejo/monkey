package com.monkey.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="p_refreshToken")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    private String username;
    private String refreshToken;
    private LocalDateTime expiration;

    public void updateRefreshToken(String refreshToken, LocalDateTime expiration) {
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}