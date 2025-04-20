package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUsername(String username);

    @Query("SELECT r.refreshToken FROM RefreshTokenEntity r WHERE r.username = :username")
    String findRefreshTokenByUsername(String username);

    boolean existsRefreshTokenEntityByUsername(String username);
}