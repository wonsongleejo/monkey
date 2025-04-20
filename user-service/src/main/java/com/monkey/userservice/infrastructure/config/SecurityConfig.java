package com.monkey.userservice.infrastructure.config;

import com.monkey.userservice.infrastructure.jwt.JWTFilter;
import com.monkey.userservice.infrastructure.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity //Security를 위한 config
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    //AuthenticationManager는 인증 처리를 담당
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //보안 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //CORS 설정
        http
                .cors(((cors) -> cors.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); //프론트 포트 허용
                        configuration.setAllowedMethods(Collections.singletonList("*")); //모든 HTTP 메서드 허용
                        configuration.setAllowCredentials(true); // 쿠키/인증 정보 허용
                        configuration.setAllowedHeaders(Collections.singletonList("*")); //모든 헤더 허용
                        configuration.setExposedHeaders(Collections.singletonList("Authorization")); //응답 헤더에 Authorization 포함
                        configuration.setMaxAge(3600L); //허용 시간

                        return configuration;
                    }
                })));

        //기본 인증, CSRF, 로그인 폼 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
        ;

        //경로별 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET,"/v1/users").hasAuthority("MASTER")
                        .requestMatchers(HttpMethod.GET,"/v1/users/master/{userId}").hasAuthority("MASTER")
                        .requestMatchers(HttpMethod.GET,"/v1/users/details").hasAnyAuthority("USER","MASTER")
                        .requestMatchers(HttpMethod.GET,"/v1/users/{userId}/store-reservations").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET,"/v1/users/{userId}/product-reservations").hasAuthority("USER")
                        .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/v1/auth/**",
                        "/v1/users/**",
                        "/v1/products/**",
                        "/v1/product-reservations/**",
                        "/v1/slacks/**",
                        "/v1/store-reservations/**",
                        "/v1/stores/**"
                        ).permitAll()
                        .requestMatchers("/admin").hasRole("MASTER")
                        .anyRequest().authenticated()
                );

        //JWT 인증 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //서버에서 세션 생성 X
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}