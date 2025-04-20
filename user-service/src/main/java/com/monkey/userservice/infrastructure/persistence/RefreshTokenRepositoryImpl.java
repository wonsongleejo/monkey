package com.monkey.userservice.infrastructure.persistence;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.userservice.domain.entity.RefreshTokenEntity;
import com.monkey.userservice.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshTokenEntity> findByUsername(String username) {
        RefreshTokenEntity refreshToken = refreshTokenJpaRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.TOKEN_NOT_FOUND));
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void save(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenJpaRepository.save(refreshTokenEntity);
    }

    @Override
    public String findRefreshTokenByUsername(String username) {
        return refreshTokenJpaRepository.findRefreshTokenByUsername(username);
    }

    @Override
    public boolean existRefreshTokenByUsername(String username) {
        return refreshTokenJpaRepository.existsRefreshTokenEntityByUsername(username);
    }
}