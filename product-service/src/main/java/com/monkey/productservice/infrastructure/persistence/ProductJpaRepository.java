package com.monkey.productservice.infrastructure.persistence;

import com.monkey.productservice.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ProductEntity> findAllByIsDeletedFalse();
}
