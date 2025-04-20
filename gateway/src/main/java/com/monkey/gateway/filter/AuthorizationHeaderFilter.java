package com.monkey.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    // JWT 시크릿 키 값
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain)-> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 특정 경로에 대한 인증 우회
            if (path.equals("/v1/auth/sign-up") || path.equals("/v1/auth/sign-in")
                    || path.contains("/v3/api-docs")) {
                return chain.filter(exchange);
            }

            // Authorization 헤더가 존재하는지 확인
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "인증 헤더가 없습니다", HttpStatus.UNAUTHORIZED);
            }

            // Authorization 헤더에서 토큰 추출
            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return onError(exchange, "인증 헤더 형식이 잘못되었습니다", HttpStatus.UNAUTHORIZED);
            }

            // "Bearer " 제거 후 JWT만 추출
            String jwt = authorizationHeader.substring(7);

            Claims claims;

            try {
                // JWT 검증 및 클레임 추출
                claims = validateAndExtractClaims(jwt);
            } catch (Exception e) {
                log.error("JWT 파싱 에러: {}", e.getMessage());
                return onError(exchange, "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED);
            }

            // JWT의 클레임에서 사용자 정보를 꺼내서 새로운 요청 헤더에 담음
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(claims.get("userId")))
                    .header("X-User-Name", claims.get("username", String.class))
                    .header("X-User-Role", claims.get("role", String.class))
                    .build();

            // 변경된 요청으로 새 exchange 생성
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            // 변경된 exchange로 필터 체인 계속 진행
            return chain.filter(mutatedExchange);
        };
    }

    // JWT를 검증하고 클레임을 반환하는 메서드
    private Claims validateAndExtractClaims(String jwt) {
        try {

            byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
            SecretKey key = Keys.hmacShaKeyFor(decodedKey);

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

        } catch (Exception e) {
            log.error("JWT 검증 오류: {}", e.getMessage());
            return null;
        }
    }

    // 인증 실패 시 에러 메시지를 응답으로 반환하는 메서드
    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        byte[] bytes = errorMessage.getBytes();
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    // 필터 설정을 위한 내부 클래스
    public static class Config {}
}
