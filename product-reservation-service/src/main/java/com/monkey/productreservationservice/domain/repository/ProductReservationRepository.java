package com.monkey.productreservationservice.domain.repository;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductReservationRepository {
    ProductReservationEntity save(ProductReservationEntity entity);
    Optional<ProductReservationEntity> findByProductReservationIdAndIsDeletedFalse(UUID productReservationId);
    Page<ProductReservationEntity> findAllByIsDeletedFalse(Pageable pageable);
    Page<ProductReservationEntity> findByUserIdAndIsDeletedFalse(long userId, Pageable pageable);
    boolean existsByUserIdAndProductIdAndIsDeletedFalse(long userId, UUID productId);
}
