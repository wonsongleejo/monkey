package com.monkey.userservice.application.service;

import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserServiceApiV1 {

    Page<UserEntity> getBy(Predicate predicate, Pageable pageable);

    UserEntity getMyDetails(Long userId);

    UserEntity getUserByUserId(Long userId);

    UserEntity putByUserId(Long userId, ReqUserPutDTOApiV1 reqDto);

    void deleteBy(Long userId);
}
