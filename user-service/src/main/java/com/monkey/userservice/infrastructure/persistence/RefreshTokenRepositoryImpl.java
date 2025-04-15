package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.RefreshTokenEntity;
import com.monkey.userservice.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void save(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenJpaRepository.save(refreshTokenEntity);
    }
}