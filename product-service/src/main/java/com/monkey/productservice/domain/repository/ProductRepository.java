package com.monkey.productservice.domain.repository;

import com.monkey.productservice.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByIdAndIsDeletedIsFalse(UUID productId);

    List<ProductEntity> findAllByIsDeletedFalse();
}
