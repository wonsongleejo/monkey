package com.monkey.productreservationservice.domain.repository;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductReservationRepository {
    ProductReservationEntity save(ProductReservationEntity entity);
    Optional<ProductReservationEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ProductReservationEntity> findAllByIsDeletedFalse();
}
