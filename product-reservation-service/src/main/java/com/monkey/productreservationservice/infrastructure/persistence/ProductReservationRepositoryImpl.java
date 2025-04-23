package com.monkey.productreservationservice.infrastructure.persistence;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public List<ProductReservationEntity> findAllByIsDeletedFalse() {
        return productReservationJpaRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Page<ProductReservationEntity> findAllByIsDeletedFalse(Pageable pageable) {
        return productReservationJpaRepository.findAllByIsDeletedFalse(pageable);
    }

    @Override
    public boolean existsByUserIdAndProductIdAndIsDeletedFalse(long userId, UUID productId) {
        return productReservationJpaRepository.existsByUserIdAndProductIdAndIsDeletedFalse(userId, productId);
    }
}
