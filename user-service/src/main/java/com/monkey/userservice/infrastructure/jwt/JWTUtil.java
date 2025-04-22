package com.monkey.userservice.infrastructure.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey secretKey;
    private final Long accessTokenExpiration; // 1시간
    private final Long refreshTokenExpiration; // 1일

    public JWTUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") Long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") Long refreshTokenExpiration
    ) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey); // 안전하게 Key 생성
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    //Access Token 생성
    public String createAccessToken(Long userId, String username, String role) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .claim("type", "ACCESS")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    //Refresh Token 생성
    public String createRefreshToken(Long userId, String username, String role) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    //Refresh Token 유효성 검사
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(refreshToken) // Bearer prefix 제거
                    .getPayload();

            return true;

        } catch (ExpiredJwtException e) {
            log.error("Refresh token expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    // Bearer 삭제
    private String removeBearerPrefix(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return token;
    }

    // JWTUtil 클래스에 메서드 추가
    public LocalDateTime getRefreshTokenExpirationTime() {
        return LocalDateTime.now().plus(Duration.ofMillis(refreshTokenExpiration));
    }

    //토큰 검증을 진행할 메소드들
    public Long getUserId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(removeBearerPrefix(token)).getPayload().get("userId", Long.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(removeBearerPrefix(token)).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(removeBearerPrefix(token)).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(removeBearerPrefix(token)).getPayload().getExpiration().before(new Date());
    }
}
