package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
}