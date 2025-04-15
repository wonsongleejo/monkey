package com.monkey.userservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.request.ReqAuthPostSingUpDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignUpDTOApiV1;
import com.monkey.userservice.domain.entity.RefreshTokenEntity;
import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.RefreshTokenRepository;
import com.monkey.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthControllerApiV1 {
    //private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    @PostMapping("/sign-up")
    public ResDTO<ResAuthPostSignUpDTOApiV1> signUp(@RequestBody ReqAuthPostSingUpDTOApiV1 request) {

        //UserEntity userEntity = userService.saveUser(request);

        UserEntity userEntity = UserEntity.builder()
                .username(request.getUser().getUsername())
                .password(request.getUser().getPassword())
                .slackId(request.getUser().getSlackId())
                .role(request.getUser().getRole())
                .build();

        userRepository.save(userEntity);

        return ResDTO.success(ResAuthPostSignUpDTOApiV1.of(userEntity));
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResDTO<ResAuthPostSignInDTOApiV1> signIn(@RequestBody ReqAuthPostSignInDTOApiV1 request) {

        String accessJwt = "access token";
        String refreshJwt = "refresh token";

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .username(request.getUser().getUsername())
                .refresh(refreshJwt)
                .expiration(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshToken);

        return ResDTO.success(ResAuthPostSignInDTOApiV1.of(accessJwt, refreshJwt));
    }
}
