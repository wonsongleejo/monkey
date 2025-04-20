package com.monkey.userservice.infrastructure.jwt;

import com.monkey.userservice.domain.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil; //SecurityConfig에 필터 설정 시 필요

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 회원가입, 로그인, API 문서 요청은 필터링 대상에서 제외
        String path = request.getRequestURI();
        if (path.equals("/v1/auth/sign-up") || path.equals("/v1/auth/sign-in") || path.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Gateway에서 전달한 사용자 인증 정보 헤더에서 추출
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-User-Name");
        String role = request.getHeader("X-User-Role");

        // 인증 정보 누락 시 401 응답 처리
        if (userId == null || username == null || role == null) {
            log.warn("인증 헤더 누락 - userId: {}, username: {}, role: {}", userId, username, role);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 정보가 누락되었습니다.");
            return;
        }

        UserEntity userEntity = UserEntity.builder()
                .userId(Long.valueOf(userId))
                .username(username)
                .role(UserEntity.Role.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = CustomUserDetails.of(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        //다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}