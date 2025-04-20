package com.monkey.userservice.presentation.controller;

import com.monkey.userservice.application.dto.request.ReqAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.request.ReqAuthPostSingUpDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostRefreshDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignUpDTOApiV1;
import com.monkey.userservice.application.service.AuthServiceApiV1;
import com.monkey.common_module.dto.ResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthControllerApiV1 {
    private final AuthServiceApiV1 authService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ResDTO<ResAuthPostSignUpDTOApiV1>> signUp(@RequestBody ReqAuthPostSingUpDTOApiV1 reqDto) {

        return new ResponseEntity<>(
                ResDTO.success(ResAuthPostSignUpDTOApiV1.of(authService.signUp(reqDto))),
                HttpStatus.OK
        );
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<ResDTO<ResAuthPostSignInDTOApiV1>> signIn(@RequestBody ReqAuthPostSignInDTOApiV1 reqDto) {

        ResAuthPostSignInDTOApiV1 resAuthPostSignInDTOApiV1 = authService.signIn(reqDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        String AUTHORIZATION_PREFIX = "Bearer ";
        httpHeaders.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION_PREFIX + resAuthPostSignInDTOApiV1.getAccessJwt());
        httpHeaders.set("refresh-token", resAuthPostSignInDTOApiV1.getRefreshJwt());

        return new ResponseEntity<>(
                ResDTO.success(
                        resAuthPostSignInDTOApiV1
                ),
                httpHeaders,
                HttpStatus.OK
        );
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<ResDTO<ResAuthPostRefreshDTOApiV1>> refreshBy(
            @RequestHeader("X-User-Name") String username
    ) {

        return new ResponseEntity<>(
                ResDTO.<ResAuthPostRefreshDTOApiV1>builder()
                        .code("000")
                        .message("토큰이 재발급되었습니다.")
                        .data(authService.refreshBy(username))
                        .build(),
                HttpStatus.OK
        );
    }
}