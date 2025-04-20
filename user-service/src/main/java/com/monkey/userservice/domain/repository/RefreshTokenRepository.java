package com.monkey.userservice.domain.repository;

import com.monkey.userservice.domain.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository {

    Optional<RefreshTokenEntity> findByUsername(String username);

    void save(RefreshTokenEntity refreshTokenEntity);

    String findRefreshTokenByUsername(String username);

    boolean existRefreshTokenByUsername(String username);
}