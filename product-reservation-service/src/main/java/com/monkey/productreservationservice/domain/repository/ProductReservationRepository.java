package com.monkey.productreservationservice.domain.repository;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductReservationRepository {
    ProductReservationEntity save(ProductReservationEntity entity);
    Optional<ProductReservationEntity> findByProductReservationIdAndIsDeletedFalse(UUID productReservationId);
    List<ProductReservationEntity> findAllByIsDeletedFalse();
    Page<ProductReservationEntity> findAllByIsDeletedFalse(Pageable pageable);
    boolean existsByUserIdAndProductIdAndIsDeletedFalse(long userId, UUID productId);
}
