package com.monkey.productreservationservice.infrastructure.persistence;

import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductReservationRepositoryImpl implements ProductReservationRepository {
    private final ProductReservationJpaRepository productReservationJpaRepository;

    @Override
    public ProductReservationEntity save(ProductReservationEntity entity) {
        return productReservationJpaRepository.save(entity);
    }

    @Override
    public Optional<ProductReservationEntity> findByIdAndIsDeletedFalse(UUID id) {
        return productReservationJpaRepository.findByIdAndIsDeletedFalse(id);
    }

    @Override
    public List<ProductReservationEntity> findAllByIsDeletedFalse() {
        return productReservationJpaRepository.findAllByIsDeletedFalse();
    }
}
