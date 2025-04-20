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

            // Authorization 헤더 존재 여부 확인
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "인증 헤더가 없습니다", HttpStatus.UNAUTHORIZED);
            }

            // JWT 토큰 추출
            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("Authorization Header: {}", authorizationHeader);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return onError(exchange, "인증 헤더 형식이 잘못되었습니다", HttpStatus.UNAUTHORIZED);
            }

            String jwt = authorizationHeader.substring(7);
            log.info("JWT: {}", jwt);

            Claims claims;

            try {
                claims = validateAndExtractClaims(jwt);
            } catch (Exception e) {
                log.error("JWT 파싱 에러: {}", e.getMessage());
                return onError(exchange, "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(claims.get("userId")))
                    .header("X-User-Name", claims.get("username", String.class))
                    .header("X-User-Role", claims.get("role", String.class))
                    .build();

            log.info("mutatedRequest: {}", claims.get("role", String.class));

            // JWT 검증 및 claims 추출
            /*Claims claims = validateAndExtractClaims(jwt);
            if (claims == null) {
                return onError(exchange, "유효하지 않은 JWT 토큰입니다", HttpStatus.UNAUTHORIZED);
            }

            // 사용자 정보를 요청 헤더에 추가
            Integer id = claims.get("userId", Integer.class);
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);

            // 사용자 정보가 포함된 요청으로 변경
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(id))
                    .header("X-User-Name", username)
                    .header("X-User-Role", role)
                    .build();*/

            // 변경된 요청으로 새 exchange 생성
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            // 변경된 exchange로 필터 체인 계속 진행
            return chain.filter(mutatedExchange);
        };
    }

    private Claims validateAndExtractClaims(String jwt) {
        try {
//            byte[] secretKeyBytes = Base64.getEncoder().encode(jwtSecret.getBytes());
            //Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            //SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

            byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
            SecretKey key = Keys.hmacShaKeyFor(decodedKey);

            // JWT 파싱 및 검증
            /*JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();

            // claims 추출 및 반환
            return parser.parseClaimsJws(jwt).getBody();*/

            /*return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();*/

            //return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getBody();
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

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        byte[] bytes = errorMessage.getBytes();
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {}
}
