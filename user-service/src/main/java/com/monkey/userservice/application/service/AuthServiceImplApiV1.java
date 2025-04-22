package com.monkey.userservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.userservice.application.dto.request.ReqAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.request.ReqAuthPostSingUpDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostRefreshDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignInDTOApiV1;
import com.monkey.userservice.domain.entity.RefreshTokenEntity;
import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.RefreshTokenRepository;
import com.monkey.userservice.domain.repository.UserRepository;
import com.monkey.userservice.infrastructure.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImplApiV1 implements AuthServiceApiV1 {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    // 회원 가입
    @Override
    @Transactional
    public UserEntity signUp(ReqAuthPostSingUpDTOApiV1 reqDto) {

        String username = reqDto.getUser().getUsername();
        String password = reqDto.getUser().getPassword();

        // 아이디 중복 체크
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isPresent()) {
            throw new CustomException(ResponseCode.USER_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(password);

        // 사용자 생성
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(encryptedPassword)
                .slackId(reqDto.getUser().getSlackId())
                .role(reqDto.getUser().getRole())
                .build();
        userRepository.save(userEntity);

        return userEntity;
    }

    // 로그인 및 JWT 발급
    @Override
    @Transactional
    public ResAuthPostSignInDTOApiV1 signIn(ReqAuthPostSignInDTOApiV1 reqDto) {
        // 존재하는 회원인지 확인
        UserEntity userEntity = userRepository.findByUsername(reqDto.getUser().getUsername())
                .orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

        // 탈퇴한 회원 체크
        if(userEntity.isDeleted()){
            throw new CustomException(ResponseCode.USER_NOT_FOUND);
        }

        // 비밀번호가 일치하는지 확인
        if(!passwordEncoder.matches(reqDto.getUser().getPassword(), userEntity.getPassword())) {
            throw new CustomException(ResponseCode.PASSWORD_MISMATCH);
        }

        // accessToken 발급
        String accessToken = jwtUtil.createAccessToken(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getRole().name()
        );

        // refreshToken 발급
        String refreshToken = jwtUtil.createRefreshToken(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getRole().name()
        );

        // refresh 토큰 저장
        RefreshTokenEntity refreshEntity = RefreshTokenEntity.builder()
                .username(userEntity.getUsername())
                .refreshToken(refreshToken)
                .expiration(jwtUtil.getRefreshTokenExpirationTime())
                .build();

        if (refreshTokenRepository.existRefreshTokenByUsername(userEntity.getUsername())) {
            RefreshTokenEntity userRefreshToken = refreshTokenRepository
                    .findByUsername(userEntity.getUsername())
                    .orElse(null);

            userRefreshToken.updateRefreshToken(refreshToken, jwtUtil.getRefreshTokenExpirationTime());

        } else {
            refreshTokenRepository.save(refreshEntity);
        }

        return ResAuthPostSignInDTOApiV1.of(accessToken, refreshToken);
    }

    // refreshToken을 통한 accessToken 재발급
    @Override
    @Transactional
    public ResAuthPostRefreshDTOApiV1 refreshBy(String username) {

        String token = refreshTokenRepository.findRefreshTokenByUsername(username);
        //String token = reqDto.getRefreshJwt();

        // refresh 토큰 유효성 확인
        if(!jwtUtil.validateRefreshToken(token)){
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        // 토큰에서 정보 추출
        Long findUserId = jwtUtil.getUserId(token);
        String findUsername = jwtUtil.getUsername(token);
        String findRole = jwtUtil.getRole(token);

        // refresh 존재 유무 확인
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByUsername(findUsername);
        RefreshTokenEntity refreshToken = refreshTokenEntity.get();

        if (!refreshToken.getRefreshToken().equals(token)) {
            throw new CustomException(ResponseCode.TOKEN_MISMATCH);
        }

        // 새로운 access/refresh 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(findUserId, findUsername, findRole);
        String newRefreshToken = jwtUtil.createRefreshToken(findUserId, findUsername, findRole);

        // refresh 토큰 갱신
        refreshToken.updateRefreshToken(newRefreshToken, jwtUtil.getRefreshTokenExpirationTime());
        refreshTokenRepository.save(refreshToken);

        return ResAuthPostRefreshDTOApiV1.builder()
                .accessJwt(newAccessToken)
                .refreshJwt(newRefreshToken)
                .build();
    }
}
