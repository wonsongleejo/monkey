package com.monkey.productservice.infrastructure.persistence;

import com.monkey.productservice.domain.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByProductIdAndIsDeletedFalse(UUID productId);
    List<ProductEntity> findAllByIsDeletedFalse();
    Page<ProductEntity> findAllByIsDeletedFalse(Pageable pageable);
}
