package com.monkey.productreservationservice.infrastructure.persistence;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductReservationJpaRepository extends JpaRepository<ProductReservationEntity, UUID> {
    Optional<ProductReservationEntity> findByProductReservationIdAndIsDeletedFalse(UUID productReservationId);
    List<ProductReservationEntity> findAllByIsDeletedFalse();
    boolean existsByUserIdAndProductIdAndIsDeletedFalse(long userId, UUID productId);
}
