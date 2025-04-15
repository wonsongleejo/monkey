package com.monkey.userservice.domain.repository;

import com.monkey.userservice.domain.entity.RefreshTokenEntity;

public interface RefreshTokenRepository {
    void save(RefreshTokenEntity refreshTokenEntity);
}