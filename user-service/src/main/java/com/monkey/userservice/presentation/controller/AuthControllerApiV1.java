package com.monkey.userservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.request.ReqAuthPostSingUpDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignUpDTOApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthControllerApiV1 {

    // 회원가입
    @PostMapping("/sign-up")
    public ResDTO<ResAuthPostSignUpDTOApiV1> signUp(@RequestBody ReqAuthPostSingUpDTOApiV1 request) {

        UserEntity userEntity = UserEntity.builder()
                .username(request.getUser().getUsername())
                .password(request.getUser().getPassword())
                .slackId(request.getUser().getSlackId())
                .role(request.getUser().getRole())
                .build();

        return ResDTO.success(ResAuthPostSignUpDTOApiV1.of(userEntity));
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResDTO<ResAuthPostSignInDTOApiV1> signIn(@RequestBody ReqAuthPostSignInDTOApiV1 request) {

        String accessJwt = "access token";
        String refreshJwt = "refresh token";

        return ResDTO.success(ResAuthPostSignInDTOApiV1.of(accessJwt, refreshJwt));
    }
}
