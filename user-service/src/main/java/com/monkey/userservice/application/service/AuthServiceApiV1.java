package com.monkey.userservice.application.service;

import com.monkey.userservice.application.dto.request.ReqAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.request.ReqAuthPostSingUpDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostRefreshDTOApiV1;
import com.monkey.userservice.application.dto.response.ResAuthPostSignInDTOApiV1;
import com.monkey.userservice.domain.entity.UserEntity;

public interface AuthServiceApiV1 {
    UserEntity signUp(ReqAuthPostSingUpDTOApiV1 reqDto);

    ResAuthPostSignInDTOApiV1 signIn(ReqAuthPostSignInDTOApiV1 reqDto);

    ResAuthPostRefreshDTOApiV1 refreshBy(String username);
}
