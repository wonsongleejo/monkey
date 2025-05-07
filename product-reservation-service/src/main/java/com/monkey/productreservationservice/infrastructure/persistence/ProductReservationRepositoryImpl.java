package com.monkey.productreservationservice.infrastructure.persistence;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductReservationRepositoryImpl implements ProductReservationRepository {
    private final ProductReservationJpaRepository productReservationJpaRepository;

    @Override
    public ProductReservationEntity save(ProductReservationEntity productReservationEntity) {
        return productReservationJpaRepository.save(productReservationEntity);
    }

    @Override
    public Optional<ProductReservationEntity> findByProductReservationIdAndIsDeletedFalse(UUID productReservationId) {
        return productReservationJpaRepository.findByProductReservationIdAndIsDeletedFalse(productReservationId);
    }

    @Override
    public Optional<ProductReservationEntity> findByProductIdAndUserId(UUID productId, Long userId) {
        return productReservationJpaRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Page<ProductReservationEntity> findAllByIsDeletedFalse(Pageable pageable) {
        return productReservationJpaRepository.findAllByIsDeletedFalse(pageable);
    }

    @Override
    public Page<ProductReservationEntity> findByUserIdAndIsDeletedFalse(long userId, Pageable pageable) {
        return productReservationJpaRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
    }


    @Override
    public boolean existsByUserIdAndProductIdAndIsDeletedFalse(long userId, UUID productId) {
        return productReservationJpaRepository.existsByUserIdAndProductIdAndIsDeletedFalse(userId, productId);
    }
}
